import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
//====================================================================
public class SynchronizedQueue {
    //----------------------------------------------------------------
    public static void main(String[] args) {
        BlockingQueue<Integer> REQUEST  = new LinkedBlockingQueue<>();
        BlockingQueue<Result>  RESPONSE = new LinkedBlockingQueue<>();
        Operation OBJ = new Operation(REQUEST,RESPONSE);
        OBJ.start();
        try {
            REQUEST.put(100);
            REQUEST.put(500);
            REQUEST.put(60);
            System.out.println(RESPONSE.take());
            System.out.println(RESPONSE.take());
            System.out.println(RESPONSE.take());
        }
        catch (InterruptedException ERROR) {
            ERROR.printStackTrace();
            System.out.println(ERROR);
        }
        System.exit(0);
    }
}
//====================================================================
class Operation {
    private final BlockingQueue<Integer> IN;
    private final BlockingQueue<Result>  OUT;
    //----------------------------------------------------------------
    public Operation(BlockingQueue<Integer> REQUEST, BlockingQueue<Result> RESPONSE) {
        this.IN  = REQUEST;
        this.OUT = RESPONSE;
    }
    //----------------------------------------------------------------
    private int FFF(int N) {
    int S;
        S = 0;
        for(int i=1; i<=N; i++) {
            if(N%i==0) {
                S = S + i;
                System.out.println(N + " - " + i);
            }
        }
        return S;
    }
    //----------------------------------------------------------------
    public void start() {
        new Thread(new Runnable() {
            public void run() {
            int A,B;
                while (true) {
                    try {
                        A  = IN.take();
                        B = FFF (A);
                        OUT.put(new Result(A,B));
                    }
                    catch (InterruptedException ERROR) {
                        ERROR.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
//====================================================================
class Result {
    private final int IN;
    private final int OUT;
    //----------------------------------------------------------------
    public Result(int A, int B) {
        this.IN  = A;
        this.OUT = B;
    }
    //----------------------------------------------------------------
    @Override public String toString() {
        return "\nSuma de Divisores de " + IN + ": " + OUT;
    }
}