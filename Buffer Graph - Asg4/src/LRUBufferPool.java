import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Class for Buffer Pool. In charge of sorting file by storing linked list of
 * buffers that each handle a specific number of bytes (or a block) that sorting
 * operations are done upon. Also reads and writes from file.
 * @author Jack Guttman
 * @version October 20th, 2016
 */
public class LRUBufferPool {
    private RandomAccessFile disk;
    private LinkedQue lru;
    private boolean cacheFlag;
    private int blockSize = 4096;
    private int fileEnd;
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
    public LRUBufferPool(String file, int numBuffers)
            throws IOException {        
        String binfile = file;
        disk = new RandomAccessFile(binfile, "rw");
        lru = new LinkedQue(numBuffers);
        for (int i = 0; i < numBuffers; i++) {
            lru.add(new Buffer(null, -1));
        }
        fileEnd = 0;
    }

    // -------------------------------------------------------------------------
    /**
     * Removes a buffer from the pool, LRU (least recently used) from the back.
     * @throws IOException
     */
    public void removeFromPool() throws IOException {
        Buffer remove = lru.remove();
        if (remove != null && remove.getDirtyBit()) {
            disk.seek(remove.getPos() * getBlockSize()); //B(1) * 32?
            byte[] temp = remove.getArray();
            disk.write(temp);
            writes += 1;
            disk.seek(0);
            fileEnd += getBlockSize();
        }

        lru.setOccupied(lru.getOccupied() - 1);
    }
    // -------------------------------------------------------------------------
    /**
     * Retrieves a specific Buffer based on the param pos
     * @param pos index to determine what buffer it is in the pool
     * @return the buffer holding that pos
     * @throws IOException
     */
    public Buffer getBuffer(int pos) throws IOException {
        int bufferP = (pos) / getBlockSize(); //block number
        Buffer ret = lru.find(bufferP);
        //if not in the buffer pool, add it to buffer pool from reading file
        if (ret == null) {
            byte[] newBuff = new byte[getBlockSize()];
            if (fileEnd > getBlockSize() * bufferP) {
                disk.seek(getBlockSize() * bufferP);
                disk.read(newBuff, 0, getBlockSize());
                disk.seek(0);
            }
            ret = new Buffer(newBuff, bufferP);
            //ret = new Buffer(newBuff, bufferP);
            lru.add(ret);
//            lru.head.getN().getBuf().setArray(newBuff);
//            lru.head.getN().getBuf().setPos(bufferP);
            lru.setOccupied(lru.getOccupied() + 1);
            //ret = lru.head.getN().getBuf();
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
     * copies "sz" bytes from "space" to position "pos" in storage
     * @param space where to copy
     * @param sz number of bytes to copy
     * @param pos where to in storage
     * @throws IOException
     */
    public void insert(byte[] space, int sz, int pos) throws IOException {
        int bPos = pos % getBlockSize(); //pos inside block 
        int able = getBlockSize() - bPos;
        if (sz <= able) {
            Buffer ret = getBuffer(pos);
            byte[] temp = ret.getArray();
            for (int i = 0; i < sz; i++) {
                temp[bPos] = space[i];
                bPos++;
            }
            ret.setArray(temp);
            ret.setDirtyBit(true);   
            if (cacheFlag) {
                hits += 1;
            }
        }
        else {
            int remaining = sz - able;
            byte[] firstOfBlock = new byte[able];
            for (int i = 0; i < able; i++) {
                firstOfBlock[i] = space[i];
            }
            byte[] tempSpace = new byte[remaining];
            System.arraycopy(space, able, tempSpace, 0, remaining);
            insert(firstOfBlock, able, pos);
            insert(tempSpace, remaining, pos + able);
        }
        
    }
    // -------------------------------------------------------------------------
    /**
     * copies "sz" bytes from position "pos" of storage to "space"
     * @param sz how many bytes to copy
     * @param pos where from in storage
     * @return      the bytes copied
     * @throws IOException
     */
    public byte[] getbytes(int sz, int pos) throws IOException {
        int bPos = pos % getBlockSize();
        int able = getBlockSize() - bPos;
        if (sz <= able) {
            Buffer ret = getBuffer(pos);
            byte[] temp = ret.getArray();
            byte[] tempSpace = new byte[sz];
            for (int i = 0; i < sz; i++) {
                tempSpace[i] = temp[bPos];
                bPos++;
            }
            if (cacheFlag) {
                hits += 1;
            }
            return tempSpace;
        }
        else {
            int remaining = sz - able;
            
            byte[] ret = getbytes(able, pos);
            byte[] ret2 = getbytes(remaining, pos + able);
            byte[] ret3 = new byte[ret.length + ret2.length];
            System.arraycopy(ret, 0, ret3, 0, ret.length);
            System.arraycopy(ret2, 0, ret3, ret.length, ret2.length);
            return ret3;
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
        closeFile();
    }
    // -------------------------------------------------------------------------
    /**
     * @param fileName
     * @throws IOException
     */
    public void clear() throws IOException {
        disk.setLength(0);
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
     * closes the file
     * @throws IOException 
     */
    public void closeFile() throws IOException {
        disk.close();
    }
    // -------------------------------------------------------------------------
    /**
     * @return the bLOCK_SIZE
     */
    public int getBlockSize() {
        return blockSize;
    }
    // -------------------------------------------------------------------------
    /**
     * @param block the bLOCK_SIZE to set
     */
    public void setBlockSize(int block) {
        blockSize = block;
    }
    // -------------------------------------------------------------------------
    /**
     * @return the fileEnd
     */
    public int getFileEnd() {
        return fileEnd;
    }
    // -------------------------------------------------------------------------
    /**
     * @param fileEnd the fileEnd to set
     */
    public void setFileEnd(int fileEnd) {
        this.fileEnd = fileEnd;
    }

}
