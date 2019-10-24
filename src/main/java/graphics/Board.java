package graphics;
import utils.Log;
import utils.XmlHandler;
import actors.GameObject;

import java.awt.Graphics;
import java.io.File;
import java.util.Map;
import javax.swing.JPanel;

public class Board extends JPanel {

    private String TAG = "Board";

    public Board() {
        initBoard();
    }

    private void initBoard() {
        loadImages();
    }

    private void loadImages() {
        GameObject gmOb = (GameObject) XmlHandler.XmlToObject("GameObjects/spaceship.xml");
        gmOb.loadImageFile(true);
        GraphicsController.register(gmOb);

        Log.send(Log.type.INFO, TAG, "LoadImage complete.");
    }

    @Override
    public void paintComponent(Graphics g) {
        Map<String, GameObject> m = GraphicsController.getGraphicalItems();
        for(Map.Entry<String, GameObject> entry : m.entrySet()){
            GameObject sprite = entry.getValue();
            g.drawImage(sprite.getImage(), sprite.getxPos(), sprite.getyPos(), null);
            Log.send(Log.type.INFO, TAG, "Painting component id: " + entry.getValue().getId());
        }
    }
}