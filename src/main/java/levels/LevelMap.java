package levels;

import utils.Log;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement (name = "LevelMap")
public class LevelMap {

    //global members
    @XmlTransient
    private static final String TAG = "LevelMap";
    private static final String DEFAULT_LEVEL_ID = "defaultId";

    //instance members
    @XmlElement (name = "MapSections", required=true, nillable=true)
    private GameSegment[][] map;
    @XmlElement
    private int maxWidth, maxHeight;
    @XmlElement
    private int atX, atY;
    @XmlElement
    private String levelId;

    public LevelMap(){
        maxWidth = 0;
        maxHeight = 0;
        atX = 0;
        atY = 0;
        map = null;
        levelId = DEFAULT_LEVEL_ID;
    }

    public LevelMap(int maxWidth, int maxHeight, String id){
        this();
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        levelId = id;
        constructSegments();
    }

    //this is public despite lack of setters/getters due to impact of XML stuff
    public void constructSegments(){
        map = new GameSegment[maxWidth][maxHeight];
    }

    public GameSegment getSegment(int x, int y){
        GameSegment out = null;
        if(x >= 0 && y >= 0) {
            if (x < maxWidth && y < maxHeight) {
                out = map[x][y];
                if(out.getId() == DEFAULT_LEVEL_ID){ //if only default
                    out = null;
                }
            }
        }
        return out;
    }

    public boolean putSegment(int x, int y, GameSegment gs){
        boolean result = false;
        if(x >= 0 && y >= 0){
            if(x < maxWidth && y > maxHeight){
                map[x][y] = gs;
                result = true;
            }
        }
        return result;
    }

    //Map movement functions
    public GameSegment move(int horizontal, int vertical){
        if(atX + horizontal >= 0 && atX + horizontal < maxWidth){
            if(atY + vertical >= 0 && atY + vertical < maxHeight){
                atX += horizontal;
                atY += vertical;
            } else {
                Log.send(Log.type.WARNING, TAG, "move failed - vertical movement invalid.");
            }
        } else {
            Log.send(Log.type.WARNING, TAG, "move failed - horizontal movement invalid.");
        }

        GameSegment out = map[atX][atY];

        if(map[atX][atY].getId() == DEFAULT_LEVEL_ID){   //if only default
            out = null;         //return null because no change
            atX -= horizontal;  //reverse movement change
            atY -= vertical;    //reverse movement change
        }

        return out;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    /** Xml load functions **/ /*

    public void loadFromXml(String dir){
        XmlHandler<GameSegment> _x = new XmlHandler<>(GameSegment.class);

        File directory = new File(dir); /* "./Files/Levels" */ /*
        File[] directoryListing = directory.listFiles();
        if(directoryListing != null){
            for (File child : directoryListing){
                if(child.exists() && child.getName().contains(".xml")) {
                    GameSegment childseg = _x.readFromXml(child);
                    map[childseg.getMapX()][childseg.getMapY()] = childseg;
                    Log.send(Log.type.DEBUG, TAG, "Loaded map area: " + childseg.getId());
                }
            }
        }

    }

     public static LevelMap createFromXml(String dir, String id, int x, int y){
        LevelMap l = new LevelMap(x, y, id);
        l.loadFromXml(dir);
        return l;
    } */

}
