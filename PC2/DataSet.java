import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;

public class DataSet {
    static int filas = 5;
    static int columnas = 5;
    static String DATAFILE = "Datos.TXT";
    //------------------------------------------------------
    public static void CreateFile(int M, int N) {
        Random random = new Random();
        PrintWriter pw = null;
        
        try {   
            pw = new PrintWriter(DATAFILE);
            for (int i = 0; i < M; i++) {
                for (int j = 0; j < N; j++) {
                    double valor = random.nextDouble() * 10;
                    pw.printf("%10.2f", valor);
                }
                pw.println("");
            }
            
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
            
        } finally {
            pw.close();
        }
    }
    //------------------------------------------------------
    public static double[][] ReadFile(int M, int N) {
    double[][] MTX = new double[M][N];
    Path filePath = Paths.get(DATAFILE);
    Scanner scanner = null;
            try {
                    scanner = new Scanner(filePath);
            }
            catch (IOException ex) {
                    System.out.println(ex.getMessage());
             }   
            int i = 0, j = 0;
            while (scanner.hasNext()) {
                    if (scanner.hasNextDouble()) {
                            MTX[i][j] = scanner.nextDouble();
                            j++;
                            if (j == N) {
                                    j = 0;
                                    i++;
                            }
                    }
                    else {
                            scanner.next();
                    }
            }
            return MTX;
    }
    //------------------------------------------------------
    public static void WriteFile(Matrix M, String archivo){
    int mm = M.getRows();
    int nn = M.getCols();
            PrintWriter pw = null;
            try {
                    pw = new PrintWriter(archivo);
                    for (int i = 0; i < mm; i++) {
                            for (int j = 0; j < nn; j++) {
                                    double valor = M.GetCell(i, j);
                                    pw.printf("%.2f", valor);
                            }
                            pw.println("");
                    }
            }
            catch (FileNotFoundException ex) {
                    System.out.println(ex.getMessage());
            }
            finally {
                    pw.close();
            }
    }
    //------------------------------------------------------
    public static void WriteFileHTML(Matrix M, String archivo){
    int mm = M.getRows();
    int nn = M.getCols();
        System.out.println(mm);
        System.out.println(nn);

        PrintWriter pw = null;
        try {
                pw = new PrintWriter(archivo + ".html");
                pw.println("<!DOCTYPE html>");
                pw.println("<HTML>");
                pw.println("<HEAD><title>"+archivo+"</title></HEAD>");                    
                pw.println("<BODY>");
                pw.println("<h1>"+archivo+" </h1>");
                pw.println("<table >");
                for (int i = 0; i < mm; i++) {
                    pw.print("<tr>");
                    for (int j = 0; j < nn; j++) {
                        pw.print("<td>");
                            double valor = M.GetCell(i, j);
                            pw.printf("%.2f</td>", valor);
                    }
                    pw.println("</tr>");
                }
                pw.print("</table></body></html>");
        }
        catch (FileNotFoundException ex) {
                System.out.println(ex.getMessage());
        }
        finally {
                pw.close();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CreateFile(filas, columnas);
    }
}
