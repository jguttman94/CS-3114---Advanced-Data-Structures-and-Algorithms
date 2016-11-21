import java.util.List;

/**
 * 
 */

/**
 * Processes the commands received from Parser and then calls the appropriate
 * class/methods for those calls
 * @author Jack Guttman
 * @version September 1, 2016
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
    /**
     * two three tree object used to call commands
     */
    public TTTree tree;

    // -------------------------------------------------------------------------
    /**
     * Initialization that setups up hash tables
     * @param initSize size of hash tables retrieved from parser
     * @param blockSize size of memory pool retrieved form parser
     */
    public ComdProc(int initSize, int blockSize) {
        manager = new MemManager(blockSize);
        tree = new TTTree();
        artistTable = new Hash(initSize, manager);
        songTable = new Hash(initSize, manager);
    }

    // -------------------------------------------------------------------------
    /**
     * inserts new record into desired table, resizes if necessary
     * @param artist name of artist to insert
     * @param song name of song to insert
     */
    public void insert(String artist, String song) {
        // inserts artist
        Handle art = null;
        Handle son = null;
        if (artistTable.check(artist) == -1) {
            // checks for resizing
            if (artistTable.resize()) {
                System.out.println("Artist hash table size doubled.");
            }
            artistTable.insert(artist);
            art = artistTable.getHand();
            System.out.println(
                    "|" + artist + "| is added to the artist database.");
        }
        else {
            art = artistTable.getHand();
            System.out.println("|" + artist
                    + "| duplicates a record already in the artist database.");
        }
        // inserts song
        if (songTable.check(song) == -1) {
            // checks for resizing
            if (songTable.resize()) {
                System.out.println("Song hash table size doubled.");
            }
            songTable.insert(song);
            son = songTable.getHand();
            System.out.println("|" + song + "| is added to the song database.");
        }
        else { // duplicate record
            son = songTable.getHand();
            System.out.println("|" + song
                    + "| duplicates a record already in the song database.");
        }
        //inserts into tree
        System.out.print("The KVPair (|" + artist + "|,|" + song + "|)");
        tree.insert(new KVPair(art, son));
        System.out.print("The KVPair (|" + song + "|,|" + artist + "|)");
        tree.insert(new KVPair(son, art));
    }

    // -------------------------------------------------------------------------
    /**
     * attempts to remove record from desired hash table
     * @param token name of item to remove
     * @param type artist or song, determines what table to remove from
     */
    public void remove(String token, String type) {
        // determines what kind of remove based on type
        if (type.equals("artist")) {
            artistTable.setHand(null);
            artistTable.check(token);
            Handle found = artistTable.getHand();
            if (found != null) {
                //removes every found node in tree with token's value
                List<Handle> ret = tree.list(found);
                for (int i = 0; i < ret.size(); i++) {
                    String song = artistTable.retrieveString(ret.get(i));
                    delete(token, song);
                }
            }
            else {
                System.out.println("|" + token
                        + "| does not exist in the artist database.");
            }
        }
        else {
            //removing songs has to be done long way since comdproc's
            //remove prints the artist remove first
            songTable.setHand(null);
            songTable.check(token);
            Handle found = songTable.getHand();
            if (found != null) {
                List<Handle> ret = tree.list(found);
                for (int i = 0; i < ret.size(); i++) {
                    String artist = songTable.retrieveString(ret.get(i));
                    System.out.print(
                            "The KVPair (|" + token + "|,|" + artist + "|)");
                    tree.delete(new KVPair(found, ret.get(i)));
                    System.out.print(
                            "The KVPair (|" + artist + "|,|" + token + "|)");
                    tree.delete(new KVPair(ret.get(i), found));
                    if (tree.list(found).size() == 0) {
                        songTable.remove(token);
                        System.out.println("|" + token
                                + "| is deleted from the song database.");
                    }
                    if (tree.list(ret.get(i)).size() == 0) {
                        artistTable.remove(artist);
                        System.out.println("|" + artist
                                + "| is deleted from the artist database.");
                    }
                }
            }
            else {
                System.out.println(
                        "|" + token + "| does not exist in the song database.");
            }
        }

    }

    // -------------------------------------------------------------------------
    /**
     * prints out the items currently in the hash table, and their total num
     * @param type artist or song, determines what table to print
     */
    public void print(String type) {
        // determines the kind of print based on type
        if (type.equals("artist")) {
            if (artistTable.print()) {
                System.out
                        .println("total artists: " + artistTable.getHashSize());
            }
            else {
                System.out.println("total artists: 0");
            }
        }
        else if (type.equals("song")) {
            if (songTable.print()) {
                System.out.println("total songs: " + songTable.getHashSize());
            }
            else {
                System.out.println("total songs: 0");
            }
        }
        else if (type.equals("blocks")) {
            manager.dump();
        }
        else {
            //prints the tree
            System.out.println("Printing 2-3 tree:");
            tree.print();
        }

    }

    // -------------------------------------------------------------------------
    /**
     * Lists the type in tree in order
     * @param name name to list
     * @param type artist or song, determines what to list
     */
    public void list(String name, String type) {
        if (type.equals("artist")) {
            // lists artists
            artistTable.setHand(null);
            artistTable.check(name);
            Handle found = artistTable.getHand();
            if (found != null) {
                List<Handle> ret = tree.list(found);
                for (int i = 0; i < ret.size(); i++) {
                    String print = artistTable.retrieveString(ret.get(i));
                    System.out.println("|" + print + "|");
                }
            }
            else {
                System.out.println("|" + name
                        + "| does not exist in the artist database.");
            }
        }
        else {
            // list songs
            songTable.setHand(null);
            songTable.check(name);
            Handle found = songTable.getHand();
            if (found != null) {
                List<Handle> ret = tree.list(found);
                for (int i = 0; i < ret.size(); i++) {
                    String print = songTable.retrieveString(ret.get(i));
                    System.out.println("|" + print + "|");
                }
            }
            else {
                System.out.println(
                        "|" + name + "| does not exist in the song database.");
            }
        }
    }

    // -------------------------------------------------------------------------
    /**
     * Record to delete
     * @param artist artist of record
     * @param song song of record
     */
    public void delete(String artist, String song) {
        artistTable.setHand(null);
        songTable.setHand(null);
        artistTable.check(artist);
        Handle foundArt = artistTable.getHand();
        songTable.check(song);
        Handle foundSon = songTable.getHand();

        // deletes from 2-3tree if in hashtables
        if (foundArt != null && foundSon != null) {
            System.out.print("The KVPair (|" + artist + "|,|" + song + "|)");
            tree.delete(new KVPair(foundArt, foundSon));
            System.out.print("The KVPair (|" + song + "|,|" + artist + "|)");
            tree.delete(new KVPair(foundSon, foundArt));
            //if the artist was the last instance of the artist, remove from
            //hash table and mem manager too
            if (tree.list(foundArt).size() == 0) {
                artistTable.remove(artist);
                System.out.println("|" + artist
                        + "| is deleted from the artist database.");
            }
            //if the song was the last instance of the song, remove from hash
            //table and mem manager too
            if (tree.list(foundSon).size() == 0) {
                songTable.remove(song);
                System.out.println(
                        "|" + song + "| is deleted from the song database.");
            }
        }
        if (foundArt == null) {
            System.out.println(
                    "|" + artist + "| does not exist in the artist database.");
        }

        else if (foundSon == null) {
            System.out.println(
                    "|" + song + "| does not exist in the song database.");
        }
    }

    // -------------------------------------------------------------------------
    /**
     * Getter method for retrieve
     * @return hash object
     */
    public Hash getArtistTable() {
        return artistTable;
    }

    // -------------------------------------------------------------------------
    /**
     * Getter method for retrieve
     * @return hash object
     */
    public Hash getSongTable() {
        return songTable;
    }

}
