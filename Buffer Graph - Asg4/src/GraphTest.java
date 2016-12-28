import java.util.Arrays;

import student.TestCase;

/**
 * Test case for buffer class
 * @author Jack Guttman
 * @version October 20th, 2016
 */
public class GraphTest extends TestCase {
    private Graph graph;
    /**
     * sets up testing cases
     */
    public void setUp() {
        graph = new Graph(20);
    }
    /**
     * first test
     */
    public void insertTests() {
        //from p4 sample input vals
        graph.addNode(0, 1);
        graph.addNode(2, 3);
        graph.addNode(4, 5);
        graph.addNode(6, 7);
        graph.addNode(8, 9);
        graph.addNode(6, 9);
    }
    /**
     * continue testing variables
     */
    public void tests() {
        assertEquals(graph.edgeCount(), 0);
        assertEquals(graph.getBiggestWeight(), 0);
        assertEquals(graph.getBiggestWeightIndex(), -1);
        assertEquals(graph.nodeCount(), 20);
    }
    /**
     * tests changing of global vars
     */
    public void testsGlobalVarsWInsert() {
        insertTests();
        graph.addHelper(0, 1, 1);
        graph.printNodeArray();
        assertEquals(graph.edgeCount(), 12);
    }
    /**
     * tests neighbors
     */
    public void testNeighbors() {
        insertTests();
        int[] temp = graph.neighbors(6);
        assertEquals(temp[0], 7);
        assertEquals(temp[1], 9);
        temp = graph.neighbors(9);
        assertEquals(temp[0], 6);
        assertEquals(temp[1], 8);
    }
    /**
     * tests retrieving the next available index
     */
    public void testNextAvail() {
        insertTests();
        int ret = graph.getNextAvailableIndex();
        assertEquals(ret, 10);
        graph.addNode(10, 11);
        graph.addNode(12, 13);
        graph.addNode(14, 15);
        assertEquals(graph.getNextAvailableIndex(), 16);
    }
    /**
     * tests printing the tree
     */
    public void testprintNodeArray() {
        insertTests();
        systemOut().clearHistory();
        graph.printNodeArray();
        assertEquals(systemOut().getHistory(),
                "| 0| - 1\n" + "| 1| - 0\n| 2| - 3\n| 3| - 2\n| 4| - 5\n"
                        + "| 5| - 4\n| 6| - 7 - 9\n| 7| - 6\n"
                        + "| 8| - 9\n| 9| - 6 - 8\n| -1|\n| -1|\n"
                        + "| -1|\n| -1|\n| -1|\n| -1|\n| -1|\n| -1|\n"
                        + "| -1|\n| -1|\n");
        int ret = graph.getNextAvailableIndex();
        assertEquals(ret, 10);
        assertEquals(graph.getBiggestIndex(), 10);
    }
    /**
     * tests parent pointer tree creation
     */
    public void testPPTree() {
        insertTests();
        graph.createParentAndWeights();
        String ret1 = "";
        String ret2 = "";
        for (int i = 0; i < graph.getParentArray().length; i++) {
            ret1 += graph.getParentArray()[i];
            ret2 += graph.getWeights()[i];
        }
        assertEquals(ret1, "-1-1-1-1-1-1-1-1-1-1-1");
        assertEquals(ret2, "11111111111");
    }
    /**
     * Tests remove
     */
    public void testRemove() {
        graph.addNode(0, 1);
        graph.addNode(2, 3);
        graph.addNode(3, 4);
        graph.addNode(3, 5);
        graph.addNode(4, 6);
        graph.addNode(5, 6);
        graph.addNode(5, 7);
        graph.printNodeArray();
        systemOut().clearHistory();
        graph.printAll();
        assertEquals(systemOut().getHistory(),
                "There are 2 connected components\n"
                        + "The largest connected component has 6 elements\n"
                        + "The diameter of the largest component is 3\n");
    }
    /**
     * tests floyd
     */
    public void test2DArray() {
        insertTests();
        graph.createParentAndWeights();
        graph.generateGraph();
        int[][] ret = graph.generateEdgeArray();
        System.out.print(Arrays.deepToString(ret));
        assertEquals(systemOut().getHistory(), "[[0, 1, 2147483647, 1], "
                + "[1, 0, 2147483647, 2147483647], " + 
                "[2147483647, 2147483647, 0, 1], " + "[1, 2147483647, 1, 0]]");
    }
    /**
     * tests complicated diameter
     */
    public void testDiameter() {
        insertTests();
        graph.createParentAndWeights();
        graph.generateGraph();
        assertEquals(graph.countComponents(), 4);
        graph.addNode(6, 10);
        graph.addNode(9, 10);
        graph.addNode(7, 11);
        graph.addNode(8, 11);
        graph.addNode(8, 12);
        graph.addNode(12, 10);
        systemOut().clearHistory();
        graph.printNodeArray();
        assertEquals(systemOut().getHistory(),
                "| 0| - 1\n" + "| 1| - 0\n| 2| - 3\n| 3| - 2\n"
                        + "| 4| - 5\n| 5| - 4\n| 6| - 7 - 9 - 10\n"
                        + "| 7| - 6 - 11\n| 8| - 9 - 11 - 12\n"
                        + "| 9| - 6 - 8 - 10\n| 10| - 6 - 9 - 12\n"
                        + "| 11| - 7 - 8\n| 12| - 8 - 10\n| -1|\n| -1|\n"
                        + "| -1|\n| -1|\n| -1|\n| -1|\n| -1|\n");
        systemOut().clearHistory();
        graph.printAll();
        assertEquals(systemOut().getHistory(),
                "There are 4 connected components\n"
                        + "The largest connected component has 7 elements\n"
                        + "The diameter of the largest component is 3\n");
        graph.addNode(13, 12);
        graph.addNode(13, 11);
        graph.addNode(13, 14);
        graph.addNode(14, 15);
        graph.printNodeArray();
        systemOut().clearHistory();
        graph.printAll();
        assertEquals(systemOut().getHistory(),
                "There are 4 connected components\n"
                        + "The largest connected component has 10 elements\n"
                        + "The diameter of the largest component is 5\n");
        graph.addNode(15, 16);
        graph.addNode(16, 11);
        systemOut().clearHistory();
        graph.printAll();
        assertEquals(systemOut().getHistory(),
                "There are 4 connected components\n"
                        + "The largest connected component has 11 elements\n"
                        + "The diameter of the largest component is 4\n");
        graph.printNodeArray();
        graph.addNode(6, 17);
        graph.addNode(17, 15);
        graph.addNode(18, 17);
        graph.addNode(18, 19);
        systemOut().clearHistory();
        graph.printAll();
        assertEquals(systemOut().getHistory(),
                "There are 4 connected components\n"
                        + "The largest connected component has 14 elements\n"
                        + "The diameter of the largest component is 5\n");
        // adjacency list is full
        assertEquals(graph.nodeCount(), 40);
        assertEquals(graph.getNextAvailableIndex(), 20);
        // adjacency list doubles
        graph.printNodeArray();
        graph.remove(13);
        graph.printNodeArray();
        systemOut().clearHistory();
        graph.printAll();
        assertEquals(systemOut().getHistory(),
                "There are 4 connected components\n"
                        + "The largest connected component has 13 elements\n"
                        + "The diameter of the largest component is 5\n");
        int[][] ret = graph.generateEdgeArray();
        System.out.println(ret[0].length);
        for (int i = 0; i < ret.length; i++) {
            for (int j = 0; j < ret.length; j++) {
                if (ret[i][j] == Integer.MAX_VALUE) {
                    ret[i][j] = 9;
                }
            }
            System.out.println(Arrays.toString(ret[i]));
        }
        graph.remove(6);
        graph.printNodeArray();
        systemOut().clearHistory();
        graph.printAll();
        assertEquals(systemOut().getHistory(),
                "There are 4 connected components\n"
                        + "The largest connected component has 12 elements\n"
                        + "The diameter of the largest component is 8\n");
    }
}
