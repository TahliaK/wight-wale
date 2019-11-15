package levels;

import utils.Log;
import utils.XmlHandler;

import javax.xml.bind.annotation.*;
import java.io.File;

/**
 * Singleton which manages the information associated
 * with each level and GameSegment for display
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "LevelCollection")
public class LevelController {

    //global members
    @XmlTransient
    private static final String TAG = "LevelMap";
    private static final int defaultSize = 20;
    public static LevelController activeController = null;

    //instance members
    @XmlElement
    private int levelCount;
    @XmlElement (name = "Levels", required=false)
    public LevelMap[] levels;

    /**
     * Returns the active LevelController
     * @return LevelController or null if none are active
     */
    public static LevelController getActiveController(){
        if(activeController == null){
            activeController = new LevelController();
            Log.send(Log.type.INFO, TAG, "LevelController created: " + activeController.toString());
        }

        return activeController;
    }

    /**
     * Sets the active LevelController
     * @param lc    LevelController to use as active
     */
    public static void setActiveController(LevelController lc){
        Log.send(Log.type.INFO, TAG, "Set levelController: " + lc.toString());
        activeController = lc;
    }

    /**
     * Constructs a LevelController from the XML contents of the specified directory
     * Directory must contain no visible files except for LevelMap XML documents
     * @param dir   directory to use for levels
     * @param numLevels number of levels to load
     * @return  the LevelController specified in the XML document
     */
    public static LevelController createFromXml(String dir, int numLevels){  /* dir = "./Files/Levels" */
        LevelController lc = getActiveController();
        lc.constructLevelList(numLevels);
        File directory = new File(dir);
        File[] directoryListing = directory.listFiles();
        int i = 0;
        if(directoryListing != null){
            for (File child : directoryListing){
                if(!child.isHidden()) {
                    lc.loadFromXML(child, i);
                    i++;
                    Log.send(Log.type.INFO, TAG, "Level loaded: " + child.getPath());
                }
            }
        }

        setActiveController(lc);
        return lc;
    }

    /**
     * Default constructor
     * Sets levelCount to 20
     */
    public LevelController(){
        constructLevelList(defaultSize);
    }

    /**
     * Custom constructor with specified size
     * @param size number of levels to load
     */
    public LevelController(int size){
        constructLevelList(size);
    }

    /**
     * Sets levelCount and re-initializes level list
     * @param size number of levels
     */
    public void constructLevelList(int size){
        levelCount = size;
        levels = new LevelMap[levelCount];
    }

    /**
     * Returns the number of levels / level array size
     */
    public int getLevelCount() {
        return levelCount;
    }

    /**
     * Sets the level count variable.
     * @warning This DOES NOT change the underlying array size
     * @param levelCount
     */
    public void setLevelCount(int levelCount) {
        this.levelCount = levelCount;
    }

    /**
     * Returns the Array of levels
     * @return LevelMap array
     */
    public LevelMap[] getLevels() {
        return levels;
    }

    /**
     * Returns a specified level
     * @param index number (by load order)
     * @return LevelMap or null if index is invalid
     */
    public LevelMap getLevel(int index){
        LevelMap lm = null;
        if(index < levels.length && index >= 0){
            lm = levels[index];
        }
        return lm;
    }

    /**
     * Sets the levelMap in use
     * @param levels
     */
    public void setLevels(LevelMap[] levels) {
        this.levels = levels;
    }

    /**
     * Loads a singular LevelMap XML exported file
     * @param xmlFile levelMap.xml file
     * @param position  position in levels array (= resulting index)
     */
    public void loadFromXML(File xmlFile, int position){
        XmlHandler<LevelMap> _x = new XmlHandler<>(LevelMap.class);
        LevelMap lm = null;

        if(xmlFile.exists() && xmlFile.getName().contains(".xml")){
            if(position >= 0 && position <= levels.length) {
                lm = _x.readFromXml(xmlFile);
                if(lm != null) {
                    levels[position] = lm;
                    Log.send(Log.type.DEBUG, TAG, "Loaded levelMap: " + lm.getLevelId());
                } else {
                    Log.send(Log.type.ERROR, TAG, "Xml loading failed [check class annotation]");
                }
            } else {
                Log.send(Log.type.WARNING, TAG, "Invalid level location for loaded file " + xmlFile.getPath());
            }
        }
    }

    /**
     * Exports a given LevelController's full content to XML
     * @param l LevelController item to export
     */
    public static void exportToXml(LevelController l){
        XmlHandler<LevelMap> _x = new XmlHandler<>(LevelMap.class);

        for(int i = 0; i < l.levels.length; i++){
            _x.writeToXml(l.levels[i], "", l.levels[i].getLevelId());
        }
    }

}
