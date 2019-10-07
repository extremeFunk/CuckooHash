package io.rainrobot.cuckoohash;

public class FourUniqueHashGenerator implements IHashGenerator {

    public Integer[] getHash(Object val, int maxSize) {
        //offset = 0-3
        int numOfVariations = 4;
        Integer[] hashList = new Integer[numOfVariations];

        int firstPosition = (val.hashCode() % maxSize);
        for (int i = 0; i < numOfVariations; i++) {
            int sectionSize = (Math.floorDiv(maxSize, numOfVariations));
            hashList[i] = (firstPosition + i + (i * sectionSize)) % maxSize;
        }
        return hashList;
    }

    //1st         2nd         3rd         4th position
    //- - - - | - - - - | - - - - | - - - - |
    //X           X           X           X
}
