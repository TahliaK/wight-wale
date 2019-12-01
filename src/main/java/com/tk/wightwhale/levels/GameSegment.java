package com.tk.wightwhale.levels;

//For loading from XML
import com.tk.wightwhale.actors.BackgroundGameObject;
import com.tk.wightwhale.actors.GameObject;
import com.tk.wightwhale.actors.MovingObject;
import com.tk.wightwhale.actors.PlayerControlledObject;
import com.tk.wightwhale.utils.Log;

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
    /** Debug tag **/
    @XmlTransient
    private static final String TAG = "GameSegment";
    @XmlTransient
    public static final String DEFAULT_ID = "defaultId";

    /** GameSegment ID string **/
    @XmlElement
    private String id;
    /** X axis position in levelMap **/
    @XmlElement
    private int mapX = 0;
    /** Y axis position in levelMap **/
    @XmlElement
    private int mapY = 0;
    /** PlayerControlledObjects in GameSegment - moving, controllable **/
    @XmlElement
    private Map<String, PlayerControlledObject> playerControlledItems;
    /** MovingObjects in GameSegment - moving **/
    @XmlElement
    private Map<String, MovingObject> movingItems;
    /** GameObjects in GameSegment - static, non-moving **/
    @XmlElement
    private Map<String, GameObject> staticItems;
    @XmlElement
    private Map<String, BackgroundGameObject> backgroundItems;

    public GameSegment(){
        id = DEFAULT_ID;
        playerControlledItems = new HashMap<>();
        movingItems = new HashMap<>();
        staticItems = new HashMap<>();
        backgroundItems = new HashMap<>();
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

    //Todo: javadoc
    public BackgroundGameObject getBackgroundItemsById(String id){
        return backgroundItems.get(id);
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
     * Registers a Background object to this specific level
     * @param bObj   the Background to be registered
     * @return      true if success, log + false if failure
     */
    public boolean registerBackground(BackgroundGameObject bObj){
        boolean result = false;

        if(bObj.getId() == null){
            Log.send(Log.type.ERROR, TAG, "Failed to register object; no ID assigned.");
        } else if(backgroundItems.containsKey(bObj.getId())){
            if(backgroundItems.containsValue(bObj)){
                Log.send(Log.type.WARNING, TAG, "Failed to register " + bObj.getId() +
                        ", this object is already registered.");
            } else {
                Log.send(Log.type.ERROR, TAG, "Failed to register " + bObj.getId() +
                        ", ID already in use.");
            }
        } else {
            backgroundItems.put(bObj.getId(), bObj);
            Log.send(Log.type.INFO, TAG, "Successfully registered " + bObj.getId());
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

    /**
     * Gets all background items
     * @return Map of backgroundGameObjects
     */
    public Map<String, BackgroundGameObject> getBackgroundItems() {
        return backgroundItems;
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

    public void setBackgroundItems(Map<String, BackgroundGameObject> backgroundItems) {
        this.backgroundItems = backgroundItems;
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
