package utils;
import actors.GameObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XmlHandler {
    public static final String TAG = "XmlHandler";
    public static String PUT_DIR = "Generated/";
    public static String FIND_DIR = "Files/";

    public static void ObjectToXml(GameObject gObj) {
        try {
            JAXBContext contextObj = JAXBContext.newInstance(GameObject.class);
            Marshaller marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            FileOutputStream fOut = new FileOutputStream(PUT_DIR + gObj.getId() + ".xml");
            marshallerObj.marshal(gObj, fOut);
            Log.send(Log.type.INFO, TAG, "Exported object id=" + gObj.getId());
        } catch (JAXBException _jEx) {
            Log.send(Log.type.ERROR, TAG, "XML problem: " + _jEx.getMessage()); //Issue with JAXB
        } catch (FileNotFoundException _fNfEx) {
            Log.send(Log.type.ERROR, TAG, "File problem: " +  _fNfEx.getMessage()); //File IO issue
        } catch (NullPointerException _npEx) {
            Log.send(Log.type.ERROR, TAG, "Tried to output gameObject with" + //gameObject issue
                    " ID or object = null: " + _npEx.getMessage());
        }
    }

    public static GameObject XmlToObject(String filename) {
        GameObject out = null;
        try{
            File xmlSource = new File(FIND_DIR + filename);
            JAXBContext jaxbContext = JAXBContext.newInstance(GameObject.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            out = (GameObject) jaxbUnmarshaller.unmarshal(xmlSource);

        } catch (JAXBException _ex) {
            Log.send(Log.type.ERROR, TAG, "XML error: " + _ex.getMessage());
        } catch (Exception _ex) {
            Log.send(Log.type.ERROR, TAG, "Couldn't load " + filename +
                    " reason: " + _ex.getMessage());
        }

        return out;
    }
}
