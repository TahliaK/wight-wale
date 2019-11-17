package com.tk.wightwhale.collision;

import com.tk.wightwhale.actors.*;
import com.tk.wightwhale.utils.Log;

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
        Log.send(Log.type.INFO, TAG, type.toString());
        switch(type){
            case BLOCK:
                movingObject.setdX(0);
                movingObject.setdY(0); //todo: pick correct axis
                break;
            case BOUNCE_BACK:
                movingObject.setdX(movingObject.getdX() * -1);
                movingObject.setdY(movingObject.getdY() * -1);
                break;
            case LOAD_AREA_TRIGGER:
                //nada
                break;
            case EVENT:
                executeEvent();
                break;
        }
    }

    private void executeEvent(){
        //todo: implement stat adjustment
    }

}