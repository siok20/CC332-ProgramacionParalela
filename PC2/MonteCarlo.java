import java.util.Random;

public class MonteCarlo {

    private static final int NUM_POINTS = 100000000; 
    private static final int NUM_THREADS = 4;     

    public static void main(String[] args) {
        // Ejecucion serial
        long startTimeSerial = System.currentTimeMillis();
        double piSerial = monteCarloPiSerial(NUM_POINTS);
        long endTimeSerial = System.currentTimeMillis();
        
        // Mostrar resultados seriales
        System.out.println("Estimacion de Pi (Serial): " + piSerial);
        System.out.println("Tiempo de ejecucion (Serial): " + (endTimeSerial - startTimeSerial) + " ms");

        // Ejecucion paralela
        long startTimeParallel = System.currentTimeMillis();
        double piParallel = monteCarloPiParallel(NUM_POINTS, NUM_THREADS);
        long endTimeParallel = System.currentTimeMillis();
        
        // Mostrar resultados paralelos
        System.out.println("Estimacion de Pi (Paralelo): " + piParallel);
        System.out.println("Tiempo de ejecucion total (Paralelo): " + (endTimeParallel - startTimeParallel) + " ms");
    }
    
    // Método serial para el cálculo de Pi
    public static double monteCarloPiSerial(int numPoints) {
        Random rand = new Random();
        int pointsCircle = 0;

        for (int i = 0; i < numPoints; i++) {
            double x = rand.nextDouble();
            double y = rand.nextDouble();
            if ((x * x + y * y) <= 1.0) {
                pointsCircle++;
            }
        }
        
        return 4.0 * pointsCircle / numPoints;
    }

    // Método paralelo para el cálculo de Pi
    public static double monteCarloPiParallel(int numPoints, int numThreads) {
        Thread[] threads = new Thread[numThreads];
        MonteCarloTask[] tasks = new MonteCarloTask[numThreads];
        int pointsPerThread = numPoints / numThreads;

        for (int i = 0; i < numThreads; i++) {
            tasks[i] = new MonteCarloTask(pointsPerThread, i);
            threads[i] = new Thread(tasks[i]);
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        int totalpointsCircle = 0;
        for (MonteCarloTask task : tasks) {
            totalpointsCircle += task.getpointsCircle();
        }

        return 4.0 * totalpointsCircle / numPoints;
    }
}

class MonteCarloTask implements Runnable {
    private int pointsCircle = 0;
    private int numPoints;
    private int threadId;
    private Random rand = new Random();

    public MonteCarloTask(int numPoints, int threadId) {
        this.numPoints = numPoints;
        this.threadId = threadId;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        int inside = 0;
        for (int i = 0; i < numPoints; i++) {
            double x = rand.nextDouble();
            double y = rand.nextDouble();
            if ((x * x + y * y) <= 1.0) {
                inside++;
            }
        }
        pointsCircle = inside;

        long endTime = System.currentTimeMillis();
        System.out.println("Hilo " + threadId + " tiempo de ejecucion: " + (endTime - startTime) + " ms");
    }

    public int getpointsCircle() {
        return pointsCircle;
    }
}
