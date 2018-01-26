package com.miolean.arena;

import com.miolean.arena.entities.Bullet;
import com.miolean.arena.entities.Cog;
import com.miolean.arena.entities.ControlledRobot;
import com.miolean.arena.entities.Entity;
import com.miolean.arena.entities.Robot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static com.miolean.arena.Global.ARENA_SIZE;
import static com.miolean.arena.Global.BORDER;

public class MainPanel extends JPanel implements Runnable, KeyListener, MouseListener, WindowListener, MouseMotionListener {

    private com.miolean.arena.Renderer renderer;
    private Handler handler;
    private com.miolean.arena.Window window;

    Entity viewholder;

    private boolean isRunning = true;


    public static void main(String[] args) {
        Thread gameThread = new Thread(new MainPanel());
        gameThread.run();
    }

    private MainPanel() {

        requestFocus();

        Entity[] entities = new Entity[256];

        renderer = new com.miolean.arena.Renderer(entities);
        handler = new Handler(entities);
      
        window = new Window(this, handler.topRobots, entities, handler.getRobots());

        entities[0] = new ControlledRobot(300, 300, handler);
        viewholder = entities[0];

        Bullet rogue = new Bullet(null, handler);
        handler.add(rogue);
        rogue.setX(200);
        rogue.setY(200);

        com.miolean.arena.entities.Robot dummy = new Robot(Global.class.getClassLoader().getResourceAsStream("gen/cain.ergo"), handler);
        window.setActiveTank(dummy);
        dummy.setHealth(256);

        handler.add(dummy);

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

        long lastUpdate = System.currentTimeMillis();
        long lastRender = System.currentTimeMillis();
        long lastDisplay = System.currentTimeMillis();
        long lastDistribute = System.currentTimeMillis();

        while(isRunning) {

            long time = System.currentTimeMillis();

            if(time > lastUpdate + Global.updateCycle) {
                handler.update();
                lastUpdate = time;
            }
            time = System.currentTimeMillis();

            if(time > lastRender + Global.renderCycle) {
                repaint();
                lastRender = time;
            }
            time = System.currentTimeMillis();

            if(time > lastDisplay + Global.displayCycle) {
                window.display();
                lastDisplay = time;
            }
            time = System.currentTimeMillis();

            if(time > lastDistribute + Global.distributeCycle) {
                handler.distribute();
                lastDistribute = time;
            }
        }
    }

    public void setViewholder(int x, int y) {
        Entity e = handler.entityAtLocation(x, y);

        if (e == null) {
            if (viewholder instanceof ControlledRobot) {
                viewholder.setX(x);
                viewholder.setY(y);
            } else {
                e = new ControlledRobot(x, y, handler);
                handler.add(e);
                viewholder = e;
            }

        } else {
            if (viewholder instanceof ControlledRobot) handler.remove(viewholder.getUUID());
            viewholder = e;
        }

        if (viewholder instanceof com.miolean.arena.entities.Robot && !(viewholder instanceof ControlledRobot))
            window.setActiveTank((com.miolean.arena.entities.Robot) viewholder);
    }

    public void setViewholder(Entity e) {

        if(e == null) throw new NumberFormatException("Null viewholder.");

        if (viewholder instanceof ControlledRobot) handler.remove(viewholder.getUUID());
        viewholder = e;

        if (viewholder instanceof com.miolean.arena.entities.Robot && !(viewholder instanceof ControlledRobot))
            window.setActiveTank((com.miolean.arena.entities.Robot) viewholder);
    }

