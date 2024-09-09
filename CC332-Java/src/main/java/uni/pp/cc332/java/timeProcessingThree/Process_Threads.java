/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uni.pp.cc332.java.timeProcessingThree;

/**
 *
 * @author Usuario
 */
public class Process_Threads {
    private static Process_Task TSK = new Process_Task();
    private static  int N;
    private static long Time11 = 0;
    private static long Time12 = 0;
    private static long Time21 = 0;
    private static long Time22 = 0;
    private static long Time31 = 0;
    private static long Time32 = 0;
    private static long Time41 = 0;
    private static long Time42 = 0;
    private static long TIME_THREAD1=0;
    private static long TIME_THREAD2=0;
    private static long TIME_THREAD3=0;
    private static long TIME_THREAD4=0;
    private static int[][] A;
    private static int[][] B;
    private static long[][] C1;
    private static long[][] C2;
    private static long[][] C3;
    private static long[][] C4;
    //================================================================
    public static void Calcular_AxB(long C[][]) {
        for(int i=0;i<=N-1;i++) {
            for(int j=0;j<=N-1;j++) {
                C[i][j] = 0;
                for(int k=0;k<=N-1;k++) {
                    C[i][j] = C[i][j] + A[i][k]*B[k][j];
                }
            }
        }
    }
    //================================================================
    //================================================================
    public static void SubProceso_Hilo1(long C1[][]) {
        new Thread(new Runnable() {
            public void run() {
               Time11 = System.currentTimeMillis();
               Calcular_AxB(C1);
               Time12 = System.currentTimeMillis();
               TIME_THREAD1 = Time12 - Time11;
               System.out.println("Tiempo de Proceso Hilo 1: " + TIME_THREAD1);
            }
        }).start();
    }
    //================================================================
    public static void SubProceso_Hilo2(long C2[][]) {
        new Thread(new Runnable() {
            public void run() {
               Time21 = System.currentTimeMillis();
               Calcular_AxB(C2);
               Time22 = System.currentTimeMillis();
               TIME_THREAD2 = Time22 - Time21;
               System.out.println("Tiempo de Proceso Hilo 2: " + TIME_THREAD2);
            }
        }).start();
    }
    //================================================================
    public static void SubProceso_Hilo3(long C3[][]) {
        new Thread(new Runnable() {
            public void run() {
               Time31 = System.currentTimeMillis();
               Calcular_AxB(C3);
               Time32 = System.currentTimeMillis();
               TIME_THREAD3 = Time32 - Time31;
               System.out.println("Tiempo de Proceso Hilo 3: " + TIME_THREAD3);
            }
        }).start();
    }
    //================================================================
    public static void SubProceso_Hilo4(long C4[][]) {
        new Thread(new Runnable() {
            public void run() {
               Time41 = System.currentTimeMillis();
               Calcular_AxB(C4);
               Time42 = System.currentTimeMillis();
               TIME_THREAD4 = Time42 - Time41;
               System.out.println("Tiempo de Proceso Hilo 4: " + TIME_THREAD4);
            }
        }).start();
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
            A = TSK.ReadFileMatrix("A.TXT");
            B = TSK.ReadFileMatrix("B.TXT");
            C1 = new long[N][N];
            C2 = new long[N][N];
            C3 = new long[N][N];
            C4 = new long[N][N];
            SubProceso_Hilo1(C1);
            SubProceso_Hilo2(C2);
            SubProceso_Hilo3(C3);
            SubProceso_Hilo4(C4);
        }
        else {
           System.out.println("Error en Dimension en Archivo de Datos");
        }
    }
    //================================================================

}
