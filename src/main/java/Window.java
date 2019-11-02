import graphics.Board;
import graphics.GcElements;
import graphics.GraphicsController;
import utils.Log;
import utils.XmlHandler;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Window extends JFrame {

    private Board _board;
    private GraphicsController _gController;
    //private XmlHandler<GcElements>  gcXml;
    private static XmlHandler<GraphicsController> importer;

    public Window() {

        /* Set up GraphicsController */
        _gController = new GraphicsController();
        /* Import GraphicsController settings */ /*
        gcXml = new XmlHandler<>(GcElements.class);
        GcElements temp = gcXml.readFromXml("", "GC_Settings.xml");
        if(temp != null)
            _gController.setSettings(temp); */
        importer = new XmlHandler<>(GraphicsController.class);
        _gController = importer.readFromXml("", "GraphicsController.xml");
        if(_gController == null){
            _gController = new GraphicsController(true);
            Log.send(Log.type.WARNING, "Window", "GraphicsController import not found.");
        }
        _gController.registerActive();
        /* Initialise display */
        _board = new Board();
        initUI();
        importer.writeToXml(_gController, "", "GraphicsController");
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
            Window ex = new Window();
            ex.setVisible(true);
        });
    }
}

