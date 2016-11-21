import java.util.List;

import student.TestCase;

/**
 * @author Jack Guttman
 * @version September 30, 2016
 */
public class TTTreeTest extends TestCase {

    private TTTree test;

    /**
     * setsup testing
     */
    public void setUp() {
        test = new TTTree();
    }
    /**
     * tests initialization
     */
    public void test() {
        assertEquals(test.getRt(), null);
        assertEquals(test.list(new Handle(0)).size(), 0);
    }

    /**
     * tests tree insertion
     */
    public void testInsert() {
        KVPair one = new KVPair(new Handle(1), new Handle(23));
        KVPair three = new KVPair(new Handle(3), new Handle(23));
        KVPair five = new KVPair(new Handle(5), new Handle(23));
        KVPair seven = new KVPair(new Handle(7), new Handle(23));
        KVPair ten = new KVPair(new Handle(10), new Handle(23));
        test.insert(five);
        assertEquals(test.getRt().getLeft(), five);
        test.insert(ten);
        assertEquals(test.getRt().getRight(), ten);
        assertEquals(test.list(new Handle(5)).size(), 1);
        test.insert(one);        
        assertEquals(((InternalNode) test.getRt()).getLchild().getLeft(), one);
        assertNull(((InternalNode) test.getRt()).getLchild().getRight());
        test.insert(seven);
        assertEquals(((InternalNode) test.getRt()).getCchild().getRight(),
                null);
        assertEquals(((InternalNode) test.getRt()).getRchild().getLeft(),
                seven);
        assertEquals(
                ((LeafNode) ((InternalNode) test.getRt()).getFirst(one.key()))
                        .getNext().getLeft(),
                five);
        assertEquals(
                ((LeafNode) ((InternalNode) test.getRt()).getFirst(one.key()))
                        .getNext().getNext().getLeft(),
                seven);
        test.insert(three);
        assertEquals(((InternalNode) test.getRt()).getLchild().getRight(),
                three);
        systemOut().clearHistory();
        test.insert(three);
        assertEquals(",(3,23) duplicates a record already in the tree.\n",
                systemOut().getHistory());
    }
    // -------------------------------------------------------------------------
    /**
     * tests delete
     */
    public void testDelete() {
        KVPair zero = new KVPair(new Handle(1), new Handle(0));
        KVPair one = new KVPair(new Handle(1), new Handle(1));
        KVPair two = new KVPair(new Handle(1), new Handle(2));
        KVPair three = new KVPair(new Handle(1), new Handle(3));
        systemOut().clearHistory();
        test.delete(zero);
        assertNull(test.getRt());
        assertEquals(systemOut().getHistory(),
                " was not found in the database.\n");
        test.insert(zero);
        systemOut().clearHistory();
        test.delete(zero);
        assertEquals(systemOut().getHistory(), " is deleted from the tree.\n");
        assertEquals(test.getRt(), null);
        test.insert(one);
        test.insert(two);
        systemOut().clearHistory();
        test.delete(three);
        assertEquals(systemOut().getHistory(),
                " was not found in the database.\n");
        test.delete(one);
        test.delete(two);
        assertNull(test.getRt());
    }
    // -------------------------------------------------------------------------
    /**
     * complicated delete test with borrowing and underflow
     */
    public void testCompDelete() {
        KVPair zero = new KVPair(new Handle(1), new Handle(0));
        KVPair one = new KVPair(new Handle(1), new Handle(1));
        KVPair two = new KVPair(new Handle(1), new Handle(2));
        KVPair three = new KVPair(new Handle(1), new Handle(3));
        KVPair four = new KVPair(new Handle(1), new Handle(4));
        KVPair five = new KVPair(new Handle(1), new Handle(5));
        KVPair six = new KVPair(new Handle(6), new Handle(5));
        KVPair sev = new KVPair(new Handle(7), new Handle(9));
        KVPair eight = new KVPair(new Handle(8), new Handle(12));
        KVPair three2 = new KVPair(new Handle(3), new Handle(3));
        KVPair ten = new KVPair(new Handle(10), new Handle(4));
        KVPair elev = new KVPair(new Handle(11), new Handle(5));
        test.insert(zero);
        test.insert(one);
        test.insert(two);
        test.insert(three);
        test.insert(four);
        test.insert(five);
        test.insert(six);
        test.insert(sev);
        test.insert(eight);
        test.insert(three2);
        test.insert(ten);
        test.insert(elev);
        // start deletes
        test.delete(one);
        assertEquals(test.getRt().getLeft(), six);
        assertEquals(((InternalNode) test.getRt()).getLchild().getLeft(), four);
        assertEquals(((InternalNode) test.getRt()).getCchild().getLeft(),
                eight);
        systemOut().clearHistory();
        test.print();
        assertEquals(systemOut().getHistory(),
                "6 5\n" + "  1 4\n" + "    1 2 1 3\n" + "      1 0\n"
                        + "      1 2\n" + "      1 3\n" + "    1 5\n"
                        + "      1 4\n" + "      1 5 3 3\n" + "  8 12\n"
                        + "    7 9\n" + "      6 5\n" + "      7 9\n"
                        + "    10 4\n" + "      8 12\n" + "      10 4 11 5\n");
        List<Handle> ret = test.list(new Handle(1));
        assertEquals(ret.size(), 5);
        assertEquals(ret.get(0), zero.value());
        assertEquals(ret.get(1), two.value());
        assertEquals(ret.get(2), three.value());
        assertEquals(ret.get(3), four.value());
        assertEquals(ret.get(4), five.value());
        test.delete(four);
        systemOut().clearHistory();
        test.print();
        assertEquals(systemOut().getHistory(),
                "6 5\n" + "  1 5\n" + "    1 2 1 3\n" + "      1 0\n"
                        + "      1 2\n" + "      1 3\n" + "    3 3\n"
                        + "      1 5\n" + "      3 3\n" + "  8 12\n"
                        + "    7 9\n" + "      6 5\n" + "      7 9\n"
                        + "    10 4\n" + "      8 12\n" + "      10 4 11 5\n");
        test.delete(eight);
        systemOut().clearHistory();
        test.print();
        assertEquals(systemOut().getHistory(),
                "6 5\n" + "  1 5\n" + "    1 2 1 3\n" + "      1 0\n"
                        + "      1 2\n" + "      1 3\n" + "    3 3\n"
                        + "      1 5\n" + "      3 3\n" + "  10 4\n"
                        + "    7 9\n" + "      6 5\n" + "      7 9\n"
                        + "    11 5\n" + "      10 4\n" + "      11 5\n");
        ret = test.list(new Handle(8));
        assertEquals(ret.size(), 0);
    }
    // -------------------------------------------------------------------------
    /**
     * tests simple insert and delete
     */
    public void testInsAndDel() {
        KVPair oneZero = new KVPair(new Handle(1), new Handle(0));
        KVPair zeroOne = new KVPair(new Handle(0), new Handle(1));
        KVPair zeroTwo = new KVPair(new Handle(0), new Handle(2));
        KVPair twoZero = new KVPair(new Handle(2), new Handle(0));
        test.insert(oneZero);
        test.insert(zeroOne);
        test.insert(zeroTwo);
        test.insert(twoZero);
        test.print();
        test.delete(zeroTwo);
        test.delete(twoZero);
        systemOut().clearHistory();
        test.print();
        assertEquals(systemOut().getHistory(), "1 0\n  0 1\n  1 0\n");
    }
    // -------------------------------------------------------------------------
    /**
     * tests more inserts and deletes
     */
    public void testInsertDelete() {
        testCompDelete();
        KVPair twelve = new KVPair(new Handle(12), new Handle(0));
        KVPair twelveFi = new KVPair(new Handle(12), new Handle(5));
        KVPair fourt = new KVPair(new Handle(14), new Handle(60));
        KVPair thirty = new KVPair(new Handle(30), new Handle(56));
        KVPair twentyFi = new KVPair(new Handle(25), new Handle(2));
        test.insert(twelve);
        test.insert(twelveFi);
        test.insert(fourt);
        assertEquals(((InternalNode) test.getRt()).getCchild().getRight(),
                twelve);
        test.insert(thirty);
        test.insert(twentyFi);
        assertEquals(test.getRt().getRight(), twelve);
        systemOut().clearHistory();
        test.print();
        assertEquals(systemOut().getHistory(), "6 5 12 0\n" + "  1 5\n"
                + "    1 2 1 3\n" + "      1 0\n" + "      1 2\n"
                + "      1 3\n" + "    3 3\n" + "      1 5\n" + "      3 3\n"
                + "  10 4\n" + "    7 9\n" + "      6 5\n" + "      7 9\n"
                + "    11 5\n" + "      10 4\n" + "      11 5\n" + "  14 60\n"
                + "    12 5\n" + "      12 0\n" + "      12 5\n" + "    25 2\n"
                + "      14 60\n" + "      25 2 30 56\n");
        test.delete(twelveFi);
        systemOut().clearHistory();
        test.print();
        assertEquals(systemOut().getHistory(),
                "6 5\n" + "  1 5\n" + "    1 2 1 3\n" + "      1 0\n"
                        + "      1 2\n" + "      1 3\n" + "    3 3\n"
                        + "      1 5\n" + "      3 3\n" + "  10 4 12 0\n"
                        + "    7 9\n" + "      6 5\n" + "      7 9\n"
                        + "    11 5\n" + "      10 4\n" + "      11 5\n"
                        + "    14 60 25 2\n" + "      12 0\n" + "      14 60\n"
                        + "      25 2 30 56\n");
        KVPair sixFo = new KVPair(new Handle(6), new Handle(4));
        test.insert(sixFo);
        List<Handle> ret = test.list(new Handle(6));
        assertEquals(ret.size(), 2);
        assertEquals(ret.get(0), sixFo.value());
        assertEquals(ret.get(1),
                ((InternalNode) ((InternalNode) test.getRt()).getCchild())
                        .leftMost().getLeft().value());
        KVPair secTen = new KVPair(new Handle(10), new Handle(6));
        KVPair thirTen = new KVPair(new Handle(10), new Handle(9));
        test.insert(secTen);
        test.insert(thirTen);
        systemOut().clearHistory();
        test.print();
        assertEquals(systemOut().getHistory(),
                "6 5\n" + "  1 5\n" + "    1 2 1 3\n" + "      1 0\n"
                        + "      1 2\n" + "      1 3\n" + "    3 3\n"
                        + "      1 5\n" + "      3 3 6 4\n" + "  10 4 12 0\n"
                        + "    7 9\n" + "      6 5\n" + "      7 9\n"
                        + "    10 6 11 5\n" + "      10 4\n"
                        + "      10 6 10 9\n" + "      11 5\n"
                        + "    14 60 25 2\n" + "      12 0\n" + "      14 60\n"
                        + "      25 2 30 56\n");
        LeafNode firstTen = (LeafNode) ((InternalNode) test.getRt())
                .getFirst(new Handle(10));
        System.out.println(
                firstTen.getLeft() + ", " + firstTen.getRight() + "first 10: ");

        ret = test.list(new Handle(10));
        assertEquals(ret.size(), 3);
        assertEquals(ret.get(0),
                ((InternalNode) ((InternalNode) ((InternalNode) test.getRt())
                        .getCchild()).getCchild()).getLchild().getLeft()
                                .value());
        assertEquals(ret.get(1), secTen.value());
        assertEquals(ret.get(2), thirTen.value());
    }
    // -------------------------------------------------------------------------
    /**
     * tests borrowing
     */
    public void testBorrow() {
        KVPair four = new KVPair(new Handle(0), new Handle(4));
        KVPair four2 = new KVPair(new Handle(4), new Handle(0));
        KVPair ten = new KVPair(new Handle(0), new Handle(10));
        KVPair ten2 = new KVPair(new Handle(10), new Handle(0));
        KVPair sxTwn = new KVPair(new Handle(16), new Handle(20));
        KVPair sxTwn2 = new KVPair(new Handle(20), new Handle(16));
        KVPair twnSx = new KVPair(new Handle(0), new Handle(26));
        KVPair twnSx2 = new KVPair(new Handle(26), new Handle(0));
        KVPair thrTn = new KVPair(new Handle(32), new Handle(10));
        KVPair thrTn2 = new KVPair(new Handle(10), new Handle(32));
        KVPair thrSx = new KVPair(new Handle(0), new Handle(36));
        KVPair thrSx2 = new KVPair(new Handle(36), new Handle(0));
        KVPair sxFif = new KVPair(new Handle(16), new Handle(52));
        KVPair sxFif2 = new KVPair(new Handle(52), new Handle(16));
        KVPair fourThr = new KVPair(new Handle(0), new Handle(43));
        KVPair fourThr2 = new KVPair(new Handle(43), new Handle(0));
        KVPair fourTen = new KVPair(new Handle(4), new Handle(10));
        KVPair fourTen2 = new KVPair(new Handle(10), new Handle(4));
        test.insert(four);
        test.insert(four2);
        test.insert(ten);
        test.insert(ten2);
        test.insert(sxTwn);
        test.insert(sxTwn2);
        test.insert(twnSx);
        test.insert(twnSx2);
        test.insert(thrTn);
        test.insert(thrTn2);
        test.delete(four);
        test.delete(four2);
        systemOut().clearHistory();
        test.print();
        assertEquals(systemOut().getHistory(),
                "10 0 16 20\n" + "  0 26\n" + "    0 10\n" + "    0 26\n"
                        + "  10 32\n" + "    10 0\n" + "    10 32\n"
                        + "  20 16 26 0\n" + "    16 20\n" + "    20 16\n"
                        + "    26 0 32 10\n");
        test.insert(thrSx);
        test.insert(thrSx2);
        test.delete(twnSx);
        test.delete(twnSx2);
        systemOut().clearHistory();
        test.print();
        assertEquals(systemOut().getHistory(),
                "16 20\n" + "  10 0\n" + "    0 36\n" + "      0 10\n"
                        + "      0 36\n" + "    10 32\n" + "      10 0\n"
                        + "      10 32\n" + "  32 10\n" + "    20 16\n"
                        + "      16 20\n" + "      20 16\n" + "    36 0\n"
                        + "      32 10\n" + "      36 0\n");
        test.insert(fourThr);
        test.insert(fourThr2);
        test.insert(fourTen);
        test.insert(fourTen2);
        test.delete(thrTn);
        test.delete(thrTn2);
        systemOut().clearHistory();
        test.print();
        assertEquals(systemOut().getHistory(),
                "16 20\n  10 0\n    0 36 0 43\n      0 10\n      0 36\n"
                + "      0 43 4 10\n    10 4\n      10 0\n      10 4\n  36 0\n"
                + "    20 16\n      16 20\n      20 16\n    43 0\n      36 0\n"
                + "      43 0\n");
        test.insert(twnSx);
        test.insert(twnSx2);
        test.insert(sxFif);
        test.insert(sxFif2);
        test.delete(thrSx);
        test.delete(thrSx2);
        systemOut().clearHistory();
        test.print();
        assertEquals(systemOut().getHistory(),
                "16 20\n  10 0\n    0 26 0 43\n      0 10\n      0 26\n"
                + "      0 43 4 10\n    10 4\n      10 0\n      10 4\n  43 0\n"
                + "    20 16\n      16 20 16 52\n      20 16 26 0\n    52 16\n"
                + "      43 0\n      52 16\n");        
    }
    // -------------------------------------------------------------------------
    /**
     * test merge
     */
    public void testMerge() {
        KVPair four = new KVPair(new Handle(0), new Handle(4));
        KVPair four2 = new KVPair(new Handle(4), new Handle(0));
        KVPair tenFr = new KVPair(new Handle(10), new Handle(14));
        KVPair tenFr2 = new KVPair(new Handle(14), new Handle(10));
        test.insert(four);
        test.insert(four2);
        test.insert(tenFr);
        test.insert(tenFr2);
        systemOut().clearHistory();
        test.print();
        assertEquals(systemOut().getHistory(), 
                "4 0 10 14\n  0 4\n  4 0\n  10 14 14 10\n");
        test.delete(four);
        test.delete(four2);
        systemOut().clearHistory();
        test.print();
        assertEquals(systemOut().getHistory(), 
                "14 10\n  10 14\n  14 10\n");
        KVPair twen = new KVPair(new Handle(0), new Handle(20));
        KVPair twen2 = new KVPair(new Handle(20), new Handle(0));
        KVPair twnEig = new KVPair(new Handle(28), new Handle(34));
        KVPair twnEig2 = new KVPair(new Handle(34), new Handle(28));
        KVPair fourOn = new KVPair(new Handle(41), new Handle(48));
        KVPair fourOn2 = new KVPair(new Handle(48), new Handle(41));
        KVPair fifEig = new KVPair(new Handle(58), new Handle(66));
        KVPair fifEig2 = new KVPair(new Handle(66), new Handle(58));
        test.insert(twen);
        test.insert(twen2);
        test.insert(twnEig);
        test.insert(twnEig2);
        test.insert(fourOn);
        test.insert(fourOn2);
        test.insert(fifEig);
        test.insert(fifEig2);
        systemOut().clearHistory();
        test.print();
        assertEquals(systemOut().getHistory(), 
                "34 28\n  20 0\n    14 10\n      0 20 10 14\n      14 10\n"
                + "    28 34\n      20 0\n      28 34\n  48 41\n    41 48\n"
                + "      34 28\n      41 48\n    58 66\n      48 41\n"
                + "      58 66 66 58\n");
        test.delete(twnEig);
        test.delete(twnEig2);
        systemOut().clearHistory();
        test.print();
        assertEquals(systemOut().getHistory(), 
                "20 0 48 41\n  14 10\n    0 20 10 14\n    14 10\n  41 48\n"
                + "    20 0\n    41 48\n  58 66\n    48 41\n    58 66 66 58\n");
        KVPair fiv = new KVPair(new Handle(5), new Handle(28));
        KVPair fiv2 = new KVPair(new Handle(28), new Handle(5));
        KVPair thirFo = new KVPair(new Handle(34), new Handle(72));
        KVPair thirFo2 = new KVPair(new Handle(72), new Handle(34));
        KVPair thirFo3 = new KVPair(new Handle(34), new Handle(78));
        KVPair thirFo4 = new KVPair(new Handle(78), new Handle(34));
        KVPair tenEig = new KVPair(new Handle(10), new Handle(84));
        KVPair tenEig2 = new KVPair(new Handle(84), new Handle(10));
        KVPair ninN = new KVPair(new Handle(90), new Handle(94));
        KVPair ninN2 = new KVPair(new Handle(94), new Handle(90));
        test.insert(fiv);
        test.insert(fiv2);
        test.insert(thirFo);
        test.insert(thirFo2);
        test.insert(thirFo3);
        test.insert(thirFo4);
        test.insert(tenEig);
        test.insert(tenEig2);
        test.insert(ninN);
        test.insert(ninN2);
        test.delete(fifEig);
        test.delete(fifEig2);
        systemOut().clearHistory();
        test.print();
        assertEquals(systemOut().getHistory(), 
                "20 0 72 34\n  10 14\n    5 28\n      0 20\n      5 28\n"
                + "    14 10\n      10 14 10 84\n      14 10\n  34 72\n"
                + "    28 5\n      20 0\n      28 5\n    41 48 48 41\n"
                + "      34 72 34 78\n      41 48\n      48 41\n  84 10\n"
                + "    78 34\n      72 34\n      78 34\n    90 94\n      "
                + "84 10\n      90 94 94 90\n");
        KVPair fifEig3 = new KVPair(new Handle(58), new Handle(64));
        KVPair fifEig4 = new KVPair(new Handle(64), new Handle(58));
        test.insert(fifEig3);
        test.insert(fifEig4);
        test.delete(twen);
        test.delete(twen2);
        systemOut().clearHistory();
        test.print();
        assertEquals(systemOut().getHistory(), 
                "34 72 72 34\n  14 10\n    10 14\n      5 28\n      "
                + "10 14 10 84\n    28 5\n      14 10\n      28 5\n  48 41\n"
                + "    41 48\n      34 72 34 78\n      41 48\n    58 64\n"
                + "      48 41\n      58 64 64 58\n  84 10\n    78 34\n      "
                + "72 34\n      78 34\n    90 94\n      84 10\n"
                + "      90 94 94 90\n");
    }
    // -------------------------------------------------------------------------
    /**
     * tests parent updating
     */
    public void testParent() {
        KVPair four = new KVPair(new Handle(0), new Handle(4));
        KVPair four2 = new KVPair(new Handle(4), new Handle(0));
        KVPair ten = new KVPair(new Handle(0), new Handle(10));
        KVPair ten2 = new KVPair(new Handle(10), new Handle(0));
        KVPair sxTwn = new KVPair(new Handle(16), new Handle(20));
        KVPair sxTwn2 = new KVPair(new Handle(20), new Handle(16));
        KVPair twnSx = new KVPair(new Handle(0), new Handle(26));
        KVPair twnSx2 = new KVPair(new Handle(26), new Handle(0));
        KVPair thrTn = new KVPair(new Handle(32), new Handle(10));
        KVPair thrTn2 = new KVPair(new Handle(10), new Handle(32));
        KVPair thrSx = new KVPair(new Handle(0), new Handle(36));
        KVPair thrSx2 = new KVPair(new Handle(36), new Handle(0));
        KVPair fourTwo = new KVPair(new Handle(0), new Handle(42));
        KVPair fourTwo2 = new KVPair(new Handle(42), new Handle(0));
        KVPair six48 = new KVPair(new Handle(16), new Handle(48));
        KVPair six48to = new KVPair(new Handle(48), new Handle(16));
        KVPair thirFi = new KVPair(new Handle(32), new Handle(54));
        KVPair thirFi2 = new KVPair(new Handle(54), new Handle(32));
        KVPair sixty = new KVPair(new Handle(0), new Handle(60));
        KVPair sixty2 = new KVPair(new Handle(60), new Handle(0));
        KVPair sixtyS = new KVPair(new Handle(16), new Handle(66));
        KVPair sixtyS2 = new KVPair(new Handle(66), new Handle(16));
        KVPair thirSv = new KVPair(new Handle(32), new Handle(72));
        KVPair thirSv2 = new KVPair(new Handle(72), new Handle(32));
        KVPair svFi = new KVPair(new Handle(78), new Handle(54));
        KVPair svFi2 = new KVPair(new Handle(54), new Handle(78));
        KVPair fourTwn = new KVPair(new Handle(48), new Handle(20));
        KVPair fourTwn2 = new KVPair(new Handle(20), new Handle(48));
        KVPair sixFi = new KVPair(new Handle(16), new Handle(54));
        KVPair sixFi2 = new KVPair(new Handle(54), new Handle(16));
        test.insert(four);
        test.insert(four2);
        test.insert(ten);
        test.insert(ten2);
        test.insert(sxTwn);
        test.insert(sxTwn2);
        test.insert(twnSx);
        test.insert(twnSx2);
        test.insert(thrTn);
        test.insert(thrTn2);
        test.insert(thrSx);
        test.insert(thrSx2);
        test.insert(fourTwo);
        test.insert(fourTwo2);
        test.insert(six48);
        test.insert(six48to);
        test.insert(thirFi);
        test.insert(thirFi2);
        systemOut().clearHistory();
        test.print();
        assertEquals(systemOut().getHistory(), "16 20\n  0 26 4 0\n    0 10\n"
                + "      0 4\n      0 10\n    0 36\n      0 26\n      "
                + "0 36 0 42\n    10 0\n      4 0\n      10 0 10 32\n  "
                + "26 0 36 0\n    20 16\n      16 20 16 48\n      20 16\n"
                + "    32 10\n      26 0\n      32 10 32 54\n    42 0 48 16\n"
                + "      36 0\n      42 0\n      48 16 54 32\n");
        test.delete(six48);
        test.delete(six48to);
        systemOut().clearHistory();
        test.print();
        assertEquals(systemOut().getHistory(), "16 20\n  0 26 4 0\n    0 10\n"
                + "      0 4\n      0 10\n    0 36\n      0 26\n      "
                + "0 36 0 42\n    10 0\n      4 0\n      10 0 10 32\n"
                + "  26 0 36 0\n    20 16\n      16 20\n      20 16\n    "
                + "32 10\n      26 0\n      32 10 32 54\n    42 0 54 32\n      "
                + "36 0\n      42 0\n      54 32\n");
        test.insert(six48);
        test.insert(six48to);
        test.insert(sixty);
        test.insert(sixty2);
        test.insert(sixtyS);
        test.insert(sixtyS2);
        test.insert(thirSv);
        test.insert(thirSv2);
        test.insert(svFi);
        test.insert(svFi2);
        test.delete(thirSv);
        test.delete(thirSv2);  
        test.delete(six48);
        test.delete(six48to);
        test.insert(fourTwn);
        test.insert(fourTwn2);
        test.insert(sixFi);
        test.insert(sixFi2);
        systemOut().clearHistory();
        test.print();
        assertEquals(systemOut().getHistory(), "16 20 36 0\n  0 26 4 0\n"
                + "    0 10\n      0 4\n      0 10\n    0 36 0 42\n      0 26\n"
                + "      0 36\n      0 42 0 60\n    10 0\n      4 0\n"
                + "      10 0 10 32\n  26 0\n    16 66 20 16\n      "
                + "16 20 16 54\n      16 66\n      20 16 20 48\n    "
                + "32 10 32 54\n      26 0\n      32 10\n      32 54\n  "
                + "54 32 66 16\n    42 0 48 20\n      36 0\n      42 0\n      "
                + "48 20 54 16\n    60 0\n      54 32 54 78\n      60 0\n"
                + "    78 54\n      66 16\n      78 54\n");   
    }
}
