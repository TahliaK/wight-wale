package utils;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

public class ImageUtils {

    /**
     * Scales an image to the specified size
     * @param image Image to scale
     * @param width target width
     * @param height target height
     * @return Image object at appropriate scale
     */
    public static Image scale(Image image, int width, int height){
        BufferedImage src = toBufferedImage(image);
        BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D gTransform = out.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance(
                (double)width/image.getWidth(null),
                (double)height/image.getHeight(null)
        );

        gTransform.drawRenderedImage(src, at);
        return out;
    }

    /**
     * Turns an Image object to a BufferedImage object
     * @param img Image object as input
     * @return BufferedImage version of img
     */
    public static BufferedImage toBufferedImage(Image img){
        BufferedImage bImage;
        if(!(img instanceof BufferedImage)) {
            bImage = new BufferedImage(
                    img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
        } else {
            bImage = (BufferedImage) img;
        }

        return bImage;
    }
}
