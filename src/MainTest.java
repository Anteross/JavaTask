import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class MainTest {

    private static final String INPUT_FILE_NAME = "inputTest.txt";
    private static final String OUTPUT_FILE_NAME = "outputTest.txt";
    private static final String INPUT_FILE_CONTENTS = "Hello, world!";

    @org.junit.Test
    public void testFileCopy() throws IOException, InterruptedException {
        // Create an input file
        File inputFile = new File(INPUT_FILE_NAME);
        FileWriter inputWriter = new FileWriter(inputFile);
        inputWriter.write(INPUT_FILE_CONTENTS);
        inputWriter.close();

        // Run the file copy program
        Main.main(new String[]{INPUT_FILE_NAME, OUTPUT_FILE_NAME});

        // Verify that the output file was created and has the same contents as the input file
        File outputFile = new File(OUTPUT_FILE_NAME);
        assertTrue(outputFile.exists());
        FileReader outputReader = new FileReader(outputFile);
        StringBuilder outputContentsBuilder = new StringBuilder();
        int c;
        while ((c = outputReader.read()) != -1) {
            outputContentsBuilder.append((char) c);
        }
        outputReader.close();
        String outputContents = outputContentsBuilder.toString();
        assertEquals(INPUT_FILE_CONTENTS, outputContents);

        // Clean up the input and output files
        inputFile.delete();
        outputFile.delete();
    }

}