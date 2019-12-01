package com.tk.wightwhale.collision;

import com.tk.wightwhale.actors.*;
import com.tk.wightwhale.graphics.GraphicsController;
import com.tk.wightwhale.levels.GameSegment;
import com.tk.wightwhale.utils.Log;
import com.tk.wightwhale.utils.point2d;

import javax.xml.bind.annotation.*;

/**
 * A collision event contains details +
 * execution functions for one collision
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class CollisionEvent {

    //action codes
    /** Debug tag **/
    @XmlTransient
    public static final String TAG = "CollisionEvent";
    /* todo: remove or figure these out */
    @XmlTransient
    public static final int INSTANT_STAT_CHANGE = 0;
    @XmlTransient
    public static final int SLOW_STAT_CHANGE = 1;

    /** Collision type enum **/
    @XmlElement
    protected CollisionType type;
    /** Integer actionCode **/
    @XmlElement
    protected int actionCode;
    /** Integer statEffect code**/
    @XmlElement
    protected int statEffect;

    /**
     * Default Constructor; default type is BLOCK.
     */
    public CollisionEvent(){
        type = CollisionType.BLOCK;
        actionCode = 0; statEffect = 0;
    }

    /**
     * Constructor
     * @param type  Collision behaviour type
     * @param actionCode    integer action code
     * @param statEffect    stat effect value
     */
    public CollisionEvent(CollisionType type, int actionCode, int statEffect){
        this.type = type;
        this.actionCode = actionCode;
        this.statEffect = statEffect;
    }

    /**
     * Returns the collision event type
     * @return CollisionType enum
     */
    public CollisionType getType() {
        return type;
    }

    /**
     * Sets the collision event type
     * @param type CollisionType enum
     */
    public void setType(CollisionType type) {
        this.type = type;
    }

    /**
     * Gets the action code
     * @return int
     */
    public int getActionCode() {
        return actionCode;
    }

    /**
     * Sets the action code
     * @param actionCode int
     */
    public void setActionCode(int actionCode) {
        this.actionCode = actionCode;
    }

    /**
     * Gets the stat effect value
     * @return int
     */
    public int getStatEffect() {
        return statEffect;
    }

    /**
     * Returns the stat effect value
     * @param statEffect int
     */
    public void setStatEffect(int statEffect) {
        this.statEffect = statEffect;
    }

    /**
     * Executes the collisionEvent on two objects
     * @param movingObject  the mover object
     * @param otherObject   the other object in the collision
     */
    public void execute(MovingObject movingObject, GameObject otherObject){
        if(type != CollisionType.IGNORE) {
            //Log.send(Log.type.INFO, TAG, type.toString());
            point2d direction;
            switch (type) {
                case BLOCK:
                    direction = getDirectionOfMovement(movingObject);
                    if (direction.x != 0) {   //Horizontal
                        movingObject.setxPos(movingObject.position.x - movingObject.getdX());
                        movingObject.setdX(0);
                    }
                    if (direction.y != 0) {   //Vertical
                        movingObject.setyPos(movingObject.position.y - movingObject.getdY());
                        movingObject.setdY(0);
                    }
                    break;
                case BOUNCE_BACK: //todo: replace with actual animation/timed slow down
                    direction = getDirectionOfMovement(movingObject);
                    if (direction.x != 0) {   //Horizontal
                        movingObject.setxPos(movingObject.position.x - movingObject.getdX());
                        movingObject.setdX(-1 * movingObject.getdX());
                    }
                    if (direction.y != 0) {   //Vertical
                        movingObject.setyPos(movingObject.position.y - movingObject.getdY());
                        movingObject.setdY(-1 * movingObject.getdY());
                    }
                    break;
                case LOAD_AREA_TRIGGER:
                    loadArea(movingObject);
                    break;
                case EVENT:
                    executeEvent(movingObject);
                    break;
            }
        }
    }

    //Loads an area in the current level based on walking direction
    //basically moves across the map with the player
    private void loadArea(MovingObject mv){
        point2d direction = getDirectionOfMovement(mv);
        GraphicsController graphicsController = GraphicsController.GetController();
        GameSegment gameSegTemp = graphicsController.getLoadedArea();
        point2d mapArea = new point2d(gameSegTemp.getMapX() + direction.x, gameSegTemp.getMapY() + direction.y);
        gameSegTemp = graphicsController.moveTo(graphicsController.getLevelMap().getLevelId(),
                mapArea.x, mapArea.y);

        if(gameSegTemp == null) {   //If at edge of map square
            Log.send(Log.type.DEBUG, TAG + "_loadArea", "Map edge.");
            mv.position.y = mv.position.y - mv.getdY();
            mv.position.x = mv.position.x - mv.getdX();
        } else if (gameSegTemp.getId().equals(GameSegment.DEFAULT_ID)){  //If segment undefined
            Log.send(Log.type.DEBUG, TAG + "_loadArea", "Segment undefined.");
            mv.position.y = mv.position.y - mv.getdY();
            mv.position.x = mv.position.x - mv.getdX();
        } else {
            Log.send(Log.type.DEBUG, TAG + "_loadArea",
                    "GameSegment: ID=" + gameSegTemp.getId() + "Map coords: [" + gameSegTemp.getMapX() +
                            ", " + gameSegTemp.getMapY() + "]" );
        }
    }

    //Executes event todo
    private void executeEvent(MovingObject mv){
        //todo: implement stat adjustment
    }

    //Gets the direction of movement as a point2d value between -1 and 1
    private point2d getDirectionOfMovement(MovingObject mv){
        int xMov = mv.getdX();
        int yMov = mv.getdY();
        point2d direction = new point2d(xMov,yMov);

        if(direction.x!=0){
            if(direction.x > 0){
                direction.x = 1;
            } else {
                direction.x = -1;
            }
        }

        if(direction.y!=0){
            if(direction.y > 0){
                direction.y = 1;
            } else {
                direction.y = -1;
            }
        }
        return direction;
    }

}