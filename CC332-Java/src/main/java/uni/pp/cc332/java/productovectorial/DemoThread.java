
package uni.pp.cc332.java.productovectorial;

import java.util.Random;

public class DemoThread {
    //--------------------------------------------
private static final int N = 50;
private static int A[][] = new int[N][N];
private static int B[][] = new int[N][N];
private static int C[][] = new int[N][N];
private static int i, j;

private static Random RND = new Random();
private static Tools  TLS = new Tools();

//--------------------------------------------
public static int[][] ProductoSerial(int MTX1[][], int MTX2[][]) {
int PROD[][] = new int[N][N];
	for(int i=0;i<=N-1;i++) {
		for(int j=0;j<=N-1;j++) {
            PROD[i][j] = 0;
			for(int k=0;k<=N-1;k++) {
				PROD[i][j] = PROD[i][j] + MTX1[i][k]*MTX2[k][j];
			}
		}
	}
	return PROD;
}
//--------------------------------------------
public final static void ProductoInterno(int i, int j) {
    C[i][j] = 0;
	for(int k=0;k<=N-1;k++) {
		C[i][j] = C[i][j] + A[i][k]*B[k][j];
	}
}
//--------------------------------------------
public static int[][] ProductoParalelo() {
int PROD[][] = new int[N][N];
int R; 
	for( i=0;i<=N-1;i++) {
		for( j=0;j<=N-1;j++) {
			R = (i + j) % 4;
            //- - - - - - - - - - - - - - - - - - - - - - - -
			if(R==0) {
               new Thread(new Runnable() {
                   public void run() {
                   	  ProductoInterno(i,j);
                   }
               }).start();
			}
			else if(R==1) {
               new Thread(new Runnable() {
                   public void run() {
                   	  ProductoInterno(i,j);
                   }
               }).start();
			}
			else if(R==2) {
               new Thread(new Runnable() {
                   public void run() {
                   	  ProductoInterno(i,j);
                   }
               }).start();
			}
			else {
               new Thread(new Runnable() {
                   public void run() {
                   	  ProductoInterno(i,j);
                   }
               }).start();
			}
            //- - - - - - - - - - - - - - - - - - - - - - - -





            
		}
	}
	return PROD;
}
//--------------------------------------------
public static void LoadData(int MTX[][], int AA, int BB) {
	for(int i=0;i<=N-1;i++) {
		for(int j=0;j<=N-1;j++) {
			MTX[i][j] = RND.nextInt(AA,BB+1);
		}
	}
}
//--------------------------------------------
public static void main(String args[]) {
	LoadData(A,-10,10);
	LoadData(B,-10,10);

	TLS.PrintMatrixToScreen(A,"Matriz A");
	TLS.PrintMatrixToScreen(B,"Matriz B");
	TLS.PrintMatrixToScreen(ProductoSerial(A,B),"Producto AxB");
	TLS.PrintMatrixToScreen(ProductoSerial(B,A),"Producto BxA");

	long inicio,fin;



	inicio = System.currentTimeMillis();
	C = ProductoSerial(A,B);
	fin = System.currentTimeMillis() - inicio;
	System.out.println("Time Execution: " + inicio / 1000 + " segundos");

	//TLS.PrintMatrixToFile(C,"Producto AxB - Serial","AxB.TXT");
	//TLS.PrintMatrixToHTML(ProductoSerial(A,B),"Producto AxB","AxB.HTML");

	inicio = System.currentTimeMillis();
	ProductoParalelo();
	fin = System.currentTimeMillis() - inicio;
	System.out.println("Time Execution: " + inicio / 1000 + " segundos");
	//TLS.PrintMatrixToFile(C,"Producto AxB","AxB - Paralelo.TXT");

	

}
//--------------------------------------------
//--------------------------------------------
}
