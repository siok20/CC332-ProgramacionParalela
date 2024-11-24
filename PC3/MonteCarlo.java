import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MonteCarlo {
    public static void main(String[] args) {
        BlockingQueue<Integer> REQUEST = new LinkedBlockingQueue<>();
        BlockingQueue<Result> RESPONSE = new LinkedBlockingQueue<>();
        Operation OBJ = new Operation(REQUEST, RESPONSE);
        OBJ.start();

        try {
            REQUEST.put(1000000000);
            System.out.println(RESPONSE.take());
        } catch (InterruptedException ERROR) {
            ERROR.printStackTrace();
        }
        System.exit(0);
    }
}

class Operation {
    private final BlockingQueue<Integer> IN;
    private final BlockingQueue<Result> OUT;
    private final Random random;

    public Operation(BlockingQueue<Integer> REQUEST, BlockingQueue<Result> RESPONSE) {
        this.IN = REQUEST;
        this.OUT = RESPONSE;
        this.random = new Random();
    }

    public void start() {
        new Thread(() -> {
            while (true) {
                try {
                    int totalPoints = IN.take();
                    int pointsInsideCircle = 0;

                    for (int i = 0; i < totalPoints; i++) {
                        double x = random.nextDouble();
                        double y = random.nextDouble();
                        if (x * x + y * y <= 1) {
                            pointsInsideCircle++;
                        }
                    }

                    double piEstimate = 4.0 * pointsInsideCircle / totalPoints;
                    OUT.put(new Result(totalPoints, piEstimate));
                } catch (InterruptedException ERROR) {
                    ERROR.printStackTrace();
                }
            }
        }).start();
    }
}

class Result {
    private final int numPoints;
    private final double piEstimate;

    public Result(int numPoints, double piEstimate) {
        this.numPoints = numPoints;
        this.piEstimate = piEstimate;
    }

    @Override
    public String toString() {
        return "\nEstimaci0n de PI con " + numPoints + " puntos: " + piEstimate;
    }
}
