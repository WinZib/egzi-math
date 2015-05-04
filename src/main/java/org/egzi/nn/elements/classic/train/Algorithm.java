package org.egzi.nn.elements.classic.train;

import javafx.util.Pair;

public interface Algorithm {
    public Double[] train(Double[][] inputs,
                          Double[][] outputs,
                          double eps,
                          int maxCycles,
                          double q);

    public Pair<Double, Double[][]> testSet(Double[][] inputs, Double[][] outputs);
}
