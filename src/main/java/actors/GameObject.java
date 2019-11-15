package actors;

import utils.ImageUtils;
import utils.Log;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Image;
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

    private static final String TAG = "GameObject";
    @XmlTransient
    protected Image image;  //sprite

    @XmlElement
    protected int xPos;
    @XmlElement
    protected int yPos; //position on Screen;
    @XmlElement
    protected int width;
    @XmlElement
    protected int height;
    protected String imgFilename;

    /* Constructors */

    /**
     * Default constructor @ position 0, size 10x10, no image.
     */
    public GameObject(){
        xPos = 0; yPos = 0;
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
     */
    public GameObject(int xPos, int yPos, int width, int height, String id, String imageFile){
        this.xPos = xPos;
        this.yPos = yPos;
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
            } else {
                this.image = ImageUtils.scale(img, height, width);
            }
            loaded = true;
        } catch (IOException _ex){
            Log.send(Log.type.ERROR, TAG, "Failed to load image from file " + file.getName());
            _ex.printStackTrace();
        } catch (ClassCastException _ex){
            Log.send(Log.type.ERROR, TAG, "Scaling image return failed (this should... really not happen. Contact the dev.");
        }
        return loaded;
    }

    /**
     * Loads image from the file already stored in imgFilename
     * @param matchSpriteSizeToImage    if true, sprite will be same size as png image
     * @return  true if success, false if failure
     */
    public Boolean loadImageFile(Boolean matchSpriteSizeToImage){
        return loadImageFrom(new File(imgFilename), matchSpriteSizeToImage);
    }

    /** Getters / Setters **/

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
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
}
