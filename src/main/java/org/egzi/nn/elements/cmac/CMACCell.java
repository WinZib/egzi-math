package org.egzi.nn.elements.cmac;

import org.egzi.math.DenseVector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class CMACCell {
    private DenseVector vector;
    private Double value = 0d;

    public CMACCell(Double[] vector) {
        this.vector = new DenseVector(vector);
    }

    public CMACCell(DenseVector vector) {
        this.vector = vector;
    }

    public void add(Double value) {
        this.value += value;
    }

    public int hashCode() {
        int result = 0;
        for (Double val : vector.data) {
            result += val.intValue() * 10;
        }
        return result;
    }

    public List<DenseVector> nextRO(int offset) {
        Double[][] vectors = new Double[vector.getSize()][offset];

        //init

        //copy of base vector
        for (int j = 0; j < vector.getSize(); j++) {
            vectors[j][0] = vector.at(j);
        }

        for (int i = 1; i < offset; i++) {
            for (int j = 0; j < vector.getSize(); j++) {
                vectors[j][i] = vectors[j][i-1] + 1;
            }
        }


/*        //sort each layer
        for (int i = 0; i < vector.getSize(); i++) {
            Arrays.sort(vectors[i],
                    new CMACComparator(offset));
        }*/

        //split on vectors
        List<DenseVector> result = new ArrayList<DenseVector>();

        for (int i = 0; i < offset; i++) {
            Double[] data = new Double[vector.getSize()];
            for (int j = 0; j < vector.getSize(); j++) {
                data[j] = vectors[j][i];
            }
            result.add(new DenseVector(data));
        }

        return result;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public DenseVector getVector() {
        return vector;
    }

    private static class CMACComparator implements Comparator<Double> {
        private Integer ro;

        protected CMACComparator(Integer ro) {
            this.ro = ro;
        }

        @Override
        public int compare(Double o1, Double o2) {
            Integer obj1 = o1.intValue() % ro;
            Integer obj2 = o2.intValue() % ro;

            if (obj1 == 0) obj1 = ro;
            if (obj2 == 0) obj2 = ro;

            return obj1.compareTo(obj2);  //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
