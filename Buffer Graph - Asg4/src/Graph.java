/**
 * Graph class method. Keeps track of relevant data in memory manager
 * in an array of nodes. Information is generated from that array,
 * including which nodes have which root nodes and then calculates weight.
 * 
 * @author jgutt
 * @version December 1, 2016
 */
public class Graph {
    private int[] parentArray; // Node parentArray
    private int[] weights; // Weight array
    private Node[] nodeArray;
    private int biggestWeight;
    private int biggestWeightIndex;
    private int numNode;
    private int biggestIndex;
    // ----------------------------------------------------------------------
    /**
     * No real constructor needed, just initializes
     * @param n number of vertices
     */
    public Graph(int n) {
        init(n);
    }
    // ----------------------------------------------------------------------
    /**
     * Initialize the graph with n vertices
     * @param n vertices
     */
    public void init(int n) {
        nodeArray = new Node[n];
        // List headers;
        for (int i = 0; i < n; i++) {
            nodeArray[i] = new Node(-1, null, null);
        }
        numNode = 0;
        biggestWeight = 0;
        biggestWeightIndex = -1;
        setBiggestIndex(0);
    }
    // ----------------------------------------------------------------------
    /**
     * @return the number of vertices
     */
    public int nodeCount() {
        return nodeArray.length;
    }
    // ----------------------------------------------------------------------
    /**
     * @return the current number of edges
     */
    public int edgeCount() {
        return numNode;
    }
    // ----------------------------------------------------------------------
    /**
     * sets a val in the node array
     * @param index where to put val
     * @param val what to put
     */
    public void setNodeArrayVal(int index, int val) {
        nodeArray[index].vertex = val;
    }
    // ----------------------------------------------------------------------
    /**
     * Return the link in v's neighbor list that preceeds the one with w (or
     * where it would be)
     * @param v start index v
     * @param w what to look for
     * @return node before w
     */
    private Node find(int v, int w) {
        Node curr = nodeArray[v];
        while ((curr.next != null) && (curr.next.vertex < w)) {
            curr = curr.next;
        }
        return curr;
    }
    // ----------------------------------------------------------------------
    /**
     * Adds a new edge from node v to node w with weight wgt
     * @param v connect 1
     * @param w connect 2
     * @param wgt weight between 1 and 2
     */
    public void addHelper(int v, int w, int wgt) {
        Node curr = find(v, w);
        if ((curr.next != null) && (curr.next.vertex == w)) {
            curr.next.weight = wgt;
            return;
        }
        else {
            curr.next = new Node(w, curr, curr.next);
            if (curr.next.next != null) {
                curr.next.next.prev = curr.next;
            }
            nodeArray[v].vertex = v;
            nodeArray[v].weight = wgt;
        }
        numNode++;
    }
    // ----------------------------------------------------------------------
    /**
     * Removes the edge from the graph.
     * @param v connect 1
     */
    public void remove(int v) {
        int[] neighbors = neighbors(v);
        nodeArray[v] = new Node(-1, null, null);
        Node curr;
        for (int i = 0; i < neighbors.length; i++) {
            curr = nodeArray[neighbors[i]];
            while (curr.vertex != v) {
                curr = curr.next;
            }
            curr.prev.next = curr.next;
            if (curr.next != null) {
                curr.next.prev = curr.prev;
            }
            numNode--;
        }
    }
    // ----------------------------------------------------------------------
    /**
     * Returns an array containing the indicies of the neighbors of v
     * @param v start index
     * @return list of values
     */
    public int[] neighbors(int v) {
        int cnt = 0;
        Node curr;
        for (curr = nodeArray[v].next; curr != null; curr = curr.next) {
            cnt++;
        }
        int[] temp = new int[cnt];
        cnt = 0;
        for (curr = nodeArray[v].next; curr != null; curr = curr.next) {
            temp[cnt++] = curr.vertex;
        }
        return temp;
    }
    // ----------------------------------------------------------------------
    /**
     * inserts a track pairing
     * @param vertexStart where to insert
     * @param vertexAdding what to add
     */
    public void addNode(int vertexStart, int vertexAdding) {
        addHelper(vertexStart, vertexAdding, 1);
        addHelper(vertexAdding, vertexStart, 1);
    }
    // ----------------------------------------------------------------------
    /**
     * @return the next available index to insert upon
     */
    public int getNextAvailableIndex() {
        int ret = -1;
        for (int i = 0; i < nodeArray.length; i++) {
            if (nodeArray[i].vertex == -1) {
                if (i > getBiggestIndex()) {
                    setBiggestIndex(i);
                }
                return i;
            }
        }
        int tempRet = nodeArray.length;
        Node[] temp = new Node[tempRet * 2];
        for (int i = 0; i < tempRet * 2; i++) {
            if (i < tempRet) {
                temp[i] = nodeArray[i];
            }
            else {
                temp[i] = new Node(-1, null, null);
            }
        }
        nodeArray = temp;
        ret = tempRet;
        setBiggestIndex(tempRet);
        return ret;
    }
    // ----------------------------------------------------------------------
    /**
     * prints the node Array for visualization testing
     */
    public void printNodeArray() {
        for (int i = 0; i < nodeArray.length; i++) {
            System.out.print("| " + nodeArray[i].vertex + "|");
            Node curr = nodeArray[i];
            while ((curr.next != null)) {
                curr = curr.next;
                System.out.print(" - " + curr.vertex);
            }
            System.out.println();
        }
    }
    // ----------------------------------------------------------------------
    /**
     * creates a parent pointer tree which creates a couple arrays to use on
     * nodeArray union's and finds
     */
    public void createParentAndWeights() {
        getNextAvailableIndex();
        setBiggestWeightIndex(-1);
        setBiggestWeight(0);
        int index = getBiggestIndex() + 1;
        parentArray = new int[index];
        weights = new int[index];
        
        for (int i = 0; i < index; i++) {
            parentArray[i] = -1; // Each node is its own root to start
            weights[i] = 1;
        }
    }
    // ----------------------------------------------------------------------
    /**
     * Merge two subtrees if they are different
     * @param a first nodeArray index
     * @param b second nodeArray index
     */
    private void union(int a, int b) {
        int root1 = find(a); // Find root of node a
        int root2 = find(b); // Find root of node b
        if (root1 != root2) { // Merge with weighted union
            if (weights[root2] > weights[root1]) {
                parentArray[root1] = root2;
                weights[root2] += weights[root1];
            }
            else {
                parentArray[root2] = root1;
                weights[root1] += weights[root2];
            }
        }
    }
    // ----------------------------------------------------------------------
    /**
     * @param curr what to find
     * @return the root of curr's tree
     */
    private int find(int curr) {
        if (parentArray[curr] == -1) {
            return curr; // At root
        }
        parentArray[curr] = find(parentArray[curr]);
        return parentArray[curr];
    }
    // ----------------------------------------------------------------------
    /**
     * prepares the parent pointer tree to be printed finds the nodeArray index
     * with the greatest number of nodes and sets that index biggestWeightIndex
     */
    public void generateGraph() {
        // setBiggestWeightIndex(-1);
        for (int i = 0; i < parentArray.length; i++) {
            if (nodeArray[i].vertex == -1) {
                parentArray[i] = -2;
            }
            Node curr;
            for (curr = nodeArray[i]; curr.next != null; curr = curr.next) {
                if (nodeArray[i].vertex < curr.next.vertex) {
                    //performs unions in nodeArray
                    union(nodeArray[i].vertex, curr.next.vertex);
                }
            }
        }
        //finds the biggest weight and its index
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] >= getBiggestWeight()) {
                setBiggestWeightIndex(i);
                biggestWeight = weights[i];
            }
        }
        setBiggestWeight(weights[getBiggestWeightIndex()]);
    }
    // ----------------------------------------------------------------------
    /**
     * calcs components by determining how many -1's are in parent array
     * -1's point to root nodes
     * @return number of components in parent array
     */
    public int countComponents() {
        int cnt = 0;
        for (int i = 0; i < getBiggestIndex() + 1; i++) {
            if (parentArray[i] == -1) {
                cnt++;
            }
        }
        return cnt;
    }
    // ----------------------------------------------------------------------
    /**
     * uses genCompleteNeighbors to create symmetrical 2-D array of 
     * edges between the biggest weight's indicies
     * @return 2-D array of edges from largest component
     */
    public int[][] generateEdgeArray() {
        int[] indicies = genCompleteNeighbors();
        //symmetrical 2-D array, ex. 5x5 -- [012345][012345]
        int[][] ret = new int[indicies.length][indicies.length];
        //first for loop iterates through # rows in 2D
        for (int i = 0; i < indicies.length; i++) {
            
            //generates neighbors at specific index
            //ex, root = 6 : -> 0 -> 1 -> 3
            //[6, 0, 1, 3]
            
            int[] temp = neighbors(indicies[i]);
            //second for loop iterates through # indices in each row
            for (int j = 0; j < indicies.length; j++) {
                ret[i][j] = Integer.MAX_VALUE;
                
                //iterates through the neighbors array
                //checks for edge between index crrntly at and neighbor arry
                for (int x = 0; x < temp.length; x++) {
                    if (temp[x] == indicies[j]) {
                        ret[i][j] = 1;
                        break;
                    }
                    if (i == j) {
                        ret[i][j] = 0;
                    }
                }
            }
        }
        return ret;
    }
    // ----------------------------------------------------------------------
    /**
     * starts at biggest weight's index and retrieves all vertices 
     * connected to that root node
     * @return generates array of neighboring indices starting at 
     * biggestWeight
     */
    int[] genCompleteNeighbors() {
        generateGraph();
        int[] ret = new int[getBiggestWeight()];
        ret[0] = getBiggestWeightIndex();
        int count = 1;
        for (int i = 0; i < parentArray.length; i++) {
            if (parentArray[i] == getBiggestWeightIndex()) {
                ret[count] = i;
                count++;
            }
        }
        return ret;
    }
    // ----------------------------------------------------------------------
    /**
     * Compute all-pairs shortest paths
     * @param d 2-D Array of connected edges
     * @return the maximum diameter generated by floyd's alg
     */
    int floyd(int[][] d) {
        // Compute all k paths
        if (d[0].length == 2) {
            //edge case
            return 1;
        }
        for (int k = 0; k < getBiggestWeight(); k++) {
            for (int i = 0; i < getBiggestWeight(); i++) {
                for (int j = 0; j < getBiggestWeight(); j++) {
                    if ((d[i][k] != Integer.MAX_VALUE) && 
                            (d[k][j] != Integer.MAX_VALUE)
                            && (d[i][j] > (d[i][k] + d[k][j]))) {
                        d[i][j] = d[i][k] + d[k][j];
                    }
                }
            }
        }
        return findFloydMax(d);
    }
    // ----------------------------------------------------------------------
    /**
     * Finds the maximum value in array generated by floyd algorithm
     * @param d array to look for
     * @return max val in D
     */
    int findFloydMax(int[][] d) {
        int maximum = -1;
        for (int i = 0; i < d[0].length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                if (Integer.MAX_VALUE != d[i][j] && maximum < d[i][j]) {
                    maximum = d[i][j];
                }
            }
        }
        return maximum;
    }
    // ----------------------------------------------------------------------
    /**
     * prints out information requested by comdproc and doc specification
     */
    public void printAll() {
        this.createParentAndWeights();
        this.generateGraph();
        System.out.println(
                "There are " + countComponents() + " connected components");
        System.out.println("The largest connected component has "
                + getBiggestWeight() + " elements");
        System.out.println("The diameter of the largest " + "component is "
                + floyd(generateEdgeArray()));
    }
    // ----------------------------------------------------------------------
    /**
     * @return the parentArray
     */
    public int[] getParentArray() {
        return parentArray;
    }
    // ----------------------------------------------------------------------
    /**
     * @return the weights Array
     */
    public int[] getWeights() {
        return weights;
    }
    // ----------------------------------------------------------------------
    /**
     * @return the biggestWeightIndex
     */
    public int getBiggestWeightIndex() {
        return biggestWeightIndex;
    }
    /**
     * @param biggestWeight the biggestWeight to set
     */
    public void setBiggestWeight(int biggestWeight) {
        this.biggestWeight = biggestWeight;
    }
    /**
     * @return the biggestWeight
     */
    public int getBiggestWeight() {
        return biggestWeight;
    }
    /**
     * @param biggestWeightIndex the biggestWeight to set
     */
    public void setBiggestWeightIndex(int biggestWeightIndex) {
        this.biggestWeightIndex = biggestWeightIndex;
    }
    /**
     * @return the biggestIndex
     */
    public int getBiggestIndex() {
        return biggestIndex;
    }
    /**
     * @param biggestIndex the biggestIndex to set
     */
    public void setBiggestIndex(int biggestIndex) {
        this.biggestIndex = biggestIndex;
    }
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private class Node { // Doubly linked list node
        int vertex;
        @SuppressWarnings("unused")
        int weight;
        Node prev;
        Node next;
        Node(int v, Node p, Node n) {
            vertex = v;
            weight = 1;
            prev = p;
            next = n;
        }
    }
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
}
