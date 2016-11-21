/**
 * Class for doubley-linked list: LinkedQue. Keeps track of buffer pool.
 * @author Jack Guttman
 * @version October 20th, 2016
 */
public class LinkedQue {
    /**
     * the tail node
     */
    public Node<Buffer> tail;
    /**
     * the current node
     */
    public Node<Buffer> curr;
    /**
     * the head node
     */
    public Node<Buffer> head;
    private int size;
    private int maxSize;
    // -------------------------------------------------------------------------
    /**
     * the constructor for class
     * @param numBuffers maximum number of allowed nodes
     */
    public LinkedQue(int numBuffers) {
        tail = new Node<Buffer>(null, null, null);
        curr = tail;
        head = new Node<Buffer>(null, null, null);
        head.setN(tail);
        tail.setP(head);
        maxSize = numBuffers;
        setSize(0);
    }
    // -------------------------------------------------------------------------
    /**
     * adds a new node to the list
     * @param buff what the node will hold
     */
    public void add(Buffer buff) {
        curr = new Node<Buffer>(buff, head, head.getN());
        head.setN(curr);
        curr.getN().setP(curr);
        size++;
    }
    // -------------------------------------------------------------------------
    /**
     * removes a node from the last slot in the list
     * @return returns what it removed
     */
    public Buffer remove() {
        if (size == 0) {
            return null;
        }
        curr = tail.getP();
        // stores the val to return
        Buffer val = curr.buf;
        // adjusts the prev and next nodes
        curr.getP().setN(tail);
        tail.setP(curr.getP());
        curr = head.getN();
        size--;
        return val;
    }
    // -------------------------------------------------------------------------
    /**
     * finds a node that contains the position 'pos'
     * @param pos what to search for
     * @return which buffer contains 'pos'
     */
    public Buffer find(int pos) {
        Buffer ret = null;
        Node<Buffer> temp = head.getN();
        while (temp.getBuf() != null) {
            if (temp.getBuf().getPos() == pos) {
                ret = temp.getBuf();
                break;
            }
            temp = temp.getN();
        }
        return ret;
    }
    // -------------------------------------------------------------------------
    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }
    // -------------------------------------------------------------------------
    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }
    // -------------------------------------------------------------------------
    /**
     * @return the max size
     */
    public int getMaxSize() {
        return maxSize;
    }
    // -------------------------------------------------------------------------
    /**
     * @param s the Max Size to set
     */
    public void setMaxSize(int s) {
        this.maxSize = s;
    }
    // -------------------------------------------------------------------------
    /**
     * Inner node class that makes up the doubley-linked list
     * @author Jack Guttman
     * @version October 20th, 2016
     * @param <B> nodes hold buffers
     */
    class Node<B> {
        private Buffer buf;
        private Node<B> n;
        private Node<B> p;
        // ---------------------------------------------------------------------
        /**
         * constructor for node class
         * @param b what buffer the node will hold
         * @param prev the node's previous node
         * @param next the node's next node
         */
        public Node(Buffer b, Node<B> prev, Node<B> next) {
            n = next;
            p = prev;
            buf = b;
        }
        // ---------------------------------------------------------------------
        /**
         * @return the n
         */
        public Node<B> getN() {
            return n;
        }
        // ---------------------------------------------------------------------
        /**
         * @param n the n to set
         */
        public void setN(Node<B> n) {
            this.n = n;
        }
        // ---------------------------------------------------------------------
        /**
         * @return the p
         */
        public Node<B> getP() {
            return p;
        }
        // ---------------------------------------------------------------------
        /**
         * @param p the p to set
         */
        public void setP(Node<B> p) {
            this.p = p;
        }
        // ---------------------------------------------------------------------
        /**
         * @return the buf
         */
        public Buffer getBuf() {
            return buf;
        }
        // ---------------------------------------------------------------------
        /**
         * @param buf the buf to set
         */
        public void setBuf(Buffer buf) {
            this.buf = buf;
        }
    }
}
