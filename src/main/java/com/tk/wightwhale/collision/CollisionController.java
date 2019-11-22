package com.tk.wightwhale.collision;

import com.tk.wightwhale.actors.*;
import com.tk.wightwhale.graphics.GraphicsController;
import com.tk.wightwhale.utils.ImageUtils;
import com.tk.wightwhale.utils.Log;
import com.tk.wightwhale.utils.XmlHandler;

import javax.xml.bind.annotation.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Collisions")
public class CollisionController {

    @XmlTransient
    public static final String TAG = "CollisionController";
    @XmlTransient
    private static CollisionController activeController = null;
    @XmlTransient
    private static GraphicsController _gController;
    @XmlTransient
    private static Rectangle borders;

    @XmlElement
    private Map<String, Map<String, ArrayList<CollisionEvent>>> collisions;

    public static CollisionController getActiveController(){
        if(activeController == null){
            activeController = new CollisionController();
            Log.send(Log.type.DEBUG, TAG, "[[static]] Active controller fetched");
        }
        return activeController;
    }


    public CollisionController() {
        collisions = new HashMap<>();
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
            //Borders:
            if(borders == null) {
                borders = new Rectangle(0, 0, _gController.getWindowWidth(), _gController.getWindowHeight());
            } else {
                if(!borders.contains(mv.getBounds())){
                    Log.send(Log.type.DEBUG, TAG, "Borders trigger.");
                }
            }

            //Map:
            Map<String, BackgroundGameObject> backgroundItems = _gController.getBackgroundItems();
            for(Map.Entry<String, BackgroundGameObject> entry : backgroundItems.entrySet()){
                BackgroundGameObject obj = entry.getValue();
                for(Rectangle r : obj.getOffLimitsAreas()){
                    if(mv.getBounds().intersects(r)){
                        ArrayList<CollisionEvent> collisionEvents = getCollisionEventsFor(mv.getGroupCategory(), obj.getGroupCategory());
                        executeCollisions(collisionEvents, mv, obj);
                    }
                }
            }

            //Non-moving items:
            Map<String, GameObject> staticItems = _gController.getStaticItems();
            for(Map.Entry<String, GameObject> entry : staticItems.entrySet() ){
                GameObject obj = entry.getValue();
                if(obj != mv) {
                    if(!obj.getGroupCategory().equals("background")) {
                        if (obj.getBounds().intersects(mv.getBounds())) {   //bounding box
                            if(ImageUtils.imageCollision(obj, mv)){         //transparency check
                                ArrayList<CollisionEvent> collisionEvents = getCollisionEventsFor(mv.getGroupCategory(), obj.getGroupCategory());
                                executeCollisions(collisionEvents, mv, obj);
                            }
                        } //endif
                    }
                } //endif
            }//end static items

            //Moving items
            Map<String, MovingObject> movingItems = _gController.getMovingItems();
            for(Map.Entry<String, MovingObject> entry : movingItems.entrySet() ){
                MovingObject obj = entry.getValue();
                if(obj != mv) {
                    if (obj.getBounds().intersects(mv.getBounds())) {   //bounding box
                        if(ImageUtils.imageCollision(obj, mv)){         //transparency check
                            ArrayList<CollisionEvent> collisionEvents = getCollisionEventsFor(mv.getGroupCategory(), obj.getGroupCategory());
                            executeCollisions(collisionEvents, mv, obj);
                        }
                    } //endif
                } //endif
            }//end moving items

            //Player controlled items
            Map<String, PlayerControlledObject> PCItems = _gController.getPlayerControlledItems();
            for(Map.Entry<String, PlayerControlledObject> entry : PCItems.entrySet() ){
                PlayerControlledObject obj = entry.getValue();
                if(obj != mv) {
                    if (obj.getBounds().intersects(mv.getBounds())) {   //bounding box
                        if(ImageUtils.imageCollision(obj, mv)){         //transparency check
                            ArrayList<CollisionEvent> collisionEvents = getCollisionEventsFor(mv.getGroupCategory(), obj.getGroupCategory());
                            executeCollisions(collisionEvents, mv, obj);                        }
                    } //endif
                }
            }//end static items
        } // end if
    } // end step

    public void importFromXml(String dir){
        XmlHandler<CollisionEventDetails> importer = new XmlHandler<>(CollisionEventDetails.class);

        File dirFile = new File("Files/" + dir);
        File[] directoryList = dirFile.listFiles();
        int i;
        if(directoryList != null){
            for(File child : directoryList){
                if(!child.isHidden() && child.getName().contains(".xml")){
                    CollisionEventDetails collisionEvent = importer.readFromXml(dir, child.getName());
                    addCollisionEvent(collisionEvent);
                }
            }
        }
    }

    private void addCollisionEvent(CollisionEventDetails importedCollision){
        String movingGroup = importedCollision.getMovingGroup();
        String otherGroup = importedCollision.getOtherGroup();

        if(!collisions.containsKey(movingGroup)){   //if no entry for movingGroup, add to map
            collisions.put(movingGroup, new HashMap<>());
        }

        if(!collisions.get(movingGroup).containsKey(otherGroup)){ //if no entry for otherGroup
            ArrayList<CollisionEvent> list = new ArrayList<>(); //create list
            list.add(importedCollision.toCollisionEvent()); //add import to list
            collisions.get(movingGroup).put(otherGroup, list);  //add list to movingGroup map
        } else {
            ArrayList<CollisionEvent> list = collisions.get(movingGroup).get(otherGroup); //existing list
            list.add(importedCollision.toCollisionEvent()); //add to
        }
    }

    private ArrayList<CollisionEvent> getCollisionEventsFor(String movingGroup, String otherGroup){
        if(collisions.containsKey(movingGroup)){
            if(collisions.get(movingGroup).containsKey(otherGroup)){
                return collisions.get(movingGroup).get(otherGroup);
            }
        }

        return null;
    }

    private void executeCollisions(ArrayList<CollisionEvent> collisions, MovingObject actor, GameObject other){
        if(collisions != null) {
            for (CollisionEvent entry : collisions) {
                entry.execute(actor, other);
            }
        }
    }

    public static void exportACollision(){
        XmlHandler<CollisionEventDetails> xml = new XmlHandler<>(CollisionEventDetails.class);

        CollisionEventDetails ced = new CollisionEventDetails();
        xml.writeToXml(ced, "", "collisionExample");
    }

}
