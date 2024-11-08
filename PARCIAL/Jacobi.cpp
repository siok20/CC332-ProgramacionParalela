#include <mpi.h>
#include <iostream>
#include <cmath>
#include <vector>

using namespace std;

// Funciones auxiliares para leer la matriz y el vector (puedes personalizarlas)
void ReadMatrix(double** &A, int N);
void ReadVector(double* &b, int N);

void Jacobi(int myrank, int numnodes, int N, double* A_local, double* x, double* b_local, double abstol, int maxit) {
    int rows_local = N / numnodes; // Número de filas por nodo
    int remaining_rows = N % numnodes; // Filas adicionales para el último nodo
    int rows_assigned = (myrank == numnodes - 1) ? rows_local + remaining_rows : rows_local;

    // Comprobar que el tamaño es válido
    if (rows_assigned <= 0) {
        cerr << "Error: El número de filas asignadas es inválido." << endl;
        MPI_Abort(MPI_COMM_WORLD, -1);
    }

    vector<double> xold(N, 1.0); // Valores iniciales de x
    vector<double> local_x(rows_assigned, 0.0); // Vector local de la solución
    double error_sum_local, error_sum_global;

    // Iteraciones de Jacobi
    for (int k = 0; k < maxit; ++k) {
        error_sum_local = 0.0;

        for (int i = 0; i < rows_assigned; ++i) {
            int global_i = myrank * rows_local + i; // Índice global de la fila
            double sum1 = 0.0, sum2 = 0.0;

            // Calcular sum1 y sum2 en la fila local
            for (int j = 0; j < global_i; ++j) {
                sum1 += A_local[i * N + j] * xold[j];
            }
            for (int j = global_i + 1; j < N; ++j) {
                sum2 += A_local[i * N + j] * xold[j];
            }

            local_x[i] = (b_local[i] - sum1 - sum2) / A_local[i * N + global_i];
            error_sum_local += (local_x[i] - xold[global_i]) * (local_x[i] - xold[global_i]);
        }

        // Reducir los errores locales a un error global
        MPI_Allreduce(&error_sum_local, &error_sum_global, 1, MPI_DOUBLE, MPI_SUM, MPI_COMM_WORLD);

        // Comprobar la convergencia
        if (sqrt(error_sum_global) < abstol) {
            break; // Si el error es suficientemente pequeño, detener las iteraciones
        }

        // Recolectar los nuevos valores de x de todos los procesos
        vector<int> counts(numnodes, rows_local);
        counts[numnodes - 1] += remaining_rows; // Ajustar para el último proceso
        vector<int> displacements(numnodes, 0);
        for (int i = 1; i < numnodes; ++i) {
            displacements[i] = displacements[i - 1] + counts[i - 1];
        }

        MPI_Allgatherv(local_x.data(), rows_assigned, MPI_DOUBLE, xold.data(), counts.data(), displacements.data(), MPI_DOUBLE, MPI_COMM_WORLD);
    }

    // Guardar los resultados finales en el vector solución x
    for (int i = 0; i < rows_assigned; ++i) {
        x[myrank * rows_local + i] = local_x[i];
    }
}

int main(int argc, char* argv[]) {
    MPI_Init(&argc, &argv);

    int myrank, numnodes;
    MPI_Comm_rank(MPI_COMM_WORLD, &myrank); // Obtener el rank del proceso
    MPI_Comm_size(MPI_COMM_WORLD, &numnodes); // Obtener el número de procesos

    int N = 10; // Tamaño de la matriz (N x N), prueba con un valor pequeño para evitar problemas de memoria
    double abstol = 1e-6; // Tolerancia para la convergencia
    int maxit = 1000; // Máximo número de iteraciones

    double* A_local = nullptr; // Porción local de la matriz
    double* b_local = nullptr; // Porción local del vector b
    double* x = nullptr; // Vector solución

    if (myrank == 0) {
        // Solo el proceso 0 lee la matriz completa
        double** A = nullptr;
        double* b = nullptr;
        ReadMatrix(A, N); // Lee la matriz completa
        ReadVector(b, N); // Lee el vector completo

        // Distribuir filas de la matriz A y el vector b a los otros procesos
        int rows_local = N / numnodes;
        int remaining_rows = N % numnodes;

        for (int i = 1; i < numnodes; ++i) {
            int start_row = i * rows_local;
            int rows_to_send = (i == numnodes - 1) ? rows_local + remaining_rows : rows_local;

            // Enviar porciones de la matriz A y el vector b
            MPI_Send(&A[start_row][0], rows_to_send * N, MPI_DOUBLE, i, 0, MPI_COMM_WORLD);
            MPI_Send(&b[start_row], rows_to_send, MPI_DOUBLE, i, 0, MPI_COMM_WORLD);
        }

        // Asignar porción de A y b al proceso 0
        A_local = new double[rows_local * N];
        b_local = new double[rows_local];
        for (int i = 0; i < rows_local; ++i) {
            for (int j = 0; j < N; ++j) {
                A_local[i * N + j] = A[i][j];
            }
            b_local[i] = b[i];
        }

        // Limpiar la matriz A y el vector b completos después de distribuir
        for (int i = 0; i < N; ++i) {
            delete[] A[i];
        }
        delete[] A;
        delete[] b;
    } else {
        // Recibir el número de filas para este proceso
        int rows_local = N / numnodes;
        int remaining_rows = N % numnodes;
        int rows_assigned = (myrank == numnodes - 1) ? rows_local + remaining_rows : rows_local;

        // Reservar memoria para la porción local de la matriz y el vector b
        A_local = new double[rows_assigned * N];
        b_local = new double[rows_assigned];

        // Recibir la porción de la matriz A y el vector b
        MPI_Recv(A_local, rows_assigned * N, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        MPI_Recv(b_local, rows_assigned, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
    }

    // Todos los procesos inicializan el vector solución
    x = new double[N];

    // Llamada a la función de Jacobi
    Jacobi(myrank, numnodes, N, A_local, x, b_local, abstol, maxit);

    // El proceso 0 imprime la solución final
    if (myrank == 0) {
        cout << "Solución encontrada:" << endl;
        for (int i = 0; i < N; ++i) {
            cout << "x[" << i << "] = " << x[i] << endl;
        }
    }

    // Liberar memoria
    delete[] A_local;
    delete[] b_local;
    delete[] x;

    MPI_Finalize();
    return 0;
}

// Implementación de ejemplo para leer la matriz y el vector
void ReadMatrix(double** &A, int N) {
    A = new double*[N];
    for (int i = 0; i < N; ++i) {
        A[i] = new double[N];
        for (int j = 0; j < N; ++j) {
            A[i][j] = i * j + j; // Ejemplo de matriz
        }
    }
}

void ReadVector(double* &b, int N) {
    b = new double[N];
    for (int i = 0; i < N; ++i) {
        b[i] = i + 1; // Ejemplo de vector
    }
}
