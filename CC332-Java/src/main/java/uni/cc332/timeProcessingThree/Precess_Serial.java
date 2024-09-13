
package uni.cc332.timeProcessingThree;


public class Precess_Serial {
    private static Process_Task TSK = new Process_Task();
private static  int N;
private static int[][] A;
private static int[][] B;
private static long[][] C;
    //================================================================
    public static void Calcular_AxB() {
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
    public static void Imprimir_AxB() {
        System.out.println("\nMatriz AxB - Proceso Serial");
        System.out.println("---------------------------");
        for(int i=0;i<=N-1;i++) {
            for(int j=0;j<=N-1;j++) {
                System.out.print(C[i][j] + "\t");
            }
            System.out.println();
        }
    }
    //================================================================
    public static void main(String[] args) {
    int[] Dim_A = TSK.GetDimensionFile("A.TXT");
    int[] Dim_B = TSK.GetDimensionFile("B.TXT");
    long Time1,Time2,TIME_SERIAL;
        if((Dim_A[0]==Dim_A[1])&&
           (Dim_B[0]==Dim_B[1])&&
           (Dim_A[1]==Dim_B[0])) {
            N = Dim_A[0];
            TSK.SetDimensionMatrix(N);
            A = TSK.ReadFileMatrix("A.TXT");
            B = TSK.ReadFileMatrix("B.TXT");
            C = new long[N][N];
            Time1 = System.currentTimeMillis();
            Calcular_AxB();
            Time2 = System.currentTimeMillis();
            TIME_SERIAL = Time2 - Time1;
            System.out.println("Tiempo de Proceso Serial: " + TIME_SERIAL);
        }
        else {
           System.out.println("Error en Dimension en Archivo de Datos");
        }
    }
    //================================================================

}
