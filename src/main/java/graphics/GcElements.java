package graphics;

//For loading from XML
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class GcElements {
    @XmlElement
    public String windowTitle;
    @XmlElement
    public int windowHeight;
    @XmlElement
    public int windowWidth;

    public GcElements() {
        windowTitle = "Default Title";
        windowHeight = 600; windowWidth = 900;
    }

}
