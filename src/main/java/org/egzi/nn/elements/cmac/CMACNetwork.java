package org.egzi.nn.elements.cmac;

import org.egzi.math.DenseVector;

import java.util.HashMap;

public class CMACNetwork {
    private int r;

    private HashMap<DenseVector, CMACCell> cells = new HashMap<DenseVector, CMACCell>();

    public CMACNetwork(int r) {
        this.r = r;
    }

    //record y to x cell
    public Double addValue(DenseVector x, Double y) {
        //get cmac cell instance
        //if doesn't exist then create it
        CMACCell cell = cells.containsKey(x) ? cells.get(x) : new CMACCell(x);

        //sum of active cells
        Double sum = 0d;
        for (DenseVector vector : cell.nextRO(r)) {
            sum += cells.containsKey(vector) ? cells.get(vector).getValue() : 0d;
        }

        Double err;

        //W[n] = W[n-1] - ...
        cell.setValue(cell.getValue() + (y - sum) / r);

        err = (y - sum) * (y - sum);

        cells.put(cell.getVector(), cell);

        return Math.abs(err/ r);
    }

    public Double getValue(DenseVector vector) {
        Double res = 0d;
        for (DenseVector v : cells.get(vector).nextRO(r)) {
            res += cells.get(v).getValue();
        }
        return res;
    }

    public int size() {
        return cells.size();
    }
}
