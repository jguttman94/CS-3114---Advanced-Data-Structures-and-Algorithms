import java.util.List;

/**
 * Leaf node class. Contains a left and a right value in the node
 * has a next and prev pointer to form a doubly-linked list at the base
 * of the tree
 * @author Jack Guttman
 * @version September 30, 2016
 */
public class LeafNode implements Node {
    private KVPair left;
    private KVPair right;
    private LeafNode next;
    private LeafNode prev;

    // -------------------------------------------------------------------------
    /**
     * Constructor for Leaf node
     * 
     * @param l left kvpair
     * @param r right kvpair
     */
    public LeafNode(KVPair l, KVPair r) {
        setLeft(l);
        setRight(r);
        next = null;
        prev = null;
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
     * @return the next
     */
    public LeafNode getNext() {
        return next;
    }

    // -------------------------------------------------------------------------
    /**
     * @param next the next to set
     */
    public void setNext(LeafNode next) {
        this.next = next;
    }

    // -------------------------------------------------------------------------
    /**
     * @return the prev
     */
    public LeafNode getPrev() {
        return prev;
    }

    // -------------------------------------------------------------------------
    /**
     * @param prev the prev to set
     */
    public void setPrev(LeafNode prev) {
        this.prev = prev;
    }

    // -------------------------------------------------------------------------
    @Override
    public Node insert(KVPair it) {
        if (left == null) {
            this.setLeft(it);
            return this;
        }
        else if (right == null) { // only one key in node
            if (left.compareTo(it) < 0) { // belongs in right
                right = it;
            }
            else { // belongs in left
                right = left;
                left = it;
            }
            return this;
        }
        else { // split
               // find low, mid and high vals
            InternalNode newIntrnl = new InternalNode(null, null);
            LeafNode leftC = null;
            LeafNode centerC = null;
            // 'it' is the largest, so 'it' belongs in right child
            if (right.compareTo(it) < 0) {
                newIntrnl.setLeft(right);
                leftC = new LeafNode(left, null);
                centerC = new LeafNode(right, it);
                newIntrnl.setLchild(leftC);
                newIntrnl.setCchild(centerC);
            }
            else if (left.compareTo(it) > 0) {
                // left is bigger than it so, 'it' belongs in left child
                newIntrnl.setLeft(left);
                leftC = new LeafNode(it, null);
                centerC = new LeafNode(left, right);
                newIntrnl.setLchild(leftC);
                newIntrnl.setCchild(centerC);
            }
            else { // 'it' is middle, so promote
                newIntrnl.setLeft(it);
                leftC = new LeafNode(left, null);
                centerC = new LeafNode(it, right);
                newIntrnl.setLchild(leftC);
                newIntrnl.setCchild(centerC);
            }
            // set next and prev
            leftC.setPrev(null);
            centerC.setNext(null);
            leftC.setNext(centerC);
            centerC.setPrev(leftC);
            return newIntrnl;
        }
    }

    // -------------------------------------------------------------------------
    @Override
    public boolean find(KVPair it) {
        if (left == null) {
            return false;
        }
        else if (right == null) {
            return left.compareTo(it) == 0;
        }
        else {
            return left.compareTo(it) == 0 || right.compareTo(it) == 0;
        }
    }

    // -------------------------------------------------------------------------
    /**
     * prints leaf
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
        else if (this.getLeft() != null) {
            System.out.println(indent + this.getLeft().key() + " "
                    + this.getLeft().value());
        }
    }

    // -------------------------------------------------------------------------
    /**
     * lists leaf
     * @param list  list to add too
     * @param name artist/song to list
     */
    public void list(Handle name, List<Handle> list) {
        if (getLeft().compareTo(name) == 0) {
            list.add(getLeft().value());
        }
        if (getRight() != null && getRight().compareTo(name) == 0) {
            list.add(getRight().value());
        }
    }

    // -------------------------------------------------------------------------
    /**
     * deletes val from leaf
     * @param del val to delete
     * @return what was deleted
     */
    @Override
    public Node delete(KVPair del) {
        // simple delete (2 values in leaf node)
        if (right != null) {
            if (del.compareTo(left) == 0) {
                left = right;
                right = null;
            }
            else if (del.compareTo(right) == 0) {
                right = null;
            }
        }
        else {
            if (del.compareTo(left) == 0) {
                left = null;
                right = null;
            }
            
        }
        return this;
    }

}
