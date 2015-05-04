package org.egzi.math;

import java.util.Random;

public class RandomUtil {
    private static Random rand = new Random(System.currentTimeMillis());

    public static long getUniformDistributed(int low, int high) {
        return low + rand.nextInt() % (high - low);
    }

    public static long getNormalDistributed(int expectedValue, int dispersion) {
        return expectedValue + rand.nextInt() % (2 * dispersion) - dispersion;
    }
}
