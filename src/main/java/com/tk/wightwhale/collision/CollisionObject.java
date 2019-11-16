package com.tk.wightwhale.collision;

import java.util.Map;
import java.util.HashMap;

import com.tk.wightwhale.actors.*;

public class CollisionObject extends MovingObject {

    //global members
    private static final String TAG = "CollisionObject";

    //instance members
    private Map<String, CollisionEvent> collisionEventMap;

    public CollisionObject(){
        super();
        collisionEventMap = new HashMap<String, CollisionEvent>();
    }

    //Setters & getters
    public Map<String, CollisionEvent> getCollisionEventMap() {
        return collisionEventMap;
    }

    public void setCollisionEventMap(Map<String, CollisionEvent> collisionEventMap) {
        this.collisionEventMap = collisionEventMap;
    }

    //key = the 'type' of the collision
    public CollisionEvent getCollisionEventForKey(String key){
        return collisionEventMap.get(key);
    }
}
