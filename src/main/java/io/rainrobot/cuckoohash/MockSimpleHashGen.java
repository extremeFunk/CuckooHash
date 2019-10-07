package io.rainrobot.cuckoohash;

public class MockSimpleHashGen implements IHashGenerator {
    //return implicit value
    public Integer[] getHash(Object val, int maxSize) {
        int hash1 = val.hashCode() % maxSize;
        Integer[] arr = {hash1};
        return arr;
    }
}
