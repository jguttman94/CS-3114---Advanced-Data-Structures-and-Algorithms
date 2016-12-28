/**
 * Node class for freeblock
 * @author Jack Guttman
 * @version September 1, 2016
 * @param <I> generic going to be holding Integer
 * @param <L> generic going to be holding Integer
 */
public class Node<I, L> {
    private I index;
    private L end;
    private Node<I, L> n;
    private Node<I, L> p;
    // ----------------------------------------------------------
    /**
     * constructor
     * @param indexS index start of freeblock info
     * @param endS end of freeblock
     * @param inp next node
     * @param inn previous node
     */
    public Node(I indexS, L endS, Node<I, L> inp, Node<I, L> inn) {
        index = indexS;
        end = endS;
        p = inp;
        n = inn;
    }
    // ----------------------------------------------------------
    /**
     * returns the index
     * @return index
     */
    public I index() {
        return index;
    }
    // ----------------------------------------------------------
    /**
     * sets the index to it
     * @param it new index
     * @return index
     */
    public I setIndex(I it) {
        index = it;
        return index;
    }
    // ----------------------------------------------------------
    /**
     * returns the end
     * @return end
     */
    public L end() {
        return end;
    }
    // ----------------------------------------------------------
    /**
     * sets end to it
     * @param it new end
     * @return end
     */
    public L setEnd(L it) {
        end = it;
        return end;
    }
    // ----------------------------------------------------------
    /**
     * returns the next node
     * @return next node in chain
     */
    public Node<I, L> next() {
        return n;
    }
    // ----------------------------------------------------------
    /**
     * returns the prev node
     * @return prev node in chain
     */
    public Node<I, L> prev() {
        return p;
    }
    // ----------------------------------------------------------
    /**
     * sets the next node in chain to new node (nextval)
     * @param nextval new node to set next
     * @return the new node
     */
    public Node<I, L> setNext(Node<I, L> nextval) {
        n = nextval;
        return n;
    }
    // ----------------------------------------------------------
    /**
     * sets the prev node in chain to new node (prevval)
     * @param prevval new node to set prev
     * @return the new node
     */
    public Node<I, L> setPrev(Node<I, L> prevval) {
        p = prevval;
        return p;
    }
}
