import java.util.ArrayList;
import java.util.List;

import student.TestCase;

/**
 * @author Jack Guttman
 * @version September 30, 2016
 */
public class LeafNodeTest extends TestCase {
    private LeafNode testLeaf;
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
        testLeaf = new LeafNode(pair1, pair2);
    }

    // -------------------------------------------------------------------------
    /**
     * tests initialization and getters/setters
     */
    public void testSetup() {
        assertEquals(testLeaf.getLeft(), pair1);
        assertEquals(testLeaf.getRight(), pair2);
        assertEquals(testLeaf.getNext(), null);
        assertEquals(testLeaf.getPrev(), null);

        testLeaf.setLeft(test1);
        testLeaf.setRight(test2);
        assertEquals(testLeaf.getLeft(), test1);
        assertEquals(testLeaf.getRight(), test2);

        testLeaf.setNext(new LeafNode(pair1, pair2));
        testLeaf.setPrev(new LeafNode(test1, test2));
        assertEquals(testLeaf.getNext().getLeft(), pair1);
        assertEquals(testLeaf.getPrev().getRight(), test2);
    }

    // -------------------------------------------------------------------------
    /**
     * tests insert
     */
    public void testInsert() {
        InternalNode ret = (InternalNode) testLeaf.insert(test2);
        assertEquals(ret.getLchild().getLeft().key().getStartPos(), 4);
        assertEquals(ret.getCchild().getLeft().key().getStartPos(), 5);
        assertEquals(ret.getCchild().getRight().key().getStartPos(), 10);
        assertEquals(ret.getLeft().key().getStartPos(), 5);

        LeafNode testLeaf2 = new LeafNode(pair1, null);
        testLeaf2.insert(test2);
        assertEquals(testLeaf2.getRight().key().getStartPos(), 5);

        testLeaf2.setRight(null);
        testLeaf2.insert(new KVPair(new Handle(2), new Handle(10)));
        assertEquals(testLeaf2.getLeft().key().getStartPos(), 2);
        assertEquals(testLeaf2.getRight().key().getStartPos(), 4);

        InternalNode ret2 = (InternalNode) testLeaf2.insert(pair2);
        assertEquals(ret2.getLchild().getLeft().key().getStartPos(), 2);
        assertEquals(ret2.getCchild().getLeft().key().getStartPos(), 4);
        assertEquals(ret2.getLeft().key().getStartPos(), 4);
        assertNull(ret2.getLchild().getRight());
        assertEquals(ret2.getCchild().getRight().key().getStartPos(), 10);
        assertNull(ret2.getRight());

        KVPair newI = new KVPair(new Handle(0), new Handle(9));
        ret2.insert(newI);
        assertEquals(ret2.getLchild().getLeft().key().getStartPos(), 0);
        assertEquals(ret2.getLchild().getRight().key().getStartPos(), 2);
        assertEquals(ret2.getCchild().getLeft().key().getStartPos(), 4);
        assertEquals(ret2.getCchild().getRight().key().getStartPos(), 10);
        assertEquals(ret2.getLeft().key().getStartPos(), 4);
        assertNull(ret2.getRight());

        LeafNode test12 = new LeafNode(null, null);
        test12.insert(pair1);
        assertEquals(test12.getLeft(), pair1);
    }

    // -------------------------------------------------------------------------
    /**
     * tests where values go when they share the value with one of internal
     * nodes
     */
    public void testSameValInsert() {
        InternalNode ret1 = (InternalNode) testLeaf
                .insert(new KVPair(new Handle(20), new Handle(10)));
        assertEquals(ret1.getCchild().getRight().key().getStartPos(), 20);
        assertEquals(ret1.getCchild().getLeft().key().getStartPos(), 10);
        ret1.insert(new KVPair(new Handle(4), new Handle(10)));
        assertEquals(ret1.getLchild().getLeft().value().getStartPos(), 10);
        assertEquals(ret1.getLchild().getRight().value().getStartPos(), 14);
        assertEquals(ret1.getCchild().getLeft(), pair2);
    }

    // -------------------------------------------------------------------------
    /**
     * complicated insert with splits
     */
    public void testCompInsert() {
        KVPair five = new KVPair(new Handle(5), new Handle(10));
        InternalNode test = new InternalNode(five, test2);
        LeafNode left = new LeafNode(pair1, null);
        LeafNode right = new LeafNode(test2, test1);
        LeafNode center = new LeafNode(five, null);
        test.setLchild(left);
        test.setCchild(center);
        test.setRchild(right);
        Node ret = test.insert(new KVPair(new Handle(2), new Handle(1)));
        assertEquals(ret.getLeft().key().getStartPos(), 5);
        assertEquals(ret.getLeft().value().getStartPos(), 10);
        assertEquals(
                ((InternalNode) ret).getLchild().getLeft().key().getStartPos(),
                2);
        assertEquals(
                ((InternalNode) ret).getRchild().getLeft().key().getStartPos(),
                5);

        KVPair one = new KVPair(new Handle(1), new Handle(20));
        InternalNode ret3 = (InternalNode) ret.insert(one);
        assertEquals(ret3.getLeft(), five);
        assertEquals(ret3.getLchild().getLeft().key().getStartPos(), 2);
        assertEquals(ret3.getCchild().getLeft(), test2);
    }

    // -------------------------------------------------------------------------
    /**
     * test find
     */
    public void testFind() {
        assertTrue(testLeaf.find(pair1));
        assertTrue(testLeaf.find(pair2));
        assertFalse(testLeaf.find(test1));
        testLeaf.setLeft(null);
        testLeaf.setRight(null);
        assertFalse(testLeaf.find(pair1));
        testLeaf.setLeft(test2);
        assertTrue(testLeaf.find(test2));
        assertFalse(testLeaf.find(test1));
    }

    // -------------------------------------------------------------------------
    /**
     * tests print
     */
    public void testPrint() {
        LeafNode testP = new LeafNode(null, null);
        testP.print(1);
        assertEquals(systemOut().getHistory(), "");
        systemOut().clearHistory();
        testLeaf.print(1);
        assertEquals(systemOut().getHistory(), "  4 14 10 20\n");
        systemOut().clearHistory();
        testLeaf.setRight(null);
        testLeaf.print(0);
        assertEquals(systemOut().getHistory(), "4 14\n");
    }
    // -------------------------------------------------------------------------
    /**
     * test list
     */
    public void testList() {
        List<Handle> testList = new ArrayList<Handle>();
        LeafNode list1 = new LeafNode(pair1, pair2);
        list1.list(new Handle(6), testList);
        assertEquals(testList.size(), 0);
        list1.list(pair1.key(), testList);
        assertEquals(testList.get(0), pair1.value());
        list1.list(pair2.key(),  testList);
        assertEquals(testList.get(1), pair2.value());        
        LeafNode list3 = new LeafNode(pair1, null);
        list3.list(pair1.key(), testList);
        assertEquals(testList.get(2), pair1.value());
        list3.list(pair2.key(), testList);
        assertEquals(testList.size(), 3);
    }
    // -------------------------------------------------------------------------
    /**
     * test delete
     */
    public void testDelete() {
        LeafNode del1 = new LeafNode(pair1, pair2);
        del1.delete(pair1);
        assertEquals(del1.getLeft(), pair2);
        assertNull(del1.getRight());
        assertEquals(del1.delete(test2), del1);
        del1.delete(pair1);
        del1.delete(pair2);
        assertNotSame(del1.getLeft(), del1);
        assertNull(del1.getRight());

        del1 = new LeafNode(pair1, pair2);
        del1.delete(pair2);
        assertEquals(del1.getLeft(), pair1);
        assertNull(del1.getRight());

        del1 = new LeafNode(pair1, pair2);
        assertEquals(del1.delete(test1), del1);
    }

}