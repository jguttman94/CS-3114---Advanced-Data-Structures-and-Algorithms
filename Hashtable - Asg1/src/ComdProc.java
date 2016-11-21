/**
 * 
 */

/**
 * Processes the commands received from Parser
 * and then calls the appropriate class/methods for those calls
 * @author Jack Guttman
 * @version September 1, 2016
 *
 */
public class ComdProc {
    /**
     * hash object used to call commands when read from scanner
     */
    public Hash artistTable;
    /**
     * hash object used to call commands when read from scanner
     */
    public Hash songTable;
    /**
     * memory manager object used to call commands 
     */
    public MemManager manager;
    //----------------------------------------------------------------------
    /**
     * Initialization that setups up hash tables
     * @param initSize      size of hash tables retrieved from parser
     * @param blockSize     size of memory pool retrieved form parser
     */
    public ComdProc(int initSize, int blockSize) {
        manager = new MemManager(blockSize);
        artistTable = new Hash(initSize, manager);
        songTable = new Hash(initSize, manager);
    }
    //----------------------------------------------------------------------
    /**
     * inserts new record into desired table, resizes if necessary
     * @param artist    name of artist to insert
     * @param song      name of song to insert
     */
    public void insert(String artist, String song) {
        //inserts artist
        if (artistTable.check(artist) == -1) {
            //checks for resizing
            if (artistTable.resize()) {
                System.out.println("Artist hash table size doubled.");
            }  
            artistTable.insert(artist);
            System.out.println("|" + artist + 
                    "| is added to the artist database.");
        }
        else {
            System.out.println("|" + artist + 
                    "| duplicates a record already in the artist database.");
        }

        
        //inserts song
        if (songTable.check(song) == -1) {
            //checks for resizing
            if (songTable.resize()) {
                System.out.println("Song hash table size doubled.");
            }
            songTable.insert(song);
            System.out.println("|" + song + 
                    "| is added to the song database.");
        }
        else { //duplicate record
            System.out.println("|" + song + 
                    "| duplicates a record already in the song database.");
        }
        
    }
    //----------------------------------------------------------------------
    /**
     * attempts to remove record from desired hash table
     * @param token     name of item to remove
     * @param type      artist or song, determines what table to remove from
     */
    public void remove(String token, String type) {
        //determines what kind of remove based on type
        if (type.equals("artist")) {
            if (artistTable.remove(token)) {
                System.out.println("|" + token + 
                        "| is removed from the artist database.");
            }
            else {
                System.out.println("|" + token + 
                        "| does not exist in the artist database.");
            }
        }
        else {
            if (songTable.remove(token)) {
                System.out.println("|" + token + 
                        "| is removed from the song database.");
            }
            else {
                System.out.println("|" + token + 
                        "| does not exist in the song database.");
            }
        }
        
    }
    //----------------------------------------------------------------------
    /**
     * prints out the items currently in the hash table, and their total num
     * @param type      artist or song, determines what table to print
     */
    public void print(String type) {
        //determines the kind of print based on type
        if (type.equals("artist")) {
            if (artistTable.print()) {
                System.out.println("total artists: " + 
                        artistTable.getHashSize());
            }
            else {
                System.out.println("total artists: 0");
            }
        }
        else if (type.equals("song")) {
            if (songTable.print()) {
                System.out.println("total songs: " + 
                        songTable.getHashSize());
            }
            else {
                System.out.println("total songs: 0");
            }
        }
        else {
            manager.dump();
        }
        
    }
    //----------------------------------------------------------------------
    /**
     * Getter method for retrieve
     * @return     hash object
     */
    public Hash getArtistTable() {
        return artistTable;
    }
    //----------------------------------------------------------------------
    /**
     * Getter method for retrieve
     * @return     hash object
     */
    public Hash getSongTable() {
        return songTable;
    }

}
