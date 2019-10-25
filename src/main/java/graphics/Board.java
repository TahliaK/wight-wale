package graphics;
import actors.MovingObject;
import utils.Log;
import utils.XmlHandler;
import actors.GameObject;

import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;
import javax.swing.JPanel;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class Board extends JPanel implements ActionListener {

    private String TAG = "Board";
    private MovingObject player;
    private TAdapter keyListener;

    public Board() {
        initBoard();
    }

    private void initBoard() {
        keyListener = new TAdapter();
        addKeyListener(keyListener);
        setFocusable(true);
        requestFocusInWindow();
        loadImages();
    }

    private void loadImages() {
        GameObject gmOb = (GameObject) XmlHandler.XmlToGameObject("GameObjects/skeleton.xml");
        gmOb.loadImageFile(true);
        GraphicsController.register(gmOb);
        player = GraphicsController.getGraphicalItemsById(gmOb.getId());

        Log.send(Log.type.INFO, TAG, "LoadImage complete.");
    }

    @Override
    public void paintComponent(Graphics g) {
        Map<String, MovingObject> m = GraphicsController.getGraphicalItems();
        for(Map.Entry<String, MovingObject> entry : m.entrySet()){ //loop renders all graphics registered items
            MovingObject sprite = entry.getValue();
            g.drawImage(sprite.getImage(), sprite.getxPos(), sprite.getyPos(), null);
            Log.send(Log.type.INFO, TAG, "Painting component id: " + entry.getValue().getId());
        }

        if(player != null)  //draw player figure
            g.drawImage(player.getImage(), player.getxPos(), player.getyPos(), null);

        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Log.send(Log.type.INFO, TAG, "ACTION PERFORMED.");
        //GraphicsController.step();
        //repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
            GraphicsController.step();
            repaint();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
            GraphicsController.step();
            repaint();
        }
    }
}