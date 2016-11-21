import student.TestCase;

/**
 * Handle test class
 * @author Jack Guttman
 * @version September 1, 2016
 *
 */
public class HandleTest extends TestCase {

    private Handle testHandle;
    /**
     *Initialization for testing
     */
    public void setUp() {
        testHandle = new Handle(0);
    }

    /**
     * tests setup
     */
    public void testSetup() {
        assertEquals(testHandle.getStartPos(), 0);
    }

}
