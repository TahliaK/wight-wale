package graphics;
import actors.MovingObject;
import utils.Log;
import utils.XmlHandler;
import actors.GameObject;

import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.JPanel;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class Board extends JPanel implements ActionListener{

    private static String TAG = "Board";
    private MovingObject player; //1 object directly controlled by player
    private TAdapter keyListener;
    private GraphicsController _gController;
    //private XmlHandler<GameObject> GO_Xml;

    public Board(XmlHandler<GraphicsController> out) { //outputs gameController on init
        initBoard();
        //out.writeToXml(_gController, "", "GameController");
    }

    private void initBoard() {
        keyListener = new TAdapter();
        addKeyListener(keyListener);
        setFocusable(true);
        requestFocusInWindow();
        _gController = GraphicsController.activeController;
        if(_gController != null) {
            player = _gController.getMovingItemsById("skeleton"); //todo: make this xml based
        }
        //loadImages();
    }


    private void loadImages() { //only needed if not loading directly from GraphicsController.xml, will be removed

        /*GameObject gmOb = new GameObject();
        //GO_Xml = new XmlHandler<GameObject>(gmOb.getClass());
        //gmOb = GO_Xml.readFromXml("GameObjects", "skeleton.xml");
        gmOb.setImgFilename("Files/Images/background.png");
        gmOb.setId("background");
        gmOb.setHeight(_gController.getWindowHeight());
        gmOb.setWidth(_gController.getWindowWidth());
        gmOb.loadImageFile(false);
        gmOb.setVisibility(true);
        _gController.registerStatic(gmOb);

        MovingObject mvOb = new MovingObject();
        mvOb.setImgFilename("Files/Images/Skeleton.png");
        mvOb.setId("skeleton");
        mvOb.loadImageFile(true);
        mvOb.setVisibility(true);
        _gController.registerMoving(mvOb); */

        if(_gController != null)
            player = _gController.getMovingItemsById("skeleton");

        Log.send(Log.type.INFO, TAG, "LoadImage complete.");
    }


    @Override
    public void paintComponent(Graphics g) {
        Map<String, GameObject> s = _gController.getStaticItems();
        for(Map.Entry<String, GameObject> entry : s.entrySet()){
            GameObject obj = entry.getValue();
            if(obj.isVisible())
                g.drawImage(obj.getImage(), obj.getxPos(), obj.getyPos(), null);
        }
        Map<String, MovingObject> m = _gController.getMovingItems();
        for(Map.Entry<String, MovingObject> entry : m.entrySet()){ //loop renders all graphics registered items
            MovingObject sprite = entry.getValue();
            if(sprite.isVisible())
                g.drawImage(sprite.getImage(), sprite.getxPos(), sprite.getyPos(), null);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Log.send(Log.type.INFO, TAG, "ACTION PERFORMED.");
    }


    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            if(player != null)
                player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if(player != null)
                player.keyPressed(e);

            if(e.getKeyChar() == 'p'){
                Log.send(Log.type.DEBUG, TAG, "Player location: " + player.getxPos() + " | " + player.getyPos());
            }
        }
    }
}