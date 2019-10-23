package graphics;
import utils.Log;
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
        String filename = "src/main/resources/spaceship.png";

        GameObject gmOb = new GameObject();
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