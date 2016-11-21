import java.io.IOException;
import java.io.RandomAccessFile;

import student.TestCase;

/**
 * LRUBufferPool test class
 * @author Jack Guttman
 * @version October 20th, 2016
 */
public class LRUBufferPoolTest extends TestCase {
    private LRUBufferPool bPool;
    private CheckFile check;
    
    /**
     * tests the initialization of the buffer pool
     * @throws IOException 
     */
    public void setUp() throws IOException {
        check = new CheckFile();
        FileGenerator fileGen = new FileGenerator();
        fileGen.generateFile(new String[]{"-a", "testGenerator", "1"});
        RandomAccessFile gen = new RandomAccessFile("testGenerator", "rw");
        bPool = new LRUBufferPool(gen, 10);
    }

    /**
     * tests the initialization
     * @throws Exception 
     */
    public void testSetup() throws Exception {
        assertTrue(check.checkFile("testGenerator"));
        assertEquals(bPool.getReads(), 1);
        assertEquals(bPool.getLRU().getSize(), 0);
        assertEquals(bPool.getReads(), 1);
    }
    
    /**
     * tests remove function
     */
    public void testRemove() throws IOException {
        assertEquals(bPool.getBuffer(1023).getPos(), 0);
        assertEquals(bPool.getBuffer(1023).getArray().length, 4096);
        assertEquals(bPool.getBuffer(1).getDirtyBit(), false);
        assertEquals(bPool.getLRU().getSize(), 1);
        bPool.removeFromPool();
        assertEquals(bPool.getWrites(), 1);
        assertEquals(bPool.getLRU().getSize(), 0);
        bPool.removeFromPool();
        assertEquals(bPool.getLRU().getSize(), 0);
        bPool.removeFromPool();
        assertEquals(bPool.getLRU().getSize(), 0);
    }

}
