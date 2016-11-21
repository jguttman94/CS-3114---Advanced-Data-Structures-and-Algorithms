import static org.junit.Assert.*;

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
     * @Test testing the proper prints for comdproc class
     */
    @Test
    public void test() {
        
        testComd.run(10, 32, ".\\P1sampleInput.txt");
        assertEquals(32, testComd.getInitBlockSize());
        assertEquals(10, testComd.getInitHashSize());
        assertNotNull(testComd.getComdProc());
        
    }
}