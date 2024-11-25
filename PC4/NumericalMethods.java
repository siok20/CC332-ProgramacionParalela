import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NumericalMethods {

    public static double f(double x, double y) {
        return -2 * x * y;
    }

    public static double rungeKuttaSerial(double x0, double y0, double h, int steps) {
        double x = x0;
        double y = y0;
        for (int i = 0; i < steps; i++) {
            double k1 = h * f(x, y);
            double k2 = h * f(x + h / 2.0, y + k1 / 2.0);
            double k3 = h * f(x + h / 2.0, y + k2 / 2.0);
            double k4 = h * f(x + h, y + k3);
            y = y + (k1 + 2 * k2 + 2 * k3 + k4) / 6.0;
            x = x + h;
        }
        return y;
    }

    public static double rungeKuttaParallel(double x0, double y0, double h, int steps) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        double x = x0;
        double y = y0;
        for (int i = 0; i < steps; i++) {
            double currentX = x;
            double currentY = y;
            Future<Double> k1Future = executor.submit(() -> h * f(currentX, currentY));
            Future<Double> k2Future = executor.submit(() -> h * f(currentX + h / 2.0, currentY + k1Future.get() / 2.0));
            Future<Double> k3Future = executor.submit(() -> h * f(currentX + h / 2.0, currentY + k2Future.get() / 2.0));
            Future<Double> k4Future = executor.submit(() -> h * f(currentX + h, currentY + k3Future.get()));
            double k1 = k1Future.get();
            double k2 = k2Future.get();
            double k3 = k3Future.get();
            double k4 = k4Future.get();
            y = y + (k1 + 2 * k2 + 2 * k3 + k4) / 6.0;
            x = x + h;
        }
        executor.shutdown();
        return y;
    }

    public static double predictorCorrectorSerial(double x0, double y0, double h, int steps) {
        double x = x0;
        double y = y0;
        for (int i = 0; i < steps; i++) {
            double yPredictor = y + h * f(x, y);
            double yCorrector = y + (h / 2.0) * (f(x, y) + f(x + h, yPredictor));
            y = yCorrector;
            x = x + h;
        }
        return y;
    }

    public static double predictorCorrectorParallel(double x0, double y0, double h, int steps) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        double x = x0;
        double y = y0;
        for (int i = 0; i < steps; i++) {
            double currentX = x;
            double currentY = y;
            Future<Double> predictorFuture = executor.submit(() -> currentY + h * f(currentX, currentY));
            Future<Double> correctorFuture = executor.submit(() -> {
                double yPredictor = predictorFuture.get();
                return currentY + (h / 2.0) * (f(currentX, currentY) + f(currentX + h, yPredictor));
            });
            y = correctorFuture.get();
            x = x + h;
        }
        executor.shutdown();
        return y;
    }

    public static double extrapolationSerial(double x0, double y0, double h, int steps) {
        double x = x0;
        double y = y0;
        for (int i = 0; i < steps; i++) {
            double y1 = y + h * f(x, y);
            double y2 = y + (h / 2.0) * f(x + h / 2.0, y + h / 2.0 * f(x, y));
            y = (2 * y2 - y1);
            x = x + h;
        }
        return y;
    }

    public static double extrapolationParallel(double x0, double y0, double h, int steps) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        double x = x0;
        double y = y0;
        for (int i = 0; i < steps; i++) {
            double currentX = x;
            double currentY = y;
            Future<Double> y1Future = executor.submit(() -> currentY + h * f(currentX, currentY));
            Future<Double> y2Future = executor.submit(() -> {
                double halfH = h / 2.0;
                return currentY + halfH * f(currentX + halfH, currentY + halfH * f(currentX, currentY));
            });
            double y1 = y1Future.get();
            double y2 = y2Future.get();
            y = (2 * y2 - y1);
            x = x + h;
        }
        executor.shutdown();
        return y;
    }

    public static double eulerSerial(double x0, double y0, double h, int steps) {
        double x = x0;
        double y = y0;
        for (int i = 0; i < steps; i++) {
            y = y + h * f(x, y);
            x = x + h;
        }
        return y;
    }

    public static double eulerParallel(double x0, double y0, double h, int steps) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        double x = x0;
        double y = y0;
        for (int i = 0; i < steps; i++) {
            double currentX = x;
            double currentY = y;
            Future<Double> yFuture = executor.submit(() -> currentY + h * f(currentX, currentY));
            y = yFuture.get();
            x = x + h;
        }
        executor.shutdown();
        return y;
    }

    public static void main(String[] args) throws Exception {
        double x0 = 0.0, y0 = 1.0, h = 0.1;
        int steps = 10;

        System.out.println("Runge-Kutta Serial: " + rungeKuttaSerial(x0, y0, h, steps));
        System.out.println("Runge-Kutta Parallel: " + rungeKuttaParallel(x0, y0, h, steps));
        System.out.println("Predictor-Corrector Serial: " + predictorCorrectorSerial(x0, y0, h, steps));
        System.out.println("Predictor-Corrector Parallel: " + predictorCorrectorParallel(x0, y0, h, steps));
        System.out.println("Extrapolation Serial: " + extrapolationSerial(x0, y0, h, steps));
        System.out.println("Extrapolation Parallel: " + extrapolationParallel(x0, y0, h, steps));
        System.out.println("Euler Serial: " + eulerSerial(x0, y0, h, steps));
        System.out.println("Euler Parallel: " + eulerParallel(x0, y0, h, steps));
    }
}
