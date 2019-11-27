package com.tk.wightwhale.collision;

import com.tk.wightwhale.actors.*;
import com.tk.wightwhale.graphics.GraphicsController;
import com.tk.wightwhale.levels.GameSegment;
import com.tk.wightwhale.utils.Log;
import com.tk.wightwhale.utils.point2d;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class CollisionEvent {

    //action codes
    @XmlTransient
    public static final String TAG = "CollisionEvent";
    @XmlTransient
    public static final int INSTANT_STAT_CHANGE = 0;
    @XmlTransient
    public static final int SLOW_STAT_CHANGE = 1;

    @XmlElement
    protected CollisionType type;
    @XmlElement
    protected int actionCode;
    @XmlElement
    protected int statEffect;

    public CollisionEvent(){
        type = CollisionType.BLOCK;
        actionCode = 0; statEffect = 0;
    }

    public CollisionEvent(CollisionType type, int actionCode, int statEffect){
        this.type = type;
        this.actionCode = actionCode;
        this.statEffect = statEffect;
    }

    public CollisionType getType() {
        return type;
    }

    public void setType(CollisionType type) {
        this.type = type;
    }

    public int getActionCode() {
        return actionCode;
    }

    public void setActionCode(int actionCode) {
        this.actionCode = actionCode;
    }

    public int getStatEffect() {
        return statEffect;
    }

    public void setStatEffect(int statEffect) {
        this.statEffect = statEffect;
    }

    public void execute(MovingObject movingObject, GameObject otherObject){
        if(type != CollisionType.IGNORE) {
            Log.send(Log.type.INFO, TAG, type.toString());
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

    private void loadArea(MovingObject mv){
        point2d direction = getDirectionOfMovement(mv);
        GraphicsController graphicsController = GraphicsController.GetController();
        GameSegment gs = graphicsController.getLoadedArea();
        graphicsController.moveTo(graphicsController.getLevelMap().getLevelId(),
                gs.getMapX()+direction.x, gs.getMapY()+direction.y);
    }

    private void executeEvent(MovingObject mv){
        //todo: implement stat adjustment
    }

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