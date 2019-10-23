package graphics;

import actors.GameObject;
import utils.Log;
import utils.XmlHandler;

import java.util.Map;
import java.util.HashMap;


public class GraphicsController {

    private static final String TAG = "GraphicsController";
    private static Map<String, GameObject> graphicalItems;
    private static String windowTitle;


    public static void init(){
        graphicalItems = new HashMap<>();
        windowTitle = "Default Title";
        Log.send(Log.type.INFO, TAG, "Initialized.");
    }

    public static boolean register(GameObject obj){
        boolean result = false;

        if(obj.getId() == null){
            Log.send(Log.type.ERROR, TAG, "Failed to register object; no ID assigned.");
        } else if(graphicalItems.containsKey(obj.getId())){
            if(graphicalItems.containsValue(obj)){
                Log.send(Log.type.INFO, TAG, "Failed to register " + obj.getId() +
                        ", object already registered.");
            } else {
                Log.send(Log.type.ERROR, TAG, "Failed to register " + obj.getId() +
                        ", ID already in use.");
            }
        } else {
            graphicalItems.put(obj.getId(), obj);
            Log.send(Log.type.INFO, TAG, "Successfully registered " + obj.getId());
            result = true;
        }

        //temp
        XmlHandler.ObjectToXml(obj);
        //end temp

        return result;
    }

    public static Map<String, GameObject> getGraphicalItems() {
        return graphicalItems;
    }

    /**
     * Clears the contents of graphicalItems
     */
    public static void clearGraphicalItems(){
        graphicalItems.clear();
        Log.send(Log.type.INFO, TAG, "Graphics items cleared.");
    }

    public static String getWindowTitle() {
        return windowTitle;
    }

    public static void setWindowTitle(String windowTitle) {
        GraphicsController.windowTitle = windowTitle;
    }
}
