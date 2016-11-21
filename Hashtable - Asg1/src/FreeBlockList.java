/**
 * @author Jack Guttman
 * @version September 1, 2016
 * Double linked list. Used to store the free blocks in MemManager's byte
 * array.
 */
public class FreeBlockList {

    private Node<Integer, Integer> head;
    private Node<Integer, Integer> tail;
    private Node<Integer, Integer> curr;
    private int size;
    
    //----------------------------------------------------------------------
    /**
     * Creates a new FreeBlockList
     */
    public FreeBlockList() {
        tail = new Node<Integer, Integer>(null, null, null, null);
        curr = tail;
        head = new Node<Integer, Integer>(null, null, null, null);
        head.setNext(tail);
        tail.setPrev(head);
        size = 0;
    }
    //----------------------------------------------------------------------
    /**
     * inserts a new node with desired values into list
     * inserts before the curr node and sets the curr node to the new node
     * @param index     index to insert
     * @param end    end of record
     * @return          true if inserted
     */
    public boolean insert(int index, int end) {
        curr = new Node<Integer, Integer>(index, end, curr.prev(), curr);
        curr.prev().setNext(curr);
        curr.next().setPrev(curr);
        size++;
        return true;
    }
    //----------------------------------------------------------------------
    /**
     * Attempts to insert in ascending order
     * @param ind       what to insert
     * @param length    length of insertion
     */
    @SuppressWarnings("unused")
    public void insertInOrder(int ind, int length) {
      //first insert
        if (size == 0) {
            insert(ind, length);
            return; 
        }
        //belongs in first spot (after head, before first node)
        else if (head.next().index() > ind) {
            setCurr(head.next().index());
            insert(ind, length);            
            return;
        }
        else {
            //steps down list checking for insertion spot
            Node<Integer, Integer> after = getHead().next();
            Node<Integer, Integer> before = head;
            while (after.index() != null) {
                if (ind < after.index()) {
                    break;
                }
                before = after;
                after = after.next();                
            }
            //if insertion is last spot, just append to list
            if (after.index() == null) {
                append(ind, length);
            }
            //if insertion isnt at end, set the curr to where
            //insertion was found and insert
            else {
                setCurr(after.index());
                insert(ind, length);
            }
            return;
        }
    }
    //----------------------------------------------------------------------
    /**
     * appends a new node with desired values into list
     * inserts it right before the tail node
     * @param index     index to insert
     * @param end       end of record
     * @return      true if append worked
     */
    public boolean append(int index, int end) {
        tail.setPrev(new Node<Integer, Integer>(
                index, end, tail.prev(), tail));
        tail.prev().prev().setNext(tail.prev());
        if (curr == tail) {
            curr = tail.prev();
        }
        size++;  
        return true;
    }
    //----------------------------------------------------------------------
    /**
     * removes the node at curr from the list
     * @return      returns the value that was removed (index)
     */
    public int remove() {
        //cant check index on tail, tail.index == null
        //just exit immediately if for some reason curr == tail
        if (curr == tail) {
            return -1;
        }
        //stores the val to return
        int ival = curr.index();  
        //adjusts the prev and next nodes
        curr.prev().setNext(curr.next());
        curr.next().setPrev(curr.prev());
        curr = curr.next();
        size--;
        return ival;
    }
    //----------------------------------------------------------------------
    /**
     * Sets the curr node to the node with the desired index (value)
     * @param value     the index to look for in the list
     * @return          true if curr node was set,
     *                  false if value isnt in list
     */
    public boolean setCurr(int value) {
        //cant set curr on empty list
        if (size > 0) {
            Node<Integer, Integer> temp = head.next();
            //cant check index on tail node, so go up to the last element
            while (temp != tail) {
                if (value == temp.index()) {
                    curr = temp;
                    return true;
                }
                temp = temp.next();
            }
            return false;
        }
        else {
            return false;
        }
    }
    //----------------------------------------------------------------------
    /**
     * Gets every node in the list and stores them in an array for printing
     * ex. index = 0, length = 32. the print would return a string array and 
     * in the first index would be "(0,32)"
     * @return      the string array where the prints are stored.
     */
    public String[] print() {
        //cant print empty list
        if (!isEmpty()) {
            String[] ret = new String[size];
            Node<Integer, Integer> temp = head.next();
            int count = 0;
            
            while (temp != tail) {
                ret[count] = "(" + temp.index() + 
                        "," + temp.end() + ")";
                count++;
                temp = temp.next();

            }
            return ret;
        }
        else {
            return null;
        }
    }
    //----------------------------------------------------------------------
    /**
     * Attempts to merge adjacent blocks together
     * @return      true if a merge happened, false if not
     */
    public boolean merge() {
        boolean flag = false;
        //cant merge on empty list
        if (size > 0) {
            Node<Integer, Integer> temp = head.next();            
            
            //cant check index on tail, so goes up until the node before
            //tail
            while (temp.next() != tail) {
                //ex: (0,16) -> (16,16) -----> (0,32)
                if (temp.next().index().equals(temp.index() + temp.end())) {
                    int endStore = temp.end();
                    temp.setEnd(temp.next().end() + endStore);
                    setCurr(temp.next().index());
                    remove();
                    flag = true;
                }
                //only iterates if no merge to make sure multiple
                //merges can happen
                else {
                    temp = temp.next();
                }
            }
        }
        return flag;
    }
    //----------------------------------------------------------------------
    /**
     * Looks for a node that has the proper length and least empty space
     * for insertion (best-fit)
     * @param len       length to look for
     * @return          the index that allows for insertion, 
     *                  -1 if no insertion
     */
    public int probe(int len) {
        //cant probe on empty list
        if (size > 0) {
            
            //looks for best fit insertion
            Node<Integer, Integer> temp = head.next();
            int bestFit = Integer.MAX_VALUE;
            int bestFitsIndex = -1;
            
            while (temp != tail) {
                if (temp.end() >= len && temp.end() < bestFit) {
                    bestFit = temp.end();
                    bestFitsIndex = temp.index();
                }
                temp = temp.next();
            }
            return bestFitsIndex;
        }        
        return -1;
    }
    //----------------------------------------------------------------------
    /**
     * Returns the head node
     * @return  head
     */
    public Node<Integer, Integer> getHead() {
        return head;
    }
    //----------------------------------------------------------------------
    /**
     * Returns the tail node
     * @return  tail
     */
    public Node<Integer, Integer> getTail() {
        return tail;
    }
    //----------------------------------------------------------------------
    /**
     * Returns the curr node
     * @return  curr
     */
    public Node<Integer, Integer> getCurr() {
        return curr;
    }
    //----------------------------------------------------------------------
    /**
     * if the list is empty, return true
     * @return      true if size is 0
     */
    public boolean isEmpty() {
        return size == 0;
    }
    //----------------------------------------------------------------------
    /**
     * size of list
     * @return      size
     */
    public int getSize() {
        return size;
    }
    
