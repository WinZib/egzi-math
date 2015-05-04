package org.egzi.nn.applications;

import javafx.util.Pair;
import org.egzi.math.DenseMatrix;
import org.egzi.math.DenseVector;
import org.egzi.nn.utils.Function;

import java.util.ArrayList;
import java.util.List;


public class SLAUCalculation {
    public Pair<DenseVector, List<DenseVector>> calculate(DenseMatrix A, DenseVector b, Double gamma, Double eps, Integer maxT) {
        // subject to change...

        //val threshold = (A*b).norm

        Integer n = A.cols; // x size

        DenseVector x = new DenseVector(n).fill(new Function() {
            @Override
            public Double getValue() {
                return Math.random() - .5;
            }

            @Override
            public Double getValue(Double x) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Function getDerivation() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Double getDerivationValue(Double x) {
                throw new UnsupportedOperationException();
            }});

        Double err = eps + 1;
        Double t = 0d;

        List<DenseVector> xs = new ArrayList<DenseVector>();


        while (err > eps && t < maxT) {
            DenseVector dx = dFdX(A, b, x, n);

            System.out.println(x);
            System.out.println(dx);

            System.exit(1);

            x = x.substract(dx.multiply(gamma));

            //if (dx.norm > threshold) g = g/2d

            xs.add(x);
            err = dx.norm();
            t += 1;
            System.out.println(t + ": " + x + " | ");
        }

        return new Pair<DenseVector, List<DenseVector>>(x, xs);
    }

    private DenseVector dFdX(DenseMatrix A, DenseVector b, DenseVector x, Integer n) {
        DenseVector dx = new DenseVector(n);
        for (int k = 0; k < n; k++) {
            Double dFdXk = 0d;
            for (int i = 0; i < n; i++)
                dFdXk = 2 * A.at(i, k) * ((A.multiply(x).at(i) - b.at(i)));
            dx.set(k, dFdXk);
        }
        return dx;
    }

    public static void main(String[] args) {
            DenseMatrix A = new DenseMatrix(3 , 3,
                    new Double[]{2d,  3d,  1d,
                                -1d, -2d,  0d,
                                1d,  2d, -1d});

            DenseVector b = new DenseVector(new Double[]{-1d, 2d, -1d});


            Pair<DenseVector, List<DenseVector>> res = (new SLAUCalculation()).calculate(A, b, 1d, 0.0001d, 1000);

            System.out.println("answer:\nx .get= "+res.getKey());
            System.out.println("prove:\nA*x = "+A.multiply(res.getKey())+"\n  b = "+b);
            System.out.println("prove:\nA*x = "+A.multiply(res.getKey())+"\n  b = "+b);
    }
}


