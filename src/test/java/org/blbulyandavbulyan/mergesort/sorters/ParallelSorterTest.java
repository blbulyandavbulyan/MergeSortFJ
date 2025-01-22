package org.blbulyandavbulyan.mergesort.sorters;

import org.blbulyandavbulyan.mergesort.testutils.ArrayGenerator;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

import static org.assertj.core.api.Assertions.assertThat;

class ParallelSorterTest {
    private final ArrayGenerator arrayGenerator = new ArrayGenerator(new Random());

    @RepeatedTest(value = 10)
    void testSortingForEvenSizeSmallArray() {
        int[] actualArray = arrayGenerator.generateRandomArray(10000000);
        long standartSortingTime = System.currentTimeMillis();
        int[] expectedSortedArray = Arrays.stream(actualArray).sorted().toArray();
        standartSortingTime = System.currentTimeMillis() - standartSortingTime;

        long mySortingTime = System.currentTimeMillis();
        ForkJoinPool.commonPool().invoke(new ParallelSorter(actualArray));
        mySortingTime = System.currentTimeMillis() - mySortingTime;

        assertThat(actualArray).isEqualTo(expectedSortedArray);

        assertThat(mySortingTime).isLessThan(standartSortingTime);
    }

    @RepeatedTest(value = 10)
    void testSortingForOddSizeSmallArray() {
        int[] unsortedRandomArray = arrayGenerator.generateRandomArray(7700);
        int[] actualArray = Arrays.copyOf(unsortedRandomArray, unsortedRandomArray.length);
        int[] expectedSortedArray = Arrays.stream(unsortedRandomArray).sorted().toArray();

        ForkJoinPool.commonPool().invoke(new ParallelSorter(actualArray));
        assertThat(actualArray).isEqualTo(expectedSortedArray);
    }

}