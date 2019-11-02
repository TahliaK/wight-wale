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
    private XmlHandler<GameObject> GO_Xml;

    public Board() {
        initBoard();
    }

    private void initBoard() {
        keyListener = new TAdapter();
        addKeyListener(keyListener);
        setFocusable(true);
        requestFocusInWindow();
        _gController = GraphicsController.activeController;
        player = _gController.getMovingItemsById("skeleton");
        //player.loadImageFile(true);
        loadImages();
        //XmlHandler.GraphicsControllerToXML(_gController);
    }

    private void loadImages() { //only needed if not loading directly from GraphicsController.xml, will be removed
        GameObject gmOb = new GameObject();
        GO_Xml = new XmlHandler<GameObject>(gmOb.getClass());
        //gmOb = GO_Xml.readFromXml("GameObjects", "skeleton.xml");
        gmOb.setImgFilename("Files/Images/Skeleton.png");
        gmOb.setId("skeleton");
        gmOb.loadImageFile(true);
        _gController.register(gmOb);
        player = _gController.getMovingItemsById(gmOb.getId());

        Log.send(Log.type.INFO, TAG, "LoadImage complete.");
    }

    @Override
    public void paintComponent(Graphics g) {
        Map<String, MovingObject> m = _gController.getMovingItems();
        for(Map.Entry<String, MovingObject> entry : m.entrySet()){ //loop renders all graphics registered items
            MovingObject sprite = entry.getValue();
            g.drawImage(sprite.getImage(), sprite.getxPos(), sprite.getyPos(), null);
        }
        Map<String, GameObject> s = _gController.getStaticItems();
        for(Map.Entry<String, GameObject> entry : s.entrySet()){
            GameObject obj = entry.getValue();
            g.drawImage(obj.getImage(), obj.getxPos(), obj.getyPos(), null);
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
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
        }
    }
}