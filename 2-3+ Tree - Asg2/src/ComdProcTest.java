import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;

import student.TestCase;

/**
 * 
 */

/**
 * @author Jack Guttman
 * @version September 1, 2016
 */
public class ComdProcTest extends TestCase {
    /**
     * test parser
     */
    private Parser testParser;

    /**
     * initializes the parser to begin tests
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        testParser = new Parser();
        testParser.run(10, 32, "P2_Input1_Sample.txt");
    }

    /**
     * reads a file
     * @param path filename
     * @return string
     * @throws IOException
     */
    static String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded);
    }
    // -------------------------------------------------------------------------
    /**
     * runs the P2 input file expecting P2 output
     */
    public void testP2() throws Exception {
        String theOutput = readFile("P2_Output1_Sample.txt");
        assertNotNull(testParser.getComdProc().getArtistTable());
        assertNotNull(testParser.getComdProc().getSongTable());
        assertFuzzyEquals(systemOut().getHistory(), theOutput);
    }
    // -------------------------------------------------------------------------
    /**
     * tests a simple tree delete
     */
    public void testSimpleTreeDelete() {
        systemOut().clearHistory();
        testParser.getComdProc().insert("Green Day", "Time of your Life");
        testParser.getComdProc().insert("Green Day", "Basketcase");
        testParser.getComdProc().delete("Green Day", "Basketcase");
        assertEquals(systemOut().getHistory(),
                "|Green Day| duplicates a record already in the artist "
                + "database.\n|Time of your Life| is added to the "
                + "song database.\nThe KVPair (|Green Day|,|Time of your "
                + "Life|),(859,9291) is added to the tree.\nThe KVPair "
                + "(|Time of your Life|,|Green Day|),(9291,859) is added"
                + " to the tree.\n|Green Day| duplicates a record "
                + "already in the artist "
                + "database.\n|Basketcase| is added to the song database.\n"
                + "The KVPair (|Green Day|,|Basketcase|),"
                + "(859,9331) is added to the tree.\nThe KVPair "
                + "(|Basketcase|,|Green Day|),(9331,859) is"
                + " added to the tree.\nThe KVPair "
                + "(|Green Day|,|Basketcase|) "
                + "is deleted from the tree.\nThe KVPair "
                + "(|Basketcase|,|Green "
                + "Day|) is deleted from the tree.\n|Basketcase| is "
                + "deleted from the song database.\n");
        systemOut().clearHistory();
        testParser.getComdProc().list("Green Day", "artist");
        assertEquals(systemOut().getHistory(),
                "|Wake Me Up When September Ends (Live at Foxboro_ MA 9/3/05)|"
                + "\n|Time of your Life|\n");
        testParser.getComdProc().remove("Green Day", "artist");
        systemOut().clearHistory();
        testParser.getComdProc().list("ddbbll", "artist");
        assertEquals(systemOut().getHistory(), 
                "|ddbbll| does not exist in the artist database.\n");
        systemOut().clearHistory();
        testParser.getComdProc().list("ddbbll", "song");
        assertEquals(systemOut().getHistory(), 
                "|ddbbll| does not exist in the song database.\n");
    }
}
