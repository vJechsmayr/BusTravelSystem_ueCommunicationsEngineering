package util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Generic XML save and load Helper to use in every Class where it's needed
 *
 * @author Viktoria Jechsmayr
 * @version 1.0
 */
public class XMLHelper<T> {

    private XMLHelper() {
    }

    public static <T> void save(T instance, String filename) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(instance.getClass());
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.marshal(instance, new File(filename));
    }

    @SuppressWarnings("unchecked")
    public static <T> T load(Class<T> typeClass, String filename) throws JAXBException, FileNotFoundException {
        JAXBContext context = JAXBContext.newInstance(typeClass);
        Unmarshaller um = context.createUnmarshaller();
        return (T) um.unmarshal(new FileReader(filename));
    }
}