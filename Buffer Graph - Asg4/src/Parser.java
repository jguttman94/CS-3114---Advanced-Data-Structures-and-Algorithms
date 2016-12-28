import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author Jack Guttman
 * @version September 1st, 2016
 */
public class Parser {
    
    //----------------------------------------------------------------------
    /**
     * the initial size of the hash table, provided by args
     */
    public static int initHashSize = 0;
    /**
     * the initial size of memory pool in bytes, provided by args
     */
    public static int initBlockSize = 0;
    /**
     * comdproc object
     */
    public ComdProc comdProc;
    private long endTime;
    
    //----------------------------------------------------------------------
    /**
     * The initialization method for comdproc
     * @param binFile       name of bin file to write to
     * @param bufferP       size of buffer pool 
     * @param blockSize     size of freeblock initialization
     * @param hashSize      size of hash tables upon initialization
     * @param inputfile          name of file to read commands from
     * @param statisticsFile     name of stats file to write to
     * @throws IOException 
     */
    public void run(String binFile, int bufferP, int blockSize, int hashSize, 
            String inputfile, String statisticsFile) throws IOException {
        long startTime = System.currentTimeMillis();
        comdProc = new ComdProc(hashSize, blockSize, bufferP, binFile);
        initHashSize = hashSize;
        initBlockSize = blockSize;
        
        comdProc.clear();
        startParsingLine(inputfile); //parses said file.
        comdProc.write();
        endTime = System.currentTimeMillis() - startTime;
        
        FileWriter output = new FileWriter(statisticsFile, true);
        output.write("Cache Hits: " + 
                comdProc.manager.getBuffer().getHits() + "\n");
        output.write("Disk reads: " + 
                comdProc.manager.getBuffer().getReads() + "\n");
        output.write("Disk writes: " + 
                comdProc.manager.getBuffer().getWrites() + "\n");
        output.write("Time is " + endTime + "\n");
        output.flush();
        output.close();
    }
    
    //----------------------------------------------------------------------
    /**
     * Looks for commands in a file to begin calling appropriate methods and 
     * their associated parameters.
     * @param fileName    the name of the file that will be examined
     */
    @SuppressWarnings("resource")
    private void startParsingLine(String fileName) {
        try {
            //scanner to read the file
            Scanner sc = new Scanner(new File(fileName)); 
            //scanner to read words in each line
            Scanner scanCmd; 
            
            //while there's text left in the file
            while (sc.hasNextLine()) { 
                //get the next line
                String line = sc.nextLine(); 
                //create a scanner from said line
                scanCmd = new Scanner(line); 
                //gets the first word/command in line
                String cmd = scanCmd.next(); 
                String type;
                
                switch(cmd) {
                    case "insert": //if command "insert" is found
                        //no longer delineate by spaces, separate line by <SEP>
                        scanCmd.useDelimiter("<SEP>"); 
                        String artist = scanCmd.next().trim();
                        String song = scanCmd.next(); 
                        //insert Artist and Song into hash tables
                        comdProc.insert(artist, song);
                        break;
                        
                    case "remove": //if command "remove" is found
                        //what type of remove is happening?
                        type = scanCmd.next(); 
                        String token; 
                        //gets the name to remove since both artist and song 
                        //contain spaces, we must take the full line.
                        token = scanCmd.nextLine().trim();
                        comdProc.remove(token, type);
                        break;
                        
                    case "print": //if command "print" is found
                        //what type of print is happening?
                        type = scanCmd.next(); 
                        comdProc.print(type);
                        break;
//                    case "list":
//                        type = scanCmd.next(); 
//                        String name; 
//                        //gets the name to remove since both artist and song 
//                        //contain spaces, we must take the full line.
//                        name = scanCmd.nextLine().trim();
//                        comdProc.list(name, type);
//                        break;
//                    case "delete":
//                        //no longer delineate by spaces, separate line by 
                          //<SEP>
//                        scanCmd.useDelimiter("<SEP>"); 
//                        String art = scanCmd.next().trim();
//                        String son = scanCmd.next(); 
//                        //delete record
//                        comdProc.delete(art, son);
//                        break;
                    default:
                        //not expecting bad command inputs
                }
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    //----------------------------------------------------------------------
    /**
     * getter method for hash table
     * @return     returns initial block size
     */
    public int getInitBlockSize() {
        return initBlockSize;
    }
    /**
     * getter method for hash table
     * @return     returns initial hash size
     */
    public int getInitHashSize() {
        return initHashSize;
    }
    /**
     * getter method for hash table
     * @return     returns initial hash size
     */
    public ComdProc getComdProc() {
        return comdProc;
    }
    // -------------------------------------------------------------------------
    /**
     * @return the time elapsed
     */
    public long getTime() {
        return (endTime / 1000000);
    }

}
