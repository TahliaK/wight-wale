package actors;

import utils.Log;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//For loading from XML
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class GameObject {

    private static final String TAG = "GameObject";
    @XmlTransient
    protected Image image;  //sprite

    @XmlAttribute
    protected String id;    //global access ID
    @XmlElement
    protected int xPos, yPos; //position on Screen;
    protected int dX, dY; //movement axis
    protected int width, height; //heh
    protected String imgFilename;

    /* Constructors */

    /**
     * Default constructor @ position 0, size 10x10, no image.
     */
    public GameObject(){
        xPos = 0; yPos = 0;
        dX = 0; dY = 0;
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
    public GameObject(int xPos, int yPos, int width, int height, String id){
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.id = id;
        dX = 0; dY = 0;
        image = null;
    }

    /** Image loading **/

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public boolean loadImageFrom(File file, Boolean matchSpriteSizeToImage){
        boolean loaded = false;
        try {
            BufferedImage img = ImageIO.read(file);
            imgFilename = file.getPath();
            ImageIcon ii = new ImageIcon(img); //todo: is imageIcon even needed here?
            this.image = ii.getImage();
            if(matchSpriteSizeToImage){
                this.height = img.getHeight();
                this.width = img.getWidth();
            }
            loaded = true;
        } catch (IOException _ex){
            Log.send(Log.type.ERROR, TAG, "Failed to load image from file " + file.getName());
        }
        return loaded;
    }

    /** Getters / Setters **/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public int getdX() {
        return dX;
    }

    public void setdX(int dX) {
        this.dX = dX;
    }

    public int getdY() {
        return dY;
    }

    public void setdY(int dY) {
        this.dY = dY;
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
}
