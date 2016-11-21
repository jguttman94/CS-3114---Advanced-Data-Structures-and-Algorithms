/**
 * Stub for hash table class    
 *
 * @author Jack Guttman
 * @version September 1, 2016
 */

public class Hash    
{
    //----------------------------------------------------------------------
    /**
     * hash table to hold all the record names    
     */
    private Handle[] hashTable;
       
    /**
     * current number of items in the table
     */
    private int hashSize;
    
    /**
     * Memory manager object
     */
    private MemManager memMan;
    
    /**
     * tombstone object for deleting records
     */
    private Handle tombstone;
    //----------------------------------------------------------------------
    /**
     * Create a new Hash object. Initializes the hash tables with the
     * appropriate size
     * @param size    size of the hash tables at initialization, 
     *                 passed from parser
     * @param pool    MemManager passed in through comdproc
     */    
    public Hash(int size, MemManager pool)
    {
        //gets size from parser field set by arguments
        hashTable = new Handle[size];
        hashSize = 0;   
        memMan = pool;
        tombstone = new Handle(-1);
        
    }

    //----------------------------------------------------------------------
    /**
     * Compute the hash function. Uses the "sfold" method from the OpenDSA
     * module on hash functions
     *
     * @param s
     *            The string that we are hashing
     * @param m
     *            The size of the hash table
     * @return The home slot for that string
     */
    // Make this private in your project.
    // This is private for distributing hash function in a way that will
    // pass milestone 1 without change.
    private int h(String s, int m)
    {
        int intLength = s.length() / 4;
        long sum = 0;
        for (int j = 0; j < intLength; j++)
        {
            char[] c = s.substring(j * 4, (j * 4) + 4).toCharArray();
            long mult = 1;
            for (int k = 0; k < c.length; k++)
            {
                sum += c[k] * mult;
                mult *= 256;
            }
        }

        char[] c = s.substring(intLength * 4).toCharArray();
        long mult = 1;
        for (int k = 0; k < c.length; k++)
        {
            sum += c[k] * mult;
            mult *= 256;
        }

        return (int)(Math.abs(sum) % m);
    }
    //----------------------------------------------------------------------
    /**
     * Insert method for either hash table
     * @param name        name of item trying to be inserted
     * @return            return true if inserted, false if not.
     */
    public boolean insert(String name) {
        
        //searches for already existing name in hashtable
        if (check(name) == -1) {
            int temp = probe(name);
            //probing worked, inserting.
            if (temp != -1) {                
                hashTable[temp] = 
                        memMan.insert(name.getBytes(), name.length());
                hashSize++;                
                return true;
            }
            //probing failed
            else {
                return false;
            }
            
        }
        else {
            //value found, duplicate, insertion not possible.
            return false;
        }
               
    }
    //----------------------------------------------------------------------
    /**
     * Checks the hashtable for a specific value
     * @param value            value to search for
     * @return int             if found, index is returned, 
     *                         if not found return -1
     */
    public int check(String value) {
        //starting point of search
        int home = h(value, hashTable.length);
        int tempHome = home;
        
        //search using quadratic probing, return value if found.
        for (int counter = 0; counter < hashTable.length; counter++) {  
            //iterates if on null spot, immediately return -1
            if (hashTable[tempHome] == null) {
                return -1;
            } //if a tombstone is at hashtable[tempHome], keep iterating
            else if (hashTable[tempHome].getStartPos() == -1) {
                tempHome = 
                        ((home + (counter * counter)) % hashTable.length);
            }
            //if not a tombstone, check for equivalent value
            else if (retrieveString(hashTable[tempHome]).equals(value)) {
                return tempHome;
            }
            else {
                //not a tombstone or the equivalent value, so keep going 
                //thru table to exhaust comparisons on every item in 
                //hashtable
                tempHome = 
                        ((home + (counter * counter)) % hashTable.length);
            }
        }    
        //value not in table
        return -1;
    }

