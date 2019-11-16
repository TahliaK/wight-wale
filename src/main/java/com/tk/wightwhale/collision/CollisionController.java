package com.tk.wightwhale.collision;

import com.tk.wightwhale.actors.*;
import com.tk.wightwhale.graphics.GraphicsController;
import com.tk.wightwhale.utils.Log;

import java.util.Map;

public class CollisionController {

    public static final String TAG = "CollisionController";
    private static CollisionController activeController = null;
    private static GraphicsController _gController;

    public static CollisionController getActiveController(){
        if(activeController == null){
            activeController = new CollisionController();
        }
        return activeController;
    }



    public CollisionController() {
        useActiveGraphicsController();
        activeController = this;
    }

    public void useActiveGraphicsController(){
        _gController = GraphicsController.GetController();
        if(_gController == null){
            Log.send(Log.type.WARNING, TAG, "No active graphics controller.");
        } else {
            Log.send(Log.type.INFO, TAG, "Active graphics controller: " + _gController.toString());
        }
    }

    public void setActiveController(){
        activeController = this;
    }

    public void step(MovingObject mv){
        if(_gController != null){
            Map<String, GameObject> staticItems = _gController.getStaticItems();
            for(Map.Entry<String, GameObject> entry : staticItems.entrySet() ){
                GameObject obj = entry.getValue();
                if(obj != mv) {
                    if(!obj.getGroupCategory().equals("background")) {
                        if (obj.getBounds().intersects(mv.getBounds())) {
                            Log.send(Log.type.DEBUG, TAG, "COLLISION: " + mv.toString() + " WITH " + obj.toString());
                        } //endif
                    }
                } //endif
            }//end static items
            Map<String, MovingObject> movingItems = _gController.getMovingItems();
            for(Map.Entry<String, MovingObject> entry : movingItems.entrySet() ){
                MovingObject obj = entry.getValue();
                if(obj != mv) {
                    if (obj.getBounds().intersects(mv.getBounds())) {
                        Log.send(Log.type.DEBUG, TAG, "COLLISION: " + mv.toString() + " WITH " + obj.toString());
                    } //endif
                } //endif
            }//end moving items
            Map<String, PlayerControlledObject> PCItems = _gController.getPlayerControlledItems();
            for(Map.Entry<String, PlayerControlledObject> entry : PCItems.entrySet() ){
                PlayerControlledObject obj = entry.getValue();
                if(obj != mv) {
                    if (obj.getBounds().intersects(mv.getBounds())) {
                        Log.send(Log.type.DEBUG, TAG, "COLLISION: " + mv.toString() + " WITH " + obj.toString());
                    } //endif
                }
            }//end static items
        } // end action
    } // end step

}
