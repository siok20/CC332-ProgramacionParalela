
package uni.pp.cc332.java.timeProcessingThree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

public class Process_Task {
    private int N;
    private final int HH = 10;  //Block Size
    //=====================================================
    public Process_Task() {
        N = 0;
    }
    //=====================================================
    public Process_Task(int T) {
        N = T;
    }
    //=====================================================
    public String Replicate(char EE, int T) {
    String SS;
       SS = "";
       for(int i=0;i<=T-1;i++) {
           SS = SS + EE;
       }
       return SS;
    }
    //=====================================================
    public int[] GetDimensionFile(String DATAFILE) {
    int DIM[] = {0,0};
    String LINE;
        try {
          File FILE = new File(DATAFILE);
          Scanner SCN = new Scanner(FILE);
          DIM[0] = 0;
          while(SCN.hasNextLine()) {
             LINE = SCN.nextLine();
             DIM[0]++;
             DIM[1] = LINE.length()/HH;
          }
          SCN.close();
        }
        catch (FileNotFoundException e) {
           System.out.println("An error occurred.");
           e.printStackTrace();
        }
        return DIM;
    }
    //=====================================================
    public int GetDimensionMatrix() {
        return N;
    }
    //=====================================================
    public void SetDimensionMatrix(int T) {
        N = T;
    }
    //=====================================================
    public void PrintRandomMatrix(int[][] MTX, String TITLE) {
        System.out.println("\n" + TITLE);
        System.out.println(Replicate('-',TITLE.length()));
        for(int i=0;i<=N-1;i++) {
            for(int j=0;j<=N-1;j++) {
                System.out.print(MTX[i][j] + "\t");
            }
            System.out.println();
        }
    }
    //=====================================================
    public int[][] CreateRandomMatrix(int T) {
    int MTX[][] = new int[N][N];
    Random RDM = new Random();
        N = T;
        for(int i=0;i<=N-1;i++) {
            for(int j=0;j<=N-1;j++) {
                MTX[i][j] = 10 + RDM.nextInt(90);
                }
        }
        return MTX;
    }
    //=====================================================
    public void CreateFileMatrix(String DATAFILE, int T) {
    Random RDM = new Random();  
    PrintWriter PW = null;
    long EE;
    String SS;
        N = T;
        try {
            PW = new PrintWriter(DATAFILE);
            for(int i=0;i<=N-1;i++) {
                for(int j=0;j<=N-1;j++) {
                    EE = 10 + RDM.nextInt(90);
                    SS = "" + EE + "";
                    PW.print(Replicate(' ',HH-SS.toString().length()) + SS);
                }
                PW.println("");
            }
        }
        catch(FileNotFoundException ex) {
              System.out.println(ex.getMessage());
        }
        finally {
          PW.close();
        }
    }
    //=====================================================
    public int[][] ReadFileMatrix(String DATAFILE) {
    int MTX[][] = new int[N][N];
    int i,j,LL;
    String LINE;
        try {
          File FILE = new File(DATAFILE);
          Scanner SCN = new Scanner(FILE);
          i = 0;
          while(SCN.hasNextLine()) {
            LINE = SCN.nextLine();
            LL = LINE.length();
            j = 0;
            while(HH*j<=LL-1) {
                MTX[i][j] = Integer.parseInt(LINE.substring(HH*j,HH*j+HH).trim());
                j++;
            }
            i++;
          }
          SCN.close();
        }
        catch (FileNotFoundException e) {
           System.out.println("An error occurred.");
           e.printStackTrace();
        }
        return MTX;
    }
    //=====================================================
}



