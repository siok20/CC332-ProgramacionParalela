import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
public class pregunta1 {
    

    public static void main(String[] args) {
        int cantFilas = 10;

        /* Tamaño de las columnas 
         * 1 10 100
         * 2 20 200
         * 3 30 300
         */
        int[] sizes = {1,2,3};
        int T = sizes.length* cantFilas; 

        tools.CreateDataFile("Datos1.TXT",T,'H',sizes);
        tools.sortDataFile("Datos1.TXT", 0, sizes);
        tools.searchDataFile("Datos1.TXT", 1, 19, sizes);
        tools.deleteRow("Datos1.TXT", 0, sizes);
    }

    public class tools{
        public static void CreateDataFile(String FILENAME, int N, char Mode, int[] SIZE) {
        int NUM;
        String STR;
        double a, b;
            try {
                FileWriter FW = new FileWriter(FILENAME);
                for(int k=0;k<=N-1;k++) {
                    a = Math.pow(10.0, SIZE[k%SIZE.length]-1);
                    b = Math.pow(10.0, SIZE[k%SIZE.length])-1;                
                    NUM = (int)a + (int)(Math.random()*(b-a+1));
                    
                    STR = NUM + "";

                    FW.write(STR + (Mode=='H'?"":"\n") );
                }
                FW.close();
            }
            catch (IOException E) {
                System.out.print(E.getMessage());
            }
        }

