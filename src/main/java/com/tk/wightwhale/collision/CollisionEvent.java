package com.tk.wightwhale.collision;

import com.tk.wightwhale.actors.*;

public class CollisionEvent {

    //action codes
    public static final int INSTANT_STAT_CHANGE = 0;
    public static final int SLOW_STAT_CHANGE = 1;

    protected CollisionType type;
    protected String triggerGroup;
    protected int actionCode;
    protected int statEffect;

    public CollisionEvent(){
        type = CollisionType.BLOCK;
        actionCode = 0; statEffect = 0;
        triggerGroup = null;
    }

    public CollisionEvent(CollisionType type, int actionCode, int statEffect, String triggerGroup){
        this.type = type;
        this.actionCode = actionCode;
        this.statEffect = statEffect;
        this.triggerGroup = triggerGroup;
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

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }
}
