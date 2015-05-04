package org.egzi.algo;

import org.egzi.algo.funcs.Func;
import org.egzi.math.DenseVector;
import org.egzi.model.VectorConfig;
import org.egzi.ui.PlotContainer;

import java.util.Random;

public abstract class IterationMethod {
    protected PlotContainer plot;
    protected Configuration config;

    //Calculated value
    protected double y_n;
    protected DenseVector w;
    protected DenseVector[] u;
    private volatile boolean isInterrupt = false;

    private int step = 0;

    private Random random = new Random(System.currentTimeMillis());

    public IterationMethod(PlotContainer plot, Configuration configuration) {
        this.plot = plot;
        this.config = configuration;
    }

    protected DenseVector generateU(VectorConfig vectorConfig, Func func) {
        double median = (vectorConfig.getLowEdge() + vectorConfig.getUpEdge()) / 2;
        double dispersion = (vectorConfig.getUpEdge() - vectorConfig.getLowEdge()) / 2;

        double errorMedian = (vectorConfig.getInputErrorLow() + vectorConfig.getInputErrorUp()) / 2;
        double errorDispersion = (vectorConfig.getInputErrorUp() - vectorConfig.getInputErrorLow()) / 2;

        DenseVector u = vectorConfig.getU() != null ? vectorConfig.getU().add(
                DenseVector.newRandom(vectorConfig.getInputErrorType(), errorMedian, errorDispersion, config.getDimension()))
            :   DenseVector.newRandom(vectorConfig.getDistributionType(), median, dispersion, config.getDimension()).add(
                DenseVector.newRandom(vectorConfig.getInputErrorType(), errorMedian, errorDispersion, config.getDimension())
        );

        if (func != null) {
            double un = func.calc(u.at(u.getSize() - 1));
            u.set(u.getSize() - 1, un);
        }

        return u;
    }

    private void prepare() {
        plot.clear();
        //first value
        w = DenseVector.newRandom(config.getDimension(), 100d);

        u = new DenseVector[config.getInputVectors().size()];
        for (int i = 0; i < config.getInputVectors().size(); i++)
            u[i] = generateU(config.getInputVectors().get(i), config.getFunc());

        putLog(step);
    }

    public void bulkCalculate() {
        prepare();

        DenseVector[] w_n = new DenseVector[u.length];

        while (compareWithError(config.getClearW(), w, config.getEpsilon())) {
            if (isInterrupt)
                break;

            for (int i = 0; i < config.getInputVectors().size(); i++)
                u[i] = generateU(config.getInputVectors().get(i), config.getFunc());

            for (int i = 0; i < u.length; i++) {
                //generate outputNoize
                Double outputNoize = config.getOutputNoize().generate(
                        config.getOutputNoizeMedian(),
                        config.getOutputNoizeDispersion());

                Double y_n = config.getClearY() != null ? (config.getClearY().at(i) + outputNoize) :
                        config.getClearW().dotProduct(u[i]) + outputNoize;

                w_n[i] = iteration(y_n, u[i]);
            }

            w = w.add(DenseVector.average(w_n));

            putLog(++step);

            if (step % 10000 == 0) {
                System.out.println("---------------------------------------------");
                System.out.println("Iteration: " + step);
                System.out.println("Current value: " + w);
                System.out.println("Current Epsilon: " + calcError(config.getClearW(), w));
            }

            if (step > config.getMaxIterationCount())
                break;
        }
        System.out.println("------------------------------------------------------");
        System.out.println("--------------------FINAL RESULT----------------------");
        System.out.println("------------------------------------------------------");
        System.out.println(w);
        System.out.println("Epsilon: " + calcError(config.getClearW(), w));
    }

    public void calculate() {
        prepare();

        while (compareWithError(config.getClearW(), w, config.getEpsilon())) {
            if (isInterrupt)
                break;

            for (int i = 0; i < config.getInputVectors().size(); i++)
                u[i] = generateU(config.getInputVectors().get(i), config.getFunc());

            int i = step % w.getSize();

            //generate outputNoize
            Double outputNoize = config.getOutputNoize().generate(config.getOutputNoizeMedian(), config.getOutputNoizeDispersion());

            y_n = config.getClearY() != null ? (config.getClearY().at(i) + outputNoize) :
                    config.getClearW().dotProduct(u[i]) + outputNoize;

            w = w.add(iteration(y_n, u[i]));

            putLog(++step);

            if (step % 10000 == 0) {
                System.out.println("---------------------------------------------");
                System.out.println("Iteration: " + step);
                System.out.println("Current value: " + w);
                System.out.println("Current Epsilon: " + calcError(config.getClearW(), w));
            }

            if (step > config.getMaxIterationCount())
                break;
        }
        System.out.println("------------------------------------------------------");
        System.out.println("--------------------FINAL RESULT----------------------");
        System.out.println("------------------------------------------------------");
        System.out.println(w);
        System.out.println("Epsilon: " + calcError(config.getClearW(), w));
    }



    protected boolean compareWithError(DenseVector w_, DenseVector w, double error) {
        return calcError(w_, w) > error;
    }

    protected double calcError(DenseVector w_, DenseVector w) {
        if (w_ != null) return w_.substract(w).norm();

        Double result = 0.;

        for (int i = 0; i < u.length; i++) {
            result += (u[i].dotProduct(w) - config.getClearY().at(i)) * (u[i].dotProduct(w) - config.getClearY().at(i));
        }
        return Math.sqrt(result / config.getClearY().getSize());
    }

    protected void putLog(int step) {
        if (config.getClearW() != null) {
            plot.add((double)step, config.getClearW().substract(w).norm());
            return;
        }

        Double result = 0.;

        for (int i = 0; i < u.length; i++) {
            result += (u[i].dotProduct(w) - config.getClearY().at(i)) * (u[i].dotProduct(w) - config.getClearY().at(i));
        }
        plot.add((double) step, Math.sqrt(result / config.getClearY().getSize()));
    }


    public void interrupt() {
        isInterrupt = true;
    }

    public boolean isInterrupt() {
        return isInterrupt;
    }

    abstract DenseVector iteration(double y_n, DenseVector u);
}
