
package uni.pp.cc332.java.timeProcessingThree;

public class Process_Parallel {
    private static Process_Task TSK = new Process_Task();
    private static int N;
    private static int T;
    private static long Time1,Time2,TIME_PARALLEL;

    private static boolean Sw1 = false;
    private static boolean Sw2 = false;
    private static boolean Sw3 = false;
    private static boolean Sw4 = false;

    private static int[][] A;
    private static int[][] B;
    private static long[][] C;
    //================================================================
    public static void Salida_Hilos() {
       if((Sw1&&Sw2&&Sw3&&Sw4)==true) {
           Time2 = System.currentTimeMillis();
           TIME_PARALLEL = Time2 - Time1;
           System.out.println("Tiempo de Proceso Paralelo: " + TIME_PARALLEL);
       }
    }
    //================================================================
    public static void SubProceso_Hilo1() {
        new Thread(new Runnable() {
            public void run() {
               Calcular_AxB(0,T,0,T);
               Sw1 = true;
               Salida_Hilos();
            }
        }).start();
    }
    //================================================================
    public static void SubProceso_Hilo2() {
        new Thread(new Runnable() {
            public void run() {
               Calcular_AxB(0,T,T+1,N-1);
               Sw2 = true;
               Salida_Hilos();
            }
        }).start();
    }
    //================================================================
    public static void SubProceso_Hilo3() {
        new Thread(new Runnable() {
            public void run() {
               Calcular_AxB(T+1,N-1,0,T);
               Sw3 = true;
               Salida_Hilos();
            }
        }).start();
    }
    //================================================================
    public static void SubProceso_Hilo4() {
        new Thread(new Runnable() {
            public void run() {
               Calcular_AxB(T+1,N-1,T+1,N-1);
               Sw4 = true;
               Salida_Hilos();
            }
        }).start();
    }
    //================================================================
    public static void Calcular_AxB(int R1, int R2, int C1, int C2) {
        for(int i=R1;i<=R2;i++) {
            for(int j=C1;j<=C2;j++) {
                C[i][j] = 0;
                for(int k=0;k<=N-1;k++) {
                    C[i][j] = C[i][j] + A[i][k]*B[k][j];
                }
            }
        }
    }
    //================================================================
    public static void Imprimir_AxB() {
        System.out.println("\nMatriz AxB - Proceso Paralelo");
        System.out.println("---------------------------");
        for(int i=0;i<=N-1;i++) {
            for(int j=0;j<=N-1;j++) {
                System.out.print(C[i][j] + "\t");
            }
            System.out.println();
        }
    }
    //================================================================
    public static void main(String[] args) throws InterruptedException {
    int[] Dim_A = TSK.GetDimensionFile("A.TXT");
    int[] Dim_B = TSK.GetDimensionFile("B.TXT");
        if((Dim_A[0]==Dim_A[1])&&
           (Dim_B[0]==Dim_B[1])&&
           (Dim_A[1]==Dim_B[0])) {
            N = Dim_A[0];
            TSK.SetDimensionMatrix(N);
            T = (int)(N/2)-1;
            A = TSK.ReadFileMatrix("A.TXT");
            B = TSK.ReadFileMatrix("B.TXT");
            C = new long[N][N];
            Time1 = System.currentTimeMillis();
            SubProceso_Hilo1();
            SubProceso_Hilo2();
            SubProceso_Hilo3();
            SubProceso_Hilo4();
        }
        else {
           System.out.println("Error en Dimension en Archivo de Datos");
        }
    }
    //================================================================

}
