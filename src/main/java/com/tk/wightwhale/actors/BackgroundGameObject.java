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

    /** Debug tag **/
    @XmlTransient
    private static final String TAG = "BackgroundGameObject";

    /** Additional collision areas for background / environment **/
    @XmlTransient
    private ArrayList<Rectangle> offLimitsAreas;
    /** Collision details for XML import / export **/
    @XmlElement (name = "CollisionAreas")
    private ArrayList<RectangleInfo> collisionDetails;

    public BackgroundGameObject(){
        super();
        offLimitsAreas = new ArrayList<>();
        collisionDetails = new ArrayList<>();
    }

    /**
     * Clears collisions and re-creates from collisionDetails
     */
    public void initCollisions(){
        if(offLimitsAreas.size() != collisionDetails.size()) {
            offLimitsAreas.clear();
            for (int i = 0; i < collisionDetails.size(); i++) {
                RectangleInfo r = collisionDetails.get(i);
                offLimitsAreas.add(new Rectangle(r.x, r.y, r.width, r.height));
            }
        }
        //offLimitsAreas.add(new Rectangle(0, 100, 200, 200));
        Log.send(Log.type.INFO, TAG, "Collisions initialized.");
    }

    /**
     * Manually adds rectangular a collision area
     * @param x X position of top-left corner
     * @param y Y position of top-left corner
     * @param width width of the rectangle
     * @param height    height of the rectangle
     */
    public void addOffLimitsArea(int x, int y, int width, int height){
        collisionDetails.add(new RectangleInfo(x, y, width, height));
        offLimitsAreas.add(new Rectangle(x, y, width, height));
    }

    /**
     * Returns Awt.Rectangle collision area list
     * @return ArrayList of Java.Awt.Rectangle
     */
    public ArrayList<Rectangle> getOffLimitsAreas(){
        return offLimitsAreas;
    }

    /**
     * Overrid loadImage function which includes scaling of the collisionAreas
     * @param file  File containing the image
     * @param matchSpriteSizeToImage    if true, sprite will be same size as png image
     * @return
     */
    @Override
    public boolean loadImageFrom(File file, Boolean matchSpriteSizeToImage){
        boolean loaded = false;
        try {
            BufferedImage img = ImageIO.read(file);
            imgFilename = file.getPath();
            ImageIcon ii = new ImageIcon(img); // is imageIcon even needed here? answer: yes
            this.image = ii.getImage();
            if(matchSpriteSizeToImage){
                this.height = img.getHeight();
                this.width = img.getWidth();
            } else if(this.height != img.getHeight(null) || this.width != image.getWidth(null)){
                this.image = scale(img);
            }
            loaded = true;
        } catch (IOException _ex){
            Log.send(Log.type.ERROR, TAG, "Failed to load image from file " + file.getName());
            _ex.printStackTrace();
        } catch (ClassCastException _ex){
            Log.send(Log.type.ERROR, TAG, "Scaling image return failed (this should... really not happen. Contact the dev.)");
        }
        return loaded;
    }

    /**
     * Scales image & collisionAreas together
     * @param img
     */
    protected Image scale(Image img){
        //calculate scale - must come before image scaling;
        point2d_double scale = ImageUtils.scaleVector(
                new point2d_int(width, height),
                new point2d_int(image.getWidth(null), image.getHeight(null))
        );

        //scale the image
        this.image = ImageUtils.scale(img, width, height);

        //scale the collisions details
        for(RectangleInfo r : collisionDetails){
            r.x =(int)(r.x * scale.x);
            r.width = (int)(r.width * scale.x);
            r.y = (int)(r.y * scale.y);
            r.height = (int)(r.height * scale.y);
        }
        //scale the collision rectangles
        for (Rectangle r : offLimitsAreas){
            r.setBounds(
                    (int)(r.x * scale.x),
                    (int)(r.y * scale.y),
                    (int)(r.width * scale.x),
                    (int)(r.height * scale.y)
            );
        }

        return this.image;
    }

    public boolean loadImageFrom(File file, boolean matchSpriteSizeToImage){
        initCollisions();
        return super.loadImageFrom(file, matchSpriteSizeToImage);
    }
}
