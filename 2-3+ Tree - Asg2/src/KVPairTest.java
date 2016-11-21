import student.TestCase;

/**
 * Test the KVPair class.
 * 
 * @author CS3114 Instructor and TAs
 * @version 9/16/2016
 */

public class KVPairTest extends TestCase {
    /**
     * Set up the tests that follow.
     */
    public void setUp() {
        // Nothing Here.
    }
    
    /**
     * Test the KVPair class.
     */
    public void testK() {
        Handle zero = new Handle(0);
        Handle first = new Handle(1);
        Handle second = new Handle(2);
        KVPair myKV = new KVPair(first, second);
        KVPair lessKV = new KVPair(second, second);
        KVPair sameKV = new KVPair(first, second);
        KVPair moreKV = new KVPair(zero, second);
        assertEquals(myKV.compareTo(lessKV), -1);
        assertEquals(myKV.compareTo(sameKV), 0);
        assertEquals(myKV.compareTo(moreKV), 1);
        assertEquals(myKV.compareTo(first), 0);
        assertEquals(myKV.key(), first);
        assertEquals(myKV.value(), second);
        assertEquals(myKV.toString(), "1 2");
    }
}