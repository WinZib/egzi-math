package org.egzi.nn.elements.classic.train;

import javafx.util.Pair;
import org.egzi.math.DenseVector;
import org.egzi.nn.elements.classic.Perceptron;

import java.util.ArrayList;
import java.util.List;

public class Backpropagation implements Algorithm {
    public Perceptron perceptron;
    public double q = 0d;

    public Backpropagation(Perceptron perceptron) {
        this.perceptron = perceptron;
    }



    public Double[] train(Double[][] inputs,//input values for each learning step
                          Double[][] outputs,//output values for each learning step
                          double eps,//expected error value
                          int maxCycles,
                          double q //gradient coefficient
    ) {
        this.q = q;
        int epoch = 1; //number of current epoch - initial value - 1
        double err = 1.0; //current epoch error value - initial value - 1
        List<Double> Fs = new ArrayList<Double>();

        perceptron.initWeights();

        while (epoch <= maxCycles && err >= eps) {
            err = calcStep(inputs, outputs, q);
            Fs.add(err);
            if (epoch % 100 == 0)
                System.out.println("cycle " + epoch + ": err = " + err);

            epoch += 1;
        }

        System.out.println("Final error: " + err);
        Double[] res = new Double[Fs.size()];
        res = Fs.toArray(res);
        return res;
    }

    public double calcStep(Double[][] inputs, Double[][] outputs, double q0) {
        double totalError = 0d;
        for (int i = 0; i < inputs.length; i++) {
            //forward step
            Double[] actualVector = perceptron.forward(inputs[i]);
            //calculate error vector = expected value - actual value
            DenseVector err = new DenseVector(outputs[i]).substract(new DenseVector(actualVector));
            //back step
            perceptron.backward(err.data);
            //increment totalError on length of error vector
            totalError = totalError + err.dotProduct();
        }

        Double F0 = calcFn(0, inputs, outputs);
        Double F1 = calcFn(1, inputs, outputs);
        Double F2 = calcFn(2, inputs, outputs);

        Double eta = (F0 - F1) / (F0 - F1 - F1 + F2);

       /*
        //Double Feta = 0d;

        for (int i = 0; i < inputs.length; i++) {
            Double[] y = perceptron.forward(inputs[i], eta);
            DenseVector err = new DenseVector(outputs[i]).substract(new DenseVector(y));
            //Feta += err.dotProduct();
        }*/

        perceptron.applyWeightsChanges(eta * q);

        return totalError;
    }

    //calculate error with gradient's coefficient multiplied on n
    public Double calcFn(int n, Double[][] inputs, Double[][] outputs) {
        double error = 0d;
        for (int i = 0; i < inputs.length; i++) {
            //forward step with n*q gradient coefficient - n * q
            Double[] actualVector = perceptron.forward(inputs[i], n * q);
            DenseVector err = new DenseVector(outputs[i]).substract(new DenseVector(actualVector));
            error += err.dotProduct();
        }
        return error;
    }

    public Pair<Double, Double[][]> testSet(Double[][] inputs, Double[][] outputs) {
        Double[][] ys = new Double[inputs.length][];
        double err = 0d;
        for (int i = 0; i < inputs.length; i++) {
            ys[i] = perceptron.forward(inputs[i]);

            err += (new DenseVector(outputs[i]).substract(new DenseVector(ys[i]))).norm();
        }
        return new Pair<Double, Double[][]>(err / inputs.length, ys);
    }
}
