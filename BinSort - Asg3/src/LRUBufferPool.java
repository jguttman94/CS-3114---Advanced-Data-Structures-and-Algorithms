import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Class for Buffer Pool. In charge of sorting file by storing linked list of
 * buffers that each handle a specific number of bytes (or a block) that sorting
 * operations are done upon. Also reads and writes from file.
 * @author Jack Guttman
 * @version October 20th, 2016
 */
public class LRUBufferPool implements BufferPoolADT {
    private RandomAccessFile disk;
    private LinkedQue lru;
    private boolean cacheFlag;
    private static final int BLOCK_SIZE = 4096;
    private static final int RECORD_SIZE = 4;
    private long endTime;
    /**
     * number of cache hits (counter)
     */
    int hits = 0;
    /**
     * number of disk reads (counter)
     */
    int reads = 0;
    /**
     * number of disk writes (counter)
     */
    int writes = 0;
    // -------------------------------------------------------------------------
    /**
     * Constructor for buffer pool.
     * @param file RAF file that operations are done to
     * @param numBuffers max number of buffers passed from main's param
     * @throws IOException
     */
    public LRUBufferPool(RandomAccessFile file, int numBuffers)
            throws IOException {
        long startTime = System.currentTimeMillis();
        disk = file;
        int diskLength = (int) disk.length();
        lru = new LinkedQue(numBuffers);
        for (int i = 0; i < numBuffers; i++) {
            lru.add(new Buffer(null, -1));
        }
        new Sort(this, diskLength);
        writeFinish();
        endTime = System.currentTimeMillis() - startTime;
    }

    // -------------------------------------------------------------------------
    /**
     * Removes a buffer from the pool, LRU (least recently used) from the back.
     * @throws IOException
     */
    public void removeFromPool() throws IOException {
        Buffer remove = lru.remove();
        if (remove != null && remove.getDirtyBit()) {
            disk.seek(remove.getPos() * BLOCK_SIZE);
            byte[] temp = remove.getArray();
            disk.write(temp);
            writes += 1;
            disk.seek(0);
        }
    }
    // -------------------------------------------------------------------------
    /**
     * Retrieves a specific Buffer based on the param pos
     * @param pos index to determine what buffer it is in the pool
     * @return the buffer holding that pos
     * @throws IOException
     */
    public Buffer getBuffer(int pos) throws IOException {
        int bufferP = (pos * RECORD_SIZE) / BLOCK_SIZE;
        Buffer ret = lru.find(bufferP);
        //if not in the buffer pool, add it to buffer pool from reading file
        if (ret == null) {
            byte[] newBuff = new byte[BLOCK_SIZE];
            disk.seek(BLOCK_SIZE * bufferP);
            disk.read(newBuff, 0, BLOCK_SIZE);
            disk.seek(0);
            ret = new Buffer(newBuff, bufferP);
            lru.add(ret);
            if (lru.getSize() > lru.getMaxSize()) {
                removeFromPool();
            }
            reads += 1;
            cacheFlag = false;
            return ret;
        }
        //if found in pool, track as a cache hit
        cacheFlag = true;
        return ret;
    }
    // -------------------------------------------------------------------------
    /**
     * Retrieves a specfic key based on an index in the file
     * @param index where to get the key
     * @return the key to return
     * @throws IOException
     */
    public short retrieveKey(int index) throws IOException {
        short ret = 0;
        if (cacheFlag) {
            hits += 1;
        }
        Buffer buf = getBuffer(index);
        int bPos = (index * RECORD_SIZE) % BLOCK_SIZE;
        ret = buf.getKey(bPos);
        return ret;
    }
    // -------------------------------------------------------------------------
    @Override
    public void insert(byte[] space, int sz, int pos) throws IOException {
        Buffer ret = getBuffer(pos);
        int bPos = (pos * RECORD_SIZE) % BLOCK_SIZE;
        byte[] temp = ret.getArray();
        for (int i = 0; i < sz; i++) {
            temp[bPos] = space[i];
            bPos++;
        }
        ret.setArray(temp);
        ret.setDirtyBit(true);
    }
    // -------------------------------------------------------------------------
    @Override
    public void getbytes(byte[] space, int sz, int pos) throws IOException {
        Buffer ret = getBuffer(pos);
        int bPos = (pos * RECORD_SIZE) % BLOCK_SIZE;
        byte[] temp = ret.getArray();
        for (int i = 0; i < sz; i++) {
            space[i] = temp[bPos];
            bPos++;
        }
    }
    // -------------------------------------------------------------------------
    /**
     * called when sorting is done, finishes writing all buffers back into file
     * @throws IOException
     */
    public void writeFinish() throws IOException {
        while (lru.getSize() > 0) {
            removeFromPool();
        }
    }
    // -------------------------------------------------------------------------
    /**
     * @return the number of cache hits that occurred
     */
    public int getHits() {
        return hits;
    }
    // -------------------------------------------------------------------------
    /**
     * @return the number of disk reads that occurred
     */
    public int getReads() {
        return reads;
    }
    // -------------------------------------------------------------------------
    /**
     * @return number of disk writes that occurred
     */
    public int getWrites() {
        return writes;
    }
    // -------------------------------------------------------------------------
    /**
     * @return the linked list
     */
    public LinkedQue getLRU() {
        return lru;
    }
    // -------------------------------------------------------------------------
    /**
     * @return the time elapsed
     */
    public long getTime() {
        return (endTime / 1000000);
    }
    // -------------------------------------------------------------------------
    /**
     * closes the file
     * @throws IOException 
     */
    public void closeFile() throws IOException {
        disk.close();
    }
}