        public static void sortDataFile(String FILENAME, int column, int[] sizes) {
        try {
            RandomAccessFile raf = new RandomAccessFile(FILENAME, "r");
            FileWriter fw = new FileWriter("sort.txt");

            int bloque = Arrays.stream(sizes).sum();
            int cantFilas = (int) raf.length() / bloque;

            List<String> rows = new ArrayList<>();
            for (int i = 0; i < cantFilas; i++) {
                rows.add(readRow(raf, bloque, i));
            }

            rows.sort(Comparator.comparing(row -> extractColumnValue(row, column, sizes)));

            for (String row : rows) {
                fw.write(row + "\n");
            }

            fw.close();
            raf.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void sortDataFileParallel(String FILENAME, int column, int[] sizes) {
        try {
            RandomAccessFile raf = new RandomAccessFile(FILENAME, "r");
    
            int bloque = Arrays.stream(sizes).sum();
            int cantFilas = (int) raf.length() / bloque;
            int numThreads = 4; 
            int filasPorThread = cantFilas / numThreads;
    
            List<List<String>> sortedSegments = new ArrayList<>();
            Thread[] threads = new Thread[numThreads];
    
            for (int i = 0; i < numThreads; i++) {
                int start = i * filasPorThread;
                int end = (i == numThreads - 1) ? cantFilas : start + filasPorThread;
    
                threads[i] = new Thread(() -> {
                    try {
                        List<String> rows = new ArrayList<>();
                        for (int j = start; j < end; j++) {
                            rows.add(readRow(raf, bloque, j));
                        }
                        rows.sort(Comparator.comparing(row -> extractColumnValue(row, column, sizes)));
                        synchronized (sortedSegments) {
                            sortedSegments.add(rows);
                        }
                    } catch (IOException e) {
                        System.out.println("Error en la lectura: " + e.getMessage());
                    }
                });
                threads[i].start();
            }
    
            for (Thread thread : threads) {
                thread.join();
            }
    
            List<String> finalList = sortedSegments.stream()
                    .flatMap(List::stream)
                    .sorted(Comparator.comparing(row -> extractColumnValue(row, column, sizes)))
                    .toList();
    
            FileWriter fw = new FileWriter("sorted_" + FILENAME);
            for (String row : finalList) {
                fw.write(row + "\n");
            }
    
            fw.close();
            raf.close();
        } catch (IOException | InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    

    public static String searchDataFile(String FILENAME, int column, int targetValue, int[] sizes) {
        try {
            RandomAccessFile raf = new RandomAccessFile(FILENAME, "r");

            int bloque = Arrays.stream(sizes).sum();
            int cantFilas = (int) raf.length() / bloque;

            for (int i = 0; i < cantFilas; i++) {
                String row = readRow(raf, bloque, i);
                int columnValue = extractColumnValue(row, column, sizes);

                if (columnValue == targetValue) {
                    return "Valor encontrado en la fila " + i + ": " + row;
                }
            }

            raf.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return "Valor no encontrado.";
    }
    public static String searchDataFileParallel(String FILENAME, int column, int targetValue, int[] sizes) {
        try {
            RandomAccessFile raf = new RandomAccessFile(FILENAME, "r");
    
            int bloque = Arrays.stream(sizes).sum();
            int cantFilas = (int) raf.length() / bloque;
            int numThreads = 4;
            int filasPorThread = cantFilas / numThreads;
    
            Thread[] threads = new Thread[numThreads];
            String[] result = new String[1]; 
    
            for (int i = 0; i < numThreads; i++) {
                int start = i * filasPorThread;
                int end = (i == numThreads - 1) ? cantFilas : start + filasPorThread;
    
                threads[i] = new Thread(() -> {
                    try {
                        for (int j = start; j < end; j++) {
                            String row = readRow(raf, bloque, j);
                            int columnValue = extractColumnValue(row, column, sizes);
                            if (columnValue == targetValue) {
                                synchronized (result) {
                                    result[0] = "Valor encontrado en la fila " + j + ": " + row;
                                    return;
                                }
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Error en la búsqueda: " + e.getMessage());
                    }
                });
                threads[i].start();
            }
    
            for (Thread thread : threads) {
                thread.join();
            }
    
            raf.close();
            return result[0] != null ? result[0] : "Valor no encontrado.";
        } catch (IOException | InterruptedException e) {
            return "Error en la búsqueda.";
        }
    }
    

    public static void insertRow(String fileName, String newRow, int position, int[] sizes) {
        try {
            RandomAccessFile raf = new RandomAccessFile(fileName, "r");
            FileWriter tempFile = new FileWriter("temp.txt");

            int bloque = Arrays.stream(sizes).sum(); 
            int cantFilas = (int) raf.length() / bloque;

            for (int i = 0; i < position; i++) {
                tempFile.write(readRow(raf, bloque, i) + "\n");
            }
            tempFile.write(newRow + "\n");

            for (int i = position; i < cantFilas; i++) {
                tempFile.write(readRow(raf, bloque, i) + "\n");
            }

            tempFile.close();
            raf.close();

            replaceOriginalFile("temp.txt", fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteRow(String fileName, int position, int[] sizes) {
        try {
            RandomAccessFile raf = new RandomAccessFile(fileName, "r");
            FileWriter tempFile = new FileWriter("temp.txt");

            int bloque = Arrays.stream(sizes).sum();
            int cantFilas = (int) raf.length() / bloque;

            for (int i = 0; i < cantFilas; i++) {
                if (i != position) {
                    tempFile.write(readRow(raf, bloque, i) + "\n");
                }
            }

            tempFile.close();
            raf.close();

            replaceOriginalFile("temp.txt", fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void modifyRow(String fileName, String newRow, int position, int[] sizes) {
        try {
            RandomAccessFile raf = new RandomAccessFile(fileName, "r");
            FileWriter tempFile = new FileWriter("temp.txt");

            int bloque = Arrays.stream(sizes).sum();
            int cantFilas = (int) raf.length() / bloque;

            for (int i = 0; i < cantFilas; i++) {
                if (i == position) {
                    tempFile.write(newRow + "\n"); 
                } else {
                    tempFile.write(readRow(raf, bloque, i) + "\n");
                }
            }

            tempFile.close();
            raf.close();

            replaceOriginalFile("temp.txt", fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readRow(RandomAccessFile raf, int bloque, int fila) throws IOException {
        byte[] buffer = new byte[bloque];
        raf.seek(fila * bloque);
        raf.readFully(buffer);
        return new String(buffer).trim();
    }

    public static int extractColumnValue(String row, int column, int[] sizes) {
        int start = 0;
        for (int i = 0; i < column; i++) {
            start += sizes[i];
        }
        int end = start + sizes[column];
        return Integer.parseInt(row.substring(start, end).trim());
    }
    public static void replaceOriginalFile(String tempFileName, String originalFileName) {
        try {
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        
    }
}