    //----------------------------------------------------------------------
    /**
     * resizes the hash table if over 50% full
     * @return boolean true if resize worked
     */
    public boolean resize() {
        
        //checks to see if resizing is necessary
        //...when table is over 50% full
        if (getHashSize() >= (hashTable.length / 2)) {
            //creates a temp table to hold contents of given hash table
            int newArraySize = hashTable.length * 2;
            int tempHashSize = hashSize;
            Handle[] cleanArray = new Handle[hashTable.length];
        
            //erases tombstones from old array and converts them to null
            //in new temp array, and copies all other values to new temp
            //array.
            for (int i = 0; i < cleanArray.length; i++) {
                if (hashTable[i] != null && 
                        hashTable[i].getStartPos() == -1) {
                    cleanArray[i] = null;
                }
                else {
                    cleanArray[i] = hashTable[i];
                }
                hashSize = tempHashSize;
            }       
                
            hashTable = new Handle[newArraySize];            
            //starting at the first index, if the table contains a value
            //copy to newly sized table
            for (int i = 0; i < cleanArray.length; i++) {
                if (cleanArray[i] != null) {
                    int probeSpot = probe(retrieveString(cleanArray[i]));
                    hashTable[probeSpot] = cleanArray[i];
                }
            }
            return true;
        }
        return false;
    }
    //----------------------------------------------------------------------
    /**
     * resizes the string at Handle's location in mem pool
     * @param theHandle     handle to get string from
     * @return String at handle
     */
    public String retrieveString(Handle theHandle) {
        //if not at tombstone slot, attempt to get string
        if (theHandle.getStartPos() != -1) {
            int size = memMan.getSizeOfRecord(theHandle);
            byte[] temp = new byte[size];
            memMan.get(temp, theHandle, size);
            return new String(temp);
        }
        return "";
    }
    //----------------------------------------------------------------------
    /**
     * Probes the desired table for an insertion point
     * @param name            name to find a spot for
     * @return int   the index found by probing that is empty, 
     *               -1 if probe failed
     */
    public int probe(String name) {
        //the starting location to probe, found using hash func
        int home = h(name, hashTable.length);
        int tempHome = home;
        //quadratic probing for loop, looking for insert point starting at
        //index home (found by hashing) and incrementing by counter^2
        for (int counter = 0; counter < hashTable.length; counter++) {
            if (hashTable[tempHome] != 
                    null && hashTable[tempHome].getStartPos() != -1) {
                tempHome =
                        ((home + (counter * counter)) % hashTable.length);
            }
            else
            {
                return tempHome;
            }
        }
        return -1;
    }

    //----------------------------------------------------------------------
    /**
     * Attempts to remove a artist from the hash tables. 
     * @param name    the name of the item to remove
     * @return boolean  true if remove worked
     */
    public boolean remove(String name) {
        int found = check(name);
        
        //if the value was found in the table, stored at 'found'
        if (found != -1) {
            memMan.remove(hashTable[found]);
            hashTable[found] = tombstone;
            hashSize--;
            return true;
        } //value not in table, nothing to remove
        else {
            return false;
        }
        
    }
    //----------------------------------------------------------------------
    /**
     * Prints the current Artists found in order and the total number on
     * record.
     * @return boolean  true if printed
     */
    public boolean print() {
        //starts at beginning of hashtable and prints as iterated thru list
        if (getHashSize() > 0) {
            for (int i = 0; i < this.getHashTableSize(); i++) {
                if (hashTable[i] != null && 
                        hashTable[i] != tombstone) {
                    System.out.println("|" + retrieveString(hashTable[i]) 
                        + "| " + i);
                }
            }
            return true;
        }
        else {
            return false;
        }
    }
    //----------------------------------------------------------------------
    /**
     * Getter method for hashSize
     * @return int        current num of artists in table
     */
    public int getHashSize() {
        int count = 0;
        //returns current number of elements in the hashtable by stepping
        //through the table
        for (int i = 0; i < hashTable.length; i++) {
            if (hashTable[i] != null && 
                    hashTable[i] != tombstone) {
                count++;
            }
        }
        return count;
    }
    //----------------------------------------------------------------------
    /**
     * Getter method for hashTable size
     * @return int        current length of artist table
     */
    public int getHashTableSize() {
        return hashTable.length;
    }
    //---------------------------------------------------------------------
    /**
     * Getter for hash table
     * @return String[]        returns the artist table for accessing
     */
    public Handle[] getHashTable() {
        return hashTable;
    }
    //---------------------------------------------------------------------
    /**
     * Getter for tombstone
     * @return Handle       tombstone object
     */
    public Handle getTombstone() {
        return tombstone;
    }
    //---------------------------------------------------------------------
    /**
     * Getter for memMan
     * @return MemManager
     */
    public MemManager getMemManager() {
        return memMan;
    }
}
