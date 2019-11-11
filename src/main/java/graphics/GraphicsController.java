package graphics;

import actors.GameObject;
import actors.MovingObject;
import levels.GameSegment;
import levels.LevelMap;
import levels.LevelController;
import utils.Log;
import utils.XmlHandler;

import javax.xml.bind.annotation.*;
import java.util.Map;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement (name = "Graphics")
public class GraphicsController {

    // Global members
    @XmlTransient
    private static final String TAG = "GraphicsController";
    public static GraphicsController activeGraphicsController = null;
    public static LevelMap activeLevel = null;
    public static LevelController activeLevelController = null;
    private static XmlHandler<GcElements> settingsImporter = null;
    private static XmlHandler<LevelMap> levelImporter = null;

    // Instance members
    @XmlElement
    private GameSegment loadedArea;
    @XmlElement
    private GcElements settings;
    @XmlElement
    private LevelMap levelMap;
    @XmlTransient
    private int stepSize;
    @XmlTransient
    public boolean gameRunning;

    public GraphicsController(){
        stepSize = 1; //default time step, shouldn't really ever be used...
        gameRunning = true;
        settings = null;
        try{
            loadedArea = activeLevel.getSegment(0, 0);
        } catch (NullPointerException _ex){
            loadedArea = new GameSegment();
        }

        if(settingsImporter == null)
            settingsImporter = new XmlHandler<>(GcElements.class);

        if(levelImporter == null)
            levelImporter = new XmlHandler<>(LevelMap.class);

        //levelMap = LevelMap.activeController;
        //This may not be necessary but init here anyway:
        activeLevelController = LevelController.getActiveController();

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

        Log.send(Log.type.DEBUG, TAG, "Init for item: " + this.toString());

        if(importSettings) {
            importSettings();
            Log.send(Log.type.INFO, TAG, "Settings imported.");
        } else {
            if (settings == null){
                settings = new GcElements();
                Log.send(Log.type.INFO, TAG, "No import settings found, using defaults.");
            }
        }
        stepSize = 1000 / settings.fps; //fps into millisecond delay

        if(levelMap == null){
            if (activeLevel != null){
                levelMap = activeLevel; //take existing global variables
                loadLevelImages();
            } else {
                Log.send(Log.type.WARNING, TAG, "GraphicsController init() called without levelController assigned.");
            }
        } else {
            loadedArea = levelMap.getSegment(0, 0); //edit map segment here
            loadLevelImages();
            Log.send(Log.type.INFO, TAG, "Game segment loaded: " + loadedArea.getId());
        }

        Log.send(Log.type.INFO, TAG, "Initialized successfully.");
    }

    private void importSettings(){
        settings = settingsImporter.readFromXml("", "GC_Settings.xml");
        if (settings == null) { // no import found
            settings = new GcElements(); //default value
        }
    }

    public void loadLevelImages() {

        if (loadedArea == null) { //defaults to first segment if none already in use
            loadedArea = levelMap.getSegment(0, 0);
        }

        if (!loadedArea.getStaticItems().isEmpty()) {
            Map<String, GameObject> g = loadedArea.getStaticItems();
            for (Map.Entry<String, GameObject> entry : g.entrySet()) {
                GameObject obj = entry.getValue();
                obj.loadImageFile(false);
            }
        }


        if (!loadedArea.getMovingItems().isEmpty()) {
            Map<String, MovingObject> m = loadedArea.getMovingItems();
            for (Map.Entry<String, MovingObject> entry : m.entrySet()) {
                MovingObject obj = entry.getValue();
                obj.loadImageFile(true);
            }
        }

        Log.send(Log.type.INFO, TAG, "Images loaded for area " + loadedArea.getId());
    }

    /**
     * Clears stored graphics and removes activeController status
     * if it has it
     */
    public void close(){
        settings = null;
        if(activeGraphicsController == this) {
            activeGraphicsController = null;
        }
        Log.send(Log.type.INFO, TAG, "Destroyed successfully.");
    }

    public void registerActive(){
        if(activeGraphicsController != this){
            activeGraphicsController = this;
        } else {
            Log.send(Log.type.WARNING, TAG, "Attempted to re-register active controller.");
        }
    }

    public void deregisterActive(){
        if(activeGraphicsController == this){
            activeGraphicsController = null;
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

    /**
     *
     * @param levelNum
     * @param mapAreaX
     * @param mapAreaY
     * @return
     */
    public GameSegment moveTo(int levelNum, int mapAreaX, int mapAreaY){
        GameSegment gs = null;

        if(activeLevelController == null){
            activeLevelController = LevelController.getActiveController();
            if(activeLevelController == null){  //no active level controller
                Log.send(Log.type.ERROR, TAG, "Cannot move with no levelController created.");
            }
        }

        if(levelNum < activeLevelController.levels.length && levelNum >= 0){
            activeLevel = activeLevelController.levels[levelNum];
            gs = activeLevel.getSegment(mapAreaX, mapAreaY);
        }

        if(gs != null){

            if(!loadedArea.getStaticItems().isEmpty()){
                Map<String, GameObject> m = loadedArea.getStaticItems();
                for (Map.Entry<String, GameObject> entry : m.entrySet()) {
                    GameObject obj = entry.getValue();
                    obj.unloadImage();
                }
            }

            if(!loadedArea.getMovingItems().isEmpty()){
                Map<String, MovingObject> m = loadedArea.getMovingItems();
                for (Map.Entry<String, MovingObject> entry : m.entrySet()) {
                    MovingObject obj = entry.getValue();
                    obj.unloadImage();
                }
            }

            Log.send(Log.type.INFO, TAG, "Area " + loadedArea.getId() + " unloaded.");

            loadedArea = gs;
            loadLevelImages();
        }

        return gs;
    }

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

    public LevelMap getLevelMap() {
        return levelMap;
    }

    public void setLevelMap(LevelMap levelMap) {
        this.levelMap = levelMap;
    }
}

