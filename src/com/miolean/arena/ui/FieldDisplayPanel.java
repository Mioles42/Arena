package com.miolean.arena.ui;

import com.miolean.arena.entities.*;
import com.miolean.arena.entities.Robot;
import com.miolean.arena.framework.Option;
import com.miolean.arena.framework.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static com.miolean.arena.framework.Option.ARENA_SIZE;
import static com.miolean.arena.framework.Option.BORDER;

public class FieldDisplayPanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener, ActiveRobotListener{

    private com.miolean.arena.framework.Renderer renderer;

    java.util.List<ActiveRobotListener> listenerList = new ArrayList<>();

    Entity viewholder;
    Field field;

    private boolean isRunning = true;


    public FieldDisplayPanel(Field field) {

        this.field = field;

        requestFocus();

        renderer = new Renderer(field);

        viewholder = new ControlledRobot(300, 300, field);
        field.add(viewholder);

        Bullet rogue = new Bullet(null, field);
        rogue.setX(200);
        rogue.setY(200);
        field.add(rogue);

        com.miolean.arena.entities.Robot dummy = new Robot(Option.class.getClassLoader().getResourceAsStream("gen/cain.ergo"), field);
        dummy.setHealth(256);

        field.add(dummy);

        this.setBackground(new Color(170, 170, 160));

        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        requestFocus();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }



    public void setViewholder(int x, int y) {
        Entity e = field.atLocation(x, y);

        if (e == null) {
            if (viewholder instanceof ControlledRobot) {
                viewholder.setX(x);
                viewholder.setY(y);
            } else {
                e = new ControlledRobot(x, y, field);
                field.add(e);
                viewholder = e;
            }

        } else {
            if (viewholder instanceof ControlledRobot) field.remove(viewholder);
            viewholder = e;
        }

        if (viewholder instanceof com.miolean.arena.entities.Robot && !(viewholder instanceof ControlledRobot))
            alertInfoholderChange((Robot)e);
        alertViewholderChange(e);
    }

    public void setViewholder(Entity e) {

        if(e == null) throw new NumberFormatException("Null viewholder.");

        if (viewholder instanceof ControlledRobot) field.remove(viewholder);
        viewholder = e;

        if (viewholder instanceof com.miolean.arena.entities.Robot && !(viewholder instanceof ControlledRobot))
            alertInfoholderChange((Robot)e);
        alertViewholderChange(e);
    }

    private void render(Graphics g) {

        g.setColor(Color.BLACK);
        g.drawString((int) (1000.0/ Option.updateCycle) + "tk/s", 15, 25);
        g.drawString("Time:" + field.getTime() + "tks", 15, 45);
        g.drawString("Entities: " + field.getEntities().size() + " (Cogs: " + field.getCogs().size() + ", Tanks: " + field.getRobots().size() + ")", 15, 65);
        g.drawOval(this.getWidth()/2, this.getHeight()/2, 2, 2);

        g.translate((int) (-viewholder.getX() + this.getWidth()/2), (int) (-viewholder.getY() + this.getHeight()/2));

        g.setColor(Color.GRAY);
        for(int i = 0; i < ARENA_SIZE / 64; i++) {
            g.drawLine(i*64, BORDER, i*64, ARENA_SIZE -BORDER);
            g.drawLine(BORDER, i*64, ARENA_SIZE - BORDER, i*64);
        }

        g.setColor(Color.RED);
        g.drawRect(10, 10, ARENA_SIZE - BORDER, ARENA_SIZE - BORDER);

        renderer.tick(g);

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
            for(com.miolean.arena.entities.Robot t: field.getTopRobots()) {
                System.out.println("Robot " + t + " [Fitness " + t.getFitness() + "]");
            }
        }
    }
    @Override public void keyPressed(KeyEvent e) {
        char key = e.getKeyChar();
        if(key == 'q') Option.KEY[Option.KEY_Q] = true;
        if(key == 'w') Option.KEY[Option.KEY_W] = true;
        if(key == 'e') Option.KEY[Option.KEY_E] = true;
        if(key == 'r') Option.KEY[Option.KEY_R] = true;
        if(key == 'a') Option.KEY[Option.KEY_A] = true;
        if(key == 's') Option.KEY[Option.KEY_S] = true;
        if(key == 'd') Option.KEY[Option.KEY_D] = true;
        if(key == 'f') Option.KEY[Option.KEY_F] = true;
        if(key == ' ') Option.KEY[Option.KEY_SPACE] = true;
    }
    @Override public void keyReleased(KeyEvent e) {
        char key = e.getKeyChar();
        if(key == 'q') Option.KEY[Option.KEY_Q] = false;
        if(key == 'w') Option.KEY[Option.KEY_W] = false;
        if(key == 'e') Option.KEY[Option.KEY_E] = false;
        if(key == 'r') Option.KEY[Option.KEY_R] = false;
        if(key == 'a') Option.KEY[Option.KEY_A] = false;
        if(key == 's') Option.KEY[Option.KEY_S] = false;
        if(key == 'd') Option.KEY[Option.KEY_D] = false;
        if(key == ' ') Option.KEY[Option.KEY_SPACE] = false;
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.out.println("com.miolean.arena.GeneralDisplayPanel closing...");
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
            Cog cog = new Cog(30, field);
            cog.setX(x);
            cog.setY(y);
            field.add(cog);
        }

        if(e.getButton() == MouseEvent.BUTTON2) {
            com.miolean.arena.entities.Robot creation = new com.miolean.arena.entities.Robot(Option.class.getResourceAsStream("cain.ergo"), field);
            creation.setX(x);
            creation.setY(y);
            creation.setName("creation");
            field.add(creation);
        }
    }
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}

    public void addActiveRobotListener(ActiveRobotListener l) {
        listenerList.add(l);
    }

    public void removeActiveRobotListener(ActiveRobotListener l) {
        listenerList.remove(l);
    }

    @Override
    public void viewholderChanged(Entity e) {
        viewholder = e;
    }

    @Override
    public void infoholderChanged(Robot e) {
        //We don't really care about this
    }

    public void alertViewholderChange(Entity e) {
        for(ActiveRobotListener arl: listenerList) arl.viewholderChanged(e);
    }
    public void alertInfoholderChange(Robot e) {
        for(ActiveRobotListener arl: listenerList) arl.infoholderChanged(e);
    }
}