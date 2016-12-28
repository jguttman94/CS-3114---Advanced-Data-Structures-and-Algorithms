/**
 * 
 */
/**
 * @author jgutt
 * @version December 1st, 2016
 */
public class Record {
    private int vertex;
    private Handle handle;
    
    /**
     * Record class
     * @param hand      handle to store
     * @param vert      vert to store
     */
    public Record(Handle hand, int vert) {
        this.vertex = vert;
        this.handle = hand;
    }
    /**
     * @return the vertex
     */
    public int getVertex() {
        return vertex;
    }
    /**
     * @param vertex the vertex to set
     */
    public void setVertex(int vertex) {
        this.vertex = vertex;
    }
    /**
     * @return the handle
     */
    public Handle getHandle() {
        return handle;
    }
    /**
     * @param handle the handle to set
     */
    public void setHandle(Handle handle) {
        this.handle = handle;
    }
}
