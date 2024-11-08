import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Bsecuencial {
private   final String FILENAME = "DATOS.TXT";
private   int N = 100;
private   String KEY = "2127251533";
private   String CADENA;
private   final int BLOCK = 11;
private   byte[] RECORD = new byte[BLOCK];
private   final int numThreads = 4;
private long[] times = new long[numThreads];  
//

    //------------------------------------------------
    private   String GetString() {
    String CAD;
        CAD = "";
        for(int i=0;i<=BLOCK-2;i++) {
            CAD = CAD + (char)(RECORD[i]);
        }
        return CAD;
    }
    //------------------------------------------------
    private void PrintRecord() {
        CADENA = "";
        for(int i=0;i<=10;i++) {
            CADENA = CADENA + (char)(RECORD[i]);
        }
        System.out.println(CADENA);
    }
    //------------------------------------------------
    private   void ProcesoSerial() {
    long n,P,T,Time1,Time2;
    int i;
        try {
             RandomAccessFile RAF = new RandomAccessFile(FILENAME,"r");
             T = RAF.length();
             n = T/BLOCK;
             P = -1;
             i = 0;
             while((i<=n-1)&&(P==-1)) {
                 RAF.seek(i*BLOCK);
                 RAF.read(RECORD);
                 CADENA = GetString();
                 if(CADENA.equals(KEY)==true) {
                    P = i;
                 }
                 i++;
             }
             RAF.close();
             if(P>=0) {
                System.out.println("Elemento " + KEY + " Existente en la Posicion " + P);
             }
             else {
                System.out.println("Elemento " + KEY + " No Existe");
             }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public   String ReadFile() {
    String LINE="",CADENA="";
       try {
         File FILE = new File(FILENAME);
         BufferedReader BR = new BufferedReader(new FileReader(FILE));
         while ((LINE = BR.readLine()) != null) {
           CADENA = CADENA + LINE;
         }
       }
       catch(IOException e) {
       }
       return CADENA;
    }

    public   void WriteData() {
    double X;
    long num;
        try {
            FileWriter FW = new FileWriter(FILENAME);
            for(int i=1;i<=N;i++) {
                int p =  BLOCK-2;
                X = Math.random()*9 * Math.pow(10, p);
                num = (long) (1.0 * Math.pow(10, p) + (double)X);
                FW.write(num + " ");
            }
            FW.write("01234567890");
            FW.close();
        }
        catch (IOException E) {
            System.out.print(E.getMessage());
        }
    }

    public void ProcesoParalelo() {
        Thread[] threads = new Thread[numThreads];
        int n = N/BLOCK;
        int r = N%BLOCK;
        int P = -1;
        
        for (int t = 0; t < numThreads; t++) {
            final int hilo = t;
            final int ini = t * n;  
            final int fin = (t == numThreads - 1) ? N : ini + r;

            threads[t] = new Thread(new Runnable() {
                @Override
                public void run() {
                    times[hilo] = System.currentTimeMillis();
                    long n,P,T;
                    int i;
                        try {
                            RandomAccessFile RAF = new RandomAccessFile(FILENAME,"r");
                            T = RAF.length();
                            n = T/BLOCK;
                            P = -1;
                            i = ini;
                            while((i<=fin)&&(P==-1)) {
                                RAF.seek(i*BLOCK);
                                RAF.read(RECORD);
                                CADENA = GetString();
                                if(CADENA.equals(KEY)==true) {
                                    P = i;
                                }
                                i++;
                            }
                            RAF.close();
                            if(P>=0) {
                                System.out.println("Hilo " + hilo + " encontró");
                                System.out.println("Elemento " + KEY + " Existente en la Posicion " + P);
                            }
                            else {
                                System.out.println("Hilo " + hilo + " No encontró");
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    times[hilo] = System.currentTimeMillis() - times[hilo];
                }
            });
            threads[t].start();
            
        }

        try {
            for (int t = 0; t < numThreads; t++) {
                threads[t].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //--------------------------------------------------
    public static void main(String[] args) {
        Bsecuencial b = new Bsecuencial();
        //b.WriteData();

        long startTimeParallel = System.currentTimeMillis();
        b.ProcesoParalelo();
        long endTimeParallel = System.currentTimeMillis();
        System.out.println("Tiempo de ejecucion total (Paralelo): " + (endTimeParallel - startTimeParallel) + " ms");
        for (int i = 0; i <b.numThreads; i++) {
            System.out.println("\tTiempo del hilo " + i + ": " + b.times[i]);
        }

        long startTimeSerial = System.currentTimeMillis();
        b.ProcesoSerial();
        long endTimeSeriall = System.currentTimeMillis();
        System.out.println("Tiempo de ejecucion total (Paralelo): " + (endTimeSeriall - startTimeSerial) + " ms");

    }

    
}