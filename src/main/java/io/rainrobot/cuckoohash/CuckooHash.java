package io.rainrobot.cuckoohash;

public class CuckooHash <T extends Object>{

    private T[] array;
    private LoopProtectService loopProtect;
    private IHashGenerator hashGenerator;

    public CuckooHash(int initialSize,
                        IHashGenerator hashGenerator,
                        LoopProtectService loopProtect) {
        this.hashGenerator = hashGenerator;
        this.loopProtect = loopProtect;
        this.array = (T[])new Object[initialSize];
    }

    public void put(T val) {
            //generate hashes
        Integer[] hashList = hashGenerator.getHash(val, array.length);
            //check if a key available (resize if in loop)
        if (isKeyAvailable(val, hashList)) return;
        else {
            //replace item at first position
            T replaced = array[hashList[0]];
            array[hashList[0]] = val;
            put(replaced);
        }
    }

    private boolean isKeyAvailable(T val, Integer[] hashList) {
        for(Integer hash : hashList) {
            loopProtect.checkIfLoopAndResize(this, hash);
            if (array[hash] == null || array[hash].equals(val)) {
                array[hash] = val;
                loopProtect.clearInitialHash();
                return true;
            }
        }
        return false;
    }

    public boolean remove(T val) {
        Integer[] hashList = hashGenerator.getHash(val, array.length);
        for(Integer hash : hashList) {
            if (array[hash] == val) {
                array[hash] = null;
                return true;
            }
        }
        return false;
    }

    public boolean exist(T val) {
        Integer[] hashList = hashGenerator.getHash(val, array.length);
        for(Integer hash : hashList) {
            if (array[hash] == val) {
                return true;
            }
        }
        return false;
    }

    public void reSizeArray() {
        //copy current array ref
        int x2size = array.length * 2;
        T[] oldArray = array;

        //create new array from old values
        array = (T[])new Object[x2size];
        for(T val : oldArray) {
            if(val != null) {
                put(val);
            }
        }
    }

    public T get(int key) {
        return array[key];
    }


    public T[] getArray() {
        return array;
    }

}