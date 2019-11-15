package levels;

//For loading from XML
import actors.GameObject;
import actors.MovingObject;
import actors.PlayerControlledObject;
import utils.Log;

import javax.xml.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents 1 visible map "area" on
 * a static screen
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class GameSegment {
    @XmlTransient
    private static final String TAG = "GameSegment";

    @XmlElement
    private String id;
    @XmlElement
    private int mapX, mapY = 0;
    @XmlElement
    private Map<String, PlayerControlledObject> playerControlledItems;
    @XmlElement
    private Map<String, MovingObject> movingItems;
    @XmlElement
    private Map<String, GameObject> staticItems;

    public GameSegment(){
        id = "defaultId";
        playerControlledItems = new HashMap<>();
        movingItems = new HashMap<>();
        staticItems = new HashMap<>();
    }

    /**
     * Returns specified GameObject / StaticItem
     * @param id    id of gameObject
     * @return      GameObject or null if error (logs errors)
     */
    public GameObject getStaticItemsById(String id) {
        return staticItems.get(id);
    }

    /**
     * Returns specified MovingObject / MovingItem
     * @param id    id of MovingObject
     * @return      MovingObject or null if error (logs errors)
     */
    public MovingObject getMovingItemsById(String id) {
        return movingItems.get(id);
    }

    /**
     * Returns specified PlayerControlledObject / Items
     * @param id    id of PlayerControlledObject
     * @return      PlayerControlledObject, or null if error (logs errors)
     */
    public PlayerControlledObject getPlayerControlledItemsById(String id) {
        return playerControlledItems.get(id);
    }

    /**
     * Registers a static object to this specific level
     * @param obj   the GameObject to be registered
     * @return      true if success, log + false if failure
     */
    public boolean registerStatic(GameObject obj){
        boolean result = false;

        if(obj.getId() == null){
            Log.send(Log.type.ERROR, TAG, "Failed to register object; no ID assigned.");
        } else if(movingItems.containsKey(obj.getId())){
            if(movingItems.containsValue(obj)){
                Log.send(Log.type.WARNING, TAG, "Failed to register " + obj.getId() +
                        ", this object is already registered.");
            } else {
                Log.send(Log.type.ERROR, TAG, "Failed to register " + obj.getId() +
                        ", ID already in use.");
            }
        } else {
            staticItems.put(obj.getId(), obj);
            Log.send(Log.type.INFO, TAG, "Successfully registered " + obj.getId());
            result = true;
        }
        return result;
    }

    /**
     * Registers a moving object to this specific level
     * @param mObj   the MovingObject to be registered
     * @return      true if success, log + false if failure
     */
    public boolean registerMoving(MovingObject mObj){
        boolean result = false;

        if(mObj.getId() == null){
            Log.send(Log.type.ERROR, TAG, "Failed to register object; no ID assigned.");
        } else if(movingItems.containsKey(mObj.getId())){
            if(movingItems.containsValue(mObj)){
                Log.send(Log.type.WARNING, TAG, "Failed to register " + mObj.getId() +
                        ", this object is already registered.");
            } else {
                Log.send(Log.type.ERROR, TAG, "Failed to register " + mObj.getId() +
                        ", ID already in use.");
            }
        } else {
            movingItems.put(mObj.getId(), mObj);
            Log.send(Log.type.INFO, TAG, "Successfully registered " + mObj.getId());
            result = true;
        }
        return result;
    }

    /**
     * Registers a Player-Controlled object to this specific level
     * @param pcObj   the PlayerControlledObject to be registered
     * @return      true if success, log + false if failure
     */
    public boolean registerPlayerControlled(PlayerControlledObject pcObj){
        boolean result = false;

        if(pcObj.getId() == null){
            Log.send(Log.type.ERROR, TAG, "Failed to register object; no ID assigned.");
        } else if(playerControlledItems.containsKey(pcObj.getId())){
            if(playerControlledItems.containsValue(pcObj)){
                Log.send(Log.type.WARNING, TAG, "Failed to register " + pcObj.getId() +
                        ", this object is already registered.");
            } else {
                Log.send(Log.type.ERROR, TAG, "Failed to register " + pcObj.getId() +
                        ", ID already in use.");
            }
        } else {
            playerControlledItems.put(pcObj.getId(), pcObj);
            Log.send(Log.type.INFO, TAG, "Successfully registered " + pcObj.getId());
            result = true;
        }
        return result;
    }

    /**
     * Gets all moving items
     * @return Map of MovingObjects
     */
    public Map<String, MovingObject> getMovingItems() {
        return movingItems;
    }

    /**
     * Gets all static / non-moving items
     * @return Map of GameObjects
     */
    public Map<String, GameObject> getStaticItems() {
        return staticItems;
    }

    /**
     * Gets all Player-controlled items
     * @return Map of PlayerControlledObject
     */
    public Map<String, PlayerControlledObject> getPlayerControlledItems() {
        return playerControlledItems;
    }

    public void setStaticItems(Map<String, GameObject> staticItems) {
        this.staticItems = staticItems;
    }

    public void setMovingItems(Map<String, MovingObject> movingItems){
        this.movingItems = movingItems;
    }

    public void setPlayerControlledItems(Map<String, PlayerControlledObject> playerControlledItems) {
        this.playerControlledItems = playerControlledItems;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMapX() {
        return mapX;
    }

    public void setMapX(int mapX) {
        this.mapX = mapX;
    }

    public int getMapY() {
        return mapY;
    }

    public void setMapY(int mapY) {
        this.mapY = mapY;
    }
}
