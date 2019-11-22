package com.tk.wightwhale.actors;

import com.tk.wightwhale.utils.ImageUtils;
import com.tk.wightwhale.utils.Log;
import com.tk.wightwhale.utils.point2d;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//For loading from XML
import javax.xml.bind.annotation.*;

/**
 * A non-moving object or sprite in game
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class GameObject extends AbstractGameObject {

    /** Debug tag **/
    private static final String TAG = "GameObject";
    /** Sprite image **/
    @XmlTransient
    protected Image image;  //sprite

    /** position **/
    @XmlElement
    public point2d position;
    /** sprite width **/
    @XmlElement
    protected int width;
    /** sprite height **/
    @XmlElement
    protected int height;
    /** Sprite image file path **/
    @XmlElement
    protected String imgFilename;

    /* Constructors */

    /**
     * Default constructor @ position 0, size 10x10, no image.
     */
    public GameObject(){
        position = new point2d();
        width = 10; height = 10;
        image = null;
        id = null;
        imgFilename = null;
    }

    /**
     * Constructor for specified position and size, no image.
     * @param xPos  x axis position
     * @param yPos  y axis position
     * @param width object width in pixels
     * @param height object height in pixels
     * @param id object ID as a String
     * @param imageFile the filename to use for the image (png preferred)
     */
    public GameObject(int xPos, int yPos, int width, int height, String id, String imageFile){
        this.position = new point2d(xPos, yPos);
        this.width = width;
        this.height = height;
        this.id = id;
        this.imgFilename = imageFile;
        loadImageFrom(new File(imageFile), true);
    }

    /** Image loading **/

    /**
     * Gets the loaded image
     * @return image or null if not yet loaded
     */
    public Image getImage() {
        return image;
    }

    /**
     * Sets the loaded image
     * @param image Image object to use
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Loads image to use from a specified file
     * @param file  File containing the image
     * @param matchSpriteSizeToImage    if true, sprite will be same size as png image
     * @return true if success, false if failure
     */
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
                this.image = ImageUtils.scale(img, height, width);
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
     * Loads image from the file already stored in imgFilename
     * @param matchSpriteSizeToImage    if true, sprite will be same size as png image
     * @return  true if success, false if failure
     */
    public boolean loadImageFile(Boolean matchSpriteSizeToImage){
        return loadImageFrom(new File(imgFilename), matchSpriteSizeToImage);
    }

    /* Getters / Setters */

    public int getxPos() {
        return position.x;
    }

    public void setxPos(int xPos) {
        position.x = xPos;
    }

    public int getyPos() {
        return position.y;
    }

    public void setyPos(int yPos) {
        position.y = yPos;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImgFilename() {
        return imgFilename;
    }

    public void setImgFilename(String imgFilename) {
        this.imgFilename = imgFilename;
    }

    /**
     * Flushes image buffer
     */
    public void unloadImage(){
        image.flush();
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(position.x, position.y, width, height);
    }

    @Override
    public String toString(){
        return "Id=" + id + " Pos=[" + position.x + ":" + position.y + "]";
    }
}
