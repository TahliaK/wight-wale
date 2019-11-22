package com.tk.wightwhale;

import com.tk.wightwhale.actors.BackgroundGameObject;
import com.tk.wightwhale.actors.GameObject;
import com.tk.wightwhale.geometry.RectangleInfo;
import com.tk.wightwhale.graphics.Board;
import com.tk.wightwhale.graphics.GraphicsController;
import com.tk.wightwhale.levels.LevelController;
import com.tk.wightwhale.utils.XmlHandler;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * Main class and Window handler
 */
public class GameWindow extends JFrame {

    /** Board to display com.tk.wightwhale.graphics **/
    private Board _board;
    /** GraphicsController singleton - manages com.tk.wightwhale.graphics loop and settings **/
    private GraphicsController _gController;
    /** LevelController singleton - manages current displayed level **/
    private LevelController _levelController;
    /** GraphicsController importer **/
    public XmlHandler<GraphicsController> importer;

    /**
     * Default Constructor
     * Imports LevelController and GraphicsController if available.
     * Also creates and initializes board
     */
    public GameWindow() {

        /* Import LevelController */
        _levelController = LevelController.createFromXml("./Files/Levels", 1);
        LevelController.setActiveController(_levelController);

        /* Import GraphicsController settings */
        GraphicsController.activeLevel =  _levelController.levels[0];
        _gController = new GraphicsController(true);
        _gController.registerActive();

        /* Initialise display */
        _board = new Board(importer);
        initUI();
    }

    private void initUI() {

        add(_board);
        setSize(_gController.getWindowWidth(), _gController.getWindowHeight());
        setTitle(_gController.getWindowTitle());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Timer timer = new Timer(_gController.getStepSize(), e -> {
            _gController.step();
            _board.repaint();
        });

        timer.start();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            GameWindow ex = new GameWindow();
            ex.setVisible(true);
        });
    }
}

