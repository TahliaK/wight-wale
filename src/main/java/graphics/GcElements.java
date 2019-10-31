package graphics;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement (name = "WindowSettings")
public class GcElements {
    @XmlElement (name="Title")
    public String windowTitle;
    @XmlElement (name="Height")
    public int windowHeight;
    @XmlElement (name="Width")
    public int windowWidth;
    @XmlElement (name="FPS")
    public int fps;

    public GcElements() {
        windowTitle = "Default Title";
        windowHeight = 600; windowWidth = 900;
        fps = 24;
    }

}
