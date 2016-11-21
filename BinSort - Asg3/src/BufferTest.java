import student.TestCase;

/**
 * Test case for buffer class
 * @author Jack Guttman
 * @version October 20th, 2016
 */
public class BufferTest extends TestCase {

    private Buffer test;
    private byte[] testArray;
    /**
     * sets up testing, initializes new Buffer and array
     */
    public void setUp() {
        testArray = new byte[]{0, 1, 2};
        test = new Buffer(testArray, 15);
    }
    /**
     * basic testing to assert that values of fields are correct
     */
    public void test() {
        assertNotNull(test);
        assertEquals(test.getArray()[0], testArray[0]);
        assertEquals(test.getArray()[1], testArray[1]);
        assertEquals(test.getArray()[2], testArray[2]);
        assertEquals(test.getPos(), 15);
        assertEquals(test.getDirtyBit(), false);
        test.setDirtyBit(true);
        assertEquals(test.getDirtyBit(), true);
    }

}
