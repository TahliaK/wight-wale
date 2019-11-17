package com.tk.wightwhale.utils;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class point2d {
    @XmlElement
    public int x;
    @XmlElement
    public int y;

    public point2d(){
        x = 0; y = 0;
    }

    public point2d(int x, int y){
        this.x = x; this.y = y;
    }
}
