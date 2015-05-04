package org.egzi.math;

import org.egzi.algo.GenType;
import org.egzi.nn.utils.Function;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class DenseVector {

    public Double[] data;

    public DenseVector(){}

    public DenseVector(Double[] data) {
        this.data = data;
    }

    public DenseVector(int size) {
        this.data = new Double[size];
        Arrays.fill(this.data, 0d);
    }

    public int getSize() {
        return data.length;
    }

    public Double at(int index) {
        return data[index];
    }

    public void set(int index, double value) {
        data[index] = value;
    }

    public DenseVector map(Function f) {
        Double[] newData = new Double[getSize()];
        for (int i = 0; i < getSize(); i++)
        {
            newData[i] = f.getValue(data[i]);
            if (newData[i] == null) throw new RuntimeException();
        }
        return new DenseVector(newData);
    }


    public DenseVector add(DenseVector vector) {
        assert getSize() == vector.getSize() : "sizes of vectors must be equal ($size != $that.size)";
        Double[] newData = new Double[getSize()];

        for (int i = 0; i < getSize(); i++) newData[i] = this.at(i) + vector.at(i);
        return new DenseVector(newData);
    }

    public Double dotProduct(DenseVector vector) {
        double sum = 0d;
        for (int i = 0; i < getSize(); i++)
            sum += this.at(i) * vector.at(i);
        return sum;
    }

    public DenseVector dotProductSum(DenseVector vector) {
        Double[] newData = new Double[getSize()];
        for (int i = 0; i < getSize(); i++)
            newData[i] = this.at(i) * vector.at(i);
        return new DenseVector(newData);
    }


    public DenseVector substract(DenseVector vector) {
        assert getSize() == vector.getSize() : "sizes of vectors must be equal ($size != $that.size)";
        Double[] newData = new Double[getSize()];
        for (int i = 0; i < getSize(); i++) newData[i] = this.at(i) - vector.at(i);
        return new DenseVector(newData);
    }

    public DenseVector square() {
        Double[] newData = new Double[getSize()];
        for (int i = 0; i < getSize(); i++) newData[i] = this.at(i) * this.at(i);
        return new DenseVector(newData);
    }

    public DenseVector sin() {
        Double[] newData = new Double[getSize()];
        for (int i = 0; i < getSize(); i++) newData[i] = Math.sin(this.at(i));
        return new DenseVector(newData);
    }

    public double dotProduct() {
        double sum = 0d;
        DenseVector square = this.square();
        for (int i = 0; i < getSize(); i++)
            sum += square.at(i);
        return sum;
    }

    public DenseVector negotiate() {
        for (int i = 0; i < getSize(); i++)
            set(i, at(i) * -1);
        return this;
    }

    public static DenseVector parseDenseVector(String src) {
        String[] coordText = src.split(" ");
        Double[] coord = new Double[coordText.length];
        for (int i = 0; i < coordText.length; i++) {
            coord[i] = Double.valueOf(coordText[i]);
        }
        return new DenseVector(coord);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Double aData : data) sb.append(aData).append(" ");

        sb.delete(sb.length() - 1, sb.length());

        return sb.toString();
    }

    public DenseVector multiply(DenseMatrix that) {
        assert this.getSize() == that.rows : "incorrect data";
        Double[] newData = new Double[that.cols];
        for (int i = 0; i < that.cols; i++) {
            double vi = 0d;
            for (int k = 0; k < that.rows; k++) vi += that.at(k, i) * this.at(k);
            newData[i] = vi;
        }
        return new DenseVector(newData);
    }

    public DenseVector multiply(double q) {
        Double[] newData = new Double[getSize()];
        for (int i = 0; i < getSize(); i++) newData[i] = this.at(i) * q;
        return new DenseVector(newData);
    }

    public DenseMatrix toMatrix() {
        return new DenseMatrix(getSize(), 1, data);
    }

    public DenseMatrix toTrMatrix() {
        return new DenseMatrix(1, getSize(), data);
    }

    public DenseMatrix multiply(DenseVector that) {
        return this.toMatrix().multiply(that.toTrMatrix());
    }

    public DenseVector slice(int from, int until) {
        return new DenseVector(Arrays.copyOfRange(data, from, until));
    }

    public double norm() {
        return Math.sqrt(dotProduct());
    }


    public DenseVector join(DenseVector vector) {
        Double[] newData = new Double[this.getSize() + vector.getSize()];
        System.arraycopy(this.data, 0, newData, 0, this.getSize());
        System.arraycopy(vector.data, 0, newData, getSize(), vector.getSize());
        return new DenseVector(newData);
    }

  /* changes vector state */

    public DenseVector fill(double a) {
        for (int i = 0; i < getSize(); i++)
            this.data[i] = a;
        return this;
    }

    public DenseVector fill(Function f) {
        for (int i = 0; i < getSize(); i++)
            this.data[i] = f.getValue();
        return this;
    }

    public DenseVector apply(Function function) {
        for (int i = 0; i < getSize(); i++) data[i] = function.getValue(data[i]);
        return this;
    }

    public static DenseVector newGaussian(int dimension, double median, double dispersion) {
        Random r = new Random(System.currentTimeMillis());
        Double[] result = new Double[dimension];
        for (int i = 0; i < dimension; i++) {
            result[i] = r.nextGaussian() * dispersion + median;
        }
        return new DenseVector(result);
    }

    public static DenseVector newRandom(GenType genType, double median, double dispersion, int dimension) {
        Double[] result = new Double[dimension];
        for (int i = 0; i < dimension; i++) {
            result[i] = genType.generate(median, dispersion);
        }
        return new DenseVector(result);
    }

    public static DenseVector newRandom(int dimension, Double mod) {
        Random r = new Random(System.currentTimeMillis());
        Double[] result = new Double[dimension];
        for (int i = 0; i < dimension; i++) {
            Double val = Math.random();
            result[i] = val * mod;
        }
        return new DenseVector(result);
    }

    public Double maxCoord() {
        return Collections.max(Arrays.asList(data));
    }


    public Double minCoord() {
        return Collections.min(Arrays.asList(data));
    }


    public int hashCode() {
        int result = 0;
        for (Double val : data) {
            result += val.intValue() * 10;
        }
        return result;
    }

    public boolean equals(Object e) {
        if (!(e instanceof DenseVector))
            return false;

        DenseVector o2 = (DenseVector)e;

        if (getSize() != o2.getSize())
            return false;

        for (int i = 0; i < getSize(); i++)
            if (!at(i).equals(o2.at(i))) return false;


        return true;


    }
}
