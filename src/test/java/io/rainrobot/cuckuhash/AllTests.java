package io.rainrobot.cuckuhash;

import io.rainrobot.cuckoohash.CuckooHash;
import io.rainrobot.cuckoohash.FourUniqueHashGenerator;
import io.rainrobot.cuckoohash.LoopProtectService;
import io.rainrobot.cuckoohash.MockSimpleHashGen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AllTests {

    @Test
    public void testForHashGeneratingFunction() throws Exception {
        FourUniqueHashGenerator hashGenerator = new FourUniqueHashGenerator();
        int val = 0;
        int maxSize = 16;
        Integer[] hash = hashGenerator.getHash(val, maxSize);
        //0XXX X5XX XX10X XXX15
        Object[] expected = {0, 4+1, 8+2, 12+3};

        Assertions.assertArrayEquals(expected, hash);
    }

    @Test
    public void testForHashGeneratingBounds() throws Exception {
        FourUniqueHashGenerator hashGenerator = new FourUniqueHashGenerator();
        Integer[] hash = hashGenerator.getHash(1, 16);
        //last position 1+12+3 is 16%16 = 0
        Object[] expected = {1, 1+4+1, 1+8+2, 0};

        Assertions.assertArrayEquals(expected, hash);
    }

    @Test
    public void testWhenLoopAccuresArrayResize() throws Exception {
        //using mock hash to generate implicit value
        //lock service should detect loop and
        //double arrays size

        //when
        int initialSize = 4;
        Integer[] input = {0,1,2,3,4};
        CuckooHash cHash = new CuckooHash(initialSize,
                            new MockSimpleHashGen(),
                            new LoopProtectService());
        //do
        for(int i : input) {
            cHash.put(i);
        }
        //expect
        Object[] expected = {0, 1, 2, 3, 4, null, null, null};
        Assertions.assertArrayEquals(expected, cHash.getArray());
    }

    @Test
    public void testUniqueHashWithSmallInitialSize() throws Exception {
        //using 5 unique hash -> 5 is out of array bound
        //after mod4 and he should end at pos. 1

        //with
        int initialSize = 4;
        Integer[] input = {0,2,4};
        CuckooHash cHash = new CuckooHash(initialSize,
                new FourUniqueHashGenerator(),
                new LoopProtectService());
        //do
        for(int i : input) {
            cHash.put(i);
        }

        //0XXX
        //0X2X
        //4         1*i+i i=0
        //  4       2+1
        //4         3+2
        //  4       4+3
        //4X20XXXX  2*i+i
        //          0 is replaced by 4 and the array double to avoid infinite loop

        //expect
        Object[] expected = {4, null, 2, 0, null, null,null,null};
        Assertions.assertArrayEquals(expected, cHash.getArray());
    }


    @Test
    public void testForSameKeySeekingBehaviour() throws Exception {
        //in an array of 16, mod16=0 values should form
        //this pattern

        //with
        int initialSize = 16;
        CuckooHash cHash = new CuckooHash(initialSize,
                new FourUniqueHashGenerator(),
                new LoopProtectService());
        Integer[] input = {0,16,32,48};

        //do
        for(int i : input) {
            cHash.put(i);
        }

        //expect
        //hashes of 0: 0, 5, 10, 15
        //values     : 0, 16,32, 48
        Object[] array = cHash.getArray();

        Assertions.assertEquals(array[0], 0);
        Assertions.assertEquals(array[5], 16);
        Assertions.assertEquals(array[10], 32);
        Assertions.assertEquals(array[15], 48);
    }

}
