package org.egzi.nn.applications;


import javafx.util.Pair;
import org.egzi.math.DenseVector;
import org.egzi.nn.elements.classic.NeuralLayer;
import org.egzi.nn.elements.classic.Perceptron;
import org.egzi.nn.elements.classic.train.Algorithm;
import org.egzi.nn.elements.classic.train.Backpropagation;
import org.egzi.nn.utils.Function;

import java.util.Scanner;

import static org.egzi.nn.utils.Function.LINEAR;
import static org.egzi.nn.utils.Function.TANH;

public class Classification{

    public void run() throws Exception {
        int inputCount = 2;
        int outputCount = 3;
        int sizeOfLearningSet = 1000;

        Pair<Double[][], Double[][]> seq = learningSeq(sizeOfLearningSet,inputCount, outputCount);

        //count of neurons in the hidden layer
        int hid = 20;

        Perceptron p = new Perceptron( new NeuralLayer[]{
                new NeuralLayer(inputCount),
                new NeuralLayer(hid, TANH),//new NeuralLayer(hid, SIGM);
                new NeuralLayer(outputCount, LINEAR)});

        Algorithm alg = new Backpropagation(p);
        alg.train(seq.getKey(), seq.getValue(), 0.000001d, 1000, 0.001d);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Please enter a probable vector:");
            Double[] data = new Double[inputCount];
            for (int j = 0; j < inputCount; j++)
                data[j] = scanner.nextDouble();
            Double[] res = p.forward(data);
            int max = 0;
            for (int i = 0; i < outputCount; i++)
                if (res[i] > res[max]) max = i;
            System.out.println("Result: " + (max+1));
            System.out.println("Real result: " + getArea(new DenseVector(data)));
        }
    }

    public Pair<Double[][], Double[][]> learningSeq(
            int n,
            int nx,
            int ny) {


        Double[][] inputs = new Double[n][nx];

        Double[][] outputs = new Double[n][ny];

        for (int i = 0; i < n; i++) {
            inputs[i] = getInputs(i, n);
            outputs[i] = getOutputs(i, n);

        }


        return new Pair<Double[][], Double[][]>(inputs, outputs);
    }

    public static final Double radius = 2d;
    public static final Double side = 3d;

    public Integer getArea(DenseVector a) {
        if (a.norm() <= radius)
             return 1;
        if (a.maxCoord() <= side)
             return 2;
        return 3;
    }

    public static void main(String[] args) throws Exception{
        new Classification().run();
    }


    public Double[] getInputs(int i, int n) {
        if (i < n / 3) {
            Double radius = 2 * Math.random();
            Double dig = Math.random();
            return new Double[]{radius * Math.cos(dig), radius * Math.sin(dig)};
        }

        if (i < 2 * n / 3) {
            Double radius = 2d + Math.random();
            Double dig = Math.random();
            return new Double[]{radius * Math.cos(dig), radius * Math.sin(dig)};
        }

        Double radius = Math.random()* 10;
        if (radius < 3) radius = 3d;
        Double dig = Math.random();
        return new Double[]{radius * Math.cos(dig), radius * Math.sin(dig)};
    }

    public Double[] getOutputs(int i, int n) {
        if (i < n / 3) return new Double[]{1d, 0d, 0d};
        if (i < 2 * n / 3) return new Double[]{0d, 1d, 0d};
        return new Double[]{0d,0d,1d};
    }
}
