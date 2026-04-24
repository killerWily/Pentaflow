package equipo5.pentaflow.util;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import java.io.File;
import java.io.StringReader;

public class ValidadorEsquema {
    public static boolean validar(String xml, String xsdPath) {
        try {
            SchemaFactory f = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema s = f.newSchema(new File(xsdPath));
            Validator v = s.newValidator();
            v.validate(new StreamSource(new StringReader(xml)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}