import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;

import student.TestCase;

/**
 * @author Jack Guttman
 * @version September 1, 2016
 *
 */
public class ComdProcTest extends TestCase {
    
    /**
     * initializes the parser to begin tests
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        Parser testParser = new Parser();
        testParser.run("testBin", 10, 32, 10, "P1sampleInput.txt", 
                "stat.txt");
    }
    
    /**
     * Tests for revised output
     * @throws IOException
     */
    public void testOutputRevise() throws IOException {

        systemOut().clearHistory();
        Parser pars = new Parser();
        systemOut().clearHistory();
        pars.run("testBin", 10, 32, 10, "P1_Input2_Sample", 
                "StatsFile.txt");
        pars.getComdProc().write();
        assertFuzzyEquals(systemOut().getHistory(), new String(
                Files.readAllBytes(Paths.get(
                        "P1_Output2_Sample")), StandardCharsets.UTF_8));
    }
    
    /**
     * test project 4 sample input
     * @throws IOException 
     */
    public void testProj4() throws IOException {
        systemOut().clearHistory();
        Parser pars = new Parser();
        pars.run("P4mem1", 10, 32, 10, "P4sampleInput", "stat.txt");
        assertFuzzyEquals(systemOut().getHistory(), new String(
                Files.readAllBytes(Paths.get(
                        "P4sampleOutput")), StandardCharsets.UTF_8));
    }
    

}
