import java.io.IOException;
import java.util.Arrays;
import student.TestCase;

/**
 * LRUBufferPool test class
 * @author Jack Guttman
 * @version October 20th, 2016
 */
public class LRUBufferPoolTest extends TestCase {
    private LRUBufferPool bPool;    
    /**
     * tests the initialization of the buffer pool
     * @throws IOException 
     */
    public void setUp() throws IOException {
        FileGenerator fileGen = new FileGenerator();
        fileGen.generateFile(new String[]{"-a", "testGenerator", "1"});
        bPool = new LRUBufferPool("testGenerator", 10);
    }

    /**
     * tests the initialization
     * @throws Exception 
     */
    public void testSetup() throws Exception {
        assertEquals(bPool.getReads(), 0);
        assertEquals(bPool.getLRU().getSize(), 10);
    }
    
    /**
     * tests remove function
     */
    public void testRemove() throws IOException {
        assertEquals(bPool.getBuffer(1023).getPos(), 0);
        assertEquals(bPool.getBuffer(1023).getArray().length, 4096);
        assertEquals(bPool.getBuffer(1).getDirtyBit(), false);
        assertEquals(bPool.getLRU().getSize(), 10);
        bPool.removeFromPool();
        assertEquals(bPool.getWrites(), 0);
        assertEquals(bPool.getLRU().getSize(), 9);
        bPool.removeFromPool();
        assertEquals(bPool.getLRU().getSize(), 8);
        bPool.removeFromPool();
        assertEquals(bPool.getLRU().getSize(), 7);
    }
    
    /**
     * tests overlapping insert
     */
    public void testOverlapInsertGet() throws IOException {
        bPool.setBlockSize(10);
        byte[] tempIns = new byte[40];
        for (int i = 0; i < 40; i++) {
            tempIns[i] = (byte) i;
        }
        System.out.println(Arrays.toString(tempIns));
        bPool.insert(tempIns, 40, 0);
        assertEquals(Arrays.toString(bPool.getLRU().head.getN().
                getBuf().getArray()), 
                "[30, 31, 32, 33, 34, 35, 36, 37, 38, 39]");
        assertEquals(Arrays.toString(bPool.getLRU().head.getN().getN().
                getBuf().getArray()), 
                "[20, 21, 22, 23, 24, 25, 26, 27, 28, 29]");
        assertEquals(Arrays.toString(bPool.getLRU().head.getN().getN().getN().
                getBuf().getArray()), 
                "[10, 11, 12, 13, 14, 15, 16, 17, 18, 19]");
        assertEquals(Arrays.toString(bPool.getLRU().head.getN().getN().
                getN().getN().getBuf().getArray()), 
                "[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]");
        
        //assertEquals(bPool.getLRU().getOccupied(), 4);
        assertEquals(bPool.getLRU().head.getN().getBuf().getPos(), 3);
        
        //tempIns = new byte[15];
        //grabs from indicies 25-->40, 2 pools
        tempIns = bPool.getbytes(15, 25);
        assertEquals(Arrays.toString(tempIns),
                "[25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39]");
    }

}
