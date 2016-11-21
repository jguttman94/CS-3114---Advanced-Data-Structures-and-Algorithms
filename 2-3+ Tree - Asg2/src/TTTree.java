import java.util.ArrayList;
import java.util.List;

/**
 * @author Jack Guttman
 * @version September 30, 2016
 */
public class TTTree {
    private Node rt;

    /**
     * constructor
     */
    public TTTree() {
        rt = null;
    }

    // -------------------------------------------------------------------------
    /**
     * @return the rt
     */
    public Node getRt() {
        return rt;
    }

    // -------------------------------------------------------------------------
    /**
     * @param rt the rt to set
     */
    public void setRt(Node rt) {
        this.rt = rt;
    }

    // -------------------------------------------------------------------------
    /**
     * @param it the record to insert
     */
    public void insert(KVPair it) {
        Node temp = null;
        if (rt == null) {
            setRt(new LeafNode(null, null));
        }
        // search for duplicates
        if (rt.find(it)) {
            System.out.print(",(" + it.key() + "," + it.value()
                    + ") duplicates a record already in the tree.\n");
        }
        else {
            temp = rt.insert(it);
            setRt(temp);
            System.out.print(",(" + it.key() + "," + it.value()
                    + ") is added to the tree.\n");
        }
    }

    // -------------------------------------------------------------------------
    /**
     * print tree by calling internal's and leaf's print methods
     */
    public void print() {
        if (rt == null) {
            return;
        }
        else {
            rt.print(0);
        }
    }

    // -------------------------------------------------------------------------
    /**
     * Searches the tree for nodes with name and returns their associated vals
     * @param name name to list lists all values in tree with name
     * @return list of values associated with name
     */
    public List<Handle> list(Handle name) {
        List<Handle> retHandles = new ArrayList<Handle>();
        if (getRt() == null) {
            return retHandles;
        }
        if (getRt() instanceof InternalNode) {
            LeafNode first = (LeafNode) ((InternalNode) rt).getFirst(name);
            if (first != null && first.getLeft() != null) {
                //while the first node's next contains the value
                //and does a check that it doesnt run over the end node
                //add to list for each value found
                while (first != null && (first.getLeft()
                        .compareTo(name) == 0
                        || (first.getRight() != null
                                && first.getRight().compareTo(name) == 0))) {
                    first.list(name, retHandles);
                    first = first.getNext();
                }
            }
        }
        else {
            ((LeafNode) getRt()).list(name, retHandles);
        }
        return retHandles;
    }

    // -------------------------------------------------------------------------
    /**
     * @param record record to delete deletes record in tree
     * @return      true if delete worked
     */
    public boolean delete(KVPair record) {
        //checks that the root isnt null and that the record is in the tree
        if (rt != null && rt.find(record)) {
            Node ret = rt.delete(record);
            //if it underflowed to the root, reset root to null for new inserts
            if (getRt() instanceof LeafNode && ret.getLeft() == null) {
                rt = null;
            }
            //if it underflowed to the root, set root to the left child
            else if (getRt() instanceof InternalNode && ret.getLeft() == null) {
                rt = ((InternalNode) getRt()).getLchild();
            }
            System.out.print(" is deleted from the tree.\n");
            return true;
        }
        else {
            //not found
            System.out.print(" was not found in the database.\n");
            return false;
        }
    }
}
