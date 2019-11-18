package com.tk.wightwhale.levels;

import com.tk.wightwhale.utils.Log;
import com.tk.wightwhale.utils.XmlHandler;

import javax.xml.bind.annotation.*;
import java.io.File;
import java.util.Arrays;

/**
 * Singleton which manages the information associated
 * with each level and GameSegment for display
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "LevelCollection")
public class LevelController {

    //global members
    /** Debug tag **/
    @XmlTransient
    private static final String TAG = "LevelMap";
    /** Default level array size **/
    @XmlTransient
    private static final int defaultSize = 20;
    /** Active levelController singleton **/
    @XmlTransient
    public static LevelController activeController = null;

    //instance members
    /** Number of com.tk.wightwhale.levels in array **/
    @XmlElement
    private int levelCount;
    /** Level storage array **/
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
     * @param dir   directory to use for com.tk.wightwhale.levels
     * @param numLevels number of com.tk.wightwhale.levels to load
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
        Arrays.sort(lc.levels);
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
     * @param size number of com.tk.wightwhale.levels to load
     */
    public LevelController(int size){
        constructLevelList(size);
    }

    /**
     * Sets levelCount and re-initializes level list
     * @param size number of com.tk.wightwhale.levels
     */
    public void constructLevelList(int size){
        levelCount = size;
        levels = new LevelMap[levelCount];
    }

    /**
     * Returns the number of com.tk.wightwhale.levels / level array size
     * @return int number of com.tk.wightwhale.levels
     */
    public int getLevelCount() {
        return levelCount;
    }

    /**
     * Sets the level count variable.
     * This DOES NOT change the underlying array size
     * @param levelCount number of com.tk.wightwhale.levels / array size target
     */
    public void setLevelCount(int levelCount) {
        this.levelCount = levelCount;
    }

    /**
     * Returns the Array of com.tk.wightwhale.levels
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
     * @param levels LevelMap Array to use
     */
    public void setLevels(LevelMap[] levels) {
        this.levels = levels;
    }

    /**
     * Loads a singular LevelMap XML exported file
     * @param xmlFile levelMap.xml file
     * @param position  position in com.tk.wightwhale.levels array (= resulting index)
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
            _x.writeToXml(l.levels[i], "", "ex_" + l.levels[i].getLevelName());
        }
    }

}
