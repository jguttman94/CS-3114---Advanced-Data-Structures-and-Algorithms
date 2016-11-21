import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import student.TestCase;

/**
 * @author {Your Name Here}
 * @version {Put Something Here}
 */
public class SearchTreeTest extends TestCase {
    private String[] testArgs;
    private final ByteArrayOutputStream outContent = 
            new ByteArrayOutputStream();

    /**
     * Sets up the tests that follow. In general, used for initialization.
     */
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        testArgs = new String[2];
        testArgs[0] = "64";
        testArgs[1] = "256";
    }

    /**
     * Get code coverage of the class declaration.
     */
    public void testSInit() {
        SearchTree tree = new SearchTree();
        assertNotNull(tree);
    }

    /**
     * testing the initialization with arguments
     */
    public void testInitArgus() {
        SearchTree.main(new String[] { "10", "32", "P2_Input1_Sample.txt" });
        assertEquals(Parser.initHashSize, 10);
        assertEquals(Parser.initBlockSize, 32);
    }

    /**
     * testing initialization without proper num of args
     */
    public void testFailInit() {
        SearchTree.main(testArgs);
        assertEquals(outContent.toString(),
                "Not enough parameters provided.\n");
    }
}
