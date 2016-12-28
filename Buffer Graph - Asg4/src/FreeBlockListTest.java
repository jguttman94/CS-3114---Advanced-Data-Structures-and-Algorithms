import student.TestCase;
/**
 * @author Jack Guttman
 * @version September 1, 2016
 * Test class for freeblocklist
 */
public class FreeBlockListTest extends TestCase {

    private FreeBlockList testFbl;
    //----------------------------------------------------------------------
    /**
     * initialization for freeblocklist test
     */
    public void setUp() {
        testFbl = new FreeBlockList();
    }
    //----------------------------------------------------------------------
    /**
     * tests setup
     */
    public void testSetup() {
        assertNotNull(testFbl.getHead());
        assertNotNull(testFbl.getTail());
        assertNotNull(testFbl.getCurr());
        assertEquals(testFbl.getHead().next(), testFbl.getTail());
        assertEquals(testFbl.getTail().prev(), testFbl.getHead());
        assertEquals(testFbl.getCurr(), testFbl.getTail());
        assertEquals(testFbl.getCurr().prev(), testFbl.getHead());
        assertTrue(testFbl.isEmpty());
    }
    //----------------------------------------------------------------------
    /**
     * tests insertion
     */
    public void testInsert() {
        testFbl.insert(0, 32);
        assertTrue(testFbl.getCurr().index().equals(0));
        assertTrue(testFbl.getCurr().end().equals(32));
        assertEquals(testFbl.getSize(), 1);
        
        testFbl.insert(40, 60);
        testFbl.insert(70, 90);
        testFbl.insert(100, 120);
        assertTrue(testFbl.getCurr().index().equals(100));
        assertTrue(testFbl.getCurr().end().equals(120));
        assertEquals(testFbl.getSize(), 4);
    }
    //----------------------------------------------------------------------
    /**
     * tests appending
     */
    public void testAppend() {
        testFbl.append(60, 70);
        assertTrue(testFbl.getTail().prev().index().equals(60));
        assertTrue(testFbl.getHead().next().index().equals(60));
        testFbl.insert(0, 32);
        assertTrue(testFbl.getCurr().index().equals(0));
        testFbl.append(33, 35);
        assertTrue(testFbl.getCurr().index().equals(0));
        assertTrue(testFbl.getTail().prev().index().equals(33));
        testFbl.append(37, 39);
        testFbl.append(42, 50);
        assertTrue(testFbl.getTail().prev().index().equals(42));
        assertTrue(testFbl.getTail().prev().prev().index().equals(37));
        assertFalse(testFbl.isEmpty());
    }
    //----------------------------------------------------------------------
    /**
     * tests removal
     */
    public void testRemove() {
        assertEquals(testFbl.remove(), -1);
        testFbl.insert(45, 50);
        testFbl.insert(0, 32);
        testFbl.append(60, 70);
        assertTrue(testFbl.getCurr().index().equals(0));
        testFbl.remove();
        assertTrue(testFbl.getCurr().index().equals(45));
        assertTrue(testFbl.getHead().next().next().index().equals(60));
        assertEquals(testFbl.getSize(), 2);
        assertEquals(testFbl.remove(), 45);
        testFbl.remove();
        assertTrue(testFbl.isEmpty());
    }
    //----------------------------------------------------------------------
    /**
     * tests the set curr method
     */
    public void testSetCurr() {
        assertFalse(testFbl.setCurr(0));
        testFbl.insert(45, 50);
        testFbl.insert(0, 32);
        assertTrue(testFbl.getCurr().index().equals(0));
        assertTrue(testFbl.setCurr(45));
        assertFalse(testFbl.setCurr(60));
    }
    //----------------------------------------------------------------------
    /**
     * tests proper outcome of print method
     */
    public void testPrint() {
        assertNull(testFbl.print());
        testFbl.insert(0, 32);
        testFbl.append(33, 60);
        testFbl.append(61, 90);
        assertTrue(testFbl.print()[0].equals("(0,32)"));
        assertTrue(testFbl.print()[1].equals("(33,60)"));
        assertTrue(testFbl.print()[2].equals("(61,90)"));
    }
    //----------------------------------------------------------------------
    /**
     * tests that merge works properly
     */
    public void testMerge() {
        assertFalse(testFbl.merge());
        testFbl.insert(0, 32);
        testFbl.append(32, 33);
        testFbl.append(65, 35);
        testFbl.append(100, 50);
        testFbl.append(167, 24);
        testFbl.append(191, 1);
        testFbl.append(196, 1);
        testFbl.append(199, 101);
        assertTrue(testFbl.merge());
        assertEquals(testFbl.print()[0], "(0,150)");
        assertEquals(testFbl.print()[1], "(167,25)");
        testFbl.getHead().next().setIndex(32);
        testFbl.getHead().next().setEnd(0);
        assertTrue(testFbl.getHead().next().index().equals(32));
        assertTrue(testFbl.getHead().next().end().equals(0));
    }
    //----------------------------------------------------------------------
    /**
     * tests that probe works properly
     */
    public void testProbe() {
        assertEquals(testFbl.probe(20), -1);
        testFbl.insert(0, 32);
        testFbl.append(65, 100);
        assertEquals(testFbl.probe(25), 0);
        assertEquals(testFbl.probe(34), 65);
        assertEquals(testFbl.probe(200), -1);
    }
    //----------------------------------------------------------------------
    /**
     * tests insertion in correct ascending order
     */
    public void testInOrder() {
        assertEquals(0, testFbl.getSize());
        testFbl.insertInOrder(5, 25);
        testFbl.insertInOrder(1, 4);
        testFbl.insertInOrder(31, 200);
        assertTrue(testFbl.print()[0].equals("(1,4)"));
        assertTrue(testFbl.print()[1].equals("(5,25)"));
        assertTrue(testFbl.print()[2].equals("(31,200)"));
        testFbl.insertInOrder(201, 211);
        testFbl.insertInOrder(260, 290);
        testFbl.insertInOrder(215, 235);
        assertEquals(testFbl.print()[4], "(215,235)");
        
        FreeBlockList newFBL = new FreeBlockList();
        newFBL.insertInOrder(0, 18);
        newFBL.insertInOrder(18, 18);
        newFBL.insertInOrder(36, 18);
        newFBL.merge();
        assertEquals(newFBL.print()[0], "(0,54)");
        
        
    }
}
