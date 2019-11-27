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

/**
 * Singleton class which detects and executes
 * collisions for GameObjects
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Collisions")
public class CollisionController {

    /** Debug tag **/
    @XmlTransient
    public static final String TAG = "CollisionController";
    /** Singleton pointer **/
    @XmlTransient
    private static CollisionController activeController = null;
    /** Internal reference to the active graphicsController **/
    @XmlTransient
    private static GraphicsController _gController;
    /** Stores the borders of the game - this is initialized from _gController**/
    @XmlTransient
    private static Rectangle borders;

    /** Stores collisionEvents by triggering group types **/
    @XmlElement
    private Map<String, Map<String, ArrayList<CollisionEvent>>> collisions;

    /**
     * Gets the CollisionController singleton (initializes it if none exists)
     * @return CollisionController
     */
    public static CollisionController getActiveController(){
        if(activeController == null){
            activeController = new CollisionController();
            Log.send(Log.type.DEBUG, TAG, "[[static]] Active controller fetched");
        }
        return activeController;
    }


    /**
     * Default constructor; automatically fetches graphicsController singleton
     */
    public CollisionController() {
        collisions = new HashMap<>();
        useActiveGraphicsController();
        activeController = this;
    }

    /**
     * Sets graphicsController used to current active singleton
     */
    public void useActiveGraphicsController(){
        _gController = GraphicsController.GetController();
        if(_gController == null){
            Log.send(Log.type.WARNING, TAG, "No active graphics controller.");
        } else {
            Log.send(Log.type.INFO, TAG, "Active graphics controller: " + _gController.toString());
        }
    }

    /**
     * Sets this CollisionController as the active singleton
     */
    public void setActiveController(){
        activeController = this;
    }

    /**
     * Calculates collision detection per step for a movingObject
     * @param mv MovingObject for collision detection
     */
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
                        //Log.send(Log.type.DEBUG, TAG, "Map trigger @: " + mv.toString());
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

    /**
     * Imports collision details from the specified sub-directory in Files
     * @param dir
     */
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

    /**
     * Adds a collision to the collisionController
     * @param importedCollision CollisionEventDetails object
     */
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

    /**
     * Returns the collisionEvents for a particular pair of group tags
     * @param movingGroup   group for the moving object
     * @param otherGroup    group for the other object in the collision
     * @return  ArrayList of CollisionEvents
     */
    private ArrayList<CollisionEvent> getCollisionEventsFor(String movingGroup, String otherGroup){
        if(collisions.containsKey(movingGroup)){
            if(collisions.get(movingGroup).containsKey(otherGroup)){
                return collisions.get(movingGroup).get(otherGroup);
            }
        }

        return null;
    }

    /**
     * Executes an ArrayList of collisionEvents
     * @param collisions    ArrayList of collision events to apply
     * @param actor MovingObject in collision
     * @param other Other GameObject in collision
     */
    private void executeCollisions(ArrayList<CollisionEvent> collisions, MovingObject actor, GameObject other){
        if(collisions != null) {
            for (CollisionEvent entry : collisions) {
                entry.execute(actor, other);
            }
        }
    }

    /**
     * Exports an example collision - todo: remove
     */
    public static void exportACollision(){
        XmlHandler<CollisionEventDetails> xml = new XmlHandler<>(CollisionEventDetails.class);

        CollisionEventDetails ced = new CollisionEventDetails();
        xml.writeToXml(ced, "", "collisionExample");
    }

}
