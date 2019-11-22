package com.tk.wightwhale.actors;

import com.tk.wightwhale.geometry.*;
import com.tk.wightwhale.utils.ImageUtils;
import com.tk.wightwhale.utils.Log;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
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
    @XmlElement (name = "CollisionAreas")
    private ArrayList<RectangleInfo> collisionDetails;

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
        collisionDetails = new ArrayList<>();
    }

    public void initCollisions(){
        offLimitsAreas.clear();
        for(int i = 0; i < collisionDetails.size(); i++){
            RectangleInfo r = collisionDetails.get(i);
            offLimitsAreas.add(new Rectangle(r.x, r.y, r.width, r.height));
        }
        //offLimitsAreas.add(new Rectangle(1, 2, 3, 4));
        Log.send(Log.type.INFO, TAG, "Collisions initialized.");
    }

    public void addOffLimitsArea(int x, int y, int width, int height){
        collisionDetails.add(new RectangleInfo(x, y, width, height));
        offLimitsAreas.add(new Rectangle(x, y, width, height));
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

        //scale the collisions details
        for(RectangleInfo r : collisionDetails){
            r.x = r.x * (int)scale.x;
            r.width = r.width * (int)scale.x;
            r.y = r.y * (int)scale.y;
            r.height = r.height * (int)scale.y;
        }
        //scale the collision rectangles
        for (Rectangle r : offLimitsAreas){
            r.setBounds(
                    r.x * (int)scale.x,
                    r.y * (int)scale.y,
                    r.width * (int)scale.x,
                    r.height * (int)scale.y
            );
        }
    }

    public boolean loadImageFrom(File file, boolean matchSpriteSizeToImage){
        initCollisions();
        return super.loadImageFrom(file, matchSpriteSizeToImage);
    }
}
