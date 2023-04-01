import java.io.FileWriter;
import java.io.IOException;
import java.util.Queue;

public class FileWriterTask implements Runnable{
    private final Queue<Character> buffer;
    private final String outputFile;

    /**
     * File writer task constructor
     * @param queue {@link Queue} that is used as a buffer for writing to OutputFile
     * @param outputFile Name of the output file used for writing
     */
    public FileWriterTask(Queue<Character> queue, String outputFile) {
        buffer = queue;
        this.outputFile = outputFile;
    }

    @Override
    public void run() {
        try (FileWriter writer = new FileWriter(outputFile)) {
            while (true) {
                synchronized (buffer) {
                    while (buffer.size() == 0 || buffer.peek() == null) {
                        if (buffer.peek() != null && buffer.peek() == Main.EOF) {
                            break;
                        }
                        buffer.wait();
                    }
                    if (buffer.peek() == Main.EOF) {
                        break;
                    }
                    char character = buffer.poll();
                    //System.out.println("wrote char: " + character);
                    writer.write(character);
                    buffer.notifyAll();
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing output file: " + e.getMessage());
            System.exit(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

