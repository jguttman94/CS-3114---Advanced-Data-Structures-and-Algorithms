import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import student.TestCase;

/**
 * @author Jack Guttman
 * @version September 1, 2016
 */
public class MemmanTest extends TestCase {
    private String[] testArgs;
    private final ByteArrayOutputStream outContent = 
            new ByteArrayOutputStream();
    /**
     * Sets up the tests that follow. In general, used for initialization
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
    public void testMInit() {
        Memman mem = new Memman();
        assertNotNull(mem);               
    }
    
    /**
     * testing the initialization with arguments
     */
    public void testInitArgus() {
        Memman.main(new String[]{"20", "200", "P1sampleOutput.txt"});        
        assertEquals(Parser.initHashSize, 20);  
        assertEquals(Parser.initBlockSize, 200);
    }
    
    /**
     * testing initialization without proper num of args
     */
    public void testFailInit() {
        Memman.main(testArgs);
        assertEquals(outContent.toString(), 
                "Not enough parameters provided.\n");
    }
    
}
