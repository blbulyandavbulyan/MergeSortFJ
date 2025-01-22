package org.blbulyandavbulyan.mergesort.sorters;

import java.util.Arrays;
import java.util.concurrent.RecursiveAction;

public class ParallelSorter extends RecursiveAction {
    private static final int THRESHOLD = 100; //just a random threshold which I have chosen
    private final int[] array;
    private final int left;
    private final int right;

    private ParallelSorter(int[] array, int left, int right) {
        this.array = array;
        this.left = left;
        this.right = right;
    }

    public ParallelSorter(int[] array) {
        this(array, 0, array.length - 1);
    }

    /* helper method to merge two sorted subarrays array[l..m] and array[m+1..r] into array */
    private void merge(int left, int mid, int right) {
        // copy data to temp subarrays to be merged
        int[] leftTempArray = Arrays.copyOfRange(array, left, mid + 1);
        int[] rightTempArray = Arrays.copyOfRange(array, mid + 1, right + 1);

        // initial indexes for left, right, and merged subarrays
        int leftTempIndex = 0, rightTempIndex = 0, mergeIndex = left;

        // merge temp arrays into original
        while (leftTempIndex < mid - left + 1 || rightTempIndex < right - mid) {
            if (leftTempIndex < mid - left + 1 && rightTempIndex < right - mid) {
                if (leftTempArray[leftTempIndex] <= rightTempArray[rightTempIndex]) {
                    array[mergeIndex] = leftTempArray[leftTempIndex];
                    leftTempIndex++;
                } else {
                    array[mergeIndex] = rightTempArray[rightTempIndex];
                    rightTempIndex++;
                }
            } else if (leftTempIndex < mid - left + 1) { // copy any remaining on left side
                array[mergeIndex] = leftTempArray[leftTempIndex];
                leftTempIndex++;
            } else if (rightTempIndex < right - mid) { // copy any remaining on right side
                array[mergeIndex] = rightTempArray[rightTempIndex];
                rightTempIndex++;
            }
            mergeIndex++;
        }
    }

    private int findIndexOfMaximum(int startIndex, int endIndex) {
        int indexOfMaximum = startIndex;
        for (int i = startIndex; i <= endIndex; i++) {
            if (array[i] > array[indexOfMaximum]) {
                indexOfMaximum = i;
            }
        }
        return indexOfMaximum;
    }

    private void swapElements(int firstIndex, int secondIndex) {
        int temp = array[firstIndex];
        array[firstIndex] = array[secondIndex];
        array[secondIndex] = temp;
    }

    private void simpleSort(int startIndex, int endIndex) {
        for (int i = endIndex; i >= startIndex; i--) {
            int indexOfMaximum = findIndexOfMaximum(startIndex, i);
            swapElements(i, indexOfMaximum);
        }
    }

    @Override
    protected void compute() {
        int elementsCount = right - left;
        if (elementsCount > THRESHOLD) {
            int mid = (left + right) / 2; // find the middle point
            ParallelSorter leftPart = new ParallelSorter(array, left, mid);
            ParallelSorter rightPart = new ParallelSorter(array, mid + 1, right);
            invokeAll(leftPart, rightPart);
            merge(left, mid, right); // merge the two sorted halves
        } else {
            //base case, will be other sorting algorithm (probably bubble sort)
            simpleSort(left, right);
        }
    }
}
