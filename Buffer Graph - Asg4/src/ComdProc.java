import java.io.IOException;

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
    //private String bin;

    private Graph graph;
    //----------------------------------------------------------------------
    /**
     * Initialization that setups up hash tables
     * @param initSize      size of hash tables retrieved from parser
     * @param blockSize     size of memory pool retrieved form parser
     * @param bufferpoolSizes   number of buffers in pool
     * @param binFile           name of binFile
     */
    public ComdProc(int initSize, int blockSize, int bufferpoolSizes,
            String binFile) throws IOException {
        manager = new MemManager(blockSize, bufferpoolSizes, binFile);
        artistTable = new Hash(initSize, manager);
        songTable = new Hash(initSize, manager);

        graph = new Graph(initSize);
    }
    //----------------------------------------------------------------------
    /**
     * inserts new record into desired table, resizes if necessary
     * @param artist    name of artist to insert
     * @param song      name of song to insert
     * @throws IOException 
     */
    public void insert(String artist, String song) throws IOException {
        //inserts artist
        boolean songFound = songTable.check(song) != -1;
        if (artistTable.check(artist) == -1) {
            // insert both
            if (!songFound) {
                if (artistTable.resize()) {
                    System.out.println("Artist hash table size doubled.");
                }  
                artistTable.insert(artist);
                System.out.println("|" + artist + 
                        "| is added to the artist database.");
                if (songTable.resize()) {
                    System.out.println("Song hash table size doubled.");
                }                
                //retrieves proper graph vertices
                int vert1 = graph.getNextAvailableIndex();
                artistTable.getHand(artist).setVertex(vert1);
                graph.setNodeArrayVal(vert1, -404);
                
                songTable.insert(song);
                System.out.println("|" + song + 
                        "| is added to the song database.");
                
                //retrieves proper graph vertices
                int vert2 = graph.getNextAvailableIndex();
                songTable.getHand(song).setVertex(vert2);
                graph.setNodeArrayVal(vert2, -404);
                
                graph.addNode(vert1, vert2);
            }
            // insert only artist
            else {
              //checks for resizing
                if (artistTable.resize()) {
                    System.out.println("Artist hash table size doubled.");
                }  
                artistTable.insert(artist);
                System.out.println("|" + artist + 
                        "| is added to the artist database.");
                System.out.println("|" + song + 
                        "| duplicates a record already in the song database.");
                 
                //generates one new vertex
                int vert1 = graph.getNextAvailableIndex();
                artistTable.getHand(artist).setVertex(vert1);
                graph.setNodeArrayVal(vert1, -404);
                
                //grabs pre-existing vertex
                int vert2 = songTable.getHand(song).getVertex();
                graph.addNode(vert1, vert2);
                
            }
        }
        // artist already inside
        else if (!songFound) {
            // insert only song
            System.out.println("|" + artist + 
                    "| duplicates a record already in the artist database.");
            if (songTable.resize()) {
                System.out.println("Song hash table size doubled.");
            }
            songTable.insert(song);
            System.out.println("|" + song + 
                    "| is added to the song database.");
            

            int vert1 = graph.getNextAvailableIndex();
            songTable.getHand(song).setVertex(vert1);
            graph.setNodeArrayVal(vert1, -404);
            
            int vert2 = artistTable.getHand(artist).getVertex();

            graph.addNode(vert2, vert1);
        }
        else {
            System.out.println("|" + artist + 
                    "| duplicates a record already in the artist database.");
            System.out.println("|" + song + 
                    "| duplicates a record already in the song database.");
            

            int vert1 = artistTable.getHand(artist).getVertex();
            int vert2 = songTable.getHand(song).getVertex();
            int[] temp = graph.neighbors(vert1);
            boolean found = false;
            for (int i = 0; i < temp.length; i++) {
                if (temp[i] == vert2) {
                    found = true;
                    i = temp.length;
                }
                
            }
            if (!found) {
                graph.addNode(vert1, vert2);
            }
        }
    }
    //----------------------------------------------------------------------
    /**
     * attempts to remove record from desired hash table
     * @param token     name of item to remove
     * @param type      artist or song, determines what table to remove from
     * @throws IOException 
     */
    public void remove(String token, String type) throws IOException {
        //determines what kind of remove based on type
        if (type.equals("artist")) {
            Record toRemove = artistTable.getHand(token);
            if (toRemove != null) {
                int vertToRemove = toRemove.getVertex();
                graph.remove(vertToRemove);
                artistTable.remove(token);
                System.out.println("|" + token + 
                        "| is removed from the artist database.");
            }
            else {
                System.out.println("|" + token + 
                        "| does not exist in the artist database.");
            }
        }
        else {
            Record toRemove = songTable.getHand(token);
            if (toRemove != null) {
                int vertToRemove = toRemove.getVertex();
                graph.remove(vertToRemove);
                songTable.remove(token);
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
     * @throws IOException 
     */
    public void print(String type) throws IOException {
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
        else if (type.equals("blocks")) {
            manager.dump();
        }
        else {
            //prints graph information
            graph.printAll();
        }
        
    }
    //----------------------------------------------------------------------
    /**
     * writes the bufferpool to disk
     * @throws IOException
     */
    public void write() throws IOException {
        manager.getBuffer().writeFinish();
    }
    
    /**
     * clears the current disk
     * @throws IOException
     */
    public void clear() throws IOException {
        manager.getBuffer().clear();
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
