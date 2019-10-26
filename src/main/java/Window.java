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

    public Window() {

        GraphicsController.init();
        b = new Board();
        initUI();
    }

    private void initUI() {

        add(b);

        setSize(GraphicsController.getWindowWidth(), GraphicsController.getWindowHeight());

        setTitle(GraphicsController.getWindowTitle());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Timer timer = new Timer(GraphicsController.getStepSize(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                GraphicsController.step();
                b.repaint();
            }
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

