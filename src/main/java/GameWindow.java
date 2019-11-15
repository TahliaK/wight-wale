import graphics.Board;
import graphics.GraphicsController;
import levels.LevelMap;
import levels.LevelController;
import utils.XmlHandler;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * Main class & Window handler
 */
public class GameWindow extends JFrame {

    private Board _board;
    private GraphicsController _gController;
    private LevelController _levelController;
    private LevelMap activeLevel;
    public XmlHandler<GraphicsController> importer;

    /**
     * Default Constructor
     * Imports LevelController & GraphicsController if available.
     * Also creates and initializes board
     */
    public GameWindow() {

        /* Import LevelController */
        _levelController = LevelController.createFromXml("./Files/Levels", 1);
        LevelController.setActiveController(_levelController);

        //_levelController.loadFromXML(new File("Files/LevelMap.xml"), 0);
        activeLevel = _levelController.levels[0];

        /* Import GraphicsController settings */
        GraphicsController.activeLevel = activeLevel;
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

