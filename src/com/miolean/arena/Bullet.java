package com.miolean.arena;

import java.awt.*;
import java.awt.geom.Point2D;

import static com.miolean.arena.Global.ARENA_SIZE;
import static com.miolean.arena.Global.BORDER;

class Bullet extends Entity {

    private static final int ROGUE_SPEED = 8;
    private static final double ROGUE_TURN_SPEED = 0.1;
    private static final int ROGUE_OBSERVATION = 100;

    private Tank source;
    private Tank target;
    private int damage;

    Bullet(Tank source) {
        this.source = source;

        width = 8;
        height = 8;

        if(source != null) {
            x = source.x;
            y = source.y;
            r = source.r;
            damage = source.stats[Tank.STAT_DAMAGE].val();
            velX = (15 + source.stats[Tank.STAT_BULLET_SPEED].val()) * Math.cos(r + source.stats[Tank.STAT_BULLET_SPREAD].val() / 128.0 * (Math.random() - .5));
            velY = (15 + source.stats[Tank.STAT_BULLET_SPEED].val()) * -Math.sin(r + source.stats[Tank.STAT_BULLET_SPREAD].val() / 128.0 * (Math.random() - .5));
            velR = 0;
            accX = 0;
            accY = 0;
            accR = 0;
            health = 1;
        } else {
            x = Global.ARENA_SIZE * Math.random();
            y = Global.ARENA_SIZE * Math.random();

            health = 3;
            damage = 5;
        }
    }


    @Override
    void render(Graphics g) {


        if(target != null || source != null) g.setColor(new Color(100 + damage / 2, 150 - damage / 2, 50));
        else g.setColor(new Color(100, 100, 255));

        if(source != null) {
            g.fillOval((int) x - width / 2, (int) y - height / 2, width, height);
            g.setColor(Color.black);
            g.drawOval((int) x - width / 2, (int) y - height / 2, width, height);
        } else {

            double sinR = Math.sin(r + Math.PI);
            double cosR = Math.cos(r + Math.PI);
            double sinExtra = .25867;
            double cosExtra = .88007;

            //Fun fact: This is stolen from Tank's gun barrel.
            int[] XPoints = {
                    (int) (x+width*3*(cosR*cosExtra - sinR*sinExtra)),
                    (int) (x+width*3*(cosR*cosExtra + sinR*sinExtra)),
                    (int) (x)
            };

            int[] YPoints = {
                    (int) (y-width*3*(sinR*cosExtra + sinExtra*cosR)),
                    (int) (y-width*3*(sinR*cosExtra - sinExtra*cosR)),
                    (int) (y)

            };

            g.fillPolygon(XPoints, YPoints, 3);
            g.setColor(Color.black);
            g.drawPolygon(XPoints, YPoints, 3);
        }
    }

    void forward(int force) {
       //Translate polar force into cartesian vector
        this.accX = force * Math.cos(r) / 16; //Scaling!
        this.accY = force * -Math.sin(r) / 16;
    }


    @Override
    void update() {

        applyPhysics();

        if(source != null) {
            if (Math.abs(velX) < 1 && Math.abs(velY) < 1) health = 0;
            if ((x > ARENA_SIZE - BORDER) || (x < BORDER) || (y > ARENA_SIZE - BORDER) || (y < BORDER)) health = 0;
        } else if(target != null){
            double xdis = target.x - x;
            double ydis = target.y - y;


            double targetR = Math.atan(xdis/ydis) + Math.PI/2;

            if(ydis > 0) targetR += Math.PI;

            // (A - B) mod C = (A mod C - B mod C) mod C
            double rdis = (r - targetR) % (2*Math.PI);
            if(rdis < -Math.PI) rdis += (2*Math.PI);

            if(rdis > -2*velR) accR = -ROGUE_TURN_SPEED;
            else if(rdis < 2*velR) accR = ROGUE_TURN_SPEED;
            else accR = 0;

            forward(ROGUE_SPEED);

            //Meanwhile, look for closer targets

            for(int i = 0; i < ROGUE_OBSERVATION; i++) {

                Entity attemptedTarget = handler.getByUUID(UByte.rand(), UByte.rand());

                if (attemptedTarget != null && attemptedTarget instanceof Tank) {
                    double axdis = attemptedTarget.x - x;
                    double aydis = attemptedTarget.y - y;

                    if(axdis*axdis + aydis*aydis < xdis*xdis + ydis*ydis) {
                        target = (Tank) attemptedTarget;
                    }
                }
            }

        } else {

            velR = 0.1;

            for(int i = 0; i < ROGUE_OBSERVATION; i++){
                Entity attemptedTarget = handler.getByUUID(UByte.rand(), UByte.rand());
                if(attemptedTarget != null && attemptedTarget instanceof Tank) {
                    target = (Tank) attemptedTarget;
                    System.out.println("Found target");
                }
            }
        }
    }

    @Override
    boolean intersectsWith(Entity e) {
        if( e instanceof TrackerDot) return false;
        if(e == null || e == source || (e instanceof Bullet && ((Bullet) e).source == source)) return false; //Don't interact with your own source

        Rectangle extendedBounds = new Rectangle(new Point((int)x, (int)y));
        extendedBounds.add(new Point((int) (x-velX-accX), (int) (y-velY-accY)));
        extendedBounds.setLocation((int) (extendedBounds.getX() - e.width), (int) (extendedBounds.getY() - e.height));
        extendedBounds.setSize((int) (extendedBounds.getWidth() + 2*e.width), (int) (extendedBounds.getHeight() + 2*e.height));

        if(extendedBounds.contains(new Point2D.Double(e.x, e.y))) {
            double slope = (velY + accY) / (velX + accX);

            double k = y - slope * x;

            double a = e.x - (e.y-k)/slope;
            double b = e.y - e.x*slope - k;
            double g2 = (a*a*b*b) / (a*a + b*b);

            if(g2 < (width/2 + e.width/2)*(width/2 + e.width/2)) return true;
        }

        return false;
    }

    @Override
    void intersect(Entity e) {
        e.health -= damage;
        health--;
    }

    @Override
    void onBirth() {

    }

    @Override
    void onDeath() {

    }
}
