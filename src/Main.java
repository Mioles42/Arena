import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Main implements KeyListener, MouseListener, WindowListener, MouseMotionListener {

    private Renderer renderer;
    private Handler handler;
    private JFrame window;

    private Entity[] entities;

    public static void main(String[] args) {

        new Main().run();
    }

    public Main() {

        entities = new Entity[0xFFFF];

        renderer = new Renderer(entities);
        handler = new Handler(entities);

        entities[1] = new Tank(handler);

        window = new JFrame();

        JPanel panel = new JPanel() {
            public void paint(Graphics g) {
                super.paint(g);
                render(g);
            }
        };

        panel.setBackground(new Color(170, 170, 160));

        panel.addKeyListener(this);
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
        window.addWindowListener(this);

        window.setSize(700, 700);
        window.setName("Arena");
        window.setLocation(300, 300);

        window.add(panel);
        window.setVisible(true);
    }



    public void run() {
        System.out.println("Running...");

        //How long does a single tick last?
        double tickTime = 1000 * (1.0 / (double) Global.tickSpeed);
        System.out.println("[Main.run()] Found " + tickTime + " milliseconds per tick");

        while(true) {
            long time = System.currentTimeMillis();
            handler.update();
            Global.time++;

            while(System.currentTimeMillis() < time + tickTime) {
                window.repaint();
            }
        }
    }

    public void render(Graphics g) {
        renderer.render(g);
    }


    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyPressed(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void windowOpened(WindowEvent e) {}
    @Override public void windowClosing(WindowEvent e) {
        System.out.println("Window closing...");
        System.exit(0);
    }
    @Override public void windowClosed(WindowEvent e) {}
    @Override public void windowIconified(WindowEvent e) {}
    @Override public void windowDeiconified(WindowEvent e) {}
    @Override public void windowActivated(WindowEvent e) {}
    @Override public void windowDeactivated(WindowEvent e) {}
}