



public class PowerMatrix {
    private Matrix A;
    private final int N = 100;
    private final int HILOS = 5;
    private long[] times = new long[HILOS];
    private long time ;
    private long timeP ;
    public static void main(String[] args) {
        PowerMatrix pm = new PowerMatrix();
        int n = 50;
        Matrix C1 = pm.powerSerial(n);
        Matrix C2 = pm.powerParallel(n);

        System.out.println("Tiempo paralelo :" + pm.timeP);
        for (int i = 0; i < pm.HILOS; i++) {
            System.out.println("\tTiempo del hilo " + i + ": " + pm.times[i]);
        }
        System.out.println("Tiempo estatico :" + pm.time);


        DataSet.WriteFile(C2, "resultadoParalelo.txt");
        DataSet.WriteFile(C1, "resultado.txt");


    }

    public PowerMatrix(){
        //DataSet.CreateFile(N, N);
        this.A = new Matrix(DataSet.ReadFile(N , N));
    }

    public Matrix powerSerial(int n){
        time = System.currentTimeMillis();
        if(n==0){
            return new Matrix(new double[A.getRows()][A.getCols()]).toIdentity();
        }

        if(n==1){
            return A; 
        }

        Matrix C = new Matrix(A).toIdentity();

        for (int i = 0; i < n ; i++) {
            C = C.prod(A);
        }
        time = System.currentTimeMillis() - time;
        return C;
    }

    public Matrix powerParallel(int n){
        timeP = System.currentTimeMillis();
        if(n == 0) {
            return new Matrix(new double[A.getRows()][A.getCols()]).toIdentity();
        }
    
        if(n == 1) {
            return A;
        }
    
        Matrix[] C = new Matrix[HILOS];
        int[] pts = new int[HILOS];
        int part = n / HILOS;
        int rest = n % HILOS;
    
        for (int i = 0; i < HILOS; i++) {
            C[i] = new Matrix(A);
            pts[i] = part;
            if (i == HILOS - 1) {
                pts[i] += rest;  
            }
        }
    
        Thread[] threads = new Thread[HILOS];
    
        for (int t = 0; t < HILOS; t++) {
            final int index = t;
            threads[t] = new Thread(new Runnable() {
                @Override
                public void run() {
                    times[index] = System.currentTimeMillis();
                    Matrix temp = new Matrix(A).toIdentity();
                    for (int i = 0; i < pts[index]; i++) {
                        temp = temp.prod(A);
                    }
                    C[index] = temp;
                    times[index] = System.currentTimeMillis() - times[index];
                }
            });
            threads[t].start();
        }
    
        try {
            for (int t = 0; t < HILOS; t++) {
                threads[t].join();
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    
        Matrix result = new Matrix(A).toIdentity();
        for (Matrix matrix : C) {
            result = result.prod(matrix);
        }
    
        timeP = System.currentTimeMillis() - timeP;
        return result;

    }

}
