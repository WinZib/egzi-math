package org.egzi.nn.applications.offset_on_input;

import javafx.util.Pair;
import org.egzi.nn.elements.classic.NeuralLayer;
import org.egzi.nn.elements.classic.Perceptron;
import org.egzi.nn.elements.classic.train.Algorithm;
import org.egzi.nn.elements.classic.train.Backpropagation;
import org.egzi.ui.MainWindow;
import org.egzi.ui.PlotContainer;
import org.egzi.ui.PlotContainerImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;
import static org.egzi.nn.utils.Function.*;

public class SMTHElse {
    private Double Φ;
    private Double Ω;
    private Double ω;
    private Double A;
    private Double B;
    private Double t0;
    private Double t1;

    static PlotContainer yx = new PlotContainerImpl("YX", "X", "Y");
    static PlotContainer f = new PlotContainerImpl("F", "X", "Y");

    public SMTHElse(Double φ, Double ω, Double ω1, Double a, Double b, Double t0, Double t1) {
        Φ = φ;
        Ω = ω;
        this.ω = ω1;
        A = a;
        B = b;
        this.t0 = t0;
        this.t1 = t1;
    }

    // y'' + ω*ω*y = Φ*cos(Ω*t) -> y = y(A, B, t)
    public Double F0(Double Φ, Double Ω, Double t) {
        return Φ * cos(Ω * t);

    }

    public Double y0(Double Φ, Double Ω, Double ω, Double A, Double B, Double t) {
        if (!Ω.equals(ω)) return A * sin(ω * t + B) + Φ / (ω * ω - Ω * Ω) * cos(Ω * t);
        else return Φ / (2 * Ω) * t * sin(Ω * t);
    }

    //generate input/output vector
    public Pair<Double[], Double[]> generateFY(Double Φ, Double Ω, Double ω, Double A, Double B, Double t0, Double t1, Integer n) {
        Double[] fs = new Double[n];
        Double[] ys = new Double[n];
        Double dt = (t1 - t0) / (n - 1);

        //set values at i - moment
        for (int i = 0; i < n; i++) fs[i] = F0(Φ, Ω, t0 + i * dt);

        for (int i = 0; i < n; i++) ys[i] = y0(Φ, Ω, ω, A, B, t0 + i * dt);
        return new Pair<Double[], Double[]>(fs, ys);
    }

    public Pair<Double[][], Double[][]> generateLearningSeq(Double[] fs, Double[] ys, Integer countOfInputNeurons, Integer countOfOutputNeurons) {
        Integer n = fs.length;
        Integer i0 = (countOfOutputNeurons + 1 > countOfInputNeurons) ? countOfOutputNeurons + 1 : countOfInputNeurons;

        Double[][] uyss = new Double[n - i0][]; //  [f(t-nu+1), ..., f(t), y(t-nu), ..., y(t-1)]
        Double[][] yss = new Double[n - i0][];  //  [y(t)]

        for (int i = 0; i < n - i0; i++) {
            uyss[i] = new Double[countOfInputNeurons + countOfOutputNeurons];
            System.arraycopy(fs, i + i0 - countOfInputNeurons, uyss[i], 0, countOfInputNeurons);
            System.arraycopy(ys, i + i0 - 1 - countOfOutputNeurons, uyss[i], countOfInputNeurons, countOfOutputNeurons);
            yss[i] = new Double[]{ys[i + i0]};
        }
        return new Pair<Double[][], Double[][]>(uyss, yss);
    }

