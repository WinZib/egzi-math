package org.egzi.algo;

import org.egzi.math.DenseVector;
import org.egzi.ui.PlotContainer;

public class Kaczmarz extends IterationMethod {

    public Kaczmarz(PlotContainer plot,
                    Configuration configuration) throws Exception {
        super(plot, configuration);
    }

    protected DenseVector iteration(double y_n, DenseVector u) {
        double temp = config.getGamma() * (y_n - u.dotProduct(w)) / (config.getMu() + u.dotProduct(u));

        return u.multiply(temp);
    }
}
