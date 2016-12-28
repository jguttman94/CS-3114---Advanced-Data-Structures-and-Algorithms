import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import student.TestCase;

/**
 * 
 */

/**
 * @author Jack Guttman
 * @version September 1st, 2016
 *
 */
public class ParserTest extends TestCase {
    
    //----------------------------------------------------------------------
    /**
     * new Comd proc object
     */
    private Parser testComd;
    
    
    //----------------------------------------------------------------------
    /**
     * @Before setting the output stream to the byte array
     */
    @Before
    public void setUp() {
        testComd = new Parser();
    }
    
    //----------------------------------------------------------------------
    /**
     * @throws IOException 
     * @Test testing the proper prints for comdproc class
     */
    @Test
    public void test() throws IOException {
        
        testComd.run("P4mem1", 10, 32, 10, ".\\P4sampleInput", 
                "Stats.txt");
        assertEquals(32, testComd.getInitBlockSize());
        assertEquals(10, testComd.getInitHashSize());
        assertNotNull(testComd.getComdProc());
        
    }
}