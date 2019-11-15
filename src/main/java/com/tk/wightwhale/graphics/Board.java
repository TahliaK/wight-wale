package com.tk.wightwhale.graphics;
import com.tk.wightwhale.actors.MovingObject;
import com.tk.wightwhale.actors.PlayerControlledObject;
import com.tk.wightwhale.utils.Log;
import com.tk.wightwhale.utils.XmlHandler;
import com.tk.wightwhale.actors.GameObject;

import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.JPanel;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


/**
 * The Board does image display and actionListening
 */
public class Board extends JPanel implements ActionListener{

    /** Debug Tag  **/
    private static String TAG = "Board";
    /** Key action handler **/
    private TAdapter keyListener;
    /** GraphicsController **/
    private GraphicsController _gController = null;

    /**
     * Initializes the board
     * This fetches the active GraphicsController and should be done
     * AFTER initializing a GraphicsController.
     * @param out currently not used
     */
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

    /**
     * Called by com.tk.wightwhale.GameWindow to paint the game images on the screen
     * Should not be called directly by user
     * @param g Graphics instance
     */
    @Override
    public void paintComponent(Graphics g) {
        Map<String, GameObject> s = _gController.getStaticItems();
        for(Map.Entry<String, GameObject> entry : s.entrySet()){
            GameObject obj = entry.getValue();
            if(obj.isVisible())
                g.drawImage(obj.getImage(), obj.getxPos(), obj.getyPos(), null);
        }
        Map<String, MovingObject> m = _gController.getMovingItems();
        for(Map.Entry<String, MovingObject> entry : m.entrySet()){ //loop renders all com.tk.wightwhale.graphics registered items
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

    /**
     * Notifies that ActionEvent was received
     * @param e ActionEvent object
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Log.send(Log.type.INFO, TAG, "ACTION PERFORMED.");
    }


    /**
     * Passes on key-press and key-release information to PlayerControlledObjects
     */
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

            if(e.getKeyChar() == 'm'){ //debug - changes to second GameSegment
                Log.send(Log.type.DEBUG, TAG, "Changed to mapLocation 0-0-1");
                _gController.moveTo(0, 0, 1);
            }

            if(e.getKeyChar() == '['){ //debug - prints player location
                PlayerControlledObject pc2 = _gController.getPlayerControlledItemById("player");
                Log.send(Log.type.DEBUG, TAG, pc2.toString());
            }
        }
    }
}