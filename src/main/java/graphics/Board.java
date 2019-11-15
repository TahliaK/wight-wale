package graphics;
import actors.MovingObject;
import actors.PlayerControlledObject;
import utils.ControlScheme;
import utils.Log;
import utils.XmlHandler;
import actors.GameObject;
import levels.LevelController;

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
    private TAdapter keyListener;
    private GraphicsController _gController = null;
    private PlayerControlledObject temp_PCO = null;

    public Board(XmlHandler<GraphicsController> out) { //outputs gameController on init
        initBoard();
            //out.writeToXml(_gController, "", "GameController");
    }

    private void initBoard() {
        keyListener = new TAdapter();
        addKeyListener(keyListener);
        setFocusable(true);
        requestFocusInWindow();
        _gController = GraphicsController.activeGraphicsController;
    }


    /*private void loadImages() { //only needed if not loading directly from GraphicsController.xml, will be removed

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
        _gController.registerMoving(mvOb); */ /*

        if(_gController != null) {
            MovingObject player = _gController.getMovingItemsById("skeleton");
        }

        Log.send(Log.type.INFO, TAG, "LoadImage complete.");
    } */


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
        Map<String, PlayerControlledObject> p = _gController.getPlayerControlledItems();
        for(Map.Entry<String, PlayerControlledObject> entry : p.entrySet()){
            PlayerControlledObject sprite = entry.getValue();
            if(sprite.isVisible()){
                g.drawImage(sprite.getImage(), sprite.getxPos(), sprite.getyPos(), null);
            }
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
            for(Map.Entry<String, PlayerControlledObject> pcItem : _gController.getPlayerControlledItems().entrySet()){
                pcItem.getValue().keyReleased(e);
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            for(Map.Entry<String, PlayerControlledObject> pcItem : _gController.getPlayerControlledItems().entrySet()){
                PlayerControlledObject pc = pcItem.getValue();
                pc.keyPressed(e);
                if(e.getKeyChar() == 'p'){
                    Log.send(Log.type.DEBUG, TAG, "Player location: " + pc.getxPos() + " | " + pc.getyPos());

                }
            }

            if(e.getKeyChar() == 'm'){
                Log.send(Log.type.DEBUG, TAG, "Changed to mapLocation 0-0-1");
                _gController.moveTo(0, 0, 1);
            }

            if(e.getKeyChar() == '['){
                //Log.send(Log.type.DEBUG, TAG, temp_PCO.toString());
                PlayerControlledObject pc2 = _gController.getPlayerControlledItemById("player");
                Log.send(Log.type.DEBUG, TAG, pc2.toString());
            }
        }
    }
}