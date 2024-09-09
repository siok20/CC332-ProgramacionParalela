
package uni.pp.cc332.java.prueba1;

public class QR_Serial {
    
    public static void Process_Serial() {
    long Time1,Time2;
    double x;
    double[][] A1;
	A1 = DataSet.ReadFile(DataSet.filas, DataSet.columnas);
	Time1 = System.currentTimeMillis();
	Matrix A = new Matrix(A1);
	int filas = A.getRows();
	int columnas = A.getCols();
	Matrix R = new Matrix(new double[columnas][columnas]);
	for (int i = 0; i < columnas; i++) {
		for (int j = i; j < columnas; j++) {
			x = A.prodEsc(i, j) / Math.sqrt(A.prodEsc(i, i));
			R.SetCell(i, j, x);
		}
		for (int k = 0; k < filas; k++) {
			x = A.GetCell(k, i) / R.GetCell(i, i);
			A.SetCell(k, i, x);
		}
		for (int j = i + 1; j < columnas; j++) {
			for (int k = 0; k < filas; k++) {
				x = A.GetCell(k, j) - R.GetCell(i, j) * A.GetCell(k, i);
				A.SetCell(k, j, x);
			}
		}
	}
	DataSet.WriteFileHTML(A, "Matriz Q - Serial");
	DataSet.WriteFileHTML(R, "Matriz R - Serial");
	Time2 = System.currentTimeMillis();
        	System.out.printf("Tiempo de Procesamiento Serial: %d%n", (Time2-Time1));
}
//--------------------------------------------------------------
    //--------------------------------------------------------------
    public static void main(String[] args) {
            Process_Serial();
    }
    //--------------------------------------------------------------
    //--------------------------------------------------------------
    }//CLASS



