package actors;

import utils.Log;
import java.awt.event.KeyEvent;

public class MovingObject extends GameObject {

    private static final String TAG = "MovingObject";
    protected int dX = 0;
    protected int dY = 0; //movement axis

    public MovingObject(){
        super();
        dX = 0; dY = 0;
    }

    public void step() {
        xPos += dX;
        yPos += dY;
        Log.send(Log.type.INFO, TAG, "Location: " + xPos + ", " + yPos);
    }

    public void keyPressed(KeyEvent e) {
        Log.send(Log.type.INFO, TAG, "Key pressed: " + e.getKeyCode() + " & " + KeyEvent.VK_RIGHT);
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dX = -2;
        }

        if (key == KeyEvent.VK_RIGHT) {
            Log.send(Log.type.INFO, TAG, "Right key pressed.");
            dX = 2;

        }

        if (key == KeyEvent.VK_UP) {
            dY = -2;
        }

        if (key == KeyEvent.VK_DOWN) {
            dY = 2;
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
}
