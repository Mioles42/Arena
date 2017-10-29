package com.miolean.arena;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainPanel extends JPanel implements Runnable, KeyListener, MouseListener, WindowListener, MouseMotionListener {

    private Renderer renderer;
    private Handler handler;
    private Distributer distributer;
    private JFrame window;

    private Entity[] entities;
    private Entity viewholder;

    public static void main(String[] args) {
        Thread gameThread = new Thread(new MainPanel());
        gameThread.run();
    }

    public MainPanel() {

        entities = new Entity[0xFFFF];

        renderer = new Renderer(entities);
        handler = new Handler(entities);
        distributer = new Distributer(handler);

        handler.add(new ControlledTank(new Tank("player")));
        viewholder = entities[0];

        Entity dummy = new Tank("adam");
        dummy.health = 10;

        handler.add(dummy);

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

        requestFocus();
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
            distributer.distribute();
            Global.time++;

            while(System.currentTimeMillis() < time + tickTime) {
                this.repaint();
            }
        }
    }

    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawString(Global.tickSpeed + "tk/s", 15, 25);
        g.drawString("Time:" + Global.time + "tks", 15, 45);
        g.drawString("Entities:" + handler.numEntities, 15, 65);
        g.drawOval(this.getWidth()/2, this.getHeight()/2, 2, 2);

        g.translate((int) (-viewholder.x + this.getWidth()/2), (int) (-viewholder.y + this.getHeight()/2));
        renderer.render(g);
    }


    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyPressed(KeyEvent e) {
        char key = e.getKeyChar();
        if(key == 'q') Global.KEY[Global.KEY_Q] = true;
        if(key == 'w') Global.KEY[Global.KEY_W] = true;
        if(key == 'e') Global.KEY[Global.KEY_E] = true;
        if(key == 'r') Global.KEY[Global.KEY_R] = true;
        if(key == 'a') Global.KEY[Global.KEY_A] = true;
        if(key == 's') Global.KEY[Global.KEY_S] = true;
        if(key == 'd') Global.KEY[Global.KEY_D] = true;
        if(key == 'f') Global.KEY[Global.KEY_F] = true;
        if(key == ' ') Global.KEY[Global.KEY_SPACE] = true;
    }
    @Override public void keyReleased(KeyEvent e) {
        char key = e.getKeyChar();
        if(key == 'q') Global.KEY[Global.KEY_Q] = false;
        if(key == 'w') Global.KEY[Global.KEY_W] = false;
        if(key == 'e') Global.KEY[Global.KEY_E] = false;
        if(key == 'r') Global.KEY[Global.KEY_R] = false;
        if(key == 'a') Global.KEY[Global.KEY_A] = false;
        if(key == 's') Global.KEY[Global.KEY_S] = false;
        if(key == 'd') Global.KEY[Global.KEY_D] = false;
        if(key == ' ') Global.KEY[Global.KEY_SPACE] = false;
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.out.println("com.miolean.arena.Window closing...");
            System.exit(0);
        }
    }
    @Override public void mouseClicked(MouseEvent e) {
        System.out.println("(" +
                (e.getX() + viewholder.x - this.getWidth()/2) + ", " +
                (e.getY() + viewholder.y - this.getHeight()/2) + ")");
    }
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