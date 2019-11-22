package com.tk.wightwhale.utils;

import com.tk.wightwhale.actors.MovingObject;

import javax.xml.bind.annotation.*;
import java.awt.event.KeyEvent;

/**
 * Encapsulates common player-controlled keys
 * and responses by a moving object
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ControlScheme {

    /** Control scheme types **/
    public enum type{
        ARROW_KEYS,
        WASD//,
        //CLICK_BASED,
        //CUSTOM
    }
    /** Default control scheme (for default constructor) **/
    @XmlTransient
    private type defaultScheme = type.ARROW_KEYS;

    /** Control scheme type for instance **/
    @XmlElement (name = "CurrentControlScheme")
    private type scheme;

    public ControlScheme() { scheme = defaultScheme; }

    public ControlScheme(type t){
        scheme = t;
    }

    /**
     * Handles key-press responses for MovingObjects using set control scheme
     * @param pcO   the MovingObject / PlayerControlledObject to use
     * @param keycode   result of e.getKeyCode() on KeyEvent e
     */
    public void keyPressed(MovingObject pcO, int keycode){
        switch(scheme){
            case ARROW_KEYS:
                arrowKeys_pressed(pcO, keycode);
                break;
            case WASD:
                wasd_pressed(pcO, keycode);
                break;
        }
    }

    /**
     * Handles key-release responses for MovingObjects using set control scheme
     * @param pcO   the MovingObject / PlayerControlledObject to use
     * @param keycode   result of e.getKeyCode() on KeyEvent e
     */
    public void keyReleased(MovingObject pcO, int keycode){
        switch(scheme){
            case ARROW_KEYS:
                arrowKeys_released(pcO, keycode);
                break;
            case WASD:
                wasd_released(pcO, keycode);
                break;
        }

    }

    /* Specific arrow key control schemes */

    private void wasd_pressed(MovingObject pcO, int keycode){
        if(keycode == KeyEvent.getExtendedKeyCodeForChar('a')){
            pcO.setdX(pcO.getdX()-pcO.getStepSize());
        }

        if (keycode == KeyEvent.getExtendedKeyCodeForChar('d')) {
            pcO.setdX(pcO.getdX()+pcO.getStepSize());
        }

        if (keycode == KeyEvent.getExtendedKeyCodeForChar('w')) {
            pcO.setdY(pcO.getdY()-pcO.getStepSize());
        }

        if (keycode == KeyEvent.getExtendedKeyCodeForChar('s')) {
            pcO.setdY(pcO.getdY()+pcO.getStepSize());
        }
    }

    private void wasd_released(MovingObject pcO, int keycode){
        if (keycode == KeyEvent.getExtendedKeyCodeForChar('a')) {
            pcO.setdX(0);
        }

        if (keycode == KeyEvent.getExtendedKeyCodeForChar('d')) {
            pcO.setdX(0);
        }

        if (keycode == KeyEvent.getExtendedKeyCodeForChar('w')) {
            pcO.setdY(0);
        }

        if (keycode == KeyEvent.getExtendedKeyCodeForChar('s')) {
            pcO.setdY(0);
        }
    }

    private void arrowKeys_pressed(MovingObject pcO, int keycode){
        if (keycode == KeyEvent.VK_LEFT) {
            pcO.setdX(pcO.getdX()-pcO.getStepSize());
        }
        else if (keycode == KeyEvent.VK_RIGHT) {
            pcO.setdX(pcO.getdX()+pcO.getStepSize());
        }
        else if (keycode == KeyEvent.VK_UP) {
            pcO.setdY(pcO.getdY()-pcO.getStepSize());
        }
        else if (keycode == KeyEvent.VK_DOWN) {
            pcO.setdY(pcO.getdY()+pcO.getStepSize());
        }

        if(Math.abs(pcO.getdY()) > pcO.getStepSize()){ //Prevents excessive speed
            if(pcO.getdY() > 0){
                pcO.setdY(pcO.getStepSize());
            } else {
                pcO.setdY(-1*pcO.getStepSize());
            }
        }
        if(Math.abs(pcO.getdX()) > pcO.getStepSize()){
            if(pcO.getdX() > 0){
                pcO.setdX(pcO.getStepSize());
            } else {
                pcO.setdX(-1*pcO.getStepSize());
            }
        }
    }

    private void arrowKeys_released(MovingObject pcO, int keycode){
        if (keycode == KeyEvent.VK_LEFT) {
            pcO.setdX(0);
        }
        else if (keycode == KeyEvent.VK_RIGHT) {
            pcO.setdX(0);
        }
        else if (keycode == KeyEvent.VK_UP) {
            pcO.setdY(0);
        }
        else if (keycode == KeyEvent.VK_DOWN) {
            pcO.setdY(0);
        }
    }
}
