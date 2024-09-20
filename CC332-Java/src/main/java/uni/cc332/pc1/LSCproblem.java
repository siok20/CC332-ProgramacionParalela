
package uni.cc332.pc1;

import java.util.concurrent.CyclicBarrier;

import uni.cc332.prueba1.Matrix;

public class LSCproblem {
     
    final static int T = 3;
    
    public static int result(String A, String B){
        return searchSerial(A,B);
    }
    
   
    
    public static void SubProceso_Hilo1(Matrix M, String A, String B) {
        new Thread(new Runnable() {
            public void run() {
                int i = 1;
                synchronized (M) {
                while(i<M.getRows()){
                    System.out.println("1");
                    for(int j=1; j<M.getCols(); j++){
                        if(A.charAt(i-1) == B.charAt(j-1)){
                            M.SetCell(i, j, (int)M.GetCell(i-1, j-1)+ 1);
                        }

                        else M.SetCell(i, j, Math.max(M.GetCell(i-1, j), M.GetCell(i, j-1)));
                    }
                    i += T;
                }
                }
            }
        }).start();
    }
    
    public static void SubProceso_Hilo2(Matrix M, String A, String B) {
        new Thread(new Runnable() {
            public void run() {
                int i = 2;
                synchronized (M) {
                while(i<M.getRows()){
                    System.out.println("2");
                    for(int j=1; j<M.getCols(); j++){
                        if(A.charAt(i-1) == B.charAt(j-1)){
                            M.SetCell(i, j, (int)M.GetCell(i-1, j-1)+ 1);
                        }

                        else M.SetCell(i, j, Math.max(M.GetCell(i-1, j), M.GetCell(i, j-1)));
                    }
                    i += T;
                } }
            }
        }).start();
    }
    
    public static void SubProceso_Hilo3(Matrix M, String A, String B) {
        new Thread(new Runnable() {
            public void run() {
                int i = 3;
                synchronized (M) {
                    while(i<M.getRows()){
                        System.out.println("3");
                        for(int j=1; j<M.getCols(); j++){
                            if(A.charAt(i-1) == B.charAt(j-1)){
                                M.SetCell(i, j, (int)M.GetCell(i-1, j-1)+ 1);
                            }

                            else M.SetCell(i, j, Math.max(M.GetCell(i-1, j), M.GetCell(i, j-1)));
                        }
                        i += T;
                    }
                }
            }
        }).start();
    }
    

    private static int searchSerial(String A, String B) {
        Matrix F = new Matrix(new double[A.length()+1][B.length()+1]);
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
    
    private static int searchParallel(String A, String B){
        Matrix F = new Matrix(new double[A.length()+1][B.length()+1]);
        int M = F.getRows();
        int N = F.getCols();
        
        for(int t=0; t<M; t++) F.SetCell(t, 0, 0);
        for(int t=0; t<N; t++) F.SetCell(0, t, 0);
                
        Thread[] threads = new Thread[T];
        CyclicBarrier barrier = new CyclicBarrier(T);
    for (int t = 0; t < T; t++) {
        final int threadId = t + 1;
        threads[t] = new Thread(new Oper1(F, A, B,t+1 ));
        threads[t].start();
    }
    
    try {
        for (int t = T-1; t >= 0; t--) {
            threads[t].join();
        }
    } catch (InterruptedException e) {
        System.out.println(e.getMessage());
    }

//        SubProceso_Hilo1(F,A,B);
//        SubProceso_Hilo2(F,A,B);
//        SubProceso_Hilo3(F,A,B);
        
        F.imprimir();
        
        return (int)F.GetCell(M-1, N-1);
    }
    
    static class Oper1 implements Runnable {
        private Matrix M;
        String A;
        String B;
        private int j;

        public Oper1(Matrix M, String A, String B ,int j) {
            this.M = M;
            this.A = A;
            this.B = B;
            this.j=j;
        }

        @Override
        public void run() {
            int i= 1;
            int t = j;
            
            while(t<M.getCols()){
                while(i<M.getRows() && j>0){
                    if (A.charAt(i-1) == B.charAt(j-1)) {
                        M.SetCell(i, j, (int)M.GetCell(i-1, j-1) + 1);
                    } else {
                        M.SetCell(i, j, Math.max(M.GetCell(i-1, j), M.GetCell(i, j-1)));
                    }
                    i++; j--;
                }   
                t+=T;
            }
            
        }
    }
    
    public static void main(String[] args){
        String A = "pawheae";
        String B = "heagawghee";
        
        System.out.print(searchParallel(A,B));
    }
          
    
}
