/**
 * Stub for Handle class
 *@author Jack Guttman
 *@version September 1, 2016
 */
public class Handle {
    private int startPos;
    //----------------------------------------------------------------------
    /**
     * Constructor
     * @param startP    starting position in memmanager
     */
    public Handle(int startP) {
        startPos = startP;
    }
    //----------------------------------------------------------------------
    /**
     * @return  startPos
     */
    public int getStartPos() {
        return startPos;
    }
    // ----------------------------------------------------------
    /**
     * Overload compareTo
     *
     * @param it
     *            The handle being compared against
     * @return standard values of -1, 0, 1
     */
    public int compareTo(Handle it) {
        if (startPos < it.getStartPos()) {
            return -1;
        } 
        else if (startPos == it.getStartPos()) {
            return 0;
        } 
        else {
            return 1;
        }
    }
    // ----------------------------------------------------------
    /**
     * Overload toString
     *
     * @return A print string
     */
    public String toString() {
        return String.valueOf(startPos);
    }
}
