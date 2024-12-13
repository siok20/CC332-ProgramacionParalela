import java.io.IOException;
import java.io.RandomAccessFile;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class EulerSolver extends Application {
    private static final Object fileLock = new Object(); // Lock para sincronización global

    public static void main(String[] args) throws IOException, InterruptedException {
        double initialTime = 0, initialValue = 1, decayRate = 0.5, finalTime = 10;
        int totalSteps = 1000, threadCount = 4;

        String parallelResultsPath = "results_parallel.dat";
        String serialResultsPath = "results_serial.dat";

        long startTime = System.currentTimeMillis();
        executeParallelEuler(initialTime, initialValue, decayRate, finalTime, totalSteps, threadCount, parallelResultsPath);
        long parallelExecutionTime = System.currentTimeMillis() - startTime; 

        startTime = System.currentTimeMillis();
        executeSerialEuler(initialTime, initialValue, decayRate, finalTime, totalSteps, serialResultsPath);
        long serialExecutionTime = System.currentTimeMillis() - startTime;

        System.out.println("Parallel Results:");
        System.out.println("Execution Time: " + parallelExecutionTime + "ms");
        displayComputedResults(parallelResultsPath, totalSteps, 10);

        System.out.println("\nSerial Results:");
        System.out.println("Execution Time: " + serialExecutionTime + "ms");
        displayComputedResults(serialResultsPath, totalSteps, 10);

        // Launch visualization
        Application.launch(args);
    }

    private static void executeParallelEuler(double initialTime, double initialValue, double decayRate, double finalTime, int totalSteps, int threadCount, String outputFilePath) throws IOException, InterruptedException {
        double stepSize = (finalTime - initialTime) / totalSteps;
        RandomAccessFile outputFile = new RandomAccessFile(outputFilePath, "rw");
        outputFile.writeDouble(initialTime);
        outputFile.writeDouble(initialValue);
        outputFile.close();

        Thread[] workerThreads = new Thread[threadCount];
        int stepsPerThread = totalSteps / threadCount;

        for (int threadIndex = 0; threadIndex < threadCount; threadIndex++) {
            final int startStep = threadIndex * stepsPerThread + 1;
            final int endStep = (threadIndex == threadCount - 1) ? totalSteps : (threadIndex + 1) * stepsPerThread;

            workerThreads[threadIndex] = new Thread(() -> {
                try (RandomAccessFile threadFile = new RandomAccessFile(outputFilePath, "rw")) {
                    for (int step = startStep; step <= endStep; step++) {
                        synchronized (fileLock) { // Sincronización global
                            long previousPosition = (step - 1) * 16;
                            threadFile.seek(previousPosition);
                            double previousTime = threadFile.readDouble();
                            double previousValue = threadFile.readDouble();

                            double currentTime = previousTime + stepSize;
                            double currentValue = previousValue + stepSize * (-decayRate * previousValue);

                            long currentPosition = step * 16;
                            threadFile.seek(currentPosition);
                            threadFile.writeDouble(currentTime);
                            threadFile.writeDouble(currentValue);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            workerThreads[threadIndex].start();
        }

        for (Thread thread : workerThreads) {
            thread.join();
        }
    }

    private static void executeSerialEuler(double initialTime, double initialValue, double decayRate, double finalTime, int totalSteps, String outputFilePath) throws IOException {
        double stepSize = (finalTime - initialTime) / totalSteps;
        RandomAccessFile outputFile = new RandomAccessFile(outputFilePath, "rw");
        outputFile.writeDouble(initialTime);
        outputFile.writeDouble(initialValue);

        for (int step = 1; step <= totalSteps; step++) {
            long previousPosition = (step - 1) * 16;
            outputFile.seek(previousPosition);
            double previousTime = outputFile.readDouble();
            double previousValue = outputFile.readDouble();

            double currentTime = previousTime + stepSize;
            double currentValue = previousValue + stepSize * (-decayRate * previousValue);

            long currentPosition = step * 16;
            outputFile.seek(currentPosition);
            outputFile.writeDouble(currentTime);
            outputFile.writeDouble(currentValue);
        }

        outputFile.close();
    }

    private static void displayComputedResults(String outputFilePath, int totalSteps, int interval) throws IOException {
        try (RandomAccessFile outputFile = new RandomAccessFile(outputFilePath, "r")) {
            for (int step = 0; step <= totalSteps; step += interval) {
                long position = step * 16;
                if (position >= outputFile.length()) {
                    break;
                }
                outputFile.seek(position);
                double time = outputFile.readDouble();
                double value = outputFile.readDouble();
                System.out.printf("t = %.2f, y = %.5f%n", time, value);
            }
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        String parallelResultsPath = "results_parallel.dat";
        String serialResultsPath = "results_serial.dat";

        XYChart.Series<Number, Number> parallelSeries = loadResultsToSeries(parallelResultsPath, "Parallel Results");
        XYChart.Series<Number, Number> serialSeries = loadResultsToSeries(serialResultsPath, "Serial Results");

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Time");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Value");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Euler Method Results");
        lineChart.getData().addAll(parallelSeries, serialSeries);

        Scene scene = new Scene(lineChart, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Euler Solver Visualization");
        stage.show();
    }

    private static XYChart.Series<Number, Number> loadResultsToSeries(String filePath, String seriesName) throws IOException {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(seriesName);

        try (RandomAccessFile file = new RandomAccessFile(filePath, "r")) {
            while (file.getFilePointer() < file.length()) {
                double time = file.readDouble();
                double value = file.readDouble();
                series.getData().add(new XYChart.Data<>(time, value));
            }
        }

        return series;
    }
}
