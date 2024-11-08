public class Procesos {
    public static final int N = 1000;
    public static int[][] A = new int[N][N];
    public static int[][] B = new int[N][N];
    public static int[][] C = new int[N][N];
    public static int[][] D = new int[N][N];
    public static int[][] E = new int[N][N];
    public static int[][] F = new int[N][N];
    public static int[][] G = new int[N][N];

    public static void main(String[] args) {
        System.out.println("Iniciando ejecucion...");

        long startTimeSerial = System.currentTimeMillis();
        ejecutarSerial();
        long endTimeSerial = System.currentTimeMillis();
        System.out.println("Tiempo de ejecucion total (Serial): " + (endTimeSerial - startTimeSerial) + " ms");

        long startTimeParallel = System.currentTimeMillis();
        ejecutarParalelo();
        long endTimeParallel = System.currentTimeMillis();
        System.out.println("Tiempo de ejecucion total (Paralelo): " + (endTimeParallel - startTimeParallel) + " ms");
    }

    public static void ejecutarSerial() {
        System.out.println("Inicio del procesamiento serial...");

        tareaA();
        tareaB();
        tareaC();
        tareaD();
        tareaE();
        tareaF();
        tareaG();
        

        System.out.println("Procesamiento serial completado.\n");
    }

    public static void ejecutarParalelo() {
        System.out.println("Inicio del procesamiento paralelo...");

        Thread tareaA = new Thread(() -> {
            tareaA();            
        });
        
        Thread tareaB = new Thread(() -> {
            tareaB();
        });

        Thread tareaC = new Thread(() -> {
            try {
                tareaA.join();
                tareaB.join();
                tareaC();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread tareaD = new Thread(() -> {
            try {
                tareaC.join();
                tareaD();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread tareaE = new Thread(() -> {
            try {
                tareaA.join();
                tareaD.join();
                tareaE();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread tareaF = new Thread(() -> {
            try {
                tareaB.join();
                tareaE.join();
                tareaF();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread tareaG = new Thread(() -> {
            try {
                tareaE.join();
                tareaF.join();
                tareaG();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        tareaA.start();
        tareaB.start();
        tareaC.start();
        tareaD.start();
        tareaE.start();
        tareaF.start();
        tareaG.start();

        try {
            tareaG.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Procesamiento paralelo completado.\n");
    }

    public static void tareaA()  {
        System.out.println("Ejecutando Tarea A: Asignaci on de matriz A");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                A[i][j] = i + j;
            }
        }
    }

    public static void tareaB() {
        System.out.println("Ejecutando Tarea B: Asignaci on de matriz B");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                B[i][j] = i * j;
            }
        }
    }

    public static void tareaC()  {
        System.out.println("Ejecutando Tarea C: Suma de A y B en C");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                C[i][j] = A[i][j] + B[i][j];
            }
        }
    }

    public static void tareaD() {
        System.out.println("Ejecutando Tarea D: Cuadrado de C en D");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                D[i][j] = 0;
                for (int k = 0; k < N; k++) {
                    D[i][j] += C[i][k] * C[k][j];
                }
            }
        }
    }

    public static void tareaE()  {
        System.out.println("Ejecutando Tarea E: Suma de A y D en E");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                E[i][j] = A[i][j] + D[i][j];
            }
        }
    }

    public static void tareaF()  {
        System.out.println("Ejecutando Tarea F: Suma de B y E en F");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                F[i][j] = B[i][j] + E[i][j];
            }
        }
    }

    public static void tareaG()  {
        System.out.println("Ejecutando Tarea G: Producto de E y F en G");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                G[i][j] = 0;
                for (int k = 0; k < N; k++) {
                    G[i][j] += E[i][k] * F[k][j];
                }
            }
        }
    }
}
