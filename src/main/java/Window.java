import utils.Log;
import graphics.Board;
import graphics.GraphicsController;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Window extends JFrame {

    private Board b;

    public Window() {

        GraphicsController.init();
        b = new Board();
        initUI();
    }

    public void repaint(){
        b.repaint();
    }

    private void initUI() {

        add(b);

        setSize(GraphicsController.getWindowWidth(), GraphicsController.getWindowHeight());

        setTitle(GraphicsController.getWindowTitle());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Window ex = new Window();
            ex.setVisible(true);

        });
    }
}

