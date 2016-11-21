import student.TestCase;
/**
 * @author Jack Guttman
 * @version September 1, 2016
 *
 */
public class MemManagerTest extends TestCase {

    private MemManager memTest;
    //----------------------------------------------------------------------
    /**
     *sets up testing
     */
    public void setUp() {
        memTest = new MemManager(32);
    }
    //----------------------------------------------------------------------
    /**
     * tests initialization
     */
    public void testSetup() {        
        assertEquals(memTest.getMemPool().length, 32);
        assertTrue(memTest.getFreeBlock().getHead().next().index().equals(0));
        assertTrue(memTest.getFreeBlock().getHead().next().end().equals(32));
        assertEquals(memTest.get(null, null, 0), 0);

        memTest.setFreeBlock(null);
        assertEquals(memTest.getFreeBlock(), null);
        
    }
    //----------------------------------------------------------------------
    /**
     * testing that dump prints correctly
     */
    public void testDump() {
        //assertEquals(systemOut().getHistory(), "Empty list");
        systemOut().clearHistory();
        memTest.dump();
        assertEquals(systemOut().getHistory(), "(0,32)\n");
        memTest.getFreeBlock().append(60, 70);
        memTest.getFreeBlock().append(100, 200);
        systemOut().clearHistory();
        memTest.dump();
        assertEquals(systemOut().getHistory(), 
                "(0,32) -> (60,70) -> (100,200)\n");
        memTest.getFreeBlock().remove();
        memTest.getFreeBlock().remove();
        memTest.getFreeBlock().remove();
        memTest.dump();
        memTest.getFreeBlock().insert(32, 16);
        systemOut().clearHistory();
        memTest.dump();
        assertEquals(systemOut().getHistory(), "(32,16)\n");
        systemOut().clearHistory();
    }
    //----------------------------------------------------------------------
    /**
     * tests mempool insertion
     */
    public void testInsert() {
        String test = "Artist Name";
        memTest.insert(test.getBytes(), 11);
        String decrypted = new String(memTest.getMemPool());
        assertEquals(decrypted.substring(2, 13), "Artist Name");
        assertEquals(memTest.getFreeBlock().print()[0], "(13,19)");
        
        String test2 = "Daft Punk";
        memTest.insert(test2.getBytes(), 9);
        decrypted = new String(memTest.getMemPool());
        assertEquals(decrypted.substring(15, 24), "Daft Punk");
        assertEquals(memTest.getFreeBlock().print()[0], "(24,8)");
        
        String test3 = "Green Day";
        memTest.insert(test3.getBytes(), 9);
        decrypted = new String(memTest.getMemPool());
        assertEquals(decrypted.substring(26, 35), "Green Day");
        assertEquals(memTest.getFreeBlock().print()[0], "(35,29)");
    }
    //----------------------------------------------------------------------
    /**
     * tests removal
     */
    public void testRemove() {
        String test = "Artist Name";
        memTest.insert(test.getBytes(), 11);
        String test2 = "Daft Punk";
        memTest.insert(test2.getBytes(), 9);
        String test3 = "Green Day";
        memTest.insert(test3.getBytes(), 9);
        memTest.remove(new Handle(13));
        systemOut().clearHistory();
        memTest.dump();
        assertEquals(systemOut().getHistory(), "(13,11) -> (35,29)\n");
        assertEquals(memTest.getFreeBlock().print()[0], "(13,11)");
        assertEquals(memTest.getFreeBlock().getSize(), 2);
        memTest.remove(new Handle(24));
        memTest.dump();
        assertEquals(memTest.getFreeBlock().getSize(), 1);
        assertEquals(memTest.getFreeBlock().print()[0], "(13,51)");
        memTest.remove(new Handle(65));
    }
    //----------------------------------------------------------------------
    /**
     * tests get method
     */
    public void testGet() {
        byte[] testSpace = new byte[10];
        assertEquals(memTest.get(testSpace, new Handle(60), 9), 0);
        memTest.insert("Daft Punk".getBytes(), 9);
        assertEquals(memTest.get(testSpace, new Handle(0), 9), 9);
    }
    //----------------------------------------------------------------------
    /**
     * repeated test insert for resize 
     */
    public void testInsertAndResize() {
        systemOut().clearHistory();
        memTest.insert("Green Day".getBytes(), 9);
        memTest.insert("Kanye West".getBytes(), 10);
        memTest.insert("blink-182".getBytes(), 9);
        memTest.insert("Logic".getBytes(), 5);
        memTest.insert("Hoodie Allen".getBytes(), 12);
        memTest.insert("Sum 41".getBytes(), 6);
        memTest.insert("B.o.B".getBytes(), 5);
        memTest.insert("Cherub".getBytes(), 6);
        assertEquals(systemOut().getHistory(), 
                "Memory pool expanded to be 64 bytes.\n"
                + "Memory pool expanded to be 96 bytes.\n");        
    }
    //----------------------------------------------------------------------
    /**
     * tests insert and remove together
     */
    public void testInsertAndRemove() {
        systemOut().clearHistory();
        Handle green = memTest.insert("Green Day".getBytes(), 9);
        Handle kanye = memTest.insert("Kanye West".getBytes(), 10);
        Handle blink = memTest.insert("blink-182".getBytes(), 9);
        Handle logic = memTest.insert("Logic".getBytes(), 5);
        Handle hoodie = memTest.insert("Hoodie Allen".getBytes(), 12);
        memTest.remove(hoodie);        
        Handle sum41 = memTest.insert("Sum 41".getBytes(), 6);
        assertEquals(hoodie.getStartPos(), sum41.getStartPos());
        
        Handle bob = memTest.insert("B.o.B".getBytes(), 5);
        
        Handle cherub = memTest.insert("Cherub".getBytes(), 6);
        Handle story1 = memTest.insert("The Story So Far".getBytes(), 16);
        
        Handle story2 = memTest.insert("The Story So Fat".getBytes(), 16);
        Handle story5 = memTest.insert("The Story So Far".getBytes(), 16);
        memTest.remove(story2);
        Handle story3 = memTest.insert("The Story So Fad".getBytes(), 16);
        assertEquals(story2.getStartPos(), story3.getStartPos());
        Handle story4 = memTest.insert("The Story So Fan".getBytes(), 16);
        memTest.remove(green);
        memTest.remove(kanye);
        memTest.remove(blink);
        memTest.remove(logic);
        memTest.remove(sum41);
        memTest.remove(bob);
        memTest.remove(cherub);
        memTest.remove(story1);
        memTest.remove(story3);
        memTest.remove(story4);
        memTest.remove(story5);
        byte[] temp = new byte[18];
        memTest.get(temp, new Handle(100), 18);
        System.out.println(new String(temp));
    }
    //----------------------------------------------------------------------
    /**
     * remove all items
     */
    public void testRemoveAll() {
        systemOut().clearHistory();
        MemManager testRemoveAll = new MemManager(10000);
        testRemoveAll.remove(new Handle(999999));
        Handle one = testRemoveAll.insert(
                "This is just a test".getBytes(), 19);
        Handle two = testRemoveAll.insert(
                "Just testing remove all".getBytes(), 23);
        testRemoveAll.remove(one);
        testRemoveAll.remove(two);
        systemOut().clearHistory();
        testRemoveAll.dump();
        assertEquals(systemOut().getHistory(), "(0,10000)\n");
        Handle three = testRemoveAll.insert(
                "blankblankblank    ".getBytes(), 19);
        Handle four = testRemoveAll.insert(
                "                       ".getBytes(), 23);
        Handle five = testRemoveAll.insert(
                "zzzzzzzzzzzzzzzzzzz".getBytes(), 19);
        Handle six = testRemoveAll.insert(
                "aaaaaaaaaaaaaaaaaaaaaaa".getBytes(), 23);
        Handle seven = testRemoveAll.insert(
                "testtesttesttesttes".getBytes(), 19);
        testRemoveAll.remove(five);
        testRemoveAll.remove(seven);
        Handle eight = testRemoveAll.insert(
                "ooooooooooooooooooooooo".getBytes(), 23);
        Handle nine = testRemoveAll.insert(
                "ppppppppppppppppppp".getBytes(), 19);
        Handle ten = testRemoveAll.insert(
                "ttttttttttttttttttttttt".getBytes(), 23);
        testRemoveAll.remove(three);
        testRemoveAll.remove(four);
        testRemoveAll.remove(six);
        testRemoveAll.remove(eight);
        testRemoveAll.remove(nine);
        testRemoveAll.remove(ten);
        systemOut().clearHistory();
        testRemoveAll.dump();
        assertEquals(systemOut().getHistory(), "(0,10000)\n");
        Handle y = testRemoveAll.insert("yyyyyyyyyyyyyyyy".getBytes(), 16);
        Handle c = testRemoveAll.insert("cccccccccccccccc".getBytes(), 16);
        Handle z = testRemoveAll.insert("zzzzzzzzzzzzzzzz".getBytes(), 16);
        Handle o = testRemoveAll.insert("oooooooooooooooo".getBytes(), 16);
        testRemoveAll.remove(y);
        testRemoveAll.remove(c);
        testRemoveAll.remove(z);
        testRemoveAll.remove(o);
        systemOut().clearHistory();
        testRemoveAll.dump();
        assertEquals(systemOut().getHistory(), "(0,10000)\n");

    }
}
