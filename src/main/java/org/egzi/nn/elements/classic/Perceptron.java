package org.egzi.nn.elements.classic;

import java.util.Arrays;

public class Perceptron {
    protected NeuralLayer[] layers;
    protected int size, lastLayerNumber, lastSynapseNumber;
    protected SynapseLayer[] synapses;

    public Perceptron(NeuralLayer[] layers) {
        assert layers.length >= 2 : "incorrect";
        this.layers = layers;

        size = layers.length;
        lastLayerNumber = size - 1;
        lastSynapseNumber = size - 2;

        synapses = new SynapseLayer[size - 1];

        for (int i = 0; i <= lastSynapseNumber; i++)
            synapses[i] = new SynapseLayer(layers[i].size, layers[i + 1].size);
    }

    public void initWeights() {
        for (int i = 0; i <= lastSynapseNumber; i++) synapses[i].init();
    }

    public Double[] forward(Double[] input) {
        layers[0].set(input);
        for (int i = 1; i <= lastLayerNumber; i++) {
            //layers[i-1].mem.get(0) - output of previous layer | 0 - current value
            //calculate net
            layers[i].mem.set(0, (synapses[i - 1].weights.multiply(layers[i - 1].mem.get(0))).
                    add(synapses[i - 1].bs));//bs - смещение
            //activate PSY(net)
            layers[i].activate(0);
        }
        return layers[lastLayerNumber].mem.get(0).data;
    }

    //calculate y with corrected wights on q
    public Double[] forward(Double[] input, double q) {
        layers[0].set(input);
        for (int i = 1; i <= lastLayerNumber; i++) {
            layers[i].mem.set(0, (synapses[i - 1].newWeights(q).multiply(
                    layers[i - 1].mem.get(0))).
                    add(synapses[i - 1].bs(q)));

            layers[i].activate(0);
        }
        return layers[lastLayerNumber].mem.get(0).data;
    }

    public void backward(Double[] err) {
        layers[lastLayerNumber].set(err, 1);

        // error backpropagation
        // multiplication of error on next layer on weight of current layer
        for (int i = lastLayerNumber - 1; i >= 0; i--) {
            //dQ/dy calculation
            layers[i].mem.set(1, layers[i + 1].mem.get(1).multiply(synapses[i].weights));
        }

        // weight deltas
        for (int i = 0; i <= lastSynapseNumber; i++) {
            //ws
            //backErr multiply on net
            synapses[i].dws = synapses[i].dws.add(
                    layers[i + 1].backErr().multiply(
                            layers[i].mem.get(0)) //U - input
            );
            //bs
            synapses[i].dbs = synapses[i].dbs.add(
                    layers[i + 1].backErr().multiply(1d)
            );
        }
    }

    public void applyWeightsChanges(double eta) {
        for (int i = 0; i <= lastSynapseNumber; i++) synapses[i].applyDeltas(eta);
    }

    public String toString() {
        return Arrays.toString(layers);
    }

}
