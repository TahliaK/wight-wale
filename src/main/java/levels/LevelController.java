package levels;

import utils.Log;
import utils.XmlHandler;

import java.io.File;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement (name = "Levels")
public class LevelController {

    //global members
    @XmlTransient
    private static final String TAG = "LevelController";
    public static LevelController activeController = null;
    @XmlElement
    public static GameSegment currentLevel = null;
    //public static MapNode activeMap = null;

    //instance members
    @XmlElement (name = "GameSegmentCollection", required=true, nillable=true)
    //@XmlJavaTypeAdapter(GameSegmentAdaptor.class)
    private GameSegment[][] map = null;
    @XmlElement
    private int maxWidth, maxHeight = 0;
    @XmlElement
    private int atX, atY = 0;

    public LevelController(){
        if(activeController == null){
            activeController = this;
        }
    }

    public LevelController(int maxWidth, int maxHeight){
        this();
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
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
                if(out.getId() == "defaultId"){ //if only default
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

        if(map[atX][atY].getId() == "defaultId"){   //if only default
            out = null;         //return null because no change
            atX -= horizontal;  //reverse movement change
            atY -= vertical;    //reverse movement change
        }

        return out;
    }

    //Xml load functions
    public void loadFromXml(String dir){
        XmlHandler<GameSegment> _x = new XmlHandler<>(GameSegment.class);

        File directory = new File(dir); /* "./Files/Levels" */
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

    public static LevelController createFromXml(String dir, int x, int y){
        LevelController l = new LevelController(x, y);
        l.loadFromXml(dir);
        return l;
    }

}
