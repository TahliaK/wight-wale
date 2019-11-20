package com.tk.wightwhale.actors;

import com.tk.wightwhale.geometry.*;
import com.tk.wightwhale.utils.ImageUtils;
import com.tk.wightwhale.utils.Log;

import java.awt.*;
import java.util.ArrayList;
import javax.xml.bind.annotation.*;

/**
 * A non-moving image, displayed underneath everything, with or without
 * additional collisions defined for the image area.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class BackgroundGameObject extends GameObject {

    @XmlTransient
    private static final String TAG = "BackgroundGameObject";

    @XmlTransient
    private ArrayList<Rectangle> offLimitsAreas;
    @XmlElement
    private RectangleInfo[] collisionDetails;

    public static BackgroundGameObject makeFrom(GameObject obj){
        BackgroundGameObject bgo = new BackgroundGameObject();
        bgo.id = obj.id;
        bgo.visible = obj.visible;
        bgo.groupCategory = obj.groupCategory;
        bgo.position = obj.position;
        bgo.image = obj.image;
        bgo.imgFilename = obj.imgFilename;
        bgo.height = obj.height;
        bgo.width = obj.width;
        return bgo;
    }

    public BackgroundGameObject(){
        super();
        offLimitsAreas = new ArrayList<>();
        collisionDetails = new RectangleInfo[5];
    }

    public void initCollisions(){
        collisionDetails = new RectangleInfo[10]; //todo: make dynamic?
        for(int i = 0; i < collisionDetails.length; i++){
            RectangleInfo r = collisionDetails[i];
            offLimitsAreas.add(new Rectangle(r.x, r.y, r.width, r.height));
        }
        offLimitsAreas.add(new Rectangle(1, 2, 3, 4));
        Log.send(Log.type.INFO, TAG, "Collisions initialized.");
    }

    public void addOffLimitsArea(RectangleInfo area){
        offLimitsAreas.add(new Rectangle(area.x, area.y, area.width, area.height));
    }

    public ArrayList<Rectangle> getOffLimitsAreas(){
        return offLimitsAreas;
    }

    protected void scale(Image img){
        //calculate scale - must come before image scaling;
        point2d_double scale = ImageUtils.scaleVector(
                new point2d_int(image.getWidth(null), image.getHeight(null)),
                new point2d_int(width, height)
        );

        //scale the image
        this.image = ImageUtils.scale(img, width, height);

        //scale the pending collisions
        for(RectangleInfo r : collisionDetails){
            r.x = r.x * (int)scale.x;
            r.width = r.width * (int)scale.x;
            r.y = r.y * (int)scale.y;
            r.height = r.height * (int)scale.y;
        }
        //scale the existing collisions
        for (Rectangle r : offLimitsAreas){
            r.setBounds(
                    r.x * (int)scale.x,
                    r.y * (int)scale.y,
                    r.width * (int)scale.x,
                    r.height * (int)scale.y
            );
        }

    }
}
