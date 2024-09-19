
package uni.cc332.pc1;

import uni.cc332.prueba1.Matrix;

public class LSCproblem {
    private static Matrix F;    
        
    
    public static int result(String A, String B){
        return search(A,B);
    }

    private static int search(String A, String B) {
        F = new Matrix(new double[A.length()+1][B.length()+1]);
        int M = F.getRows();
        int N = F.getCols();

        
        for(int i=0; i<M; i++){
            for(int j=0; j<N; j++){
                if(i==0 || j==0) F.SetCell(i, j, 0);
                
                else if(A.charAt(i-1) == B.charAt(j-1)) F.SetCell(i, j, (int)F.GetCell(i-1, j-1)+ 1);
               
                else F.SetCell(i, j, Math.max(F.GetCell(i-1, j), F.GetCell(i, j-1)));

            }
        }
        F.imprimir();
        
        return (int)F.GetCell(M-1, N-1);
    }
    
    
    public static void main(String[] args){
        String A = "cadbrz";
        String B = "asbz";
        
        System.out.print(result(A,B));
    }
          
    
}
