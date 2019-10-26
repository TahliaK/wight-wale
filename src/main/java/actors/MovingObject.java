package actors;

import java.awt.event.KeyEvent;

/**
 * GameObject with ability to move
 */
public class MovingObject extends GameObject {

    private static final String TAG = "MovingObject";
    protected int dX = 0; //movement axis
    protected int dY = 0; //movement axis
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

    //response to direct key reactions
    //will be made XML-adjustable shortly
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dX = -stepSize;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dX = stepSize;

        }

        if (key == KeyEvent.VK_UP) {
            dY = -stepSize;
        }

        if (key == KeyEvent.VK_DOWN) {
            dY = stepSize;
        }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dX = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dX = 0;
        }

        if (key == KeyEvent.VK_UP) {
            dY = 0;
        }

        if (key == KeyEvent.VK_DOWN) {
            dY = 0;
        }
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
