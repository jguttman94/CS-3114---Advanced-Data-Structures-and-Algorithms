import student.TestCase;

/**
 * @author Jack Guttman
 * @version September 30, 2016
 */
public class InternalNodeTest extends TestCase {
    private InternalNode testInternal;
    private KVPair pair1;
    private KVPair pair2;
    private KVPair test1;
    private KVPair test2;

    // -------------------------------------------------------------------------
    /**
     * setsup testing
     */
    public void setUp() {
        pair1 = new KVPair(new Handle(4), new Handle(14));
        pair2 = new KVPair(new Handle(10), new Handle(20));
        test1 = new KVPair(new Handle(14), new Handle(44));
        test2 = new KVPair(new Handle(5), new Handle(54));
        testInternal = new InternalNode(pair1, pair2);
    }

    // -------------------------------------------------------------------------
    /**
     * tests initialization and getters/setters
     */
    public void testSetup() {
        assertEquals(testInternal.getLeft(), pair1);
        assertEquals(testInternal.getRight(), pair2);
        assertEquals(testInternal.getLchild(), null);
        assertEquals(testInternal.getCchild(), null);
        assertEquals(testInternal.getRchild(), null);

        testInternal.setCchild(new LeafNode(test1, test2));
        testInternal.setLchild(new LeafNode(test1, test2));
        testInternal.setRchild(new LeafNode(test1, test2));
        assertEquals(testInternal.getCchild().getLeft(), test1);
        assertEquals(testInternal.getRchild().getRight(), test2);
        assertEquals(testInternal.getLchild().getRight(), test2);
    }

    // -------------------------------------------------------------------------
    /**
     * tests absorption
     */
    public void testAbsorb() {
        testInternal.setLchild(
                new LeafNode(new KVPair(new Handle(3), new Handle(0)), null));
        testInternal.setCchild(new LeafNode(pair1, null));
        testInternal.setRchild(new LeafNode(pair2, test1));
        InternalNode testnode = new InternalNode(test2, null);
        testnode.setLchild(new LeafNode(pair1, null));
        testnode.setCchild(new LeafNode(test2, test1));
        assertNotSame(testInternal.absorb(testnode), testInternal);

        LeafNode ret1 = new LeafNode(pair1, pair2);
        InternalNode ret2 = (InternalNode) ret1
                .insert(new KVPair(new Handle(0), new Handle(1)));
        InternalNode ret3 = (InternalNode) ret2.insert(test1);
        assertEquals(ret3.getRight(), pair2);

        LeafNode absorbtest = new LeafNode(pair2, test1);
        InternalNode ret = (InternalNode) absorbtest.insert(test2);
        ret.insert(pair1);
        ret.insert(new KVPair(new Handle(3), new Handle(23)));
        assertEquals(ret.getLeft(), pair1);

    }

    // -------------------------------------------------------------------------
    /**
     * test split
     */
    public void testSplit() {
        InternalNode testInt = new InternalNode(test2, null);
        testInt.setLchild(new LeafNode(pair1, null));
        testInt.setCchild(new LeafNode(test2, pair2));
        testInt.insert(test1);
        assertEquals(testInt.getRight(), pair2);

        testInt.insert(new KVPair(new Handle(3), new Handle(23)));
        InternalNode ret = (InternalNode) testInt
                .insert(new KVPair(new Handle(2), new Handle(1)));
        assertEquals(ret.getLeft(), test2);

        ret.insert(new KVPair(new Handle(16), new Handle(234)));
        assertEquals(ret.getCchild().getRight(), test1);
        InternalNode splitret = (InternalNode) ret
                .insert(new KVPair(new Handle(20), new Handle(30)));
        assertEquals(splitret.getLeft(), test2);
        assertEquals(splitret.getRight(), test1);
        assertEquals(splitret.getCchild().getLeft(), pair2);
        assertEquals(splitret.getLchild().getLeft().key().getStartPos(), 3);
        assertEquals(splitret.getRchild().getLeft().key().getStartPos(), 16);
    }

