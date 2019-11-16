package com.tk.wightwhale.collision;

import com.tk.wightwhale.actors.*;

public class CollisionEvent {

    protected CollisionType type;
    protected int actionCode;
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
}
