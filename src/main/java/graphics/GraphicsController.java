package graphics;

import actors.GameObject;
import actors.MovingObject;
import utils.Log;
import utils.XmlHandler;

import java.util.Map;
import java.util.HashMap;

public class GraphicsController {

    private static final String TAG = "GraphicsController";
    private static Map<String, MovingObject> graphicalItems;
    private static GcElements settings = null;


    public static void init(){
        graphicalItems = new HashMap<>();
        settings = XmlHandler.ImportGcElements();
        if(settings == null) { // no import found
            settings = new GcElements(); //default value
        }
        Log.send(Log.type.INFO, TAG, "Initialized successfully.");
    }

    /**
     * Registers a GameObject for rendering
     * @param obj the GameObject to display / use
     * @return success value; log output shows errors.
     */
    public static boolean register(GameObject obj){
        boolean result = false;

        MovingObject mObj = new MovingObject();
        mObj.setHeight(obj.getHeight());
        mObj.setWidth(obj.getWidth());
        mObj.setId(obj.getId());
        mObj.setImgFilename(obj.getImgFilename());
        mObj.setImage(obj.getImage());

        if(obj.getId() == null){
            Log.send(Log.type.ERROR, TAG, "Failed to register object; no ID assigned.");
        } else if(graphicalItems.containsKey(obj.getId())){
            if(graphicalItems.containsValue(obj)){
                Log.send(Log.type.INFO, TAG, "Failed to register " + mObj.getId() +
                        ", object already registered.");
            } else {
                Log.send(Log.type.ERROR, TAG, "Failed to register " + mObj.getId() +
                        ", ID already in use.");
            }
        } else {
            graphicalItems.put(mObj.getId(), mObj);
            Log.send(Log.type.INFO, TAG, "Successfully registered " + mObj.getId());
            result = true;
        }

        return result;
    }

    public static Map<String, MovingObject> getGraphicalItems() {
        return graphicalItems;
    }

    public static MovingObject getGraphicalItemsById(String id) {
        return graphicalItems.get(id);
    }

    /**
     * Moves any moving sprites forward 1 step
     */
    public static void step(){
        Log.send(Log.type.INFO, TAG, "step() triggered");
        Map<String, MovingObject> m = GraphicsController.getGraphicalItems();
        for(Map.Entry<String, MovingObject> entry : m.entrySet()){
            MovingObject obj = entry.getValue();
            obj.step();
        }
    }

    /**
     * Exports all graphical items currently rendering to XML documents.
     */
    public static void exportAll(){
        for(Map.Entry<String, MovingObject> entry : graphicalItems.entrySet()){
            XmlHandler.ObjectToXml(entry.getValue());
        }
    }

    /**
     * Clears the contents of graphicalItems
     */
    public static void clearGraphicalItems(){
        graphicalItems.clear();
        Log.send(Log.type.INFO, TAG, "Graphics items cleared.");
    }

    /** Getters and setters **/
    public static String getWindowTitle() {
        return settings.windowTitle;
    }

    public static void setWindowTitle(String windowTitle) {
        settings.windowTitle = windowTitle;
    }

    public static int getWindowHeight() {
        return settings.windowHeight;
    }

    public static void setWindowHeight(int windowHeight) {
        settings.windowHeight = windowHeight;
    }

    public static int getWindowWidth() {
        return settings.windowWidth;
    }

    public static void setWindowWidth(int windowWidth) {
        settings.windowWidth = windowWidth;
    }
}

