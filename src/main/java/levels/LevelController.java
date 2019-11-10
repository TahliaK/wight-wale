package levels;

import utils.Log;
import utils.XmlHandler;

import javax.xml.bind.annotation.*;
import java.io.File;

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

    public static LevelController getActiveController(){
        if(activeController == null){
            Log.send(Log.type.INFO, TAG, "LevelController created.");
            activeController = new LevelController();
        }

        return activeController;
    }

    public static void setActiveController(LevelController lc){
        Log.send(Log.type.INFO, TAG, "Manually set LevelController.");
        activeController = lc;
    }

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
                }
            }
        }
        return lc;
    }

    public LevelController(){
        constructLevelList(defaultSize);
    }

    public LevelController(int size){
        constructLevelList(size);
    }

    public void constructLevelList(int size){
        levelCount = size;
        levels = new LevelMap[levelCount];
    }

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

    public void loadFromXML(File xmlFile, int position){
        XmlHandler<LevelMap> _x = new XmlHandler<>(LevelMap.class);
        LevelMap lm = null;

        if(xmlFile.exists() && xmlFile.getName().contains(".xml")){
            if(position >= 0 && position <= levels.length) {
                lm = _x.readFromXml(xmlFile);
                levels[position] = lm;
            } else {
                Log.send(Log.type.WARNING, TAG, "Invalid level location for loaded file " + xmlFile.getPath());
            }
        }
    }

}
