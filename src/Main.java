import java.io.File;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class Main {
    public static final int MAX_BUFFER_LENGTH = 10;
    public static final char EOF = '\0';

    public static void main(String[] args) {
        String inputFileName = "input.txt";
        String outputFileName = "output.txt";

        if (args.length == 2) {
            inputFileName = args[0];
            outputFileName = args[1];
        } else {
            try {
                new File(inputFileName).createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Queue<Character> buffer = new LinkedBlockingDeque<>(MAX_BUFFER_LENGTH);

        Thread readerThread = new Thread(new FileReaderTask(buffer, inputFileName));
        Thread writerThread = new Thread(new FileWriterTask(buffer, outputFileName));

        readerThread.start();
        writerThread.start();

        try {
            readerThread.join();
            writerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("File copy completed.");
    }
}