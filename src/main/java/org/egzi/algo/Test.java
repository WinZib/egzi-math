package org.egzi.algo;

import org.egzi.algo.funcs.Functions;
import org.egzi.math.DenseVector;
import org.egzi.model.VectorConfig;

import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by WinZib on 4/22/2015.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        Configuration config = new Configuration();
        config.setClearW(new DenseVector(new Double[]{1., 1.}));
        config.setGamma(0.2);
        config.setOutputNoize(GenType.NO_GEN);
        config.setOutputNoizeLowEdge(-1.);
        config.setOutputNoizeUpEdge(1.);
        config.setFuncType(Functions.NO);
        config.setMu(0.);
        config.setEpsilon(0.02);

        VectorConfig vectorConfig1 = new VectorConfig();
        vectorConfig1.setName("v1");
        vectorConfig1.setDistributionType(GenType.GAUSIAN);
        vectorConfig1.setLowEdge(-1.);
        vectorConfig1.setUpEdge(1.);
        vectorConfig1.setInputErrorType(GenType.NO_GEN);


        VectorConfig vectorConfig2 = new VectorConfig();
        vectorConfig2.setName("v1");
        vectorConfig2.setDistributionType(GenType.GAUSIAN);
        vectorConfig2.setLowEdge(-1.);
        vectorConfig2.setUpEdge(1.);
        vectorConfig2.setInputErrorType(GenType.NO_GEN);

        config.setInputVectors(Arrays.asList(vectorConfig1, vectorConfig2));

        Context.getInstance().createMarshaller().marshal(config, new File("D:/nc/config.xml"));

        Configuration config2 = (Configuration)Context.getInstance().createUnmarshaller().unmarshal(new File("D:/nc/config.xml"));

        System.out.println(config.getClearW() + " vs. " + config2.getClearW());
    }

}
