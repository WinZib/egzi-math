package org.egzi.nn.utils;

import static java.lang.Math.*;

public abstract class Function {
    public abstract Double getValue();
    public abstract Double getValue(Double x);
    public abstract Function getDerivation();
    public abstract Double getDerivationValue(Double x);


    static class SIGM extends Function {

        public Double getValue(Double x) {
            return sigmoid(x);
        }

        public Function getDerivation() {
            return dSIGM;
        }

        public Double getValue() {
            throw new UnsupportedOperationException();
        }

        public Double getDerivationValue(Double x) {
            return dSIGM.getValue(x);
        }
    }
    
    public static class LINEAR extends Function {
        Double value;
        
        public LINEAR() {
            this.value = 1d;
        }
        
        public LINEAR(Double value) {
            this.value = value;    
        }


        public Double getValue(Double x) {
            return x * value;
        }


        public Function getDerivation() {
            return CONST.setCoefficient(value);
        }


        public Double getValue() {
            throw new UnsupportedOperationException();
        }


        public Double getDerivationValue(Double x) {
            return value;
        }
    }
    
    static class TANH extends Function {

        public Double getValue(Double x) {
            return tanh(x);
        }


        public Double getValue() {
            throw new UnsupportedOperationException();
        }


        public Function getDerivation() {
            return dTANH;
        }


        public Double getDerivationValue(Double x) {
            return dTANH.getValue(x);
        }
    }
    
    static class dSIGM extends Function {

        public Double getValue(Double x) {
            return dSigmoid(x);
        }


        public Function getDerivation() {
            return dSIGM;
        }


        public Double getValue() {
            throw new UnsupportedOperationException();
        }


        public Double getDerivationValue(Double x) {
            throw new UnsupportedOperationException();
        }
    }
    
    static class CONST extends Function {
        Double value;

        private CONST() {
            this.value = 0d;
        }

        private CONST(Double value) {
            this.value = value;
        }

        public CONST setCoefficient(Double value) {
            return new CONST(value);
        }

        public Double getValue(Double x) {
            return value;
        }

        public Function getDerivation() {
            return CONST.setCoefficient(0d);
        }


        public Double getValue() {
            return value;
        }


        public Double getDerivationValue(Double x) {
            return 0d;
        }
    }
    
    static class dTANH extends Function {

        public Double getValue() {
            throw new UnsupportedOperationException();
        }


        public Double getValue(Double x) {
            return dTanh(x);
        }


        public Function getDerivation() {
            throw new UnsupportedOperationException();
        }


        public Double getDerivationValue(Double x) {
            throw new UnsupportedOperationException();
        }
    }

    static class RANDOM extends Function {

        @Override
        public Double getValue() {
            return Math.random();
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
        }
    }

    static class SIN extends Function {
        @Override
        public Double getValue() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Double getValue(Double x) {
            return sin(x);
        }

        @Override
        public Function getDerivation() {
            return COS;
        }

        @Override
        public Double getDerivationValue(Double x) {
            return cos(x);
        }
    }

    static class COS extends Function {
        @Override
        public Double getValue() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Double getValue(Double x) {
            return cos(x);
        }

        @Override
        public Function getDerivation() {
            return SIN;
        }

        @Override
        public Double getDerivationValue(Double x) {
            return -sin(x);
        }
    }

    public static class FILL extends Function {
        double maxVal;

        public FILL(Double maxVal) {
            this.maxVal = maxVal;
        }

        public void setMaxVal(double maxVal) {
            this.maxVal = maxVal;
        }

        @Override
        public Double getValue() {
            return 2*(0.5 - random()) * maxVal;
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
        }
    }

    public static Function TANH = new TANH();
    public static Function dTANH = new dTANH();

    public static Function SIGM = new SIGM();
    public static Function dSIGM = new dSIGM();

    public static CONST CONST = new CONST(0d);
    public static Function LINEAR = new LINEAR();

    public static Function RANDOM = new RANDOM();

    public static Function SIN = new SIN();
    public static Function COS = new COS();

    public static Function FILL = new FILL(0d);

    public static Double sigmoid(Double x) {
        return 1d/(1d + exp(-x));
    }

    public static Double dSigmoid(Double x) {
        Double y = sigmoid(x);
        return y*(1d - y);
    }

    public Double tanh(Double x) {
        return Math.tanh(x);
    }

    public Double dTanh(Double x) {
        Double y = cosh(x);
        return 1/y/y;
    }


}
