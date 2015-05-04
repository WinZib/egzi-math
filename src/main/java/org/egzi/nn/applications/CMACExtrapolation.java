package org.egzi.nn.applications;

import org.egzi.math.DenseVector;
import org.egzi.nn.elements.cmac.CMACNetwork;

import java.util.Scanner;

public class CMACExtrapolation {
    public static Double func(DenseVector v) {
        if (v.getSize() != 3)
            throw new IllegalArgumentException();
        return v.at(0)*v.at(0) + v.at(1) * v.at(1) + v.at(2)*v.at(2);
    }


    public static void main(String[] args) {
        int r = 8;
        int maxCycles = 1000;
        int currentCycle = 0;

        CMACNetwork network = new CMACNetwork(r);

        Double epsilon = 0.1;   //expected error
        Double error = 1.0;     //current error

        while (error > epsilon && currentCycle < maxCycles) {
            error = 0.0;
            //learning in the cube 10x10x10
            for (int i = 0; i < 10 + r; i++)
                for (int j = 0; j < 10 + r; j++)
                    for (int k = 0; k < 10 + r; k++) {
                        DenseVector d = new DenseVector(new Double[]{(double)i, (double)j, (double)k});
                        Double err = network.addValue(d, func(d));

                        //only for cells in the cube
                        if (i < 10 && j < 10 && k < 10)
                            error += err;
                    }
            if (currentCycle % 100 == 0)
                System.out.println("Current cycle " + currentCycle);
            currentCycle++;
        }

        System.out.println("Please insert a 3-dimension vector: ");

        while (true) {
            Scanner scanner = new Scanner(System.in);
            DenseVector vector = new DenseVector(new Double[]{scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble()});
            System.out.println(network.getValue(vector));
        }
    }
}
