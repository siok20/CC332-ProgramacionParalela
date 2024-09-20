public class Busqueda {
    private Matrix A;
    private final int N = 100;  
    private final int HILOS = 3;  
    private long[] times = new long[HILOS];  
    private long time; 
    private long timeP;  

    public static void main(String[] args) {
        Busqueda bsq = new Busqueda();
        double target = 5.71;  

        int c1 = bsq.search(target);
        
        int c2 = bsq.searchParallel(target);

        System.out.println("Tiempo paralelo: " + bsq.timeP);
        for (int i = 0; i < bsq.HILOS; i++) {
            System.out.println("\tTiempo del hilo " + i + ": " + bsq.times[i]);
        }
        System.out.println("\tEncontrado en la posición : " + c2 + "indice: ("+ c2/bsq.N+ ","+c2%bsq.N+")" );

        System.out.println("Tiempo serial: " + bsq.time);
        System.out.println("\tEncontrado en la posición : " + c1 + "indice: ("+ c1/bsq.N+ ","+c1%bsq.N+")" ) ;
    }

    public Busqueda() {
        this.A = new Matrix(DataSet.ReadFile(N, N));
    }

    public int search(double target) {
        time = System.currentTimeMillis();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (A.GetCell(i, j) == target) {
                    time = System.currentTimeMillis() - time;
                    return i * N + j;  
                }
            }
        }
        time = System.currentTimeMillis() - time;
        return -1;
    }

    public int searchParallel(double target) {
        timeP = System.currentTimeMillis();
        Thread[] threads = new Thread[HILOS];
        int[] result = new int[HILOS];  
        int part = N / HILOS;  
        int rest = N % HILOS;  

        for (int i = 0; i < HILOS; i++) {
            result[i] = -1;  
        }

        for (int t = 0; t < HILOS; t++) {
            final int hilo = t;
            final int ini = t * part;  
            final int fin = (t == HILOS - 1) ? N : ini + rest;  

            threads[t] = new Thread(new Runnable() {
                @Override
                public void run() {
                    times[hilo] = System.currentTimeMillis();
                    for (int i = ini; i < fin; i++) {
                        for (int j = 0; j < N; j++) {
                            if (A.GetCell(i, j) == target) {
                                result[hilo] = i * N + j;  
                                return;  
                            }
                        }
                    }
                    times[hilo] = System.currentTimeMillis() - times[hilo];
                }
            });
            threads[t].start();
        }

        // Esperar a que todos los hilos terminen
        try {
            for (int t = 0; t < HILOS; t++) {
                threads[t].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Combinar los resultados de los hilos
        for (int t = 0; t < HILOS; t++) {
            if (result[t] != -1) {
                timeP = System.currentTimeMillis() - timeP;
                return result[t];  // Retorna el resultado encontrado por algún hilo
            }
        }

        timeP = System.currentTimeMillis() - timeP;
        return -1;  // No se encontró el valor
    }
}
