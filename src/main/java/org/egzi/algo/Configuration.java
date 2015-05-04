package org.egzi.algo;

import org.egzi.algo.funcs.Func;
import org.egzi.algo.funcs.Functions;
import org.egzi.math.DenseVector;
import org.egzi.model.VectorConfig;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "config")
public class Configuration {
    @XmlTransient
    DenseVector clearW;

    @XmlTransient
    DenseVector clearY;

    @XmlAttribute(name = "epsilon")
    Double epsilon = 0.02;

    @XmlAttribute(name = "gamma")
    Double gamma = 1.;

    @XmlAttribute(name = "mu")
    Double mu = 0.;

    @XmlAttribute(name = "output-noize-type")
    GenType outputNoize = GenType.NO_GEN;

    @XmlAttribute(name = "output-noize-low")
    Double outputNoizeLowEdge = 0.;

    @XmlAttribute(name = "output-noize-up")
    Double outputNoizeUpEdge = 0.;

    @XmlAttribute(name = "function-type")
    Functions funcType = Functions.NO;

    @XmlElementWrapper(name = "input-vectors")
    @XmlElement(name = "vector")
    List<VectorConfig> inputVectors;

    @XmlAttribute(name = "max-iteration-count")
    Integer maxIterationCount = 10000;

    public DenseVector getClearW() {
        return clearW;
    }

    public void setClearW(DenseVector clearW) {
        this.clearW = clearW;
    }

    public DenseVector getClearY() {
        return clearY;
    }

    public void setClearY(DenseVector clearY) {
        this.clearY = clearY;
    }

    @XmlTransient
    public Double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(Double epsilon) {
        this.epsilon = epsilon;
    }

    @XmlTransient
    public Double getGamma() {
        return gamma;
    }

    public void setGamma(Double gamma) {
        this.gamma = gamma;
    }

    @XmlTransient
    public Double getMu() {
        return mu;
    }

    public void setMu(Double mu) {
        this.mu = mu;
    }

    @XmlTransient
    public GenType getOutputNoize() {
        return outputNoize;
    }

    public void setOutputNoize(GenType outputNoize) {
        this.outputNoize = outputNoize;
    }

    @XmlTransient
    public Double getOutputNoizeLowEdge() {
        return outputNoizeLowEdge;
    }

    public void setOutputNoizeLowEdge(Double outputNoizeLowEdge) {
        this.outputNoizeLowEdge = outputNoizeLowEdge;
    }

    @XmlTransient
    public Double getOutputNoizeUpEdge() {
        return outputNoizeUpEdge;
    }

    @XmlTransient
    public Double getOutputNoizeMedian() {
        return (outputNoizeLowEdge + outputNoizeUpEdge) / 2;
    }

    @XmlTransient
    public Double getOutputNoizeDispersion() {
        return (outputNoizeUpEdge - outputNoizeLowEdge) / 2;
    }

    public void setOutputNoizeUpEdge(Double outputNoizeUpEdge) {
        this.outputNoizeUpEdge = outputNoizeUpEdge;
    }

    @XmlTransient
    public Functions getFuncType() {
        return funcType;
    }

    public Func getFunc() {
        if (funcType == Functions.NO)
            return null;
        return funcType.func();
    }

    public void setFuncType(Functions funcType) {
        this.funcType = funcType;
    }

    @XmlTransient
    public List<VectorConfig> getInputVectors() {
        return inputVectors;
    }

    public void setInputVectors(List<VectorConfig> inputVectors) {
        this.inputVectors = inputVectors;
    }

    @XmlTransient
    public Integer getMaxIterationCount() {
        return maxIterationCount;
    }

    public void setMaxIterationCount(Integer maxIterationCount) {
        this.maxIterationCount = maxIterationCount;
    }

    public int getDimension() {
        return clearW != null ? clearW.getSize() :
                (inputVectors != null && inputVectors.size() > 0) ? inputVectors.get(0).getU().getSize() : clearY.getSize();
    }
}
