import student.TestCase;

/**
 * Test class for Double Linked list
 * @author Jack Guttman
 * @version October 20th, 2016
 */
public class LinkedQueTest extends TestCase {
    private LinkedQue linkTest;
    private Buffer buf1;
    private Buffer buf2;
    private Buffer buf3;
    private Buffer buf4;
    private Buffer buf5;
    
    /**
     * setsup by initializing a few buffers and a new linkedQue
     */
    public void setUp() {
        buf1 = new Buffer(new byte[5], 5);
        buf2 = new Buffer(new byte[5], 6);
        buf3 = new Buffer(new byte[5], 7);
        buf4 = new Buffer(new byte[5], 8);
        buf5 = new Buffer(new byte[5], 9);
        linkTest = new LinkedQue(5);
    }

    /**
     * tests that all field are proper values
     */
    public void testSetup() {
        assertEquals(linkTest.getSize(), 0);
        assertEquals(linkTest.curr, linkTest.tail);
        assertEquals(linkTest.head.getN(), linkTest.curr);
        assertEquals(linkTest.getMaxSize(), 5);
        linkTest.setMaxSize(6);
        assertEquals(linkTest.getMaxSize(), 6);
        linkTest.setMaxSize(5);
        linkTest.head.getN().setBuf(buf2);
        assertEquals(linkTest.head.getN().getBuf(), buf2);
    }
    
    /**
     * tests adding to the list in proper order
     */
    public void testAdd() {        
        linkTest.add(buf1);
        assertEquals(linkTest.getSize(), 1);
        assertEquals(linkTest.head.getN().getBuf(), buf1);
        assertEquals(linkTest.head.getN().getP(), linkTest.head);
        //add to full
        Buffer buf6 = new Buffer(new byte[5], 10);
        linkTest.add(buf2);
        linkTest.add(buf3);
        linkTest.add(buf4);
        linkTest.add(buf5);
        assertEquals(linkTest.getSize(), 5);
        linkTest.add(buf6);
        assertEquals(linkTest.getSize(), 6);
        assertEquals(linkTest.head.getN().getBuf(), buf6);
        assertEquals(linkTest.head.getN().getN().getBuf(), buf5);
        assertEquals(linkTest.head.getN().getN().getN().getBuf(), buf4);
        assertEquals(linkTest.head.getN().getN().getN().getN().getBuf(), buf3);
    }
    
    /**
     * test removing from tail
     */
    public void testRemove() {        
        linkTest.add(buf2);
        linkTest.add(buf3);
        linkTest.add(buf4);
        linkTest.add(buf5);
        linkTest.remove();
        assertEquals(linkTest.tail.getP().getBuf(), buf3);
        assertEquals(linkTest.getSize(), 3);
        linkTest.remove();
        linkTest.remove();
        assertEquals(linkTest.tail.getP().getBuf(), buf5);
        assertEquals(linkTest.getSize(), 1);
        linkTest.remove();
        assertEquals(linkTest.tail.getP(), linkTest.head);
        assertEquals(linkTest.getSize(), 0);
        assertEquals(linkTest.find(9), null);
        assertEquals(linkTest.remove(), null);
    }
    
    /**
     * test finding a specific buffer position in linked list
     */
    public void testFind() {
        linkTest.add(buf2);
        linkTest.add(buf3);
        linkTest.add(buf4);
        linkTest.add(buf5);
        assertEquals(linkTest.find(9), buf5);
        assertEquals(linkTest.find(8), buf4);
        assertEquals(linkTest.find(6), buf2);
        assertEquals(linkTest.find(-1), null);
    }

}
