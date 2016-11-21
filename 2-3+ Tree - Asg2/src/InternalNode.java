/**
 * @author Jack Guttman
 * @version September 30, 2016
 */
public class InternalNode implements Node {
    private KVPair left;
    private KVPair right;
    private Node lchild;
    private Node cchild;
    private Node rchild;
    // -------------------------------------------------------------------------
    /**
     * Constructor for internal node class
     * @param l left KVPair
     * @param r right KVPair
     */
    public InternalNode(KVPair l, KVPair r) {
        setLeft(l);
        setRight(r);
        setLchild(setCchild(setRchild(null)));
    }
    // -------------------------------------------------------------------------
    /**
     * @return the left
     */
    public KVPair getLeft() {
        return left;
    }
    // -------------------------------------------------------------------------
    /**
     * @return the right
     */
    public KVPair getRight() {
        return right;
    }
    // -------------------------------------------------------------------------
    /**
     * @param right the right to set
     */
    public void setRight(KVPair right) {
        this.right = right;
    }
    // -------------------------------------------------------------------------
    /**
     * @param left the left to set
     */
    public void setLeft(KVPair left) {
        this.left = left;
    }
    // -------------------------------------------------------------------------
    /**
     * @return the left child
     */
    public Node getLchild() {
        return lchild;
    }
    // -------------------------------------------------------------------------
    /**
     * @param l child the left child to set
     */
    public void setLchild(Node l) {
        this.lchild = l;
    }
    // -------------------------------------------------------------------------
    /**
     * @return the center child
     */
    public Node getCchild() {
        return cchild;
    }
    // -------------------------------------------------------------------------
    /**
     * @param c child the center child to set
     * @return new center
     */
    public Node setCchild(Node c) {
        this.cchild = c;
        return cchild;
    }
    // -------------------------------------------------------------------------
    /**
     * @return the right child
     */
    public Node getRchild() {
        return rchild;
    }
    // -------------------------------------------------------------------------
    /**
     * @param r child the right child to set
     * @return new right
     */
    public Node setRchild(Node r) {
        this.rchild = r;
        return rchild;
    }
    // -------------------------------------------------------------------------
    @Override
    public Node insert(KVPair it) {
        Node ret = null;
        if (left.compareTo(it) > 0) {
            ret = getLchild().insert(it); // follows left child
            if (ret != this.getLchild()) { //new internal to absorb/split
                return absorb((InternalNode) ret); 
            }
            else {
                setNextElem(); //updates next and prev pointers
                return this;
            }
        }
        // first if statement occurs if right is null and left <= it
        // second statement occurs if right isnt null and left <= it > right
        else if ((right == null && (left.compareTo(it) <= 0)) || (right != null
                && (left.compareTo(it) <= 0) && it.compareTo(right) < 0)) {
            ret = getCchild().insert(it); // follows center child
            if (ret != this.getCchild()) {
                return absorb((InternalNode) ret);
            }
            else {
                setNextElem();
                return this;
            }
        }
        else {
            ret = getRchild().insert(it); // follows right child
            if (ret != this.getRchild()) {
                return absorb((InternalNode) ret);
            }
            else {
                setNextElem();
                return this;
            }
        }
    }
    // -------------------------------------------------------------------------
    /**
     * absorbs new node into internal node after split scenario
     * @param split what's returned from leaf node insert
     * @return either this node if absorb possible, or new split node
     */
    public InternalNode absorb(InternalNode split) {
        if (right == null) { // absorb possible
            // if new return node's left is less than this' left
            if (split.getLeft().compareTo(left) < 0) {
                setRight(left);
                setLeft(split.getLeft());
                this.setRchild(this.getCchild());
                this.setCchild(split.getCchild());
                this.setLchild(split.getLchild());
                setNextElem();
                return this;
            }
            else {  //new internal's left belongs in right value
                setRight(split.getLeft());
                this.setCchild(split.getLchild());
                this.setRchild(split.getCchild());
                setNextElem();
                return this;
            }
        }
        else { //no room to absorb, must split into new internal
            return split(split);
        }
    }
    // -------------------------------------------------------------------------
    /**
     * Has to split internal node based on leaf's inserts return
     * @param toSplit the internal node to split upon
     * @return returns newly split node
     */
    public InternalNode split(Node toSplit) {
        InternalNode ret = new InternalNode(null, null);
        // update all values by doing comparisons and dictating where they go
        if (toSplit.getLeft().compareTo(left) < 0) {
            ret.setLeft(this.getLeft());
            setLeft(this.getRight());
            setRight(null);
            this.setLchild(this.getCchild());
            this.setCchild(this.getRchild());
            this.setRchild(null);
            ret.setLchild(toSplit); // update pointers
            ret.setCchild(this);
            ((InternalNode) toSplit).setNextElem();
            setNextElem();
        }
        else if (toSplit.getLeft().compareTo(right) > 0) {
            ret.setLeft(this.getRight());
            setRight(null);
            this.setRchild(null);
            ret.setLchild(this); // update pointers
            ret.setCchild(toSplit);
            ((InternalNode) toSplit).setNextElem();
            setNextElem();
        }
        else {
            ret.setLeft(toSplit.getLeft());
            InternalNode newToSplit = new InternalNode(this.getRight(), null);
            newToSplit.setLchild(((InternalNode) toSplit).getCchild());
            newToSplit.setCchild(this.getRchild());
            this.setCchild(((InternalNode) toSplit).getLchild());
            this.setRight(null);
            this.setRchild(null);
            ret.setLchild(this);
            ret.setCchild(newToSplit);
            ((InternalNode) newToSplit).setNextElem();
            setNextElem();
        }
        ret.setNextElem(); //sets the next/prev pointers between the split node
        return ret;
    }
    // ------------------------------------------------------------------------
    /**
     * finds the left most value in a tree and returns that node
     * @return the left most leaf node
     */
    public LeafNode leftMost() {
        if (this.getLchild() instanceof LeafNode) {
            return (LeafNode) this.getLchild();
        }
        else {
            return ((InternalNode) getLchild()).leftMost();
        }
    }
    // -------------------------------------------------------------------------
    /**
     * finds the right most value in a tree and returns that node
     * @return the right most's internal nodes left leaf node
     */
    public LeafNode rightMost() {
        if (this.getCchild() instanceof LeafNode) {
            if (this.getRight() != null) {
                return (LeafNode) this.getRchild();
            }
            return (LeafNode) this.getCchild();
        }
        else {
            if (this.getRight() != null) {
                return ((InternalNode) getRchild()).rightMost();
            }
            return ((InternalNode) getCchild()).rightMost();
        }
    }
    // -------------------------------------------------------------------------
    /**
     * sets next elements for split and absorbs
     * @return a node to keep track of right most next val
     */
    public Node setNextElem() {
        if (this.getLchild() instanceof LeafNode) {
            ((LeafNode) getLchild()).setNext((LeafNode) getCchild());
            ((LeafNode) getCchild()).setPrev((LeafNode) getLchild());
            if (right != null) {
                ((LeafNode) getCchild()).setNext((LeafNode) getRchild());
                ((LeafNode) getRchild()).setPrev((LeafNode) getCchild());
                return getRchild();
            } //updates pointers at leaf level
            return getCchild();
        }
        else if (this.getLchild() instanceof InternalNode
                && ((InternalNode) this.getLchild()) 
                        .getLchild() instanceof LeafNode) {
            LeafNode childRet = ((InternalNode) this.getLchild()).rightMost();
            LeafNode cenLef = ((InternalNode) this.getCchild()).leftMost();
            childRet.setNext(cenLef);
            cenLef.setPrev(childRet);
            if (right != null) {
                cenLef = ((InternalNode) this.getCchild()).rightMost();
                LeafNode rigLef = ((InternalNode) this.getRchild()).leftMost();
                cenLef.setNext(rigLef);
                rigLef.setPrev(cenLef);
                return ((InternalNode) getRchild()).setNextElem();
            } //updates pointers at one internal node level
            return ((InternalNode) getCchild()).setNextElem();
        }
        else { //updates pointers between internal nodes
            LeafNode leftRet = ((InternalNode) getLchild()).rightMost();
            LeafNode cenLeft = ((InternalNode) getCchild()).leftMost();
            leftRet.setNext(cenLeft);
            cenLeft.setPrev(leftRet);
            if (right != null) {
                LeafNode cenRet = ((InternalNode) getCchild()).rightMost();
                LeafNode rigLeft = ((InternalNode) getRchild()).leftMost();
                cenRet.setNext(rigLeft);
                rigLeft.setPrev(cenRet);
                return ((InternalNode) getRchild()).setNextElem();
            } 
            return ((InternalNode) getLchild()).setNextElem();
        }
    }
    // -------------------------------------------------------------------------
    /**
     * Finds a value by recursively calling internal nodes
     * @param it value to find
     * @return true if found
     */
    @Override
    public boolean find(KVPair it) {
        if (left.compareTo(it) > 0) {
            return this.getLchild().find(it);
        }
        else if ((right == null && (left.compareTo(it) <= 0)) || (right != null
                && (left.compareTo(it) <= 0) && right.compareTo(it) > 0)) {
            return this.getCchild().find(it);
        }
        else {
            return this.getRchild().find(it);
        }
    }
    // -------------------------------------------------------------------------
    /**
     * prints out internal node
     */
    @Override
    public void print(int depth) {
        String indent = "";
        for (int i = depth; i > 0; i--) {
            indent += "  ";
        }
        if (this.getRight() != null) {
            System.out.println(indent + this.getLeft().key() + " "
                    + this.getLeft().value() + " " + this.getRight().key() + " "
                    + this.getRight().value());
        }
        else {
            System.out.println(indent + this.getLeft().key() + " "
                    + this.getLeft().value());
        } //calls its children to print
        this.getLchild().print(depth + 1);
        this.getCchild().print(depth + 1);
        if (getRight() != null) {
            this.getRchild().print(depth + 1);
        }
    }
    // -------------------------------------------------------------------------
    /**
     * gets first appearance of a handle
     * @param name artist/song to list
     * @return returns node to determine if it needs to call again
     */
    public Node getFirst(Handle name) {
        if (left.compareTo(name) >= 0) {
            if (this.getLchild() instanceof LeafNode) {
                return this.getNextNode(getLchild(), name);
            } //has to check left val incase its in the right subtree
            return ((InternalNode) this.getLchild()).getFirst(name);
        }
        else if ((right == null && (left.compareTo(name) <= 0))
                || (right != null && (left.compareTo(name) <= 0)
                        && right.compareTo(name) >= 0)) {
            if (this.getCchild() instanceof LeafNode) {
                return this.getNextNode(getCchild(), name);
            } //has to check upto right tree incase its rightmost val in center
            return ((InternalNode) this.getCchild()).getFirst(name);
        }
        else { //check right tree
            if (this.getRchild() instanceof LeafNode) {
                return this.getNextNode(getRchild(), name);
            }
            return ((InternalNode) this.getRchild()).getFirst(name);
        }
    }
    // -------------------------------------------------------------------------
    /**
     * Gets the node next to get
     * @param get the node to start on
     * @param name the name to make sure get isnt equal to
     * @return returns next node if get.left or get.right dont = name
     */
    public Node getNextNode(Node get, Handle name) {
        if (get.getLeft().compareTo(name) == 0 || (get.getRight() != null
                && get.getRight().compareTo(name) == 0)) {
            return get;
        }
        else {
            return ((LeafNode) get).getNext();
        }
    }
    // -------------------------------------------------------------------------
    /**
     * deletes value from tree
     * @param del val to delete
     * @return the Node that was deleted or their parent to use for recursion
     */
    @Override
    public Node delete(KVPair del) {
        Node retDel = null;
        if (left.compareTo(del) > 0) {
            retDel = getLchild().delete(del);  //if left is null there's under
            if (retDel.getLeft() == null) {    //flow, so check leaf v intrnl
                if (retDel instanceof LeafNode) {
                    deleteLeafHelp("left");
                }
                else {
                    underflowHelp("left");
                }
            }
            return this;
        }
        else if ((right == null && (left.compareTo(del) <= 0)) || (right != null
                && (left.compareTo(del) <= 0) && right.compareTo(del) > 0)) {
            retDel = getCchild().delete(del);
            if (retDel.getLeft() == null) {
                if (retDel instanceof LeafNode) {
                    deleteLeafHelp("center");
                }
                else {
                    underflowHelp("center");
                }
            } //updates this' parents when there isnt underflow
            else if (getCchild() instanceof InternalNode) {
                grandVals();
            }
            else {  
                this.setLeft(getCchild().getLeft());
                if (this.getRight() != null) {
                    this.setRight(getRchild().getLeft());
                }
            }
            return this;
        }
        else {
            retDel = getRchild().delete(del);
            if (retDel.getLeft() == null) {
                if (retDel instanceof LeafNode) {
                    deleteLeafHelp("right");
                }
                else {
                    underflowHelp("right");
                }
            } //updates this' parents when there isnt underflow
            else if (retDel instanceof InternalNode) {
                grandVals();
            }
            else {
                this.setLeft(getCchild().getLeft());
                if (this.getRight() != null) {
                    this.setRight(getRchild().getLeft());
                }
            }
            return this;
        }
    }
    // -------------------------------------------------------------------------
    /**
     * Assists delete when called on leaf node and needs to borrow from siblings
     * @param childType which child it is
     * @return returns parent node
     */
    public Node deleteLeafHelp(String childType) {
        if (childType.equals("left")) {
            if (deletehelp((LeafNode) getLchild(), "next")) {
                setLeft(getCchild().getLeft());
                return this;
            } //cant borrow, must return underflow and update pointers
            if (((LeafNode) getLchild()).getPrev() != null) {
                ((LeafNode) getLchild()).getPrev()
                        .setNext(((LeafNode) getLchild()).getNext());
            }
            ((LeafNode) getCchild())
                    .setPrev(((LeafNode) getLchild()).getPrev());
            ((LeafNode) getLchild()).setNext(null);
            setLchild(getCchild());
            setCchild(null);
            setLeft(null);
            if (getRight() != null) {
                setCchild(getRchild());
                setRchild(null);
                setLeft(getRight());
                setRight(null);
            }
        }
        else if (childType.equals("center")) {
            if (deletehelp((LeafNode) getCchild(), "prev")) {
                setLeft(getCchild().getLeft());
                return this;
            }
            if (getRchild() != null
                    && deletehelp((LeafNode) getCchild(), "next")) {
                setLeft(getCchild().getLeft());
                setRight(getRchild().getLeft());
                return this;
            } //cant borrow, must return underflow and update pointers
            if (((LeafNode) getCchild()).getNext() != null) {
                ((LeafNode) getCchild()).getNext()
                        .setPrev(((LeafNode) getCchild()).getPrev());
            }
            ((LeafNode) getLchild())
                    .setNext(((LeafNode) getCchild()).getNext());
            setCchild(null);
            setLeft(null);
            if (getRight() != null) {
                setCchild(getRchild());
                setRchild(null);
                setLeft(getRight());
                setRight(null);
            }
        }
        else {
            if (deletehelp((LeafNode) getRchild(), "prev")) {
                setRight(getRchild().getLeft());
                return this;
            } //cant borrow, must return underflow and update pointers
            ((LeafNode) getCchild())
                    .setNext(((LeafNode) getRchild()).getNext());
            if (((LeafNode) getRchild()).getNext() != null) {
                ((LeafNode) getRchild()).getNext()
                        .setPrev(((LeafNode) getCchild()).getPrev());
            }
            setRchild(null);
            setRight(null);
        }
        return this;
    }
    // -------------------------------------------------------------------------
    /**
     * Assists delete by grabbing proper values from siblings
     * @param it node to set values for
     * @param type where the node should grab values from
     * @return true if values were moved
     */
    public boolean deletehelp(LeafNode it, String type) {
        if (type.equals("next") && it.getNext() != null) {
            LeafNode next = it.getNext();
            if (it.getNext().getRight() != null) {
                it.setLeft(next.getLeft());
                next.setLeft(next.getRight());
                next.setRight(null);
                return true;
            } //cant borrow from siblings
            return false;
        } 
        else if (type.equals("prev") && it.getPrev() != null) {
            LeafNode prev = it.getPrev();
            if (it.getPrev().getRight() != null) {
                it.setLeft(it.getPrev().getRight());
                prev.setRight(null);
                return true;
            } //cant borrow from siblings
            return false;
        } //cant borrow from siblings
        else {
            return false;
        }
    }
    // -------------------------------------------------------------------------
    /**
     * update grandparent vals by comparing to its immediate children
     */
    public void grandVals() {
        if (getLeft().compareTo(this.getCchild().getLeft()) < 0) {
            this.setLeft(
                    ((InternalNode) this.getCchild()).leftMost().getLeft());
        }
        if (getRight() != null
                && getRight().compareTo(this.getRchild().getLeft()) < 0) {
            this.setRight(
                    ((InternalNode) this.getRchild()).leftMost().getLeft());
        }
    }
    // -------------------------------------------------------------------------
    /**
     * helper method for underflow -- borrows from sibling
     * @param l the left internal node
     * @param r the right internal node
     * @param type either left or right depending on where we want to borrow
     * @return returns the parent node (this) of l and r
     */
    public InternalNode borrow(InternalNode l, InternalNode r, String type) {
        if (type.equals("left")) {  //underflow attemping to borrow
            r.setLeft(r.leftMost().getLeft());  //ex. sub-left <- sub-center
            r.setCchild(r.getLchild());
            r.setLchild(l.getRchild());
            l.setRchild(null);
            this.setLeft(((InternalNode) getCchild()).leftMost().getLeft());
            l.setRight(null);
        }
        else {  //underflow attempting to borrow, ex. sub-left -> sub-center
            l.setCchild(r.getLchild());
            l.setLeft(r.leftMost().getLeft());
            r.setLchild(r.getCchild());
            r.setCchild(r.getRchild());
            this.setLeft(((InternalNode) getCchild()).leftMost().getLeft());
            r.setLeft(r.getRight());
            r.setRight(null);
            r.setRchild(null);
        }
        return this;
    }
    // -------------------------------------------------------------------------
    /**
     * helper method for underflow -- merges siblings
     * @param l the left internal node
     * @param r the right internal node
     * @param type either left or right depending on where we want to merge to
     * @return the parent node of l and r (this)
     */
    public InternalNode merge(InternalNode l, InternalNode r, String type) {
        if (type.equals("left")) {      //left <- center, or center <- right
            l.setRchild(r.getLchild());
            l.setRight(r.leftMost().getLeft());
            this.setLeft(null);
            if (this.getRight() != null) {
                this.setLeft(getRight());
                this.setRight(null);
                if (this.getCchild() == r) {
                    this.setCchild(this.getRchild());
                }
                this.setRchild(null);
            }
        }
        else {    //left -> center, or center -> right
            r.setRight(r.getLeft());
            r.setLeft(r.leftMost().getLeft());
            r.setRchild(r.getCchild());
            r.setCchild(r.getLchild());
            r.setLchild(l.getLchild());
            this.setLeft(null);
            this.setLchild(r);
            this.setCchild(null);
            if (this.getRight() != null) {
                this.setLeft(getRight());
                this.setRight(null);
                this.setCchild(this.getRchild());
                this.setRchild(null);
            }
        }
        return this;
    }
    // -------------------------------------------------------------------------
    /**
     * underflow helper: first try borrow left, then right, then merge L then R
     * @param childType which child to help with
     * @return returns itself
     */
    public InternalNode underflowHelp(String childType) {
        if (childType.equals("left")) {
            if (getCchild().getRight() != null) {
                this.borrow(((InternalNode) getLchild()),
                        ((InternalNode) getCchild()), "right");
            }
            else {
                this.merge(((InternalNode) getLchild()),
                        ((InternalNode) getCchild()), "right");
                if (getLeft() != null) {
                    setLeft(((InternalNode) getCchild()).leftMost().getLeft());
                }
            }
        }
        else if (childType.equals("center")) {
            if (getLchild().getRight() != null) {
                this.borrow(((InternalNode) getLchild()),
                        ((InternalNode) getCchild()), "left");
            } //have to update parents appropriately after borrowing c -> r
            else if (getRight() != null && getRchild().getRight() != null) {
                this.borrow(((InternalNode) getCchild()),
                        ((InternalNode) getRchild()), "right");
                this.setRight(
                        ((InternalNode) getRchild()).leftMost().getLeft());
            }
            else {
                this.merge(((InternalNode) getLchild()),
                        ((InternalNode) getCchild()), "left");
                if (getLeft() != null) {
                    setLeft(((InternalNode) getCchild()).leftMost().getLeft());
                }
            }
        }
        else {  //have to update parents after borrowing c <- r
            if (getCchild().getRight() != null) {
                this.borrow(((InternalNode) getCchild()),
                        ((InternalNode) getRchild()), "left");
                this.setRight(
                        ((InternalNode) getRchild()).leftMost().getLeft());
            }
            else {
                this.merge(((InternalNode) getCchild()),
                        ((InternalNode) getRchild()), "left");
                this.setLeft(((InternalNode) getCchild()).leftMost().getLeft());
            }
        }
        return this;
    }
}