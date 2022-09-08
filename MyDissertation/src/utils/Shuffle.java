package utils;

import java.util.Arrays;
import java.util.Random;

public class Shuffle {
    private Random random;

    public Shuffle() {
        this.random = new Random();
    }

    public static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public int[] shuffle(int[] array) {
        int length = array.length;
        for ( int i = length; i > 0; i-- ){
            int randInd = random.nextInt(i);
            swap(array, randInd, i - 1);
        }
        return Arrays.copyOf(array,array.length);
    }


}
