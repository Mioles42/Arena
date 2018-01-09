package com.miolean.arena;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainPanel extends JPanel implements Runnable, KeyListener, MouseListener, WindowListener, MouseMotionListener {

    private Renderer renderer;
    private Handler handler;
    private Distributor distributor;

    private Entity viewholder;

    private boolean isRunning = true;

    //Let's also measure whether we are using the extra tick time to render (if not, adding tick speed will do nothing)
    boolean renderPoint = false;

    public static void main(String[] args) {
        Thread gameThread = new Thread(new MainPanel());
        gameThread.run();
    }

    private MainPanel() {

        requestFocus();

        Entity[] entities = new Entity[0xFFFF];

        renderer = new Renderer(entities);
        handler = new Handler(entities);
        distributor = new Distributor(handler);

        handler.add(new ControlledTank(300, 300));
        viewholder = entities[0];

        Bullet rogue = new Bullet(null);
        handler.add(rogue);
        rogue.x = 200;
        rogue.y = 200;

        Entity dummy = new Tank("eve");
        dummy.health = 256;

        handler.add(dummy);

        JFrame window = new Window(this);


        this.setBackground(new Color(170, 170, 160));

        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        window.addWindowListener(this);

        window.setSize(1200, 700);
        window.setName("Arena");
        window.setLocation(20, 200);
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


        while(isRunning) {
            renderPoint = false;
            tickTime = 1000 * (1.0 / (double) Global.tickSpeed);
            long time = System.currentTimeMillis();
            handler.update();
            distributor.distribute();
            Global.time++;
            this.repaint();

            while(System.currentTimeMillis() < time + tickTime) {
                this.repaint();
                renderPoint = true;
            }
        }
    }

    private void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawString(Global.tickSpeed + "tk/s", 15, 25);
        g.drawString("Time:" + Global.time + "tks", 15, 45);
        g.drawString("Entities:" + handler.numEntities, 15, 65);
        if(! renderPoint) g.drawString("Tick limit reached", 15, 85);
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
        requestFocus();

        int x = (int) (e.getX() + viewholder.x - this.getWidth()/2);
        int y = (int) (e.getY() + viewholder.y - this.getHeight()/2);

        Entity newHolder = handler.entityAtLocation(x, y);
        if(newHolder == null) {
            if (viewholder instanceof ControlledTank) {
                viewholder.x = x;
                viewholder.y = y;
            } else {
                newHolder = new ControlledTank(x, y);
                handler.add(newHolder);
                viewholder = newHolder;
            }
        } else {
            if (viewholder instanceof ControlledTank) viewholder.health = 0;
            viewholder = newHolder;
        }

    }
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void windowOpened(WindowEvent e) {}
    @Override public void windowClosing(WindowEvent e) {
        isRunning = false;
        System.exit(0);
    }
    @Override public void windowClosed(WindowEvent e) {}
    @Override public void windowIconified(WindowEvent e) {}
    @Override public void windowDeiconified(WindowEvent e) {}
    @Override public void windowActivated(WindowEvent e) {}
    @Override public void windowDeactivated(WindowEvent e) {}
}