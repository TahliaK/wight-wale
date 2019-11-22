package com.tk.wightwhale.levels;

import com.tk.wightwhale.utils.Log;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement (name = "LevelMap")
public class LevelMap implements Comparable<LevelMap> {

    //global members
    /** Debug tag **/
    @XmlTransient
    private static final String TAG = "LevelMap";
    /** Default ID - if ID = this, object is ignored by LevelController **/
    @XmlTransient
    private static final String DEFAULT_LEVEL_ID = "defaultId";

    //instance members
    /** Map 2D array of GameSegments **/
    @XmlElement (name = "MapSections", required=true, nillable=true)
    private GameSegment[][] map;
    /** Maximum map width **/
    @XmlElement
    private int maxWidth;
    /** Maximum map height **/
    @XmlElement
    private int maxHeight;
    /** Current loaded position in X axis **/
    @XmlElement
    private int atX;
    /** Current loaded position in Y axis **/
    @XmlElement
    private int atY;
    /** Level int ID **/
    @XmlElement
    private int levelId;
    @XmlElement
    private String levelName;

    /**
     * Default constructor
     * Sets max width + height as 0, position as 0,0
     * and levelId as the default
     */
    public LevelMap(){
        maxWidth = 0;
        maxHeight = 0;
        atX = 0;
        atY = 0;
        map = null;
        levelId = -1;
        levelName = "";
    }

    /**
     * Custom constructor
     * @param maxWidth maximum number of gameSegments on X axis
     * @param maxHeight maximum number of gameSegments on Y axis
     * @param id level ID
     */
    public LevelMap(int maxWidth, int maxHeight, int id){
        this();
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        levelId = id;
        constructSegments();
    }

    /**
     * This initializes the GameSegment data structure
     * It clears the map when called
     */
    public void constructSegments(){
        map = new GameSegment[maxWidth][maxHeight];
    }

    /**
     * Returns a specific segment at location x and y
     * @param x X axis position of gameSegment
     * @param y Y axis position of gameSegment
     * @return GameSegment or null if invalid location / default ID
     */
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

    public GameSegment getCurrentSegment(){
        return map[atX][atY];
    }

    /**
     * Puts a gameSegment in the specified map location
     * @param x X axis position
     * @param y Y axis position
     * @param gs GameSegment to include
     * @return true if successful, false if x or y invalid
     */
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

    /**
     * Retrieves the Game Segment specified at the horizontal + vertical distance from current
     * @param horizontal number of X-axis segments to move (-/+ for direction)
     * @param vertical number of Y-axis segments to move (-/+ for direction)
     * @return GameSegment found or null if none stored in that location
     */
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

    /**
     * Gets the level name / ID
     * Note that com.tk.wightwhale.levels can only be retrieved by number, not ID, unlike GameObjects
     * @return level ID
     */
    public int getLevelId() {
        return levelId;
    }

    /**
     * Sets the level name / ID
     * @param levelId the ID / string name of the level
     */
    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    @Override
    public int compareTo(LevelMap o) {
        int comparison = 0;

        if(o.levelId > this.levelId){
            comparison = -1;
        } else if (o.levelId < this.levelId){
            comparison = 1;
        }

        return comparison;
    }
}
