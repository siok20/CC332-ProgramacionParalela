import java.io.IOException;
import java.io.RandomAccessFile;

public class DetSubmatr {
    private static final String FILENAME = "DATOS.txt"; //Archivo sin salto de lineas ni espacios, con numeros de tamaño BLOCK
    private static final int ROWS = 3;  // Número de filas de la matriz principal
    private static final int COLS = 3;  // Número de columnas de la matriz principal
    private static final int NUM_THREADS = 4; 
    private static int BLOCK = 1;
    private static byte[] RECORD = new byte[BLOCK];; 

    public static void main(String[] args) {
        // Tiempo de procesamiento serial
        long startSerial = System.currentTimeMillis();
        calcularDeterminantesSerial();
        long endSerial = System.currentTimeMillis();
        long tiempoSerial = endSerial - startSerial;

        System.out.println("Tiempo total de procesamiento serial: " + tiempoSerial + " ms");

        long startParallel = System.currentTimeMillis();
        long[] tiemposParalelo = calcularDeterminantesParalelo();
        long endParallel = System.currentTimeMillis();
        long tiempoParalelo = endParallel - startParallel;

        System.out.println("Tiempo total de procesamiento paralelo: " + tiempoParalelo + " ms");

        for (int t = 0; t < NUM_THREADS; t++) {
            System.out.println("Tiempo del hilo " + t + ": " + tiemposParalelo[t] + " ms");
        }
    }

    // Método de procesamiento serial
    public static void calcularDeterminantesSerial() {
        try {
            RandomAccessFile raf = new RandomAccessFile(FILENAME, "r");
            for (int i = 0; i < ROWS - 1; i++) {
                for (int j = 0; j < COLS - 1; j++) {
                    double[][] submatriz2x2 = getSubmatriz(raf, i, j, 2);
                    double det2x2 = calcularDeterminante2x2(submatriz2x2);
                    System.out.println("Serial - Determinante 2x2 en (" + i + "," + j + "): " + det2x2);

                    if (i < ROWS - 2 && j < COLS - 2) {
                        double[][] submatriz3x3 = getSubmatriz(raf, i, j, 3);
                        double det3x3 = calcularDeterminante3x3(submatriz3x3);
                        System.out.println("Serial - Determinante 3x3 en (" + i + "," + j + "): " + det3x3);
                    }
                }
            }
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long[] calcularDeterminantesParalelo() {
        Thread[] threads = new Thread[NUM_THREADS];
        long[] times = new long[NUM_THREADS];  
        int chunkSize = ROWS / NUM_THREADS;  

        for (int t = 0; t < NUM_THREADS; t++) {
            final int hilo = t;
            final int ini = t * chunkSize;
            final int fin = (t == NUM_THREADS - 1) ? ROWS : ini + chunkSize;

            threads[t] = new Thread(new Runnable() {
                @Override
                public void run() {
                    times[hilo] = System.currentTimeMillis();
                    try {
                        RandomAccessFile raf = new RandomAccessFile(FILENAME, "r");
                        for (int i = ini; i < fin - 1; i++) { 
                            for (int j = 0; j < COLS - 1; j++) {
                                double det2x2 = calcularDeterminante2x2(getSubmatriz(raf, i, j, 2));
                                System.out.println("Hilo " + hilo + " - Determinante 2x2 en (" + i + "," + j + "): " + det2x2);

                                if (i < fin - 2 && j < COLS - 2) {
                                    double det3x3 = calcularDeterminante3x3(getSubmatriz(raf, i, j, 3));
                                    System.out.println("Hilo " + hilo + " - Determinante 3x3 en (" + i + "," + j + "): " + det3x3);
                                }
                            }
                        }
                        raf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    times[hilo] = System.currentTimeMillis() - times[hilo];
                }
            });
            threads[t].start();
        }

        for (int t = 0; t < NUM_THREADS; t++) {
            try {
                threads[t].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return times;
    }

    private static String GetString() {
        String CAD;
            CAD = "";
            for(int i=0;i<=BLOCK-1;i++) {
                CAD = CAD + (char)(RECORD[i]);
            }
            return CAD;
    }

    private static double[][] getSubmatriz(RandomAccessFile raf, int fila, int col, int n) throws IOException {
        double[][] submatriz = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int pos = ((fila + i) * COLS + (col + j));
                raf.seek(pos);
                raf.read(RECORD); 
                String number = GetString();
                int num = Integer.parseInt(number);
                submatriz[i][j] = num;
            }
        }
        return submatriz;
    }

    private static double calcularDeterminante2x2(double[][] matriz) {
        return matriz[0][0] * matriz[1][1] - matriz[0][1] * matriz[1][0];
    }

    private static double calcularDeterminante3x3(double[][] matriz) {
        return matriz[0][0] * (matriz[1][1] * matriz[2][2] - matriz[1][2] * matriz[2][1])
             - matriz[0][1] * (matriz[1][0] * matriz[2][2] - matriz[1][2] * matriz[2][0])
             + matriz[0][2] * (matriz[1][0] * matriz[2][1] - matriz[1][1] * matriz[2][0]);
    }
}
