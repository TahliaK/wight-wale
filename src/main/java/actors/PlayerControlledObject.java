package actors;

import utils.ControlScheme;
import utils.Log;

import javax.xml.bind.annotation.*;
import java.awt.event.KeyEvent;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class PlayerControlledObject extends MovingObject {

    @XmlTransient
    public static ControlScheme.type DEFAULT_CONTROLS = ControlScheme.type.ARROW_KEYS;
    @XmlTransient
    private static String TAG = "PlayerControlledObject";

    @XmlElement (name = "ControlScheme")
    ControlScheme controls;
    @XmlElement (name = "KeepBetweenAreas")
    boolean persistent;

    public PlayerControlledObject(){
        super();
        controls = new ControlScheme(DEFAULT_CONTROLS);
        persistent = false;
    }

    public PlayerControlledObject(MovingObject mo, ControlScheme.type controlScheme){
        dX = mo.dX;
        dY = mo.dY;
        xPos = mo.xPos;
        yPos = mo.yPos;
        //maxSpeed = mo.maxSpeed;
        stepSize = mo.stepSize;
        id = mo.id;
        visible = mo.visible;
        imgFilename = mo.imgFilename;
        image = mo.image;
        persistent = false;
        controls = new ControlScheme(controlScheme);
    }

    public void keyPressed(KeyEvent e) {
        //Log.send(Log.type.DEBUG, TAG + ":" + id, "Key pressed: " + e.getKeyCode());
        controls.keyPressed(this, e.getKeyCode());
    }

    public void keyReleased(KeyEvent e) {
        //Log.send(Log.type.DEBUG, TAG + ":" + id, "Key released: " + e.getKeyCode());
        controls.keyReleased(this, e.getKeyCode());
    }

    public void setPersistent(boolean status){
        persistent = status;
    }

    public boolean getPersistent(){
        return persistent;
    }

    @Override
    public String toString(){
        String out = "Id=" + id + " Position:[" + xPos + ":" + yPos
                + "] Visible: " + visible + " Image filename: " + imgFilename;
        return out;
    }

}
