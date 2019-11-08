import graphics.Board;
import graphics.GraphicsController;
import levels.LevelController;
import utils.Log;
import utils.XmlHandler;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.Timer;

public class GameWindow extends JFrame {

    private Board _board;
    private GraphicsController _gController;
    private LevelController _lController;
    public XmlHandler<GraphicsController> importer;

    public GameWindow() {

        /* Import LevelController */
        _lController = LevelController.createFromXml("./Files/Levels", 2, 2);

        /* Import GraphicsController settings */
        GraphicsController.activeLevel = _lController;
        if(_gController == null){
            _gController = new GraphicsController(true);
        }
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

        String s = String.valueOf(_gController.getStepSize());

        Log.send(Log.type.VALUE, "GameWindow", "Step size", "41.6", s);

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

