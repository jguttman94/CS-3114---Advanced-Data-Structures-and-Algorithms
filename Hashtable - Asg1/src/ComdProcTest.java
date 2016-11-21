import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import student.TestCase;

/**
 * 
 */

/**
 * @author Jack Guttman
 * @version September 1, 2016
 *
 */
public class ComdProcTest extends TestCase {
    /**
     * test parser
     */
    private Parser testParser;
    /**
     * testing output from parser input
     */
    private String output;
    
    /**
     * initializes the parser to begin tests
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        testParser = new Parser();
        testParser.run(10, 32, "P1sampleInput.txt");
        output = "|When Summer's Through| "
                + "does not exist in the song database.\n(0,32)\n"
                + "total songs: 0\ntotal artists: 0\n"
                + "|Blind Lemon Jefferson| is added to the artist database.\n"
                + "Memory pool expanded to be 64 bytes.\n"
                + "|Long Lonesome Blues| is added to the song database.\n"
                + "|Ma Rainey| is added to the artist database.\n"
                + "Memory pool expanded to be 96 bytes.\n"
                + "|Ma Rainey's Black Bottom| is added to the song database.\n"
                + "Memory pool expanded to be 128 bytes.\n"
                + "|Charley Patton| is added to the artist database.\n"
                + "|Mississippi Boweavil Blues| is "
                + "added to the song database.\n"
                + "Memory pool expanded to be 160 bytes.\n"
                + "|Sleepy John Estes| is added to the artist database.\n"
                + "Memory pool expanded to be 192 bytes.\n"
                + "|Street Car Blues| is added to the song database.\n"
                + "|Bukka White| is added to the artist database.\n"
                + "Memory pool expanded to be 224 bytes.\n"
                + "|Fixin' To Die Blues| is added to the song database.\n"
                + "|Blind Lemon Jefferson| 0\n|Sleepy John Estes| 1\n"
                + "|Charley Patton| 4\n|Bukka White| 5\n|Ma Rainey| 7\n"
                + "total artists: 5\n|Fixin' To Die Blues| 1\n"
                + "|Mississippi Boweavil Blues| 2\n|Long Lonesome Blues| 5\n"
                + "|Ma Rainey's Black Bottom| 6\n|Street Car Blues| 9\n"
                + "total songs: 5\n"
                + "Artist hash table size doubled.\n"
                + "|Guitar Slim| is added to the artist database.\n"
                + "Song hash table size doubled.\n"
                + "Memory pool expanded to be 256 bytes.\n"
                + "|The Things That I Used To Do| "
                + "is added to the song database.\n"
                + "|Style Council| does not exist in the artist database.\n"
                + "|Ma Rainey| is removed from the artist database.\n"
                + "|Mississippi Boweavil Blues| "
                + "is removed from the song database.\n"
                + "|(The Best Part Of) Breakin' Up| "
                + "does not exist in the song database.\n"
                + "(44,11) -> (97,28) -> (239,17)\n"
                + "|Blind Lemon Jefferson| "
                + "duplicates a record already in the artist database.\n"
                + "|Got The Blues| is added to the song database.\n"
                + "|Little Eva| is added to the artist database.\n"
                + "Memory pool expanded to be 288 bytes.\n"
                + "|The Loco-Motion| is added to the song database.\n"
                + "|Blind Lemon Jefferson| 0\n|Bukka White| 4\n"
                + "|Sleepy John Estes| 10\n|Guitar Slim| 12\n"
                + "|Charley Patton| 14\n|Little Eva| 18\ntotal artists: 6\n"
                + "|Fixin' To Die Blues| 1\n|Street Car Blues| 5\n"
                + "|Got The Blues| 8\n|Long Lonesome Blues| 15\n"
                + "|Ma Rainey's Black Bottom| 16\n"
                + "|The Things That I Used To Do| 17\n"
                + "|The Loco-Motion| 18\ntotal songs: 7\n"
                + "|Jim Reeves| is added to the artist database.\n"
                + "|Jingle Bells| is added to the song database.\n"
                + "Memory pool expanded to be 320 bytes.\n"
                + "|Mongo Santamaria| is added to the artist database.\n"
                + "|Watermelon Man| is added to the song database.\n"
                + "(44,11) -> (121,4) -> (319,1)\n";
    }
    
    /**
     * tests the file read against the programs output
     */
    @Test
    public void testOutput() {
        assertNotNull(testParser.getComdProc().getArtistTable());
        assertNotNull(testParser.getComdProc().getSongTable());
        assertEquals(systemOut().getHistory(), output);
        systemOut().clearHistory();
        testParser.getComdProc().insert("test", "Watermelon Man");        
        assertEquals(systemOut().getHistory(), 
                "|test| is added to the artist database.\n"
                + "|Watermelon Man| duplicates a "
                + "record already in the song database.\n");
    }
    
    /**
     * Tests for revised output
     * @throws IOException
     */
    public void testOutputRevise() throws IOException {

        systemOut().clearHistory();
        Parser pars = new Parser();
        pars.run(10, 32, "P1_Input2_Sample");
        assertEquals(systemOut().getHistory(), new String(
                Files.readAllBytes(Paths.get(
                        "P1_Output2_Sample")), StandardCharsets.UTF_8));
    }
    

}
