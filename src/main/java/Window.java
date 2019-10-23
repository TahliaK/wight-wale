import graphics.Board;
import graphics.GraphicsController;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Window extends JFrame {

    public Window() {

        GraphicsController.init();
        initUI();
    }

    private void initUI() {

        add(new Board());

        setSize(330, 330);

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

