package com.tk.wightwhale.graphics;

import com.tk.wightwhale.actors.BackgroundGameObject;
import com.tk.wightwhale.actors.GameObject;
import com.tk.wightwhale.actors.MovingObject;
import com.tk.wightwhale.actors.PlayerControlledObject;
import com.tk.wightwhale.collision.CollisionController;
import com.tk.wightwhale.levels.GameSegment;
import com.tk.wightwhale.levels.LevelMap;
import com.tk.wightwhale.levels.LevelController;
import com.tk.wightwhale.utils.Log;
import com.tk.wightwhale.utils.XmlHandler;

import javax.xml.bind.annotation.*;
import java.util.Map;

/**
 * Singleton class which encapsulates all the graphical display info
 * and executes the update loop tasks
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement (name = "Graphics")
public class GraphicsController {

    // Global members
    /** Debug Tag **/
    @XmlTransient
    private static final String TAG = "GraphicsController";
    /** Active graphicsController singleton **/
    @XmlTransient
    private static GraphicsController activeGraphicsController = null;
    /** Active level map **/
    @XmlTransient
    public static LevelMap activeLevel = null;
    /** Active level controller  **/
    @XmlTransient
    public static LevelController activeLevelController = null;
    /** Active collision controller **/
    @XmlTransient
    public static CollisionController activeCollisionController = null;
    /** XmlHandler to import settings in init() **/
    @XmlTransient
    private static XmlHandler<GcElements> settingsImporter = null;
    /** XmlHandler to import com.tk.wightwhale.levels **/
    @XmlTransient
    private static XmlHandler<LevelMap> levelImporter = null;

    public static GraphicsController GetController(){
        return activeGraphicsController;
    }

    // Instance members
    /** GameSegment being displayed in com.tk.wightwhale.graphics **/
    @XmlElement
    private GameSegment loadedArea;
    /** Window settings **/
    @XmlElement
    private GcElements settings;
    /** Level map for current level **/
    @XmlElement
    private LevelMap levelMap;
    /** Refresh rate in milisecond **/
    @XmlTransient
    private int stepSize;
    /** Game running status - if false, step() does nothing **/
    @XmlTransient
    public boolean gameRunning;

    /**
     * Default constructor
     * You MUST call "init" yourself if you use this
     */
    public GraphicsController(){
        registerActive();
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
        activeCollisionController = CollisionController.getActiveController();
        activeCollisionController.importFromXml("CollisionInfo");
    }

    /**
     * Constructs graphicsController and calls Init(importSettings)
     * @param importSettings boolean, true if you want to import from Files/GC_Settings.xml
     */
    public GraphicsController(boolean importSettings){
        this();
        init(importSettings);
    }

    /**
     * Initializes (or reinitializes) the GraphicsController to the starting
     * GameSegment and Level
     * @param importSettings boolean, true if you want to import from Files/GC_Settings.xml
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
            loadedArea = levelMap.getSegment(0, 0);
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

    /**
     * Loads all images for the current area to memory
     * By default, this will let you specify the size for Static Items,
     * but it will match moving + controlled items to the png size.
     */
    public void loadLevelImages() {

        if (loadedArea == null) { //defaults to first segment if none already in use
            loadedArea = levelMap.getSegment(0, 0);
        }

        if(!loadedArea.getBackgroundItems().isEmpty()){
            Map<String, BackgroundGameObject> b = loadedArea.getBackgroundItems();
            for(Map.Entry<String, BackgroundGameObject> entry : b.entrySet()){
                BackgroundGameObject obj = entry.getValue();
                obj.loadImageFile(false);
            }
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

        if(!loadedArea.getPlayerControlledItems().isEmpty()){
            Map<String, PlayerControlledObject> p = loadedArea.getPlayerControlledItems();
            for(Map.Entry<String, PlayerControlledObject> entry : p.entrySet()){
                PlayerControlledObject obj = entry.getValue();
                obj.loadImageFile(true);
            }
        }

        Log.send(Log.type.INFO, TAG, "Images loaded for area " + loadedArea.getId());
    }

    /**
     * Clears stored com.tk.wightwhale.graphics and removes activeController status
     * if it has it
     */
    public void close(){
        settings = null;
        if(activeGraphicsController == this) {
            activeGraphicsController = null;
        }
        Log.send(Log.type.INFO, TAG, "Destroyed successfully.");
    }

    /**
     * Registers the current GraphicsController in use as active.
     */
    public void registerActive(){
        if(activeGraphicsController != this){
            activeGraphicsController = this;
        } else {
            Log.send(Log.type.WARNING, TAG, "Attempted to re-register active controller.");
        }
    }

    /**
     * De-registers the current GraphicsController in use as active.
     */
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

    /**
     * Registers a moveable object for rendering
     * @param mObj the MovingObject to display / use
     * @return success value; log output shows errors
     */
    public boolean registerMoving(MovingObject mObj){
        return loadedArea.registerMoving(mObj);
    }

    /**
     * Registers a player-controllable object for rendering
     * @param pcObj the PlayerControlledObject to display / use
     * @return success value; log output shows errors
     */
    public boolean registerPlayerControlled(PlayerControlledObject pcObj) {
        return loadedArea.registerPlayerControlled(pcObj);
    }

    //todo: javadoc
    public boolean registerBackground(BackgroundGameObject bgObj){
        return loadedArea.registerBackground(bgObj);
    }

    /**
     * Returns full Map of MovingObjects for current display area
     * @return success value; log output shows errors
     */
    public Map<String, MovingObject> getMovingItems() {
        return loadedArea.getMovingItems();
    }

    /**
     * Returns a specific MovingObject by its ID value
     * Note: this requires the entry tag and the id tag to match in the XMl
     * for an imported level.
     * @param id String ID (unique among MovingObjects)
     * @return success value; log output shows errors
     */
    public MovingObject getMovingItemsById(String id) {
        return loadedArea.getMovingItemsById(id);
    }

    /**
     * Returns full Map of GameObjects for current display area
     * @return success value; log output shows errors
     */
    public Map<String, GameObject> getStaticItems() {
        return loadedArea.getStaticItems();
    }

    /**
     * Returns a specific GameObject by its ID value
     * Note: this requires the entry tag and the id tag to match in the XMl
     * for an imported level.
     * @param id String ID (unique among GameObjects)
     * @return success value; log output shows errors
     */
    public GameObject getStaticItemsById(String id) {
        return loadedArea.getStaticItemsById(id);
    }

    /**
     * Returns full Map of PlayerControlledObjects for current display area
     * @return success value; log output shows errors
     */
    public Map<String, PlayerControlledObject> getPlayerControlledItems() {
        return loadedArea.getPlayerControlledItems();
    }

    /**
     * Returns a specific PlayerControlledObject by its ID value
     * Note: this requires the entry tag and the id tag to match in the XMl
     * for an imported level.
     * @param id String ID (unique among PlayerControlledObjects)
     * @return success value; log output shows errors
     */
    public PlayerControlledObject getPlayerControlledItemById(String id) {
        return loadedArea.getPlayerControlledItemsById(id);
    }

    //todo: javadoc
    public Map<String, BackgroundGameObject> getBackgroundItems(){
        return loadedArea.getBackgroundItems();
    }

    public BackgroundGameObject getBackgroundItemById(String id){
        return loadedArea.getBackgroundItemsById(id);
    }

    public GameSegment getLoadedArea() {
        return loadedArea;
    }

    public void setLoadedArea(GameSegment loadedArea) {
        this.loadedArea = loadedArea;
    }

    /**
     * Unloads current area and loads the specified level and gameSegment
     * @param levelNum level number (in import/addition order)
     * @param mapAreaX First coordinate value for segment
     * @param mapAreaY Second coordinate value for segment
     * @return GameSegment in use if successful, null if unsuccessful
     */
    public GameSegment moveTo(int levelNum, int mapAreaX, int mapAreaY){
        GameSegment gs = null;

        if(activeLevelController == null){  //Checks that LevelController exists
            activeLevelController = LevelController.getActiveController();
            if(activeLevelController == null){  //no active level controller
                Log.send(Log.type.ERROR, TAG, "Cannot move with no levelController created.");
            }
        }

        if(levelNum < activeLevelController.levels.length && levelNum >= 0){    //checks that level number is in range
            activeLevel = activeLevelController.levels[levelNum];
            gs = activeLevel.getSegment(mapAreaX, mapAreaY);
        }

        if(gs != null){ //checks that segment was successfully retrieved

            if(!loadedArea.getBackgroundItems().isEmpty()){
                Map<String, BackgroundGameObject> b = loadedArea.getBackgroundItems();
                for(Map.Entry<String, BackgroundGameObject> entry : b.entrySet()){
                    BackgroundGameObject obj = entry.getValue();
                    obj.unloadImage();
                }
            }

            if(!loadedArea.getStaticItems().isEmpty()){ //checks for static items to unload
                Map<String, GameObject> m = loadedArea.getStaticItems();
                for (Map.Entry<String, GameObject> entry : m.entrySet()) {
                    GameObject obj = entry.getValue();
                    obj.unloadImage();
                }
            }

            if(!loadedArea.getMovingItems().isEmpty()){ //checks for moving items to unload
                Map<String, MovingObject> m = loadedArea.getMovingItems();
                for (Map.Entry<String, MovingObject> entry : m.entrySet()) {
                    MovingObject obj = entry.getValue();
                    obj.unloadImage();
                }
            }

            Log.send(Log.type.INFO, TAG, "Area " + loadedArea.getId() + " unloaded.");

            loadedArea = gs; //changes loaded area to retrieved gameSegment
            loadLevelImages();  //loads images for new level
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
                activeCollisionController.step(obj);
            }
            Map<String, PlayerControlledObject> p = loadedArea.getPlayerControlledItems();
            for(Map.Entry<String, PlayerControlledObject> entry : p.entrySet()) {
                PlayerControlledObject obj = entry.getValue();
                obj.step();
                activeCollisionController.step(obj);
            }
        }
    }

    /**
     * Moves all sprites 1 step forward based on time in game loop
     * Currently not functional
     * @param delta time in miliseconds
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

    /* Getters and setters */
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
     * Gives the number of miliseconds between com.tk.wightwhale.graphics
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

