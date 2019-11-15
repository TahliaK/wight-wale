package com.tk.wightwhale.graphics;
import javax.xml.bind.annotation.*;

/**
 * Encapsulates Window Settings (title, height, width + FPS) for import
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement (name = "WindowSettings")
public class GcElements {
    /** Title of the window **/
    @XmlElement (name="Title")
    public String windowTitle;
    /** Window height in pixels **/
    @XmlElement (name="Height")
    public int windowHeight;
    /** Window width in pixels **/
    @XmlElement (name="Width")
    public int windowWidth;
    /** Graphics refresh rate (FPS) **/
    @XmlElement (name="FPS")
    public int fps;

    public GcElements() {
        windowTitle = "Default Title";
        windowHeight = 600; windowWidth = 900;
        fps = 24;
    }

}
