import java.io.IOException;

import student.TestCase;

// -------------------------------------------------------------------------
/**
 *  Test the hash class
 *
 *  @author CS3114 staff
 *  @version Aug 27, 2016
 */
public class HashTest extends TestCase {
    /**
     * test table filled with values to assert upon
     */
    private Handle[] testTable;
    /**
     * hash object
     */
    private Hash myHash;
    /**
     * memory manager object
     */
    private MemManager mem;
    //----------------------------------------------------------------------
    /**
     * Sets up the tests that follow.
     * @throws IOException 
     */
    public void setUp() throws IOException {
        mem = new MemManager(32, 10, "testBin");
        myHash = new Hash(10, mem);
        testTable = new Handle[] {null, null, new Handle(2), null, null, 
            null, null, new Handle(7), new Handle(8), new Handle(9)};

    }
    //----------------------------------------------------------------------
    /**
     * Test initialization
     */
    public void testMain() {
        assertEquals(myHash.getHashSize(), 0);
        assertEquals(myHash.getHashTableSize(), 10);
        assertEquals(myHash.getHashTable().length, 10);
        assertEquals(myHash.getTombstone().getHandle().getStartPos(), -1);
        
    }
    //----------------------------------------------------------------------
    /**
     * Test the check func
     * @throws IOException 
     */
    public void testCheck() throws IOException {
        assertEquals(testTable[7].getStartPos(), 7);
        assertEquals(testTable[2].getStartPos(), 2);
        assertEquals(testTable[9].getStartPos(), 9);
        assertNull(testTable[0]);
        myHash.insert("song1");
        assertEquals(myHash.check("song1"), 8);
        assertEquals(myHash.check("empty"), -1);
    }
    //----------------------------------------------------------------------
    /**
     * Test the probe func
     * @throws IOException 
     */
    public void testProbe() throws IOException {
        assertEquals(myHash.insert("song3"), true);
        assertEquals(myHash.check("song3"), 0);
        assertEquals(myHash.getHashTable()[0].getHandle().getStartPos(), 0);
        assertEquals(myHash.getHashTable()[1], null);
        
        myHash.remove("song3");
        assertEquals(myHash.getHashTable()[0], myHash.getTombstone());
        assertEquals(myHash.probe("song3"), 0);
        assertEquals(myHash.probe("song5"), 2);
        assertEquals(myHash.probe("song3"), 0);
        assertEquals(myHash.probe("song"), 9);
        myHash.insert("song1");
        myHash.insert("song2");
        myHash.insert("song3");
        myHash.insert("song4");
        myHash.insert("song5");
        myHash.insert("song6");
        myHash.insert("song7");
        myHash.insert("song8");
        myHash.insert("song9");
        myHash.insert("song10");
        assertEquals(myHash.probe("test"), -1);
        assertEquals(myHash.insert("song13"), false);
        
    }
    //----------------------------------------------------------------------
    /**
     * Test insert func
     * @throws IOException 
     */
    public void testInsert() throws IOException {
        assertEquals(myHash.insert("song1"), true);
        myHash.insert("song1");
        assertEquals(myHash.check("song1"), 8);
        assertEquals(myHash.retrieveString(
                myHash.getHashTable()[8].getHandle()), "song1");
        assertEquals(myHash.insert("song1"), false);
        
    }
    //----------------------------------------------------------------------
    /**
     *  testing resizing when needed
     * @throws IOException 
     */
    public void testResize() throws IOException {
        Hash resize1 = new Hash(2, mem);
        assertEquals(resize1.getHashTableSize(), 2);
        resize1.resize();
        assertEquals(resize1.getHashTableSize(), 2);
        resize1.insert("song");
        resize1.resize();
        assertEquals(resize1.getHashTableSize(), 4);
        resize1.insert("song2");
        resize1.insert("song3");
        resize1.remove("song");
        resize1.resize();
        assertEquals(resize1.getHashTableSize(), 8);
    }
    //----------------------------------------------------------------------
    /**
     * tests the remove method for artists
     * @throws IOException 
     */
    public void testRemove() throws IOException {
        myHash.insert("test");
        assertEquals(myHash.getHashSize(), 1);
        assertEquals(myHash.check("test"), 8);
        
         
        myHash.remove("test");
        assertEquals(myHash.getHashSize(), 0);
        assertEquals(myHash.check("test"), -1);
        assertEquals(myHash.getHashTable()[8], myHash.getTombstone());
        
        myHash.print();
        assertEquals(myHash.print(), false);
        
        //test remove on an item not in the table
        assertEquals(myHash.remove("test"), false);
    }
    /**
     * tests emptying the table
     * @throws IOException 
     */
    public void testRepeatInsertAndRemove() throws IOException {
        Hash testHash = new Hash(10, mem);
        testHash.insert("test");
        testHash.insert("test1");
        testHash.insert("test2");
        testHash.insert("test3");
        assertEquals(testHash.insert("test4"), true);
        testHash.insert("test5");
        testHash.insert("test6");
        testHash.insert("test7");
        testHash.insert("test8");
        testHash.insert("test9");
        testHash.insert("test10");
        systemOut().clearHistory();
        testHash.print();
        assertEquals(systemOut().getHistory(), "|test3| 0\n|test4| 1\n"
                + "|test5| 2\n|test6| 3\n|test7| 4\n|test8| 5\n"
                + "|test9| 6\n|test1| 7\n|test| 8\n|test2| 9\n");
        
        systemOut().clearHistory();
        assertEquals(testHash.remove("test4"), true);
        testHash.print();
        assertEquals(systemOut().getHistory(), "|test3| 0\n"
                + "|test5| 2\n|test6| 3\n|test7| 4\n|test8| 5\n"
                + "|test9| 6\n|test1| 7\n|test| 8\n|test2| 9\n");
        
        systemOut().clearHistory();
        testHash.remove("test5");
        testHash.remove("test6");
        testHash.remove("test7");
        testHash.remove("test8");
        testHash.remove("test9");
        testHash.remove("test10");
        testHash.remove("test1");
        testHash.remove("test");
        testHash.remove("test2");
        testHash.remove("test3");
        testHash.print();
        assertEquals(systemOut().getHistory(), "");
    }
    //----------------------------------------------------------------------
    /**
     * tests printing artists
     * @throws IOException 
     */
    public void testPrint() throws IOException {
        assertEquals(myHash.getHashSize(), 0);
        myHash.print();
        assertEquals(myHash.print(), false);

        systemOut().clearHistory();
        myHash.insert("test");
        myHash.insert("test 1");
        
        assertEquals(myHash.getHashSize(), 2);
        myHash.print();
        assertEquals(systemOut().getHistory(),
                "|test 1| 4\n|test| 8\n");
        myHash.remove("test");
        
        systemOut().clearHistory();
        myHash.print();
        assertEquals(systemOut().getHistory(),
                "|test 1| 4\n");
        
        assertEquals(myHash.retrieveString(new Handle(-1)), "");
    }
}
