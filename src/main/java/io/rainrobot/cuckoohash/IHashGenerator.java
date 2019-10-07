package io.rainrobot.cuckoohash;

public interface IHashGenerator {
    Integer[] getHash(Object val, int maxSize);
}
