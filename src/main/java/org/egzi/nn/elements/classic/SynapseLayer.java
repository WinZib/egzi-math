package org.egzi.nn.elements.classic;

import org.egzi.math.DenseMatrix;
import org.egzi.math.DenseVector;
import org.egzi.nn.utils.Function;

public class SynapseLayer {
    protected int inputs;
    protected int outputs;

    protected DenseMatrix weights = null, dws = null;
    protected DenseVector bs = null, dbs = null;

    public SynapseLayer(int inputs, int outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.weights = new DenseMatrix(inputs, outputs);
        this.dws = new DenseMatrix(inputs, outputs);
        this.bs = new DenseVector(outputs);
        this.dbs = new DenseVector(outputs);
    }

    public DenseMatrix newWeights(double q) {
        return weights.add(dws.multiply(q));
    }

    public DenseVector bs(double q) {
        return bs.add(dbs.multiply(q));
    }

    public void applyDeltas(double eta) {
        //W[n] = W[n-1] + dW * eta
        weights = weights.add(dws.multiply(eta));
        bs = bs.add(dbs.multiply(eta));
        dws.fill(0);
        dbs.fill(0);
    }

    public void init() {
        double maxVal = 1d / (2*inputs);
        Function f = new Function.FILL(maxVal);

        weights = weights.fill(f);
        bs = bs.fill(f);
    }

    public String toString() {
        return "---\n"+ weights.toString()+"\n---\n"+bs.toString()+"\n---";
    }
}
