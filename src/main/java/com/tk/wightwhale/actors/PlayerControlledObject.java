package com.tk.wightwhale.actors;
import com.tk.wightwhale.utils.ControlScheme;

import javax.xml.bind.annotation.*;
import java.awt.event.KeyEvent;

/**
 * A PlayerControlledObject is any object which is interacted
 * with by the user
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class PlayerControlledObject extends MovingObject {

    /** Default control scheme (for default constructor) **/
    @XmlTransient
    public static ControlScheme.type DEFAULT_CONTROLS = ControlScheme.type.ARROW_KEYS;
    /** Debug tag **/
    @XmlTransient
    private static String TAG = "PlayerControlledObject";

    /** ControlScheme in use **/
    @XmlElement (name = "ControlScheme")
    ControlScheme controls;
    /** Persistent status **/
    @XmlElement (name = "KeepBetweenAreas")
    boolean persistent;

    /**
     * Default constructor
     * Sets to non-persistent, with Arrow-key movement controls
     */
    public PlayerControlledObject(){
        super();
        controls = new ControlScheme(DEFAULT_CONTROLS);
        persistent = false;
    }

    /**
     * Constructor takes a movingObject and adds controlScheme
     * @param mo MovingObject to upgrade
     * @param controlScheme ControlScheme to apply
     */
    public PlayerControlledObject(MovingObject mo, ControlScheme.type controlScheme){
        dX = mo.dX;
        dY = mo.dY;
        position = mo.position;
        //maxSpeed = mo.maxSpeed;
        stepSize = mo.stepSize;
        id = mo.id;
        visible = mo.visible;
        imgFilename = mo.imgFilename;
        image = mo.image;
        persistent = false;
        controls = new ControlScheme(controlScheme);
    }

    /**
     * Calls control scheme response to key press
     * @param e KeyEvent to pass
     */
    public void keyPressed(KeyEvent e) {
        //Log.send(Log.type.DEBUG, TAG + ":" + id, "Key pressed: " + e.getKeyCode());
        controls.keyPressed(this, e.getKeyCode());
    }

    /**
     * Calls control scheme response to key release
     * @param e KeyEvent to pass
     */
    public void keyReleased(KeyEvent e) {
        //Log.send(Log.type.DEBUG, TAG + ":" + id, "Key released: " + e.getKeyCode());
        controls.keyReleased(this, e.getKeyCode());
    }

    /**
     * Sets as persistent between com.tk.wightwhale.levels
     * @param status true if persistent
     */
    public void setPersistent(boolean status){
        persistent = status;
    }

    /**
     * Gets status as persistent or not
     * @return true if persistent
     */
    public boolean getPersistent(){
        return persistent;
    }

    @Override
    public String toString(){
        String out = "Id=" + id + " Position:[" + position.x + ":" + position.y
                + "] Visible: " + visible + " Image filename: " + imgFilename;
        return out;
    }

}