    // -------------------------------------------------------------------------
    /**
     * test insert with absorb and split working
     */
    public void testInsert() {
        LeafNode insTest = new LeafNode(pair1, pair2);
        InternalNode ins = (InternalNode) insTest
                .insert(new KVPair(new Handle(10), new Handle(12)));
        assertEquals(ins.getCchild().getLeft().value().getStartPos(), 12);
        ins.insert(test1);
        assertEquals(ins.getCchild().getLeft().value().getStartPos(), 12);

        ins.insert(new KVPair(new Handle(10), new Handle(14)));
        assertEquals(ins.getCchild().getRight().value().getStartPos(), 14);
        ins.getRchild().setRight(null);
        ins.insert(new KVPair(new Handle(100), new Handle(100)));
        assertEquals(ins.getRchild().getRight().key().getStartPos(), 100);
        InternalNode fins = (InternalNode) ins
                .insert(new KVPair(new Handle(10), new Handle(15)));
        assertEquals(fins.getLeft().value().getStartPos(), 14);
        assertEquals(fins.getLchild().getLeft().value().getStartPos(), 12);
        assertEquals(fins.getCchild().getLeft().value().getStartPos(), 20);   
        
        //tests get left most here to not have to insert more
        assertEquals(fins.leftMost().getLeft(), pair1);
    }
    // -------------------------------------------------------------------------
    //test on setnextelem is done in TTTree with a lot more inserts
    // -------------------------------------------------------------------------
    /**
     * test find
     */
    public void testFind() {
        LeafNode insTest = new LeafNode(pair1, pair2);
        KVPair fourt = new KVPair(new Handle(14), new Handle(12));
        InternalNode ins = (InternalNode) insTest.insert(fourt);
        KVPair three = new KVPair(new Handle(3), new Handle(8));
        KVPair fif = new KVPair(new Handle(15), new Handle(4));
        KVPair thirt = new KVPair(new Handle(13), new Handle(5));
        KVPair thirtDoub = new KVPair(new Handle(13), new Handle(3));
        KVPair pair2Doub = new KVPair(new Handle(10), new Handle(30));
        assertTrue(ins.find(pair1));
        assertTrue(ins.find(pair2));
        assertFalse(ins.find(three));
        assertFalse(ins.find(fif));
        assertTrue(ins.find(fourt));
        assertEquals(ins.getRight(), null);
        assertFalse(ins.find(pair2Doub));
        assertFalse(ins.find(thirtDoub));

        ins.insert(thirt);
        assertEquals(ins.getCchild().getLeft(), pair2);
        assertEquals(ins.getLchild().getLeft(), pair1);
        assertEquals(ins.getRchild().getLeft(), thirt);
        assertEquals(ins.getRchild().getRight(), fourt);
        assertFalse(ins.find(pair2Doub));
        assertTrue(ins.find(pair2));
        assertTrue(ins.find(thirt));
        assertFalse(ins.find(fif));
        assertFalse(ins.find(thirtDoub));
    }

