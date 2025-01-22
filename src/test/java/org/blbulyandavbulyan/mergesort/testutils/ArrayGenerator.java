package org.blbulyandavbulyan.mergesort.testutils;

import java.util.Random;

public class ArrayGenerator {
    private final Random random;

    public ArrayGenerator(Random random) {
        this.random = random;
    }

    public int[] generateRandomArray(int size) {
        return random.ints().limit(size).toArray();
    }
}
