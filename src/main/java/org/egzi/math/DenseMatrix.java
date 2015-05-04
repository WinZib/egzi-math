package org.egzi.math;

import org.egzi.nn.utils.Function;

import java.util.Arrays;

public class DenseMatrix {
    private Double[] data;
    public int cols, rows;

    public DenseMatrix(int rows, int cols, Double[] data) {
        assert rows * cols == data.length : "Incorrect input data";
        this.data = data;
        this.cols = cols;
        this.rows = rows;
    }

    public DenseMatrix(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        this.data = new Double[cols * rows];
        Arrays.fill(this.data, 0d);
    }

    public DenseMatrix(int n) {
        this(n, n);
    }

    public int getSize() {
        return data.length;
    }

    public int idx(int i, int j) {
        assert i <= cols && j <= rows : "Incorrect arguments";
        return i * cols + j;
    }

    public Double at(int i, int j) {
        return data[idx(i, j)];
    }

    public void set(int i, int j, double value) {
        data[idx(i, j)] = value;
    }

    private DenseMatrix newSameSize(Double[] data) {
        return new DenseMatrix(rows, cols, data);
    }

    public DenseMatrix map(Function f) {
        Double[] newData = new Double[getSize()];
        for (int i = 0; i < getSize(); i++)
        {
            newData[i] = f.getValue(data[i]);
        }
        return new DenseMatrix(rows, cols, newData);
    }

    public DenseMatrix multiply(double a) {
        Function f = new Function.LINEAR(a);
        return this.map(f);
    }


    public DenseMatrix divide(double a) {
        Function f = new Function.LINEAR(1/a);
        return this.map(f);
    }

    public DenseMatrix multiply(DenseMatrix a) {
        assert this.rows == a.cols && this.cols == a.rows : "Incorrect size of matrix";
        Double[] newData = new Double[this.rows * a.cols];
        for (int r = 0; r < this.rows; r++)
            for (int c = 0; c < a.cols; c++) {
                Double sum = 0d;
                for (int k = 0; k < this.cols; k++){
                    sum += this.at(r, k) * a.at(k, c);
                }
                newData[r * a.cols + c] = sum;
            }
        return new DenseMatrix(this.rows, a.cols, newData);
    }

    public DenseVector multiply(DenseVector a) {
        assert this.cols == a.getSize() : "incorrect matrix multiplication: [$rows x $cols]*[$that.size x 1]";
        Double[] newData = new Double[this.rows];
        //Arrays.fill(newData, 0d);
        for (int i = 0; i < newData.length; i++){
            Double vi = 0d;
            for (int k = 0; k < this.cols; k++)
            {
                vi += this.at(i, k) * a.at(k);//(a.at(k) == null ? 0d : a.at(k));
            }
            newData[i] = vi;
        }
        return new DenseVector(newData);
    }

    public DenseMatrix add(DenseMatrix a) {
        assert this.cols == a.cols && this.rows == a.rows : "Incorrect matrix addition: [$rows x $cols] + [$that.rows x $that.rows]";
        Double[] newData = new Double[getSize()];
        for (int i = 0; i < getSize(); i++) newData[i] = this.data[i] + a.data[i];
        return new DenseMatrix(rows, cols, newData);
    }

    public DenseMatrix substract(DenseMatrix a) {
        assert this.cols == a.cols && this.rows == a.rows : "Incorrect matrix addition: [$rows x $cols] + [$that.rows x $that.rows]";
        Double[] newData = new Double[getSize()];
        for (int i = 0; i < getSize(); i++) newData[i] = this.data[i] - a.data[i];
        return newSameSize(newData);
    }

    public DenseMatrix under(Function f) {

        Double[] newData = new Double[getSize()];
        for (int i = 0; i < getSize(); i++) newData[i] = f.getValue(this.data[i]);
        return newSameSize(newData);
    }

    public DenseMatrix transpose() {
        Double[] newData = new Double[getSize()];
        for (int r = 0; r < cols; r++)
            for (int c = 0; c < rows; c++)
                newData[r*rows + c] = data[c*cols + r];
        return newSameSize(newData);
    }

    public DenseMatrix diag() {
        Double[] newData = new Double[getSize()];
        Arrays.fill(newData, 0d);
        for (int r = 0; r < cols; r++)
            for (int c = 0; c < rows; c++)
                if (r == c) newData[r*rows + c] = data[c*cols + r];
        return newSameSize(newData);
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                s.append(at(r, c)).append(" ");
            }
            s.append("\n");
        }
        return s.deleteCharAt(s.length() - 1).toString();
    }

    public DenseMatrix apply(Function f) {
        for (int i = 0; i < getSize(); i++) data[i] = f.getValue(data[i]);
        return this;
    }

    public DenseMatrix fill(double a) {
        for (int i = 0; i < getSize(); i++) data[i] = a;
        return this;
    }

    public DenseMatrix fill(Function f) {
        for (int i = 0; i < getSize(); i++) data[i] = f.getValue();
        return this;
    }

}

