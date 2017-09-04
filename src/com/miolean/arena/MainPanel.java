package com.miolean.arena;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainPanel extends JPanel implements KeyListener, MouseListener, WindowListener, MouseMotionListener {

    private Renderer renderer;
    private Handler handler;
    private JFrame window;

    private Entity[] entities;

    public static void main(String[] args) {

        new MainPanel().run();
    }

    public MainPanel() {

        entities = new Entity[0xFFFF];

        renderer = new Renderer(entities);
        handler = new Handler(entities);

        entities[1] = new Tank();

        window = new Window(this);


        this.setBackground(new Color(170, 170, 160));

        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        window.addWindowListener(this);

        window.setSize(1200, 700);
        window.setName("Arena");
        window.setLocation(300, 300);
        window.setVisible(true);
    }

    public void paint(Graphics g) {
        super.paint(g);
        render(g);
    }

    public void run() {
        System.out.println("Running...");

        //How long does a single tick last?
        double tickTime = 1000 * (1.0 / (double) Global.tickSpeed);
        System.out.println("[com.miolean.arena.MainPanel.run()] Found " + tickTime + " milliseconds per tick");

        while(true) {
            tickTime = 1000 * (1.0 / (double) Global.tickSpeed);
            long time = System.currentTimeMillis();
            handler.update();
            Global.time++;

            while(System.currentTimeMillis() < time + tickTime) {
                this.repaint();
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
        System.out.println("com.miolean.arena.Window closing...");
        System.exit(0);
    }
    @Override public void windowClosed(WindowEvent e) {}
    @Override public void windowIconified(WindowEvent e) {}
    @Override public void windowDeiconified(WindowEvent e) {}
    @Override public void windowActivated(WindowEvent e) {}
    @Override public void windowDeactivated(WindowEvent e) {}
}