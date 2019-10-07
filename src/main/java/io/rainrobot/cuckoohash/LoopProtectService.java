package io.rainrobot.cuckoohash;

public class LoopProtectService {
    private int initialHash;
    public static final int NULL_VALUE = -1;
    
    public LoopProtectService() {
        initialHash = NULL_VALUE;
    }
    
    public void checkIfLoopAndResize(CuckooHash cuckooHash, int currentHash) {
        //if this is the first value's hash mark it to avoid loop
        if(initialHash == NULL_VALUE) initialHash = currentHash;
        else if (initialHash == currentHash) {
            clearInitialHash();
            cuckooHash.reSizeArray();
        }
    }


    public void clearInitialHash() {
        initialHash = NULL_VALUE;
    }

    public int getInitialHash() {
        return initialHash;
    }
}
