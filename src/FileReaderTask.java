import java.io.*;
import java.util.Queue;
public class FileReaderTask implements Runnable {
    private final Queue<Character> buffer;
    private final String inputFile;

    /**
     * File reader task constructor
     * @param queue {@link Queue} that is used as a buffer for reading characters to InputFile
     * @param inputFile Name of the input file used for reading
     */
    public FileReaderTask(Queue<Character> queue, String inputFile) {
        buffer = queue;
        this.inputFile = inputFile;
    }

    @Override
    public void run() {
        try (FileReader reader = new FileReader(inputFile)) {
            int c;
            while (true) {
                synchronized (buffer) {
                    while (buffer.size() >= Main.MAX_BUFFER_LENGTH) {
                        buffer.wait();
                    }
                    c = reader.read();
                    if (c == -1) {
                        buffer.offer(Main.EOF);
                    } else {
                        //System.out.println("read char: " + (char) c);
                        buffer.offer((char) c);
                    }
                    buffer.notifyAll();
                    if (c == -1) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            System.exit(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}


