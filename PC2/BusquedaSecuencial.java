import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

public class BusquedaSecuencial {
private static String FILENAME = "DATOS.TXT";
private static int    N = 10;
private static String KEY = "01234567890";
private static String CADENA;
private static final int BLOCK = 11;
private static final byte[] RECORD = new byte[BLOCK];
//

    //------------------------------------------------
    private static String GetString() {
    String CAD;
        CAD = "";
        for(int i=0;i<=10;i++) {
            CAD = CAD + (char)(RECORD[i]);
        }
        return CAD;
    }
    //------------------------------------------------
    /*private static void PrintRecord() {
        CADENA = "";
        for(int i=0;i<=10;i++) {
            CADENA = CADENA + (char)(RECORD[i]);
        }
        System.out.println(CADENA);
    }*/
    //------------------------------------------------
    private static void ProcesoSerial() {
    long n,P,T,Time1,Time2;
    int i;
        try {
             RandomAccessFile RAF = new RandomAccessFile(FILENAME,"r");
             Time1 = System.currentTimeMillis();
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
             Time2 = System.currentTimeMillis();
             System.out.printf("Tiempo de Procesamiento Serial: %d%n", (Time2-Time1));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static String ReadFile() {
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

    public static void WriteData() {
    double X;
    long num;
        try {
            FileWriter FW = new FileWriter(FILENAME);
            for(int i=1;i<=N-1;i++) {
                X = Math.random()*9.0;
                num = 1 + (long)X;
                FW.write(""+num);
            }
            //FW.write("01234567890");
            FW.close();
        }
        catch (IOException E) {
            System.out.print(E.getMessage());
        }
    }


    //--------------------------------------------------
    public static void main(String[] args) {
      WriteData();
      ProcesoSerial();
    }

}