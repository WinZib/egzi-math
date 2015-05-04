package org.egzi.algo;

import org.egzi.ui.PlotContainer;

public class Kaczmarz extends IterationMethod {

    public Kaczmarz(PlotContainer plot,
                    Configuration configuration) throws Exception {
        super(plot, configuration);
    }

    protected void iteration(double y_n, int i) {
        double temp = config.getGamma() * (y_n - u[i].dotProduct(w)) / (config.getMu() + u[i].dotProduct(u[i]));

        w = w.add(u[i].multiply(temp));
    }
}
