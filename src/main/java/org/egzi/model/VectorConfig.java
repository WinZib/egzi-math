package org.egzi.model;

import org.egzi.algo.GenType;
import org.egzi.math.DenseVector;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

public class VectorConfig {
    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "distribution")
    private GenType distributionType = GenType.GAUSIAN;

    @XmlAttribute(name = "low")
    private Double lowEdge = -1.;

    @XmlAttribute(name = "up")
    private Double upEdge = 1.;

    @XmlAttribute(name = "input-error-type")
    private GenType inputErrorType = GenType.NO_GEN;

    @XmlAttribute(name = "input-error-low")
    private Double inputErrorLow = 0.;

    @XmlAttribute(name = "input-error-up")
    private Double inputErrorUp = 0.;

    @XmlElement(name = "u")
    DenseVector u;

    public VectorConfig() {
    }

    public VectorConfig(String name, GenType distributionType, Double lowEdge, Double upEdge, boolean enableInputError,
                        GenType inputErrorType, Double inputErrorLow, Double inputErrorUp) {
        this(name, distributionType, lowEdge, upEdge, enableInputError, inputErrorType, inputErrorLow, inputErrorUp, null);
    }

    public VectorConfig(String name, GenType distributionType, Double lowEdge, Double upEdge, boolean enableInputError,
                        GenType inputErrorType, Double inputErrorLow, Double inputErrorUp, String clearVal) {
        this.name = name;
        this.distributionType = distributionType;
        this.lowEdge = lowEdge;
        this.upEdge = upEdge;
        this.inputErrorType = inputErrorType;
        this.inputErrorLow = inputErrorLow;
        this.inputErrorUp = inputErrorUp;

        if (clearVal != null) {
            String[] dataStr = clearVal.split(" ");
            Double[] data = new Double[dataStr.length];
            for (int i = 0; i < dataStr.length; i++)
                data[i] = Double.parseDouble(dataStr[i]);

            this.u = new DenseVector(data);
        }
    }

    @XmlTransient
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public GenType getDistributionType() {
        return distributionType;
    }

    public void setDistributionType(GenType distributionType) {
        this.distributionType = distributionType;
    }

    @XmlTransient
    public Double getLowEdge() {
        return lowEdge;
    }

    public void setLowEdge(Double lowEdge) {
        this.lowEdge = lowEdge;
    }

    @XmlTransient
    public Double getUpEdge() {
        return upEdge;
    }

    public void setUpEdge(Double upEdge) {
        this.upEdge = upEdge;
    }

    @XmlTransient
    public GenType getInputErrorType() {
        return inputErrorType;
    }

    public void setInputErrorType(GenType inputErrorType) {
        this.inputErrorType = inputErrorType;
    }

    @XmlTransient
    public Double getInputErrorLow() {
        return inputErrorLow;
    }

    public void setInputErrorLow(Double inputErrorLow) {
        this.inputErrorLow = inputErrorLow;
    }

    @XmlTransient
    public Double getInputErrorUp() {
        return inputErrorUp;
    }

    public void setInputErrorUp(Double inputErrorUp) {
        this.inputErrorUp = inputErrorUp;
    }

    @XmlTransient
    public DenseVector getU() {
        return u;
    }

    public void setU(String u) {
        String[] dataStr = u.split(" ");
        Double[] data = new Double[dataStr.length];
        for (int i = 0; i < dataStr.length; i++)
            data[i] = Double.parseDouble(dataStr[i]);

        this.u = new DenseVector(data);
    }

    public void setU(DenseVector u) {
        this.u = u;
    }


}

