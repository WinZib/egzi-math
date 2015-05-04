package org.egzi.nn.applications.extrapolation;

import javafx.util.Pair;
import org.egzi.nn.elements.classic.NeuralLayer;
import org.egzi.nn.elements.classic.Perceptron;
import org.egzi.nn.elements.classic.train.Algorithm;
import org.egzi.nn.elements.classic.train.Backpropagation;
import org.egzi.nn.ui.EducationalConfiguratorMenuItem;
import org.egzi.nn.utils.Function;
import org.egzi.ui.MainWindow;
import org.egzi.ui.PlotContainer;
import org.egzi.ui.PlotContainerImpl;

import javax.swing.*;

import static org.egzi.nn.utils.Function.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;


public class Extrapolation {
    static PlotContainer plot = new PlotContainerImpl("Result", "X", "Y");

    static void frameCreate() {
        final MainWindow window = new MainWindow();
        window.setPreferredSize(new Dimension(900, 500));
        window.addPlot(plot);
        window.addMenuItem(new EducationalConfiguratorMenuItem());


        JPanel active = new JPanel();
        active.setLayout(new GridLayout(6, 2));

        JLabel inCountLabel = new JLabel("Count of input neurons:");
        active.add(inCountLabel);

        final JTextField inCount = new JTextField("3");
        active.add(inCount);

        JLabel hidCountLabel = new JLabel("Count of neuron:");
        active.add(hidCountLabel);

        final JTextField hidCount = new JTextField("100");
        active.add(hidCount);


        JLabel maxCycleLabel = new JLabel("Max count of cycles:");
        active.add(maxCycleLabel);

        final JTextField maxCycle = new JTextField("10000");
        active.add(maxCycle);


        JLabel epsilonLabel = new JLabel("Epsilon:");
        active.add(epsilonLabel);

        final JTextField epsilon = new JTextField("0.0001");
        active.add(epsilon);

        JButton start = new JButton("Start");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                plot.clear();

                Integer inputNeuron = Integer.valueOf(inCount.getText());
                Integer hiddenNeuron = Integer.valueOf(hidCount.getText());
                Integer maxCycles = Integer.valueOf(maxCycle.getText());
                Double epsilonValue = Double.valueOf(epsilon.getText());

                try {
                    run(inputNeuron, hiddenNeuron, maxCycles, epsilonValue);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(window, exc.getMessage());
                }
            }
        });


        window.add(active);
        active.add(start);
        window.pack();
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        frameCreate();
    }

    public static void run(Integer inputNeurons, Integer sizeOfLearningSet, Integer maxCycles, Double epsilon) throws Exception{

        //function which will be extrapolated
        Function function = SIN;
        //start value for learning
        Double startXValue = 0d;
        //count of input neurons
        int nx = inputNeurons;
        //count of output neurons
        int ny = 1;

        Double h = 0.2d;
        Double offset = 0.2d;
        int n = sizeOfLearningSet;
        Double dxy = 0.2d;

        Pair<Double[][], Double[][]> seq = learningSeq(function, n, startXValue, nx, h, ny, dxy, offset);

        //count of neurons in hidden layer
        int hid = 10;

        //creation of perceptron
        Perceptron p = new Perceptron( new NeuralLayer[]{
                new NeuralLayer(nx),
                new NeuralLayer(hid, TANH),
                //new NeuralLayer(11, SIGM),
                new NeuralLayer(ny, LINEAR)});

        //provide learning
        Algorithm alg = new Backpropagation(p);
        alg.train(seq.getKey(), seq.getValue(), epsilon, maxCycles, 0.001d);   //0.001 - gradient coefficient


        System.out.println("Learning was finished");

        System.out.println("Start generate result");

        List<Double> xs = new ArrayList<Double>();
        for (int i = 0; i < n; i++) xs.add(function.getValue(startXValue + i * h));

        //1000 count of test points
        for (int i = 0; i < 1000; i++) {
            Double[] input = new Double[nx];
            input = xs.subList(xs.size() - nx, xs.size()).toArray(input);
            Double[] out = p.forward(input);
            Collections.addAll(xs, out);
        }

        System.out.println("Calculation was successful. Start to save data in file");

        saveResults(xs, h);

        System.out.println("Data was saved");
    }

    /**
     * Construct learning set
    **/
    public static Pair<Double[][], Double[][]> learningSeq(
            Function f, //function which will be extrapolated - SIN
            int n, //size of learning set - 100
            Double x0, //start point - 0d
            int nx, //count of input neurons
            Double h, //step
            int nj, //count of output neurons
            Double dxy,  //offset between input and output
            Double offset//offset between steps
            ) {

        Double[][] inputs = initInputs(n, nx, x0, offset, h, f);

        Double[][] outputs = initOutputs(n, nj, x0, offset, nx, h, dxy, f);

        return new Pair<Double[][], Double[][]>(inputs, outputs);
    }

    public static Double[][] initInputs(int n, int nx, Double x0, Double offset, Double h, Function f) {
        Double[][] inputs = new Double[n][nx];
        for (int i = 0; i < n; i++) {
            //ix - number of input neuron
            for (int ix = 0; ix < nx; ix++) inputs[i][ix] = f.getValue(x0 + i * offset + ix * h);
        }
        return inputs;
    }

    public static Double[][] initOutputs(int n,
                                  int ny,
                                  Double x0,
                                  Double offset,
                                  int nx,
                                  Double h,
                                  Double dxy,
                                  Function f) {
        Double[][] outputs = new Double[n][ny];
        for (int i = 0; i < n; i++) {
            //j - number of output neuron
            for (int j = 0; j < ny; j++)
                outputs[i][j] = f.getValue(x0 + i * offset + (nx - 1) * h + dxy + j * h);
        }
        return outputs;
    }


    //save result on the plot
    public static void saveResults(List<Double> result, double dx) throws FileNotFoundException {
        for (int i = 0; i < result.size(); i++)
        {
            plot.add(i * dx, result.get(i));
        }
    }
}
