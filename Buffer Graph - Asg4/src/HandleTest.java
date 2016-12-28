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
        Handle myHandle = new Handle(1);
        Handle lessHandle = new Handle(2);
        Handle sameHandle = new Handle(1);
        Handle moreHandle = new Handle(0);
        assertEquals(myHandle.compareTo(lessHandle), -1);
        assertEquals(myHandle.compareTo(sameHandle), 0);
        assertEquals(myHandle.compareTo(moreHandle), 1);
        assertEquals(myHandle.getStartPos(), 1);
        assertEquals(myHandle.toString(), "1");
    }

}
