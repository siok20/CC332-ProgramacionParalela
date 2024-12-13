import java.io.IOException;
import java.io.RandomAccessFile;

public class eulerSolution {
    public static void main(String[] args) throws IOException, InterruptedException {
        double t0 = 0, y0 = 1, k = 0.5, tEnd = 10;
        int steps = 1000, numThreads = 4;

        String radFilePathParallel = "results_parallel.dat";
        String radFilePathSerial = "results_serial.dat";
        long t = System.currentTimeMillis();
        solveEulerWithRADParallel(t0, y0, k, tEnd, steps, numThreads, radFilePathParallel);
        long paralelo = System.currentTimeMillis() - t; 
        t = System.currentTimeMillis();
        solveEulerWithRADSerial(t0, y0, k, tEnd, steps, radFilePathSerial);
        long serial = System.currentTimeMillis() - t;

        System.out.println("Resultados Paralelos:");
        System.out.println("en :" + paralelo+ "ms");

        displayResults(radFilePathParallel, steps, 10);
        System.out.println("\nResultados Seriales:");
        System.out.println("en :" + serial+ "ms");
        displayResults(radFilePathSerial, steps, 10);
    }

    private static void solveEulerWithRADParallel(double t0, double y0, double k, double tEnd, int steps, int numThreads,
                                                  String radFilePath) throws IOException, InterruptedException {
        double h = (tEnd - t0) / steps;
        RandomAccessFile rad = new RandomAccessFile(radFilePath, "rw");
        rad.writeDouble(t0);
        rad.writeDouble(y0);

        Thread[] threads = new Thread[numThreads];
        int chunkSize = steps / numThreads;

        for (int i = 0; i < numThreads; i++) {
            final int start = i * chunkSize + 1;
            final int end = (i == numThreads - 1) ? steps : (i + 1) * chunkSize;

            threads[i] = new Thread(() -> {
                try (RandomAccessFile threadRad = new RandomAccessFile(radFilePath, "rw")) {
                    for (int j = start; j <= end; j++) {
                        long prevPosition = (j - 1) * 16;
                        threadRad.seek(prevPosition);
                        double tPrev = threadRad.readDouble();
                        double yPrev = threadRad.readDouble();

                        double t = tPrev + h;
                        double y = yPrev + h * (-k * yPrev);

                        long currentPosition = j * 16;
                        threadRad.seek(currentPosition);
                        threadRad.writeDouble(t);
                        threadRad.writeDouble(y);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        rad.close();
    }

    private static void solveEulerWithRADSerial(double t0, double y0, double k, double tEnd, int steps,
                                                String radFilePath) throws IOException {
        double h = (tEnd - t0) / steps;
        RandomAccessFile rad = new RandomAccessFile(radFilePath, "rw");
        rad.writeDouble(t0);
        rad.writeDouble(y0);

        for (int i = 1; i <= steps; i++) {
            long prevPosition = (i - 1) * 16;
            rad.seek(prevPosition);
            double tPrev = rad.readDouble();
            double yPrev = rad.readDouble();

            double t = tPrev + h;
            double y = yPrev + h * (-k * yPrev);

            long currentPosition = i * 16;
            rad.seek(currentPosition);
            rad.writeDouble(t);
            rad.writeDouble(y);
        }

        rad.close();
    }

    private static void displayResults(String radFilePath, int steps, int displayInterval) throws IOException {
        try (RandomAccessFile rad = new RandomAccessFile(radFilePath, "r")) {
            for (int i = 0; i <= steps; i += displayInterval) {
                long position = i * 16;
                if (position >= rad.length()) {
                    break;  // Evita intentar leer más allá del final del archivo
                }
                rad.seek(position);
                double t = rad.readDouble();
                double y = rad.readDouble();
                System.out.printf("t = %.2f, y = %.5f%n", t, y);
            }
        }
    }
}
