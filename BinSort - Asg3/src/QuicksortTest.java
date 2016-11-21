import java.io.IOException;
import java.io.RandomAccessFile;

import student.TestCase;

/**
 * @author Jack Guttman
 * @version October 20th, 2016
 */
public class QuicksortTest extends TestCase {
    private CheckFile check;
    private FileGenerator fileGen;
    /**
     * Sets up the tests that follow. In general, used for initialization.
     * @throws IOException
     */
    public void setUp() throws IOException {
        check = new CheckFile();
        fileGen = new FileGenerator();
        fileGen.generateFile(new String[] { "-a", "testGenerator", "1000" });
        RandomAccessFile gen = new RandomAccessFile("testGenerator", "rw");
        gen.close();
    }
    /**
     * Get code coverage of the class declaration.
     * @throws Exception
     */
    public void testQInit() throws Exception {
        Quicksort.main(new String[] { "", "10" });
        assertEquals(systemOut().getHistory(),
                "Not enough parameters provided.\n");
        systemOut().clearHistory();
        Quicksort.main(new String[] { "", "10", "" });
        assertEquals(systemOut().getHistory(), "No such file with name: \n");
        Quicksort.main(new String[] { "testGenerator", "100", "testOutput" });
        assertTrue(check.checkFile("testGenerator"));
        Quicksort tree = new Quicksort();
        assertNotNull(tree);
        Quicksort.main(null);
    }
    /**
     * tests binary
     */
    public void testBinary() throws Exception {
        fileGen.generateFile(new String[] { "-b", "testGenerator", "100" });
        Quicksort.main(new String[] { "testGenerator", "10", "testOutput" });
        assertTrue(check.checkFile("testGenerator"));
    }
    /** 
     * tests for code coverage
     */
    public void testCodeCov() throws Exception {
        fileGen.generateFile(new String[] { "-c", "testGenerator", "100" });
        assertNotNull(fileGen);
    }
}
