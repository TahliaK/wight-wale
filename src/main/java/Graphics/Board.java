package Graphics;
import utils.Log;
import Actors.GameObject;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Board extends JPanel {

    private String TAG = "Board";
    private GameObject gmOb;

    public Board() {
        initBoard();
    }

    private void initBoard() {
        loadImages();

        try {
            int w = gmOb.getImage().getWidth(this);
            int h = gmOb.getImage().getHeight(this);
            setPreferredSize(new Dimension(w, h));
        } catch (NullPointerException _nEx) {
            Log.send(Log.type.INFO, TAG, "initBoard failure: " + _nEx.getMessage());
        } catch (Exception _ex){
            Log.send(Log.type.ERROR, TAG, "initBoard failure: " + _ex.getMessage());
        }
    }

    private void loadImages() {
        String filename = "src/main/resources/spaceship.png";

        gmOb = new GameObject();
        Boolean returnValue = gmOb.loadImageFrom(new File(filename), true);
        gmOb.setId("spaceship");
        GraphicsController.register(gmOb);

        Log.send(Log.type.INFO, TAG, "LoadImage complete, result: " + returnValue);
    }

    @Override
    public void paintComponent(Graphics g) {
        Map<String, GameObject> m = GraphicsController.getGraphicalItems();
        for(Map.Entry<String, GameObject> entry : m.entrySet()){
            g.drawImage(entry.getValue().getImage(), 0, 0, null);
            Log.send(Log.type.INFO, TAG, "Painting component id: " + entry.getValue().getId());
        }
    }
}