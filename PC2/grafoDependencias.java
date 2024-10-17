public class grafoDependencias {
    public static void main(String[] args) {
        System.out.println("Iniciando ejecución...");
        
        // Elige la implementación a ejecutar
        ejecutarSerial();
        ejecutarParalelo();
    }
    
    public static void ejecutarSerial() {
        System.out.println("Inicio del procesamiento serial...");
        
        try {
            tareaA();
            tareaB();
            tareaC();
            tareaD();
            tareaE();
            tareaF();
            tareaG();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Procesamiento serial completado.\n");
    }
    
    public static void ejecutarParalelo() {
        System.out.println("Inicio del procesamiento paralelo...");

        Thread tareaA = new Thread(new Tarea("A", 1000));
        Thread tareaB = new Thread(new Tarea("B", 1000));
        Thread tareaC = new Thread(() -> {
            try {
                tareaA.join();
                tareaB.join();
                System.out.println("Ejecutando Tarea C");
                Thread.sleep(1000); // tC
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread tareaD = new Thread(() -> {
            try {
                tareaC.join();
                System.out.println("Ejecutando Tarea D");
                Thread.sleep(1000); // tD
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread tareaE = new Thread(() -> {
            try {
                tareaA.join();
                tareaD.join();
                System.out.println("Ejecutando Tarea E");
                Thread.sleep(1000); // tE
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread tareaF = new Thread(() -> {
            try {
                tareaB.join();
                tareaE.join();
                System.out.println("Ejecutando Tarea F");
                Thread.sleep(1000); // tF
            } catch (InterruptedException e) {
                e.printStackTrace();
            } 
        });

        Thread tareaG = new Thread(() -> {
            try {
                tareaE.join();
                tareaF.join();
                System.out.println("Ejecutando Tarea G");
                Thread.sleep(1000); // tG
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Iniciar las tareas en paralelo según el grafo de dependencias
        tareaA.start();
        tareaB.start();
        tareaC.start();
        tareaD.start();
        tareaE.start();
        tareaF.start();
        tareaG.start();

        try {
            tareaG.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Procesamiento paralelo completado.\n");
    }

    // Métodos para el procesamiento serial
    public static void tareaA() throws InterruptedException {
        System.out.println("Ejecutando Tarea A");
        Thread.sleep(1000); // tA
    }

    public static void tareaB() throws InterruptedException {
        System.out.println("Ejecutando Tarea B");
        Thread.sleep(1000); // tB
    }

    public static void tareaC() throws InterruptedException {
        System.out.println("Ejecutando Tarea C");
        Thread.sleep(1000); // tC
    }
    
    public static void tareaD() throws InterruptedException {
        System.out.println("Ejecutando Tarea D");
        Thread.sleep(1000); // tD
    }
    
    public static void tareaE() throws InterruptedException {
        System.out.println("Ejecutando Tarea E");
        Thread.sleep(1000); // tE
    }
    
    public static void tareaF() throws InterruptedException {
        System.out.println("Ejecutando Tarea F");
        Thread.sleep(1000); // tF
    }

    public static void tareaG() throws InterruptedException {
        System.out.println("Ejecutando Tarea G");
        Thread.sleep(1000); // tG
    }
}

class Tarea implements Runnable {
    private String nombre;
    private int tiempo;

    public Tarea(String nombre, int tiempo) {
        this.nombre = nombre;
        this.tiempo = tiempo;
    }

    @Override
    public void run() {
        System.out.println("Ejecutando Tarea " + nombre);
        try {
            Thread.sleep(tiempo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
