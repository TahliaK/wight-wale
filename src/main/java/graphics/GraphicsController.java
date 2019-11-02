package graphics;

import actors.GameObject;
import actors.MovingObject;
import levels.GameSegment;
import utils.Log;
import utils.XmlHandler;

import javax.xml.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement (name = "Graphics")
public class GraphicsController {

    // Global members
    private static final String TAG = "GraphicsController";
    public static GraphicsController activeController = null;
    private static XmlHandler<GcElements> settingsImporter = null;

    // Instance members
    //@XmlElement
    //private Map<String, MovingObject> movingItems;
    //@XmlElement
    //private Map<String, GameObject> staticItems;
    @XmlElement
    private GameSegment loadedArea;
    @XmlElement
    private GcElements settings;
    @XmlTransient
    private int stepSize;
    @XmlTransient
    public boolean gameRunning;

    public GraphicsController(){
        stepSize = 1; //default time step, shouldn't really ever be used...
        gameRunning = true;
        settings = null;
        //movingItems = new HashMap<>();
        //staticItems = new HashMap<>();
        loadedArea = new GameSegment();

        if(settingsImporter == null)
            settingsImporter = new XmlHandler<GcElements>(new GcElements().getClass());

        init(false);
    }

    public GraphicsController(boolean importSettings){
        stepSize = 1;
        gameRunning = true;
        settings = null;

        if(settingsImporter == null)
            settingsImporter = new XmlHandler<GcElements>(new GcElements().getClass());

        init(importSettings);
    }

    /**
     * This initialises the class and, if found, imports settings from Files/GraphicsController.xml
     */
    public void init(boolean importSettings){
        if(importSettings) {
            importSettings();
        } else if (settings == null){
            settings = new GcElements();
        }
        stepSize = 1000 / settings.fps; //fps into milsecond delay


        if(!loadedArea.getStaticItems().isEmpty()){
            Map<String, GameObject> g = loadedArea.getStaticItems();
            for (Map.Entry<String, GameObject> entry : g.entrySet()) {
                GameObject obj = entry.getValue();
                obj.loadImageFile(false);
            }
        }

        if(!loadedArea.getMovingItems().isEmpty()){
            Map<String, MovingObject> m = loadedArea.getMovingItems();
            for (Map.Entry<String, MovingObject> entry : m.entrySet()) {
                MovingObject obj = entry.getValue();
                obj.loadImageFile(true);
            }
        }

        Log.send(Log.type.INFO, TAG, "Initialized successfully.");
    }

    private void importSettings(){
        settings = settingsImporter.readFromXml("", "GC_Settings.xml");
        if (settings == null) { // no import found
            settings = new GcElements(); //default value
        }
    }

    /**
     * Clears stored graphics and removes activeController status
     * if it has it
     */
    public void close(){
        settings = null;
        if(activeController == this) {
            activeController = null;
        }
        Log.send(Log.type.INFO, TAG, "Destroyed successfully.");
    }

    public void registerActive(){
        if(activeController != this){
            activeController = this;
        } else {
            Log.send(Log.type.WARNING, TAG, "Attempted to re-register active controller.");
        }
    }

    public void deregisterActive(){
        if(activeController == this){
            activeController = null;
        } else {
            Log.send(Log.type.WARNING, TAG, "Attempted to de-register inactive controller.");
        }
    }

    /**
     * Registers a GameObject for rendering
     * @param obj the GameObject to display / use
     * @return success value; log output shows errors.
     */
    public boolean registerStatic(GameObject obj){
        return loadedArea.registerStatic(obj);
    }

    public boolean registerMoving(MovingObject mObj){
        return loadedArea.registerMoving(mObj);
    }

    public Map<String, MovingObject> getMovingItems() {
        return loadedArea.getMovingItems();
    }

    public MovingObject getMovingItemsById(String id) {
        return loadedArea.getMovingItemsById(id);
    }


    public Map<String, GameObject> getStaticItems() {
        return loadedArea.getStaticItems();
    }

    public GameObject getStaticItemsById(String id) { return loadedArea.getStaticItemsById(id); }
/*
    public void setStaticItems(Map<String, GameObject> staticItems) {
        this.staticItems = staticItems;
    }

    public void setMovingItems(Map<String, MovingObject> movingItems){
        this.movingItems = movingItems;
    } */

    /**
     * Moves all sprites 1 step forward
     */
    public void step() {
        if(gameRunning) {
            Map<String, MovingObject> m = loadedArea.getMovingItems();
            for (Map.Entry<String, MovingObject> entry : m.entrySet()) {
                MovingObject obj = entry.getValue();
                obj.step();
            }
        }
    }

    /**
     * Moves all sprites 1 step forward based on time in game loop
     */
    public void step(double delta) {
        Map<String, MovingObject> m = loadedArea.getMovingItems();
        for(Map.Entry<String, MovingObject> entry : m.entrySet()){
            MovingObject obj = entry.getValue();
            //adjusts distance of step
            double xAxis = obj.getdX() * delta;
            double yAxis = obj.getdY() * delta;
            obj.setdX((int)xAxis);
            obj.setdY((int)yAxis);
            //end
            obj.step();
        }
    }

    /**
     * Exports all graphical items currently rendering to XML documents.
     * In "Generated/..." directory.
     */
    public void exportAll(){
        for(Map.Entry<String, MovingObject> entry : loadedArea.getMovingItems().entrySet()){
            //XmlHandler.ObjectToXml(entry.getValue());
        }
    }

    /** Getters and setters **/
    public String getWindowTitle() {
        return settings.windowTitle;
    }

    public void setWindowTitle(String windowTitle) {
        settings.windowTitle = windowTitle;
    }

    public int getWindowHeight() {
        return settings.windowHeight;
    }

    public void setWindowHeight(int windowHeight) {
        settings.windowHeight = windowHeight;
    }

    public int getWindowWidth() {
        return settings.windowWidth;
    }

    public void setWindowWidth(int windowWidth) {
        settings.windowWidth = windowWidth;
    }

    public GcElements getSettings() {
        return settings;
    }

    public void setSettings(GcElements settings) {
        this.settings = settings;
    }

    /**
     * Gives the number of miliseconds between graphics
     * processing loops.
     * @return 1000 / fps = miliseconds wait time
     */
    public int getStepSize() {
        return stepSize;
    }

    public void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }
}

