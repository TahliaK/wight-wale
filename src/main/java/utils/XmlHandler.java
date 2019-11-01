package utils;
import actors.GameObject;
import graphics.GcElements;
import graphics.GraphicsController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XmlHandler<T> {
    public static final String TAG = "XmlHandler";
    public static String PUT_DIR = "Generated/";
    public static String FIND_DIR = "Files/";

    private JAXBContext jaxbContext;

    public <T> XmlHandler(Class<T> type){
        try {
            jaxbContext = JAXBContext.newInstance(type);
        } catch (JAXBException e){
            Log.send(Log.type.ERROR, TAG, e.getMessage());
            jaxbContext = null;
        }
    }

    /**
     * Writes any XML-annotated item to a file
     * @param item the item to be written
     * @param dir Sub-directory within "Files/"
     * @param itemId The ID, which is used as a filename
     */
    public <T> void writeToXml(T item, String dir, String itemId) {
        if (jaxbContext != null) {
            try {
                Marshaller marshallerObj = jaxbContext.createMarshaller();
                marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                FileOutputStream fOut = new FileOutputStream(PUT_DIR + dir + "/" + itemId + ".xml");
                marshallerObj.marshal(item, fOut);
            } catch (JAXBException _jEx) {
                Log.send(Log.type.ERROR, TAG, "XML problem: " + _jEx.getMessage()); //Issue with JAXB
            } catch (FileNotFoundException _fNfEx) {
                Log.send(Log.type.ERROR, TAG, "File problem: " + _fNfEx.getMessage()); //File IO issue
            } catch (NullPointerException _npEx) {
                Log.send(Log.type.ERROR, TAG, "Tried to output gameObject with" + //gameObject issue
                        " ID or object = null: " + _npEx.getMessage());
            }
        }
    }

    /**
     * Reads any XML annotated class from document
     * @param dir
     * @param filename
     * @param <T>
     * @return
     */
    public <T> T readFromXml(String dir, String filename){
        T out = null;
        if (jaxbContext != null){
            try{
                File xmlSource = new File(FIND_DIR + "/" + dir + "/" + filename);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                out = (T) jaxbUnmarshaller.unmarshal(xmlSource);
            } catch (JAXBException _ex) {
                Log.send(Log.type.ERROR, TAG, "XML error: " + _ex.getMessage());
            } catch (Exception _ex) {
                Log.send(Log.type.ERROR, TAG, "Couldn't load " + filename +
                        " reason: " + _ex.getMessage());
            }
        }
        return out;
    }
}