    // ----------------------------------------------------------
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // ----------------------------------------------------------    
    /**
     * Node class for freeblock
     * @author Jack Guttman
     * @version September 1, 2016
     * @param <I>     generic going to be holding Integer
     * @param <L>     generic going to be holding Integer
     */
    public class Node<I, L> {
        private I index;
        private L end;
        private Node<I, L> n;
        private Node<I, L> p;
        
        // ----------------------------------------------------------        
        /**
         * constructor
         * @param indexS    index start of freeblock info
         * @param endS      end of freeblock
         * @param inp       next node
         * @param inn       previous node
         */
        public Node(I indexS, L endS, 
                Node<I, L> inp, Node<I, L> inn) {
            index = indexS;
            end = endS;
            p = inp;
            n = inn;
        }
        // ----------------------------------------------------------      
        /**
         * returns the index
         * @return  index
         */
        public I index() {
            return index;
        }
        // ----------------------------------------------------------
        /**
         * sets the index to it
         * @param it    new index   
         * @return      index
         */
        public I setIndex(I it) {
            index = it;
            return index;
        }
        // ----------------------------------------------------------
        /**
         * returns the end
         * @return      end
         */
        public L end() {
            return end;
        }
        // ----------------------------------------------------------
        /**
         * sets end to it
         * @param it    new end
         * @return      end
         */
        public L setEnd(L it) {
            end = it;
            return end;
        }
        // ----------------------------------------------------------
        /**
         * returns the next node
         * @return      next node in chain
         */
        public Node<I, L> next() {
            return n;
        }
        // ----------------------------------------------------------
        /**
         * returns the prev node
         * @return      prev node in chain
         */
        public Node<I, L> prev() {
            return p;
        }
        // ----------------------------------------------------------
        /**
         * sets the next node in chain to new node (nextval)
         * @param nextval       new node to set next
         * @return              the new node
         */
        public Node<I, L> setNext(Node<I, L> nextval) {
            n = nextval;
            return n;
        }
        // ----------------------------------------------------------
        /**
         * sets the prev node in chain to new node (prevval)
         * @param prevval       new node to set prev
         * @return              the new node
         */
        public Node<I, L> setPrev(Node<I, L> prevval) {
            p = prevval;
            return p;
        }
        
    }

}
