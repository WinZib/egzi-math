package org.egzi.algo;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.egzi.math.DenseVector;
import org.egzi.model.VectorConfig;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by WinZib on 4/26/2015.
 */
public class CsvLoader {
    public CsvLoader() {}

    public Configuration load(File file) throws Exception {
        Configuration config = new Configuration();
        Reader in = new FileReader(file);
        CSVParser parser = new CSVParser(in, CSVFormat.EXCEL.withHeader().withDelimiter(';'));

        List<VectorConfig> inputVectors = new ArrayList<>();
        List<Double> yVector = new ArrayList<>();

        for (CSVRecord record : parser) {
            List<Double> w = new ArrayList<>();
            for (String key : parser.getHeaderMap().keySet()) {
                if (key.startsWith("X"))
                    w.add(Double.parseDouble(record.get(key)));
            }
            String y = record.get("Y");

            Double[] u = w.toArray(new Double[w.size()]);
            VectorConfig vc = new VectorConfig();
            vc.setU(new DenseVector(u));
            inputVectors.add(vc);

            yVector.add(Double.parseDouble(y));
        }
        config.setClearY(new DenseVector(yVector.toArray(new Double[yVector.size()])));
        config.setInputVectors(inputVectors);

        return config;
    }
}
