import java.io.IOException;

/**
 * the Sorting algorithm class (uses modified quicksort)
 * @author Jack Guttman
 * @version October 20th, 2016
 */
public class Sort {
    /**
     * the buffer pool
     */
    public static LRUBufferPool bPool;
    private static final int RECORD_SIZE = 4;
    // -------------------------------------------------------------------------
    /**
     * constructor for sort class
     * @param pool the buffer pool to sort on
     * @param filelength length of file
     * @throws IOException
     */
    // -------------------------------------------------------------------------
    public Sort(LRUBufferPool pool, int filelength) throws IOException {
        bPool = pool;
        quicksort(0, (filelength / RECORD_SIZE) - 1);
    }
    // -------------------------------------------------------------------------
    /**
     * Sorting method
     * @param i where to start
     * @param j where to end
     * @throws IOException
     */
    private void quicksort(int i, int j) throws IOException {
        if (j <= i) {
            return;
        }
        int iTemp = i;
        int jTemp = j;
        short v = bPool.retrieveKey(i);
        int count = i;
        while (count <= jTemp) {
            if (bPool.retrieveKey(count) < v) {
                swap(iTemp++, count++);
            }
            else if (bPool.retrieveKey(count) > v) {
                swap(count, jTemp--);
            }
            else {
                count++;
            }
        }
        quicksort(i, iTemp - 1);
        quicksort(jTemp + 1, j);
    }
    // -------------------------------------------------------------------------
    /**
     * swaps 2 values in an array
     * @param l left index to swap
     * @param r right index to swap
     * @throws IOException
     */
    private void swap(int l, int r) throws IOException {
        byte[] lTemp = new byte[RECORD_SIZE];
        byte[] rTemp = new byte[RECORD_SIZE];
        bPool.getbytes(lTemp, RECORD_SIZE, l);
        bPool.getbytes(rTemp, RECORD_SIZE, r);
        bPool.insert(lTemp, RECORD_SIZE, r);
        bPool.insert(rTemp, RECORD_SIZE, l);
    }
}
