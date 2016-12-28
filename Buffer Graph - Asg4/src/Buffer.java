import java.nio.ByteBuffer;

/**
 * Buffer class. Holds a byte array as data and a boolean flag that changes if
 * any of the data in the array is altered.
 * @author Jack Guttman
 * @version October 20th, 2016
 */
public class Buffer {
    private byte[] array;
    private boolean dirtyBit;
    private int pos;
    // -------------------------------------------------------------------------
    /**
     * constructor for Buffer class
     * @param b the byte array that this buffer holds
     * @param position what position it is in the buffer pool
     */
    public Buffer(byte[] b, int position) {
        if (b != null) {
            array = new byte[b.length];
            setArray(b);
        }
        else {
            array = null;
        }
        dirtyBit = false;
        setPos(position);
    }
    // -------------------------------------------------------------------------
    /**
     * Gets the key at a specific index, which is the first 2 bytes at the index
     * @param index index to find key at
     * @return short representation of first 2 bytes
     */
    public short getKey(int index) {
        short ret = 0;
        ByteBuffer dbuf = ByteBuffer.allocate(2);
        dbuf.put(array, index, 2);
        ret = dbuf.getShort(0);
        return ret;
    }
    // -------------------------------------------------------------------------
    /**
     * @return the array
     */
    public byte[] getArray() {
        return array;
    }
    // -------------------------------------------------------------------------
    /**
     * @param a the array to set
     */
    public void setArray(byte[] a) {
        if (array == null) {
            array = a;
        }
        for (int i = 0; i < a.length; i++) {
            array[i] = a[i];
        }
    }
    // -------------------------------------------------------------------------
    /**
     * @return the dirtyBit
     */
    public boolean getDirtyBit() {
        return dirtyBit;
    }
    // -------------------------------------------------------------------------
    /**
     * @param b the dirtyBit to set
     */
    public void setDirtyBit(boolean b) {
        dirtyBit = b;
    }
    // -------------------------------------------------------------------------
    /**
     * @return the pos
     */
    public int getPos() {
        return pos;
    }
    // -------------------------------------------------------------------------
    /**
     * @param p the pos to set
     */
    public void setPos(int p) {
        pos = p;
    }
}
