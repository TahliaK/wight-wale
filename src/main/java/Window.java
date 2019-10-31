import utils.Log;
import graphics.Board;
import graphics.GraphicsController;

import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Window extends JFrame {

    private Board b;
    private GraphicsController _gController;

    public Window() {

        _gController = new GraphicsController();
        _gController.init();
        _gController.registerActive();
        b = new Board();
        initUI();
    }

    private void initUI() {

        add(b);

        setSize(_gController.getWindowWidth(), _gController.getWindowHeight());

        setTitle(_gController.getWindowTitle());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Timer timer = new Timer(_gController.getStepSize(), e -> {
            _gController.step();
            b.repaint();
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