    // -------------------------------------------------------------------------
    /**
     * tests print and printchildren
     */
    public void testPrint() {
        systemOut().clearHistory();
        LeafNode insTest = new LeafNode(pair1, pair2);
        KVPair fourt = new KVPair(new Handle(14), new Handle(12));
        InternalNode ins = (InternalNode) insTest.insert(fourt);
        ins.print(0);
        assertEquals(systemOut().getHistory(),
                "10 20\n  4 14\n  10 20 14 12\n");
        KVPair fif = new KVPair(new Handle(15), new Handle(4));
        ins.insert(fif);
        systemOut().clearHistory();
        ins.print(1);
        assertEquals(systemOut().getHistory(),
                "  10 20 14 12\n    4 14\n    10 20\n    14 12 15 4\n");
    }
    // -------------------------------------------------------------------------
    /**
     * tests delete
     */
    public void testDelete() {
        LeafNode delTest = new LeafNode(pair1, pair2);
        KVPair zero = new KVPair(new Handle(1), new Handle(0));
        KVPair one = new KVPair(new Handle(1), new Handle(1));
        InternalNode delRet = (InternalNode) delTest.insert(zero);
        assertEquals(delRet.getFirst(new Handle(1)), delRet.getLchild());
        //simple left delete
        delRet.delete(zero);
        assertEquals(delRet.getLchild().getLeft(), pair1);
        assertNull(delRet.getLchild().getRight());
        assertEquals(delRet.getCchild().getLeft(), pair2);
        assertNull(delRet.getCchild().getRight());
        assertEquals(delRet.getLeft(), pair2);
        
        //simple center delete
        delRet.insert(test2);
        assertEquals(delRet.getLchild().getRight(), test2);
        delRet.delete(pair2);
        assertEquals(delRet.getCchild().getLeft(), test2);
        assertEquals(delRet.getLchild().getLeft(), pair1);
        assertEquals(delRet.getLeft(), test2);
        assertNull(delRet.getLchild().getRight());
        assertNull(delRet.getCchild().getRight());
        
        //simple right delete
        LeafNode delTest2 = new LeafNode(pair1, pair2);
        InternalNode delRet2 = (InternalNode) delTest2.insert(zero);
        delRet2.insert(one);
        delRet2.insert(test1);
        delRet2.insert(test2);
        assertEquals(delRet2.getLeft(), pair1);
        assertEquals(delRet2.getRight(), pair2);
        assertEquals(delRet2.getRchild().getRight(), test1);
        delRet2.delete(test1);
        assertEquals(delRet2.getRchild().getRight(), null);
        delRet2.delete(pair2);
        assertEquals(delRet2.getRchild().getLeft(), test2);
        assertEquals(delRet2.getRight(), test2);
        
        InternalNode delRet3 = (InternalNode) delRet2.insert(test1);
        assertEquals(delRet3.getRchild().getLeft(), test2);
        assertEquals(delRet3.getRchild().getRight(), test1);
        assertEquals(delRet3.getCchild().getRight(), null);
        assertEquals(delRet3.getCchild().getLeft(), pair1);
        assertEquals(delRet3.getLchild().getLeft(), zero);
        assertEquals(delRet3.getLchild().getRight(), one);
        assertEquals(delRet3.getLeft(), pair1);
        delRet3.delete(one);
        assertEquals(delRet3.getRchild().getLeft(), test2);
        assertNull(delRet3.getCchild().getRight());
        assertEquals(delRet3.getLeft(), pair1);
        assertEquals(delRet3.getRight(), test2);
        delRet3.delete(pair1);
        assertEquals(delRet3.getCchild().getLeft(), test2);
        assertEquals(delRet3.getRchild().getLeft(), test1);
        assertEquals(delRet3.getRight(), test1);
        delRet3.insert(new KVPair(new Handle(15), new Handle(7)));
        delRet3.delete(zero);
        assertEquals(delRet3.getRchild(), null);
        assertEquals(delRet3.getLchild().getLeft(), test2);
        
        //center borrows 
        delTest = new LeafNode(pair1, pair2);
        delRet = (InternalNode) delTest.insert(zero);
        delRet.insert(test1);
        delRet.delete(test1);
        delRet.delete(pair1);
        assertEquals(delRet.getCchild().getLeft(), pair2);
        assertNull(delRet.getCchild().getRight());
        assertEquals(delRet.getLeft(), pair2);
        assertNull(delRet.getRight());
        
    }
    // -------------------------------------------------------------------------
    //complicated test delete is done in TTTree class
    // -------------------------------------------------------------------------
    /**
     * test delete help
     */
    public void testDeleteHelp() {
        KVPair one = new KVPair(new Handle(1), new Handle(1));
        KVPair two = new KVPair(new Handle(2), new Handle(2));
        KVPair thr = new KVPair(new Handle(3), new Handle(3));
        KVPair fiv = new KVPair(new Handle(5), new Handle(5));
        KVPair six = new KVPair(new Handle(6), new Handle(6));
        InternalNode test = new InternalNode(fiv, six);
        
        LeafNode first = new LeafNode(one, null);
        LeafNode secnd = new LeafNode(two, null);
        LeafNode third = new LeafNode(thr, null);
        first.setNext(secnd);
        secnd.setPrev(first);
        secnd.setNext(third);
        third.setPrev(secnd);
        assertFalse(test.deletehelp(first, "next"));
        assertFalse(test.deletehelp(secnd, "prev"));
        assertFalse(test.deletehelp(third, "next"));
        secnd.setRight(fiv);
        first.setLeft(null);
        assertTrue(test.deletehelp(first, "next"));
        assertEquals(first.getLeft(), two);
        assertEquals(secnd.getLeft(), fiv);
        assertNull(secnd.getRight());
        secnd.setLeft(null);
        first.setRight(six);
        assertTrue(test.deletehelp(secnd, "prev"));
        assertEquals(secnd.getLeft(), six);
        assertNull(first.getRight());
        secnd.setPrev(null);
        assertFalse(test.deletehelp(secnd, "prev"));
    }
    // -------------------------------------------------------------------------
    /**
     * tests borrow
     */
    public void testBorrow() {
        KVPair six = new KVPair(new Handle(6), new Handle(5));
        KVPair oneFi = new KVPair(new Handle(1), new Handle(2));
        KVPair oneFo = new KVPair(new Handle(1), new Handle(4));
        KVPair three = new KVPair(new Handle(3), new Handle(3));
        KVPair sev = new KVPair(new Handle(7), new Handle(9));
        KVPair eig = new KVPair(new Handle(8), new Handle(12));
        KVPair ten = new KVPair(new Handle(10), new Handle(4));
        InternalNode grandP = new InternalNode(six, null);
        InternalNode leftP = new InternalNode(null, null);
        InternalNode centP = new InternalNode(sev, eig);
        LeafNode leftL = new LeafNode(oneFo, null);
        LeafNode leftC = new LeafNode(oneFi, null);
        LeafNode cenL = new LeafNode(six, null);
        LeafNode cenC = new LeafNode(sev, null);
        LeafNode cenR = new LeafNode(eig, ten);
        grandP.setLchild(leftP);
        grandP.setCchild(centP);
        leftP.setLchild(leftL);
        centP.setLchild(cenL);
        centP.setCchild(cenC);
        centP.setRchild(cenR);
        //testing borrowing from center child (to the right)
        grandP.borrow(leftP, centP, "right");
        assertEquals(grandP.getLeft(), sev);
        assertEquals(leftP.getLeft(), six);
        assertEquals(leftP.getCchild().getLeft(), six);
        assertEquals(centP.getLchild(), cenC);
        assertEquals(centP.getCchild(), cenR);
        assertEquals(centP.getLeft(), eig);
        
        //testing borrowing from left child (to the left)
        leftP.setLeft(oneFi);
        leftP.setRight(three);
        leftP.setRchild(new LeafNode(three, six));
        leftP.setCchild(leftC);
        centP.setRchild(null);
        centP.setRight(null);
        grandP.borrow(leftP, centP, "left");
        assertEquals(grandP.getLeft(), three);
        assertEquals(centP.getLeft(), sev);
        assertEquals(centP.getLchild().getLeft(), three);
        assertEquals(centP.getCchild(), cenC);
    }
    // -------------------------------------------------------------------------
    /**
     * tests merge
     */
    public void testMergeSimple() {
        KVPair six = new KVPair(new Handle(6), new Handle(5));
        KVPair oneFi = new KVPair(new Handle(1), new Handle(2));
        KVPair oneFo = new KVPair(new Handle(1), new Handle(4));
        KVPair eig = new KVPair(new Handle(8), new Handle(12));
        KVPair ten = new KVPair(new Handle(10), new Handle(4));
        InternalNode grandP = new InternalNode(six, null);
        InternalNode leftP = new InternalNode(oneFi, null);
        InternalNode centP = new InternalNode(null, null);
        LeafNode leftL = new LeafNode(oneFo, null);
        LeafNode cenL = new LeafNode(six, null);
        LeafNode cenR = new LeafNode(eig, ten);
        grandP.setLchild(leftP);
        grandP.setCchild(centP);
        leftP.setLchild(leftL);
        centP.setLchild(cenL);
        grandP.merge(leftP, centP, "left");
        assertEquals(grandP.getLeft(), null);
        assertEquals(leftP.getRight(), six);
        assertEquals(leftP.getRchild(), cenL);
        centP.setLchild(cenL);
        centP.setCchild(cenR);
        centP.setLeft(eig);
        grandP.setLeft(six);
        leftP.setLeft(null);
        leftP.setCchild(null);
        grandP.merge(leftP, centP, "right");
        assertEquals(grandP.getLeft(), null);
        assertEquals(grandP.getLchild(), centP);
        assertEquals(grandP.getCchild(), null);
        assertEquals(centP.getLeft(), six);
        assertEquals(centP.getRight(), eig);
    }
    /**
     * test merge with right child
     */
    public void testMergeComp() {
        KVPair six = new KVPair(new Handle(6), new Handle(5));
        KVPair oneFi = new KVPair(new Handle(1), new Handle(2));
        KVPair oneFo = new KVPair(new Handle(1), new Handle(4));
        KVPair three = new KVPair(new Handle(3), new Handle(3));
        KVPair sev = new KVPair(new Handle(7), new Handle(9));
        KVPair eig = new KVPair(new Handle(8), new Handle(12));
        KVPair ten = new KVPair(new Handle(10), new Handle(4));
        KVPair elv = new KVPair(new Handle(11), new Handle(5));
        InternalNode grandP = new InternalNode(six, eig);
        InternalNode leftP = new InternalNode(oneFo, null);
        InternalNode centP = new InternalNode(null, null);
        InternalNode rigP = new InternalNode(ten, null);
        LeafNode leftL = new LeafNode(oneFi, null);
        LeafNode leftC = new LeafNode(oneFo, three);
        LeafNode cenL = new LeafNode(six, null);
        LeafNode cenC = new LeafNode(sev, null);
        LeafNode rigL = new LeafNode(eig, null);
        LeafNode rigC = new LeafNode(ten, elv);
        grandP.setLchild(leftP);
        grandP.setCchild(centP);
        grandP.setRchild(rigP);
        leftP.setLchild(leftL);
        leftP.setCchild(leftC);
        centP.setLchild(cenL);
        //centP.setCchild(cenC);
        rigP.setLchild(rigL);
        rigP.setCchild(rigC);
        grandP.merge(leftP, centP, "left");
        assertEquals(grandP.getLeft(), eig);
        assertEquals(grandP.getRight(), null);
        assertEquals(grandP.getCchild(), rigP);
        assertEquals(grandP.getRchild(), null);
        
        leftP.setLeft(null);
        leftP.setRight(null);
        leftP.setCchild(null);
        leftP.setRchild(null);
        grandP.setLeft(six);
        grandP.setRight(eig);
        centP.setLeft(sev);
        centP.setLchild(cenL);
        centP.setCchild(cenC);
        grandP.setLchild(leftP);
        grandP.setCchild(centP);
        grandP.setRchild(rigP);
        grandP.merge(leftP, centP, "right");
        assertEquals(grandP.getLeft(), eig);
        assertEquals(grandP.getLchild(), centP);
        assertEquals(grandP.getCchild(), rigP);
        assertEquals(centP.getLchild(), leftL);
        assertEquals(centP.getCchild(), cenL);
        assertEquals(centP.getRchild(), cenC);    
    }
    // -------------------------------------------------------------------------
    /**
     * tests minor underflow deletes
     */
    public void testUnderflow() {
        KVPair zero = new KVPair(new Handle(1), new Handle(0));
        KVPair one = new KVPair(new Handle(1), new Handle(1));
        KVPair two = new KVPair(new Handle(1), new Handle(2));
        KVPair three = new KVPair(new Handle(1), new Handle(3));
        InternalNode grandP = new InternalNode(two, null);
        InternalNode leftP = new InternalNode(one, null);
        InternalNode centP = new InternalNode(three, null);
        LeafNode leftL = new LeafNode(zero, null);
        LeafNode leftC = new LeafNode(one, null);
        LeafNode cenL = new LeafNode(two, null);
        LeafNode cenC = new LeafNode(three, null);
        grandP.setLchild(leftP);
        grandP.setCchild(centP);
        leftP.setLchild(leftL);
        leftP.setCchild(leftC);
        centP.setLchild(cenL);
        centP.setCchild(cenC);
        grandP.delete(one);
        assertEquals(grandP.getLchild().getLeft(), two);
        assertEquals(grandP.getLeft(), null);
        assertEquals(grandP.getCchild(), null);
        assertEquals(grandP.getLchild(), centP);
        assertEquals(centP.getLchild(), leftL);
        assertEquals(centP.getCchild(), cenL);
        assertEquals(centP.getRchild(), cenC);
    }

}
