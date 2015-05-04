package org.egzi.nn.applications.inverse;

import javafx.util.Pair;
import org.egzi.nn.elements.classic.NeuralLayer;
import org.egzi.nn.elements.classic.Perceptron;
import org.egzi.nn.elements.classic.train.Algorithm;
import org.egzi.nn.elements.classic.train.Backpropagation;
import org.egzi.nn.utils.Function;
import org.egzi.ui.MainWindow;
import org.egzi.ui.PlotContainer;
import org.egzi.ui.PlotContainerImpl;

import java.util.ArrayList;
import java.util.List;
public class InverseManagement {
    static PlotContainer YsPlot = new PlotContainerImpl("Ys", "Time", "Value");
    static PlotContainer Y1sPlot = new PlotContainerImpl("Y1s", "Time", "Value");
    static PlotContainer FsPlot = new PlotContainerImpl("Fs", "Time", "Value");
    static PlotContainer F1sPlot = new PlotContainerImpl("F1s", "Time", "Value");

    public static void main(String[] args) throws Exception{

        MainWindow window = new MainWindow();
        window.addPlot(YsPlot);
        window.addPlot(Y1sPlot);
        window.addPlot(FsPlot);
        window.addPlot(F1sPlot);
        window.pack();
        window.setVisible(true);

        //create black box
        BlackBox box = new BlackBox(2d, 0d, 0.1d, 0.02d);

        int countOfOutNeurons = 1;
        int sizeOfLearningSet = 250;
        Pair<Double[][], Double[][]> learningSequence = learningSeq(box, countOfOutNeurons, sizeOfLearningSet);

        Double a0 = 2d;
        Double omega0 = 4d;

        int countOfNeuronsInHiddenLayer = 4;
        Perceptron perceptron = new Perceptron(new NeuralLayer[]{
                new NeuralLayer(3),/*3 - count of input signals in BlackBox*/
                new NeuralLayer(countOfNeuronsInHiddenLayer, Function.TANH),
                new NeuralLayer(1 /*1 - count of output signals in BlackBox*/, Function.LINEAR)                 }
        );

        Algorithm alg = new Backpropagation(perceptron);
        alg.train(learningSequence.getKey(), learningSequence.getValue(), 0.0001/*eps*/, 5000/*maxCycle*/, 0.25/*q*/);

        //generate test sequence
        int t = 500;
        Double[] testYs = testSequence(box, a0, omega0, t);
        List<Double> fs = new ArrayList<Double>();
        List<Double> f1s = new ArrayList<Double>();


        List<Double> ys = new ArrayList<Double>();
        List<Double> y1s = new ArrayList<Double>();


        for (int i = 0; i < t; i++) {
            f1s.add(box.bestF(testYs[i]));
            y1s.add(box.tryY(f1s.get(f1s.size() - 1)));
            Double[] f = perceptron.forward(new Double[]{box.yPrevious(), box.yCurrent(), testYs[i]});
            fs.add(f[0]);
            Double y = box.yNext(f[0]);
            ys.add(y);
        }

        saveResults(testYs, fs, ys, f1s, y1s);

    }

    public static void saveResults(Double[] xs,
                    List<Double> fs,
                    List<Double> ys,
                    List<Double> f1s,
                    List<Double> y1s) throws Exception{
        for (int i = 0; i < xs.length; i++)
        {
            FsPlot.add(i * 0.02d, fs.get(i));
            YsPlot.add(i * 0.02d, ys.get(i));
            F1sPlot.add(i * 0.02d, f1s.get(i));
            Y1sPlot.add(i * 0.02d, y1s.get(i));
        }
    }

    static Pair<Double[][], Double[][]> learningSeq(
            BlackBox box,
            Integer countOfOutNeurons,
            Integer sizeOfLearningSet) {

        box.reset();

        Double a0 = 2.0;
        Double omega0 = 4.0;

        Double[][] inputs = new Double[countOfOutNeurons][];
        Double[][] outputs = new Double[countOfOutNeurons][];

        Double dt = box.dt;

        //form input/output values
        for (int i = 0; i < countOfOutNeurons; i++) {
            Double a = a0;//Math.random()*a0;
            Double omega = omega0;//Math.random()*omega0;

            for (int j = 0; j < sizeOfLearningSet; j++) {
                //input
                Double[] x = new Double[] {box.yPrevious(), box.yCurrent(), box.yNext(yFunction(a, omega, j * dt))};
                //output
                Double[] y = new Double[] {box.forces.get(box.forces.size() - 1)};
                inputs[i] = x;
                outputs[i] = y;
            }
        }

        return new Pair<Double[][], Double[][]>(inputs, outputs);
    }

    //calculate force
    static Double yFunction(Double A, Double W, Double t) {
        return A*Math.cos(W*t);
    }

    //calculate test sequence
    static Double[] testSequence(
            BlackBox box,
            Double a0, Double omega,
            Integer nt) {

        Double[] testInput = new Double[nt];

        for (int i = 0; i < nt; i++) {
            testInput[i] = yFunction(a0, omega, i * box.dt);
        }
        return testInput;
    }
}




