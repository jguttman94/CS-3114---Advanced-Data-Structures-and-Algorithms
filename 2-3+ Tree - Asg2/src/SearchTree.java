/**
 * {Project Description Here}
 */

/**
 * The class containing the main method.
 *
 * @author Jack Guttman
 * @version September 30, 2016
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

public class SearchTree {
    /**
     * @param args
     *      Command line parameters.
     */
    public static void main(String[] args) {
        //tests for appropriate number of parameters.
        if (args.length < 3) {
            System.out.println("Not enough parameters provided.");
        }
        else {
            //calls the command processor class to deal with the
            //parameters correctly
            new Parser().run(Integer.parseInt(args[0]), 
                    Integer.parseInt(args[1]), args[2]);
        }
    }
}
