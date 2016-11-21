/**
 * @author Jack Guttman
 * @version September 30, 2016
 */
public interface Node {
    /**
     * @return the left kvpair
     */
    KVPair getLeft();

    /**
     * @return the right kvpair
     */
    KVPair getRight();

    /**
     * @param it set the left kvpair to it
     */
    void setLeft(KVPair it);

    /**
     * @param it set the right kvpair to it
     */
    void setRight(KVPair it);

    /**
     * inserts it
     * 
     * @param it    what to insert
     * @return      whats actually inserted
     */
    Node insert(KVPair it);

    /**
     * deletes node from tree
     * 
     * @param del delete val
     * @return what node was deleted
     */
    Node delete(KVPair del);

    /**
     * find value in tree
     * 
     * @param it value to find
     * @return true if found
     */
    boolean find(KVPair it);

    /**
     * @param depth depth of printing node print tree
     */
    void print(int depth);

}