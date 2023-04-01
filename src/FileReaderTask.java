import java.io.*;
import java.util.Queue;
public class FileReaderTask implements Runnable {

    private final static int END_OF_STREAM = -1;
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
            int character;
            while (true) {
                synchronized (buffer) {
                    while (buffer.size() >= Main.MAX_BUFFER_LENGTH) {
                        buffer.wait();
                    }
                    character = reader.read();
                    if (character == END_OF_STREAM) {
                        buffer.offer(Main.EOF);
                    } else {
                        //System.out.println("read char: " + (char) character);
                        buffer.offer((char) character);
                    }
                    buffer.notifyAll();
                    if (character == END_OF_STREAM) {
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


