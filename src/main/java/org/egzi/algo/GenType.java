package org.egzi.algo;

import java.util.Random;

public enum GenType {

    NO_GEN,
    GAUSIAN,
    UNIFORM;

    private static Random random = new Random(System.currentTimeMillis());

    public double generate(double median, double dispersion) {
        switch (this) {
            case NO_GEN:
                return 0.;
            case GAUSIAN:
                return dispersion * random.nextGaussian() + median;
            case UNIFORM:
                return median + (random.nextDouble()-0.5) * dispersion;
            default:
                return 0.;
        }
    }
}
