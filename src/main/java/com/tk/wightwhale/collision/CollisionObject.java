package com.tk.wightwhale.collision;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import com.tk.wightwhale.actors.*;
import com.tk.wightwhale.utils.Log;

public class CollisionObject extends MovingObject {

    //global members
    private static final String TAG = "CollisionObject";

    //instance members
    private Map<String, ArrayList<CollisionEvent>> collisionEventMap;

    public CollisionObject(){
        super();
        collisionEventMap = new HashMap<String, ArrayList<CollisionEvent>>();
    }

    //Setters & getters
    public Map<String, ArrayList<CollisionEvent>> getCollisionEventMap() {
        return collisionEventMap;
    }

    public void setCollisionEventMap(Map<String, ArrayList<CollisionEvent>> collisionEventMap) {
        this.collisionEventMap = collisionEventMap;
    }

    public void putCollisionEvent(CollisionEvent cE){
        if(collisionEventMap.containsKey(cE.getTriggerGroup())){ /* add effect to existing entry */
            ArrayList<CollisionEvent> effectList = collisionEventMap.get(cE.getTriggerGroup());
            if(effectList.contains(cE)){
                Log.send(Log.type.DEBUG, TAG, "Duplicate collision event.");
            } else {
                effectList.add(cE);
            }
        } else { /* Generate new entry */
            ArrayList<CollisionEvent> effectList = new ArrayList<>();
            effectList.add(cE);
            collisionEventMap.put(cE.getTriggerGroup(), effectList);
        }
    }

    public ArrayList<CollisionEvent> getCollisionEvent(String key){
        return collisionEventMap.get(key);
    }
}
