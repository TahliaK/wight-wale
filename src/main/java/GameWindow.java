import graphics.Board;
import graphics.GraphicsController;
import utils.Log;
import utils.XmlHandler;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.Timer;

public class GameWindow extends JFrame {

    private Board _board;
    private GraphicsController _gController;
    public XmlHandler<GraphicsController> importer;

    public GameWindow() {

        /* Import GraphicsController settings */
        importer = new XmlHandler<>(GraphicsController.class);
        _gController = importer.readFromXml("", "GraphicsController.xml");
        if(_gController == null){ //if none found, use defaults & attempt to import GC_Settings.xml
            _gController = new GraphicsController(true);
            Log.send(Log.type.WARNING, "Window", "GraphicsController import not found.");
        }
        _gController.registerActive();
        _gController.init(false);

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

