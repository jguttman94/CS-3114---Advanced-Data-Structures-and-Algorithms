import java.nio.ByteBuffer;

/**
 * Memory manager class stub. Used to store bytes in an array
 * with insert/remove/print methods.
 * @author Jack Guttman
 * @version September 1, 2016
 *
 */
public class MemManager {
    
    private byte[] memPool;
    private FreeBlockList freeblock;
    private int initpoolSize;
    private int sizeToByte = 2;
    //----------------------------------------------------------------------
    /**
     * Constructor for the memory manager
     * @param poolsize      defines the size of the
     *                      memory pool in bytes
     */
    public MemManager(int poolsize) {
        initpoolSize = poolsize;
        setMemPool(new byte[initpoolSize]);
        freeblock = new FreeBlockList();
        freeblock.insertInOrder(0, initpoolSize);
    }
    //----------------------------------------------------------------------
    /**
     * Insert a record and return its position
     * @param space     the record to be inserted
     * @param size      of length size
     * @return          position handle
     */
    public Handle insert(byte[] space, int size) {
        //looks for the best fit insert        
        int index = freeblock.probe(size + sizeToByte);
        
        //if no such spot is found, resize the byte array
        while (index == -1) {
            resize();
            index = freeblock.probe(size + sizeToByte);
        }
        
        
        //creates a temp array and copies over past bytes
        byte[] temp = new byte[getMemLen()];
        System.arraycopy(memPool, 0, temp, 0, getMemLen());
        
        //creates the 2 byte array for storing the length of space
        //and copies them over to the temp array
        ByteBuffer dbuf = ByteBuffer.allocate(sizeToByte);
        dbuf.putShort((short) size);
        byte[] bytes = dbuf.array();
        System.arraycopy(bytes, 0, temp, index, sizeToByte);
        
        //copies over 'space' into temp
        System.arraycopy(space, 0, temp, index + sizeToByte, size);
        
        //starts process to update the freeblock, stores the current
        //length of freeblock for updating.
        freeblock.setCurr(index);
        int end = freeblock.getCurr().end();
        
        //changes the freeblock's information to reflect open spots
        //there is open space in the byte array
        int newBlockStart = index + size + sizeToByte;
        //new length of freeblock is the original length minus the size
        //of the bytes just inserted minus the first two bytes that store
        //the length of the record
        freeblock.getCurr().setEnd(end - size - sizeToByte);
        freeblock.getCurr().setIndex(newBlockStart);
        //if the insert took up entirety of freeblock, remove it
        if (freeblock.getCurr().end() == 0) {
            freeblock.remove();
        }
        
        
        //copies temp into memPool to update
        System.arraycopy(temp, 0, memPool, 0, temp.length);
        
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
     */
    public void remove(Handle theHandle) { 
        //gets the size of the record
        int sizeRec = getSizeOfRecord(theHandle);
        //gets the loc of the record
        int loc = theHandle.getStartPos();
        if (sizeRec != -1) {
            //removing by adding freeblock, doesn't actually change
            //whats in the byte array (will be overwritten by future insert)
            freeblock.insertInOrder(loc, sizeRec + sizeToByte);
            freeblock.merge();
        }
    }
    //----------------------------------------------------------------------
    /**
     * Retrieves the size of the record by going into the memory pool
     * at theHandle's location and getting the length in the first 2 bytes
     * @param theHandle     what record to look for (in handle format)
     * @return              size/length of that record
     */
    public int getSizeOfRecord(Handle theHandle) {
        //gets starting location according to handle
        int loc = theHandle.getStartPos();
        //checks to make sure it's within the memory pool
        if (loc < getMemLen()) {
            //unwrap the first 2 bytes to get the length of the record
            byte[] lenByte = new byte[] {memPool[loc], memPool[loc + 1]};
            ByteBuffer wrapped = ByteBuffer.wrap(lenByte);
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
     */
    public int get(byte[] space, Handle theHandle, int size) {
        //checks that handle isnt null
        if (theHandle != null) {
            //retrieves handle location
            int handleLoc = theHandle.getStartPos();
            //checks that the location is within the mempool
            if (handleLoc < getMemLen()) {
                //copies whats in mempool into space
                System.arraycopy(memPool, handleLoc + sizeToByte,
                        space, 0, size);
                //tracks how many bytes were actually inserted and ret it
                int count = 0;
                for (int i = 0; i < space.length; i++) {
                    if (space[i] != 0) {
                        count++;
                    }
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
        int currMemLen = getMemLen();
        byte[] temp = new byte[getMemLen() + initpoolSize];
        System.arraycopy(memPool, 0, temp, 0, getMemLen());
        memPool = temp;
        
        //inserts new node to reflect increased size and merges
        freeblock.insertInOrder(currMemLen, initpoolSize);
        freeblock.merge();
        System.out.println("Memory pool expanded to be " 
                + getMemLen() + " bytes.");
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
            printS = "(" + getMemLen() + ",0)";
            System.out.println(printS);
        }
        else {
            for (int i = 0; i < ret.length; i++) {
//                if (ret[i] == null) {
//                    //dead code
//                    printS += "";
//                }
//                else 
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
     * @return the memPool
     */
    public byte[] getMemPool() {
        return memPool;
    }
    //----------------------------------------------------------------------
    /**
     * @param memPool the memPool to set
     */
    public void setMemPool(byte[] memPool) {
        this.memPool = memPool;
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
     * @return the memPool current length
     */
    public int getMemLen() {
        return memPool.length;
    }
    //----------------------------------------------------------------------
    /**
     * @return the freeblock
     */
    public FreeBlockList getFreeBlock() {
        return freeblock;
    }

    

}
