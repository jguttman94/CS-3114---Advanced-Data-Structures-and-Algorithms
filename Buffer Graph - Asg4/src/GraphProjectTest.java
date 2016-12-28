import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import student.TestCase;

/**
 * @author Jack Guttman
 * @version November 11, 2016
 */
public class GraphProjectTest extends TestCase {
    /**
     * Sets up the tests that follow. In general, used for initialization.
     */
    public void setUp() {
        // Nothing Here
    }
    /**
     * Get code coverage of the class declaration.
     * @throws NumberFormatException
     * @throws IOException
     */
    public void testGInit() throws NumberFormatException, IOException {
        GraphProject gph = new GraphProject();
        assertNotNull(gph);
        GraphProject.main(new String[] { "0" });
        assertEquals(systemOut().getHistory(),
                "Not enough parameters " + "provided.\n");
    }
    // ----------------------------------------------------------------------
    /**
     * tests sample proj 4 input
     */
    public void testProj4() throws IOException {
        String[] tmp = new String[] {
            "P4mem1", "10", "32", "10", 
            "P4sampleInput", "StatsFile.txt" };
        GraphProject.main(tmp);
        assertFuzzyEquals(systemOut().getHistory(),
                new String(Files.readAllBytes(Paths.get("P4sampleOutput")),
                        StandardCharsets.UTF_8));
    }
}
