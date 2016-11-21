import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * The class containing the main method. Takes in 3 parameters.
 * The first parameter is the filename of a binary file of 
 * some multiple of BLOCK_SIZE bytes. These bytes are then 
 * divided up into BLOCK_SIZE buffers and tracked in a buffer
 * pool. The buffer pool is a doubley-linked list that is of a
 * limited size, denoted by the second argument in main. 
 * The byte file is sorted by using a modified quicksort method
 * and is written back into the original file once sorting is
 * complete. A file with specific statistics about the sort
 * is printed to argument 3 after the file is sorted.
 * @author Jack Guttman
 * @version October 20th, 2016
 */
// On my honor:
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.
//
// --- Jack R. Guttman
public class Quicksort {
    // -------------------------------------------------------------------------
    /**
     * @param args Command line parameters.
     */
    public static void main(String[] args) throws IOException {
        // This is the main file for the program.
        if (args == null || args.length < 3) {
            System.out.println("Not enough parameters provided.");
        }
        else {
            int numbBufs = Integer.parseInt(args[1]);
            RandomAccessFile toSort = null;
            try {
                toSort = new RandomAccessFile(args[0], "rw");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            
            if (toSort != null) {
                FileWriter output = new FileWriter(args[2], true);
                //starts sorting on file with first argument name
                LRUBufferPool bufPool = new LRUBufferPool(toSort, numbBufs);
                bufPool.closeFile();
                // filewriter for stat file
                output.write("Sort on " + args[0] + "\n");
                output.write("Cache Hits: " + bufPool.getHits() + "\n");
                output.write("Disk reads: " + bufPool.getReads() + "\n");
                output.write("Disk writes: " + bufPool.getWrites() + "\n");
                output.write("Time is " + bufPool.getTime() + "\n");
                output.flush();
                output.close();
            }
            else {
                System.out.println("No such file with name: " + args[0]);
            }
        }
    }
}
