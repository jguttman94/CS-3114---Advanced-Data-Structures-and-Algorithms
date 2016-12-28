import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Memory manager class stub. Used to store bytes in an array
 * with insert/remove/print methods.
 * @author Jack Guttman
 * @version September 1, 2016
 *
 */
public class MemManager {
    private FreeBlockList freeblock;
    private int initBlockSize;
    private static final int SIZE_TO_BYTE = 2;
    private LRUBufferPool lru;
    private int currentLen;
    //----------------------------------------------------------------------
    /**
     * Constructor for the memory manager
     * @param blockSize      defines the size of the
     *                      memory pool in bytes
     * @param numBuffs      number of buffers in pool
     * @param file          file name to write and read to/from
     * @throws IOException 
     */
    public MemManager(int blockSize, int numBuffs, String file)
            throws IOException {
        initBlockSize = blockSize;
        freeblock = new FreeBlockList();
        freeblock.insertInOrder(0, initBlockSize);
        lru = new LRUBufferPool(file, numBuffs);
        lru.setBlockSize(blockSize);
        setCurrentLen(blockSize);
    }
    //----------------------------------------------------------------------
    /**
     * Insert a record and return its position
     * @param space     the record to be inserted
     * @param size      of length size
     * @return          position handle
     * @throws IOException 
     */
    public Handle insert(byte[] space, int size) throws IOException {
        //looks for the best fit insert        
        int index = freeblock.probe(size + SIZE_TO_BYTE);
        
        //if no such spot is found, resize the byte array
        while (index == -1) {
            resize();
            index = freeblock.probe(size + SIZE_TO_BYTE);
        }
        
        //creates a temp array of space size and the 2 byte len
        byte[] temp = new byte[size + SIZE_TO_BYTE];
        
        //creates the 2 byte array for storing the length of space
        //and copies them over to the temp array
        ByteBuffer dbuf = ByteBuffer.allocate(SIZE_TO_BYTE);
        dbuf.putShort((short) size);
        byte[] bytes = dbuf.array();
        System.arraycopy(bytes, 0, temp, 0, SIZE_TO_BYTE);
        
        //copies over 'space' into temp
        System.arraycopy(space, 0, temp, SIZE_TO_BYTE, size);
        
        //inserts into buffer pool
        lru.insert(temp, temp.length, index);
        
        //starts process to update the freeblock, stores the current
        //length of freeblock for updating.
        freeblock.setCurr(index);
        int end = freeblock.getCurr().end();
        
        //changes the freeblock's information to reflect open spots
        //there is open space in the byte array
        int newBlockStart = index + size + SIZE_TO_BYTE;
        //new length of freeblock is the original length minus the size
        //of the bytes just inserted minus the first two bytes that store
        //the length of the record
        freeblock.getCurr().setEnd(end - size - SIZE_TO_BYTE);
        freeblock.getCurr().setIndex(newBlockStart);
        //if the insert took up entirety of freeblock, remove it
        if (freeblock.getCurr().end() == 0) {
            freeblock.remove();
        }
        
        //returns the handle--position in memory where 'space'
        //is stored, starting at the 2-byte header taken up by
        //the size of 'space'
        Handle ret = new Handle(index);
        return ret;
    }
    //----------------------------------------------------------------------
    /** 
     * Free a block at the position specified by 
     * @param theHandle     target location
     * @throws IOException 
     */
    public void remove(Handle theHandle) throws IOException { 
        //gets the size of the record
        int sizeRec = getSizeOfRecord(theHandle);
        //gets the loc of the record
        int loc = theHandle.getStartPos();
        if (sizeRec != -1) {
            //removing by adding freeblock, doesn't actually change
            //whats in the byte array (will be overwritten by future insert)
            freeblock.insertInOrder(loc, sizeRec + SIZE_TO_BYTE);
            freeblock.merge();
        }
    }
    //----------------------------------------------------------------------
    /**
     * Retrieves the size of the record by going into the memory pool
     * at theHandle's location and getting the length in the first 2 bytes
     * @param theHandle     what record to look for (in handle format)
     * @return              size/length of that record
     * @throws IOException 
     */
    public int getSizeOfRecord(Handle theHandle) throws IOException {
        //gets starting location according to handle
        int loc = theHandle.getStartPos();
        //checks to make sure it's within the memory pool
        if (loc < getCurrentLen()) {
            //unwrap the first 2 bytes to get the length of the record
            
            byte[] lenBytes = new byte[SIZE_TO_BYTE];
            lenBytes = lru.getbytes(SIZE_TO_BYTE, loc);
            ByteBuffer wrapped = ByteBuffer.wrap(lenBytes);
            int num = wrapped.getShort();
            //returns the length
            return num;
        }
        else {
            return -1;
        }
    }
    //----------------------------------------------------------------------
    /**
     * Return the record with handle posHandle,
     * up to size bytes, by copying it into space
     * @param space         storage
     * @param theHandle     what record is being located
     * @param size          size of record
     * @return      number of bytes actually copied
     *              into space.
     * @throws IOException 
     */
    public int get(byte[] space, Handle theHandle, int size)
            throws IOException {
        //checks that handle isnt null
        if (theHandle != null) {
            //retrieves handle location
            int handleLoc = theHandle.getStartPos();
            //checks that the location is within the bufferpool
            if (handleLoc < getCurrentLen()) {
                //copies whats in bufferpool into temp
                byte[] temp;
                temp = lru.getbytes(size, handleLoc + SIZE_TO_BYTE);
                //tracks how many bytes were actually inserted and ret it
                int count = 0;
                for (int i = 0; i < space.length; i++) {
                    if (temp[i] != 0) {
                        count++;
                    }
                    space[i] = temp[i];
                }
                return count;
            }
            //loc not in mempool
            else {
                return 0;
            }
        }
        //handle is null
        else {
            return 0;
        }
        
    }
    //----------------------------------------------------------------------
    /**
     * Resizes the byte array
     */
    public void resize() {
        //creates temp byte array with double the size of mempool
        int currMemLen = getCurrentLen();
        int updatedLen = currMemLen + initBlockSize;
        setCurrentLen(updatedLen);
        //inserts new node to reflect increased size and merges
        freeblock.insertInOrder(currMemLen, initBlockSize);
        freeblock.merge();
        System.out.println("Memory pool expanded to be " 
                + updatedLen + " bytes.");
    }
    //----------------------------------------------------------------------
    /**
     * Dumps a printout of the freeblock list
     */
    public void dump() {
        //sets up return string for printing
        String[] ret = freeblock.print();
        String printS = "";
        //all freeblocks are used, output (X,0) instead 
        //where X is size of array
        if (freeblock.isEmpty()) {        
            printS = "(" + getCurrentLen() * initBlockSize + 
                    ",0)";
            System.out.println(printS);
        }
        else {
            for (int i = 0; i < ret.length; i++) {
                if (i == ret.length - 1) { //if the last node in freeblock
                    printS += ret[i];
                }
                else {
                    printS += ret[i] + " -> "; //anything besides last node
                }                
            }
            System.out.println(printS);
        }
    }
    //----------------------------------------------------------------------
    /**
     * @param newfreeblock the freeblock to set
     */
    public void setFreeBlock(FreeBlockList newfreeblock) {
        this.freeblock = newfreeblock;
    }
    //----------------------------------------------------------------------
    /**
     * @return the freeblock
     */
    public FreeBlockList getFreeBlock() {
        return freeblock;
    }
    /**
     * @return the buffer
     */
    public LRUBufferPool getBuffer() {
        return lru;
    }
    /**
     * @param buffer the buffer to set
     */
    public void setBuffer(LRUBufferPool buffer) {
        this.lru = buffer;
    }
    
  //----------------------------------------------------------------------
    /**
     * @param newB the freeblock to set
     */
    public void setInitBlockSize(int newB) {
        this.initBlockSize = newB;
    }
    //----------------------------------------------------------------------
    /**
     * @return the freeblock
     */
    public int getInitBlockSize() {
        return initBlockSize;
    }
    /**
     * @return the currentLen
     */
    public int getCurrentLen() {
        return currentLen;
    }
    /**
     * @param currentLen the currentLen to set
     */
    public void setCurrentLen(int currentLen) {
        this.currentLen = currentLen;
    }

    

}
