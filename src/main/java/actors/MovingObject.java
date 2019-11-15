package actors;

import java.awt.event.KeyEvent;
import javax.xml.bind.annotation.*;

/**
 * GameObject with ability to move
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso({PlayerControlledObject.class})
public class MovingObject extends GameObject {

    @XmlTransient
    private static final String TAG = "MovingObject";
    @XmlTransient
    protected int dX = 0; //movement axis
    @XmlTransient
    protected int dY = 0; //movement axis
    @XmlElement (name = "moveDistance")
    protected int stepSize = 2; //movement distance

    public MovingObject(){
        super();
        dX = 0; dY = 0;
        stepSize = 2;
    }

    /**
     * Updates the current position based on current movement.
     */
    public void step() {
        xPos += dX;
        yPos += dY;
    }

    public int getdX() {
        return dX;
    }

    public void setdX(int dX) {
        this.dX = dX;
    }

    public int getdY() {
        return dY;
    }

    public void setdY(int dY) {
        this.dY = dY;
    }

    public int getStepSize() {
        return stepSize;
    }

    public void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }
}