    private void render(Graphics g) {

        g.setColor(Color.BLACK);
        g.drawString((int) (1000.0/Global.updateCycle) + "tk/s", 15, 25);
        g.drawString("Time:" + Global.time + "tks", 15, 45);
        g.drawString("Entities: " + handler.numEntities + " (Cogs: " + handler.numCogs + ", Tanks: " + handler.numTanks + ")", 15, 65);
        g.drawOval(this.getWidth()/2, this.getHeight()/2, 2, 2);

        g.translate((int) (-viewholder.getX() + this.getWidth()/2), (int) (-viewholder.getY() + this.getHeight()/2));

        g.setColor(Color.GRAY);
        for(int i = 0; i < ARENA_SIZE / 64; i++) {
            g.drawLine(i*64, BORDER, i*64, ARENA_SIZE -BORDER);
            g.drawLine(BORDER, i*64, ARENA_SIZE - BORDER, i*64);
        }

        g.setColor(Color.RED);
        g.drawRect(10, 10, ARENA_SIZE - BORDER, ARENA_SIZE - BORDER);

        renderer.render(g);

        g.translate((int) -(-viewholder.getX() + this.getWidth()/2), (int) -(-viewholder.getY() + this.getHeight()/2));

        g.setColor(new Color(255, 100, 100, 200));
        g.fillRect(15, getHeight()-60, (int) viewholder.getHealth(), 20);
        g.setColor(Color.BLACK);
        g.drawRect(15, getHeight()-60, (int) viewholder.getHealth(), 20);
        if(viewholder.getHealth() < 20) g.drawString((int) viewholder.getHealth() + "", 18 + (int) viewholder.getHealth(), getHeight()-45);
        else g.drawString(viewholder.getHealth() + "", 18, getHeight()-45);

        if(viewholder instanceof com.miolean.arena.entities.Robot) {
            g.setColor(new Color(100, 100, 255, 200));
            g.fillRect(15, getHeight() - 90, (int)((com.miolean.arena.entities.Robot)viewholder).getCogs(), 20);
            g.setColor(Color.BLACK);
            String label = String.format("%2.2f", ((com.miolean.arena.entities.Robot)viewholder).getCogs());
            if(((com.miolean.arena.entities.Robot)viewholder).getCogs() < 20) g.drawString(label, 18 + (int) ((com.miolean.arena.entities.Robot)viewholder).getCogs(), getHeight()-75);
            else g.drawString(label, 18, getHeight()-75);
            g.drawRect(15, getHeight() - 90, (int) ((com.miolean.arena.entities.Robot)viewholder).getCogs(), 20);

            g.setColor(new Color(100, 255, 100, 200));
            g.fillRect(15, getHeight() - 120, (int) ((com.miolean.arena.entities.Robot)viewholder).getFitness(), 20);
            g.setColor(Color.BLACK);
            label = String.format("%2.2f", ((com.miolean.arena.entities.Robot)viewholder).getFitness());
            if(((com.miolean.arena.entities.Robot)viewholder).getFitness() < 20) g.drawString(label, (int) (18 + ((com.miolean.arena.entities.Robot)viewholder).getFitness()), getHeight()-105);
            else g.drawString(label, 18, getHeight()-105);
            g.drawRect(15, getHeight() - 120, (int) ((com.miolean.arena.entities.Robot)viewholder).getFitness(), 20);
        }


    }

    @Override public void keyTyped(KeyEvent e) {
        char key = e.getKeyChar();
        if(key == 'l') {
            System.out.println("============Top robots============");
            for(com.miolean.arena.entities.Robot t: handler.topRobots) {
                System.out.println("Robot " + t + " [Fitness " + t.getFitness() + "]");
            }
        }
    }
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

        int x = (int) (e.getX() + viewholder.getX() - this.getWidth() / 2);
        int y = (int) (e.getY() + viewholder.getY() - this.getHeight() / 2);

        if(e.getButton() == MouseEvent.BUTTON1) {
            if (!this.hasFocus()) {
                this.requestFocus();
                return;
            }

            setViewholder(x, y);
        }

        if(e.getButton() == MouseEvent.BUTTON3) {
            Cog cog = new Cog(100, handler);
            cog.setX(x);
            cog.setY(y);
            handler.add(cog);
        }

        if(e.getButton() == MouseEvent.BUTTON2) {
            com.miolean.arena.entities.Robot creation = new com.miolean.arena.entities.Robot(Global.class.getResourceAsStream("cain.ergo"), handler);
            creation.setX(x);
            creation.setY(y);
            creation.setName("creation");
            handler.add(creation);
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
        for(Robot t: handler.topRobots) {
            System.out.println(t);
        }
        System.exit(0);
    }
    @Override public void windowClosed(WindowEvent e) {}
    @Override public void windowIconified(WindowEvent e) {}
    @Override public void windowDeiconified(WindowEvent e) {}
    @Override public void windowActivated(WindowEvent e) {}
    @Override public void windowDeactivated(WindowEvent e) {}
}