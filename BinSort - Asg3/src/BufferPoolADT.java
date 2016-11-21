import java.io.IOException;

/**
 * 
 */

/**
 * @author Dr. Cliff Shaffer
 * @version 1
 */
// ADT for buffer pools using the message-passing style
public interface BufferPoolADT {
    /**
     * copies "sz" bytes from "space" to position "pos" in storage
     * @param space where to copy
     * @param sz number of bytes to copy
     * @param pos where to in storage
     * @throws IOException
     */
    public void insert(byte[] space, int sz, int pos) throws IOException;

    /**
     * copies "sz" bytes from position "pos" of storage to "space"
     * @param space where to copy to
     * @param sz how many bytes to copy
     * @param pos where from in storage
     * @throws IOException
     */
    public void getbytes(byte[] space, int sz, int pos) throws IOException;
}
