package com.tk.wightwhale.geometry;

import javax.xml.bind.annotation.*;
import java.awt.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class RectangleInfo {
    @XmlElement
    public int x;
    @XmlElement
    public int y;
    @XmlElement
    public int width;
    @XmlElement
    public int height;

    public RectangleInfo(){
        x = 0; y = 0; width = 0; height = 0;
    }

    public RectangleInfo(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle toRectangle(){
        return new Rectangle(x, y, width, height);
    }
}
