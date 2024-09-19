
package uni.cc332.prueba1;


public class QR_Paralelo {
     public static final int HILOS = 2;

    // --------------------------------------------------------------
    public static void main(String[] args) {
        long Time1, Time2;
        double[][] A1 = DataSet.ReadFile(DataSet.filas, DataSet.columnas);
        Time1 = System.currentTimeMillis();
        Matrix A = new Matrix(A1);
        Matrix R = new Matrix(new double[A.getCols()][A.getCols()]);
        for (int i = 0; i < A.getCols(); i++) {
            Thread[] threads = new Thread[HILOS];
            for (int t = 0; t < HILOS; t++) {
                threads[t] = new Thread(new Oper1(t + 1, A, R, i));
                threads[t].start();
            }
            try {
                for (int t = 0; t < HILOS; t++) {
                    threads[t].join();
                }
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            double x = Math.sqrt(R.GetCell(i, i));
            for (int j = i; j < A.getCols(); j++) {
                R.SetCell(i, j, R.GetCell(i, j) / x);
            }
            for (int t = 0; t < HILOS; t++) {
                threads[t] = new Thread(new Oper2(t + 1, A, R, i));
                threads[t].start();
            }
            try {
                for (int t = 0; t < HILOS; t++) {
                    threads[t].join();
                }
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            for (int t = 0; t < HILOS; t++) {
                threads[t] = new Thread(new Oper3(t + 1, A, R, i));
                threads[t].start();
            }
            try {
                for (int t = 0; t < HILOS; t++) {
                    threads[t].join();
                }
            } catch (InterruptedException e) {

                System.out.println(e.getMessage());
            }
        }
        DataSet.WriteFile(A, "Matrix Q - Paralelo");
        DataSet.WriteFile(R, "Matrix R - Paralelo");
        Time2 = System.currentTimeMillis();
        System.out.printf("Tiempo de Procesamiento Paralelo: %d%n", (Time2 - Time1));
    }
}

class Oper1 implements Runnable {
    private Matrix A;
    private Matrix R;
    private int filIni;
    private int filFin;
    private int i;

    public Oper1(int nroHilo, Matrix A, Matrix R, int i) {
        this.A = A;
        this.R = R;
        filIni = (nroHilo - 1) * A.getRows() / QR_Paralelo.HILOS;
        filFin = nroHilo * A.getRows() / QR_Paralelo.HILOS - 1;
        this.i = i;
    }

    @Override
    public void run() {
        for (int j = i; j < A.getCols(); j++) {
            R.incrementar(i, j, A.prodEsc(i, j, filIni, filFin));
        }
    }
}

class Oper2 implements Runnable {
    private Matrix A;
    private Matrix R;
    private int filIni;
    private int filFin;
    private int i;

    public Oper2(int nroHilo, Matrix A, Matrix R, int i) {
        this.A = A;
        this.R = R;
        filIni = (nroHilo - 1) * A.getRows() / QR_Paralelo.HILOS;
        filFin = nroHilo * A.getRows() / QR_Paralelo.HILOS - 1;
        this.i = i;
    }

    @Override
    public void run() {
        double x = R.GetCell(i, i);
        for (int k = filIni; k <= filFin; k++) {
            A.SetCell(k, i, A.GetCell(k, i) / x);
        }
    }
}

class Oper3 implements Runnable {
    private Matrix A;
    private Matrix R;
    private int filIni;
    private int filFin;
    private int i;

    public Oper3(int nroHilo, Matrix A, Matrix R, int i) {
        this.A = A;
        this.R = R;
        filIni = (nroHilo - 1) * A.getRows() / QR_Paralelo.HILOS;
        filFin = nroHilo * A.getRows() / QR_Paralelo.HILOS - 1;
        this.i = i;
    }

    @Override
    public void run() {
        for (int j = i + 1; j < A.getCols(); j++) {
            double x = R.GetCell(i, j);
            for (int k = filIni; k <= filFin; k++) {
                A.SetCell(k, j, A.GetCell(k, j) - x * A.GetCell(k, i));
            }
        }
    }
    
}
