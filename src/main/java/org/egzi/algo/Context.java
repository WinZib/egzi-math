package org.egzi.algo;

import org.egzi.model.VectorConfig;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Created by WinZib on 4/26/2015.
 */
public class Context {
    private static Context context;
    private JAXBContext jaxbContext;
    private CsvLoader csvLoader = new CsvLoader();

    private Context() throws Exception{
        jaxbContext = JAXBContext.newInstance(VectorConfig.class, Configuration.class);
    }

    public static Context getInstance() throws Exception {
        if (context == null)
            context = new Context();

        return context;
    }

    public Marshaller createMarshaller() throws Exception{
        return jaxbContext.createMarshaller();
    }

    public Unmarshaller createUnmarshaller() throws Exception {
        return jaxbContext.createUnmarshaller();
    }

    public CsvLoader csvLoader() {
        return csvLoader;
    }


}
