import java.io.RandomAccessFile;

public class RuleExtraction {
    private static final int RECORD_LENGTH = 100; 
    private static final int NUM_THREADS = 4; 
    private static String[] rules = new String[100];
    private static int ruleIndex = 0; 

    public static void main(String[] args) {
        String filePath = "dataset.txt"; 
        long fileLength;

        try (RandomAccessFile file = new RandomAccessFile(filePath, "r")) {
            fileLength = file.length();
            long recordCount = fileLength / RECORD_LENGTH;

            Thread[] threads = new Thread[NUM_THREADS];

            for (int i = 0; i < NUM_THREADS; i++) {
                long start = (recordCount / NUM_THREADS) * i;
                long end = (i == NUM_THREADS - 1) ? recordCount : (recordCount / NUM_THREADS) * (i + 1);
                
                threads[i] = new Thread(new RuleExtractor(filePath, start, end));
                threads[i].start(); 
            }

            for (int i = 0; i < NUM_THREADS; i++) {
                threads[i].join(); 
            }

            for (int i = 0; i < ruleIndex; i++) {
                System.out.println(rules[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class RuleExtractor implements Runnable {
        private String filePath;
        private long start;
        private long end;

        public RuleExtractor(String filePath, long start, long end) {
            this.filePath = filePath;
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            try (RandomAccessFile file = new RandomAccessFile(filePath, "r")) {
                for (long i = start; i < end; i++) {
                    file.seek(i * RECORD_LENGTH);
                    byte[] buffer = new byte[RECORD_LENGTH];
                    file.readFully(buffer);
                    String record = new String(buffer, "UTF-8").trim();
                    if (!record.isEmpty()) {
                        extractRules(record); 
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void extractRules(String record) {
            synchronized (RuleExtraction.class) {
                if (ruleIndex < rules.length) {
                    rules[ruleIndex++] = "Regla extraida: " + record; 
                }
            }
        }
    }
}
