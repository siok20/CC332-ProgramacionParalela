import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LagrangeInterpolation {

    public static void main(String[] args) throws InterruptedException {
        int N = 20000;
        List<Double> x = generateRandomDoubleList( N, -N, N);
        List<Double> y = generateRandomDoubleList(N, -N, N);
        double xValue = 2.5;

        System.out.println("Interpolacion Lagrange Serial: " + lagrangeInterpolationSerial(x, y, xValue));
        System.out.println("Interpolacion Lagrange Paralelo con Hilos: " + lagrangeInterpolationParallelThreads(x, y, xValue));

        BlockingQueue<Point> REQUEST  = new LinkedBlockingQueue<>();
        BlockingQueue<Result> RESPONSE = new LinkedBlockingQueue<>();

        LagrangeQueue lagrange = new LagrangeQueue(REQUEST, RESPONSE);
        lagrange.setTargetX(xValue);
        lagrange.start();

        try {
            for (int i = 0; i < N; i++) {
                REQUEST.put(new Point(x.get(i), y.get(i)));
            }

            System.out.println(RESPONSE.take());
        } catch (InterruptedException ERROR) {
            ERROR.printStackTrace();
            System.out.println(ERROR);
        }
        System.exit(0);

    }

    public static double lagrangeInterpolationSerial(List<Double> x, List<Double> y, double xValue) {
        long ini = System.currentTimeMillis();
        int n = x.size();
        double result = 0.0;

        for (int i = 0; i < n; i++) {
            double term = y.get(i);
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    term *= (xValue - x.get(j)) / (x.get(i) - x.get(j));
                }
            }
            result += term;
        }

        long fin = System.currentTimeMillis();
        System.out.println("Tiempo de procesamiento serial: "+ (fin - ini));
        return result;
    }

    public static double lagrangeInterpolationParallelThreads(List<Double> xValues, List<Double> yValues, double x) throws InterruptedException {
        long timeini = System.currentTimeMillis();
        
        int n = xValues.size();
        int numThreads = 4;
        int termsPerThread = (int) Math.ceil((double) n / numThreads);

        List<LagrangeThread> workers = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            int ini = i * termsPerThread;
            int fin = Math.min(ini + termsPerThread, n);
            LagrangeThread worker = new LagrangeThread(x, xValues, yValues, ini, fin);
            workers.add(worker);
            worker.start();
        }

        double result = 0.0;
        for (LagrangeThread worker : workers) {
            worker.join();
            result += worker.getPartialResult();
        }

        long timefin = System.currentTimeMillis();
        System.out.println("Tiempo de procesamiento paralelo con hilos: "+ (timefin - timeini));
        return result;
    }
    

    public static List<Double> generateRandomDoubleList(int size, double min, double max) {
        List<Double> randomList = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            double randomNumber = min + (max - min) * random.nextDouble();
            randomList.add(randomNumber);
        }

        return randomList;
    }

    
}

class Point {
    final double x;
    final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
}

class LagrangeQueue {
    private final BlockingQueue<Point> IN;
    private final BlockingQueue<Result> OUT;
    private double targetX;

    public LagrangeQueue(BlockingQueue<Point> REQUEST, BlockingQueue<Result> RESPONSE) {
        this.IN = REQUEST;
        this.OUT = RESPONSE;
    }

    public void setTargetX(double x) {
        this.targetX = x;
    }

    private double interpolate(List<Point> points, double x) {
        double result = 0.0;
        int n = points.size();

        for (int i = 0; i < n; i++) {
            double term = points.get(i).y;
            for (int j = 0; j < n; j++) {
                if (j != i) {
                    term = term * (x - points.get(j).x) / (points.get(i).x - points.get(j).x);
                }
            }
            result += term;
        }
        return result;
    }

    public void start() {
        new Thread(new Runnable() {
            public void run() {
                List<Point> points = new ArrayList<>();
                try {
                    IN.drainTo(points);

                    double interpolatedValue = interpolate(points, targetX);

                    OUT.put(new Result(targetX, interpolatedValue));
                } catch (InterruptedException ERROR) {
                    ERROR.printStackTrace();
                }
            }
        }).start();
    }
}


class Result {
    private final double x;
    private final double interpolatedValue;

    public Result(double x, double interpolatedValue) {
        this.x = x;
        this.interpolatedValue = interpolatedValue;
    }

    @Override
    public String toString() {
        return "\nInterpolacion de Lagrange Cola " + interpolatedValue;
    }
}

class LagrangeThread extends Thread {
    private double x;
    private List<Double> xValues, yValues;
    private int ini, fin;
    private double partialResult = 0.0;

    public LagrangeThread(double x, List<Double> xValues, List<Double> yValues, int ini, int fin) {
        this.x = x;
        this.xValues = xValues;
        this.yValues = yValues;
        this.ini = ini;
        this.fin = fin;
    }

    public double getPartialResult() {
        return partialResult;
    }

    @Override
    public void run() {
        for (int i = ini; i < fin; i++) {
            double term = yValues.get(i);
            for (int j = 0; j < xValues.size(); j++) {
                if (i != j) {
                    term *= (x - xValues.get(j)) / (xValues.get(i) - xValues.get(j));
                }
            }
            partialResult += term;
        }
    }
}
