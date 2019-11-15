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

    /**
     * Gets the distance moving each step on X axis
     * @return integer
     */
    public int getdX() {
        return dX;
    }

    /**
     * Set the X axis movement
     * @param dX    integer distance in pixels
     */
    public void setdX(int dX) {
        this.dX = dX;
    }

    /**
     * Gets the distance moving each step on Y axis
     * @return integer
     */
    public int getdY() {
        return dY;
    }

    /**
     * Set the Y axis movement
     * @param dY    integer distance in pixels
     */
    public void setdY(int dY) {
        this.dY = dY;
    }

    /**
     * Gets stored move-size value
     * @return integer distance in pixels
     */
    public int getStepSize() {
        return stepSize;
    }

    /**
     * Sets default move-size value
     * @param stepSize integer distance in pixels
     */
    public void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }
}
