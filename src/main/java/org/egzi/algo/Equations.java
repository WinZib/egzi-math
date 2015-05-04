package org.egzi.algo;

import org.egzi.algo.funcs.Functions;
import org.egzi.math.DenseVector;

/**
 * Created by WinZib on 4/20/2015.
 */
public enum Equations {
    SIMPLE("y=w*u+e"),
    COMPLEX("y=Ew*u+w*f(n)+e");

    private String name;

    Equations(String name) {
        this.name = name;
    }

    public Double calculate(int i, DenseVector[] u, DenseVector w, Functions functions, Double outputNoize){
        Double result = 0.0;
        switch (this) {
            case SIMPLE:
                return w.dotProduct(u[i]) + outputNoize;
            case COMPLEX:
                for (int j = 0; j < w.getSize() - 1; j++)
                    result += w.at(j)* u[i].at(j);

                result += w.at(w.getSize() - 1) *
                        functions.func().calc(u[i].at(w.getSize() - 1)) + outputNoize;
                return result;
            default:
                return 0.;
        }
    }

    public String toString() {
        return name;
    }
}
