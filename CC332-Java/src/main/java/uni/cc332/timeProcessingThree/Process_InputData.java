
package uni.cc332.timeProcessingThree;

import java.util.Scanner;

public class Process_InputData {
    private static Process_Task TSK = new Process_Task();
    //================================================================
    public static void GenerarData() {
    int M,N;
        Scanner SCN = new Scanner(System.in);
        System.out.println("==============================");
        System.out.println("Creacion de DataSet en Archivo");
        System.out.println("==============================");
        System.out.print  ("Ingrese Numero de Filas/Columnas: ");
        N = SCN.nextInt();
        TSK.CreateFileMatrix("A.TXT",N);
        TSK.CreateFileMatrix("B.TXT",N);
    }
    //================================================================
    public static void main(String[] args) {
       GenerarData();
    }
    //================================================================

}