    public List<Object> calcWithNN(
            Integer hiddenNeurons,
            Integer maxCycles,
            Double eta,
            Integer seq,
            Integer nu,
            Integer ny,
            Double eps) {

        //create perceptron
        Perceptron p = new Perceptron(
                new NeuralLayer[]{
                        new NeuralLayer(nu + ny),
                        new NeuralLayer(hiddenNeurons, TANH),
                        new NeuralLayer(1, LINEAR)}
        );

        Algorithm alg = new Backpropagation(p);

        Pair<Double[], Double[]> learningFYs = generateFY(Φ, Ω, ω, A, B, t0, t1, 100);//100 - coount of time intervals
        Pair<Double[][], Double[][]> learningSeqs = generateLearningSeq(learningFYs.getKey(), learningFYs.getValue(), nu, ny);

        Pair<Double[], Double[]> testingFYs = generateFY(Φ, Ω, ω, A, B, t0, t1, 500);//500 - count of time intervals
        Pair<Double[][], Double[][]> testingSeqs = generateLearningSeq(testingFYs.getKey(), testingFYs.getValue(), nu, ny);

        Double[] Fs = alg.train(learningSeqs.getKey(), learningSeqs.getValue(), eps, maxCycles, eta);
        Pair<Double, Double[][]> err = alg.testSet(testingSeqs.getKey(), testingSeqs.getValue());

        List<Object> list = new ArrayList<Object>();
        list.add(err.getKey());
        list.add(Fs);
        list.add(testingFYs.getValue());
        list.add(err.getValue());
        return list;
    }

    public static void work(Double etaVal, Integer seqSize, Integer hidSize, Integer nu, Integer ny) {
        SMTHElse worker = new SMTHElse(
                200d, 20d, 15d,
                2d, 8d,
                0d, 10d);     //φ, ω, ω1, a, b, t0, t1

        System.out.println("Start calculation:(etaVal: " + etaVal + "; seqSize: " + seqSize +
                "; hidSize: " + hidSize + "; nu: " + nu + "; ny: " + ny);
        List<Object> res = worker.calcWithNN(hidSize, 10000/*maxCycles*/, etaVal, seqSize, nu, ny, 0.01/*eps*/);
        Double err = (Double) res.get(0);
        Double[] fs = (Double[]) res.get(1);
        Double[] ys = (Double[]) res.get(2);
        Double[][] zs = (Double[][]) res.get(3);

        worker.saveXYResults(ys, zs);
        worker.saveFResults(fs);

    }

    //start UI
    public static void main(String[] args) throws Exception {
        MainWindow window = new MainWindow();
        window.addPlot(yx);
        window.addPlot(f);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        JPanel panel1 = new JPanel(new FlowLayout());
        panel1.add(new JLabel("Eta:"));
        final JTextField f1 = new JTextField("0.001");
        panel1.add(f1);
        panel.add(panel1);

        JPanel panel2 = new JPanel(new FlowLayout());
        panel2.add(new JLabel("Sequence Size:"));
        final JTextField f2 = new JTextField("100");
        panel2.add(f2);
        panel.add(panel2);

        JPanel panel3 = new JPanel(new FlowLayout());
        panel3.add(new JLabel("Neuron In Hidden Layer:"));
        final JTextField f3 = new JTextField("50");
        panel3.add(f3);
        panel.add(panel3);

        JPanel panel4 = new JPanel(new FlowLayout());
        panel4.add(new JLabel("NU:"));
        final JTextField f4 = new JTextField("3");
        panel4.add(f4);
        panel.add(panel4);

        JPanel panel5 = new JPanel(new FlowLayout());
        panel5.add(new JLabel("NY:"));
        final JTextField f5 = new JTextField("3");
        panel5.add(f5);
        panel.add(panel5);

        JButton start = new JButton("Start");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                work(Double.valueOf(f1.getText()), Integer.valueOf(f2.getText()),
                        Integer.valueOf(f3.getText()), Integer.valueOf(f4.getText()), Integer.valueOf(f5.getText()));
            }
        });
        panel.add(start);

        window.add(panel);
        window.pack();
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void saveXYResults(Double[] ys, Double[][] zs) {
        for (int i = 0; i < zs.length; i++)
            yx.add(ys[i], zs[i][0]);
    }

    public void saveFResults(Double[] fs){
        for (int i = 0; i < fs.length; i++)
            f.add((double)i, fs[i]);
    }
}

//END UI

