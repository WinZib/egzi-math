package org.egzi.nn.elements.classic;

import org.egzi.math.DenseVector;
import org.egzi.nn.utils.Function;

import java.util.ArrayList;
import java.util.List;

public class NeuralLayer {
    private static final int memSize = 2;

    protected int size;
    private Function f;
    protected List<DenseVector> mem = new ArrayList<DenseVector>(memSize);

    public NeuralLayer(int size) {
        this(size, Function.LINEAR);
    }

    public NeuralLayer(int size, Function f) {
        this.size = size;
        this.f = f;

        for (int i = 0; i < memSize; i++) mem.add(new DenseVector(size));
    }


    public void set(Double[] data) {
        set(data, 0);
    }

    public void set(Double[] data, int level) {
        assert data.length == size : "Wrong data size";
        mem.set(level, new DenseVector(data));
    }

    public void activate(int level) {
        activate(level, false);
    }

    public void activate(int level, boolean isDerivation) {
        mem.set(level, mem.get(level).map(isDerivation ? f.getDerivation() : f));
    }

    public DenseVector backErr() {
        //dF(net).dotProduct(
        return mem.get(0).map(f.getDerivation()). // PSY'(net)
                dotProductSum(mem.get(1));        // dQ/dy
    }

     public String toString() {
         StringBuilder sb = new StringBuilder();
         for (int i = 0; i < memSize; i++) sb = sb.append(mem.get(i).toString()).append(" | ");
         return sb.toString();
     }
}

