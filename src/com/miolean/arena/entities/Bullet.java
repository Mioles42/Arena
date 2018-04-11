package com.miolean.arena.entities;

import com.miolean.arena.framework.Global;

import java.awt.*;
import java.awt.geom.Point2D;

import static com.miolean.arena.framework.Global.ARENA_SIZE;
import static com.miolean.arena.framework.Global.BORDER;

public class Bullet extends Entity {

    private static final int ROGUE_SPEED = 8;
    private static final int SIZE = 8;
    private static final double ROGUE_TURN_SPEED = 0.1;
    private static final int ROGUE_OBSERVATION = 100;

    private Robot source;
    private Robot target;
    private int damage;

    public Bullet(Robot source, Field field) {
        super(SIZE, SIZE, 1, field);
        this.source = source;

        if(source != null) {
            setX(source.getX());
            setY(source.getY());
            damage = source.stats[Robot.STAT_DAMAGE].val();
            setVelX((15 + source.stats[Robot.STAT_BULLET_SPEED].val()) * Math.cos(source.getR() + source.stats[Robot.STAT_BULLET_SPREAD].val() / 128.0 * (Global.random.nextFloat() - .5)));
            setVelY((15 + source.stats[Robot.STAT_BULLET_SPEED].val()) * -Math.sin(source.getR() + source.stats[Robot.STAT_BULLET_SPREAD].val() / 128.0 * (Global.random.nextFloat() - .5)));
        } else {
            setX(Global.ARENA_SIZE * Global.random.nextFloat());
            setY(Global.ARENA_SIZE * Global.random.nextFloat());
            damage = 5;
        }

        setMass(3);
    }


    @Override
    public void render(Graphics g) {


        if(target != null || source != null) g.setColor(new Color(100 + damage / 2, 150 - damage / 2, 50));
        else g.setColor(new Color(100, 100, 255));

        double x = getX();
        double y = getY();

        if(source != null) {
            g.fillOval((int) x - SIZE / 2, (int) y - SIZE / 2, SIZE, SIZE);
            g.setColor(Color.black);
            g.drawOval((int) x - SIZE / 2, (int) y - SIZE / 2, SIZE, SIZE);
        } else {

            double sinR = Math.sin(getR() + Math.PI);
            double cosR = Math.cos(getR() + Math.PI);
            double sinExtra = .25867;
            double cosExtra = .88007;

            //Fun fact: This is stolen from Robot's gun barrel.
            int[] XPoints = {
                    (int) (x+SIZE*3*(cosR*cosExtra - sinR*sinExtra)),
                    (int) (x+SIZE*3*(cosR*cosExtra + sinR*sinExtra)),
                    (int) (x)
            };

            int[] YPoints = {
                    (int) (y-SIZE*3*(sinR*cosExtra + sinExtra*cosR)),
                    (int) (y-SIZE*3*(sinR*cosExtra - sinExtra*cosR)),
                    (int) (y)

            };

            g.fillPolygon(XPoints, YPoints, 3);
            g.setColor(Color.black);
            g.drawPolygon(XPoints, YPoints, 3);
        }
    }

    void forward(int force) {
       //Translate polar force into cartesian vector
        setAccX(force * Math.cos(getR()) / 16); //Scaling!
        setAccY(force * -Math.sin(getR()) / 16);
    }


    @Override
    public void update() {

        applyPhysics();

        if(source != null) {
            if (Math.abs(getVelX()) < 1 && Math.abs(getVelY()) < 1) getField().remove(getUUID());
            if ((getX() > ARENA_SIZE - BORDER) || (getX() < BORDER) || (getY() > ARENA_SIZE - BORDER) || (getY() < BORDER)) getField().remove(getUUID());
        } else if(target != null){
            double xdis = target.getX() - getX();
            double ydis = target.getY() - getY();


            double targetR = Math.atan(xdis/ydis) + Math.PI/2;

            if(ydis > 0) targetR += Math.PI;

            // (A - B) mod C = (A mod C - B mod C) mod C
            double rdis = (getR() - targetR) % (2*Math.PI);
            if(rdis < -Math.PI) rdis += (2*Math.PI);

            if(rdis > -2*getVelR()) setAccR(-ROGUE_TURN_SPEED);
            else if(rdis < 2*getVelR()) setAccR(ROGUE_TURN_SPEED);
            else setAccR(0);

            forward(ROGUE_SPEED);

            //Meanwhile, look for closer targets

            for(int i = 0; i < ROGUE_OBSERVATION; i++) {

                Entity attemptedTarget = getField().fromUUID(Global.random.nextInt(Field.MAX_ENTITIES));

                if (attemptedTarget != null && attemptedTarget instanceof Robot) {
                    double axdis = attemptedTarget.getX() - getX();
                    double aydis = attemptedTarget.getY() - getY();

                    if(axdis*axdis + aydis*aydis < xdis*xdis + ydis*ydis) {
                        target = (Robot) attemptedTarget;
                    }
                }
            }

        } else {

            setVelR(0.1);

            for(int i = 0; i < ROGUE_OBSERVATION; i++){
                Entity attemptedTarget = getField().fromUUID(Global.random.nextInt(Field.MAX_ENTITIES));
                if(attemptedTarget != null && attemptedTarget instanceof Robot) {
                    target = (Robot) attemptedTarget;
                    System.out.println("Found target");
                }
            }
        }
    }

    @Override
    public boolean intersectsWith(Entity e) {
        if( e instanceof TrackerDot) return false;
        if(e == null || e == source || (e instanceof Bullet && ((Bullet) e).source == source)) return false; //Don't interact with your own source


        //This is going to be tedious if we don't make up some shorthands.
        //I know, memory usage and so forth, but it'll be OK
        double x = getX();
        double y = getY();
        double velX = getVelX();
        double velY = getVelY();
        double accX = getAccX();
        double accY = getAccY();

        Rectangle extendedBounds = new Rectangle(new Point((int)x, (int)y));
        extendedBounds.add(new Point((int) (x-velX-accX), (int) (y-velY-accY)));
        extendedBounds.setLocation((int) (extendedBounds.getX() - e.getWidth()), (int) (extendedBounds.getY() - e.getHeight()));
        extendedBounds.setSize((int) (extendedBounds.getWidth() + 2*e.getWidth()), (int) (extendedBounds.getHeight() + 2*e.getHeight()));

        if(extendedBounds.contains(new Point2D.Double(e.getX(), e.getY()))) {
            double slope = (velY + accY) / (velX + accX);

            double k = y - slope * x;

            double a = e.getX() - (e.getY()-k)/slope;
            double b = e.getY() - e.getX()*slope - k;
            double g2 = (a*a*b*b) / (a*a + b*b);

            if(g2 < (SIZE/2 + e.getWidth()/2)*(SIZE/2 + e.getWidth()/2)) {
                return true;
            }

        }

        return false;
    }

    @Override
    public void intersect(Entity e) {

        repel(e);
        e.damage(damage);
        damage(1);
    }

    @Override
    public void onBirth() {

    }

    @Override
    public void onDeath() {

    }

    @Override
    public String toHTML() {
        //TODO
        return null;
    }
}
