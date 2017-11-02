package com.miolean.arena;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static com.miolean.arena.Global.ARENA_SIZE;
import static com.miolean.arena.Global.BORDER;

public class Bullet extends Entity {

    Tank source;

    public Bullet(Tank source) {
        this.source = source;

        x = source.x;
        y = source.y;
        r = source.r;
        velX = (15 + source.stats[Tank.STAT_BULLET_SPEED].val()) * Math.cos(r + source.stats[Tank.STAT_BULLET_SPREAD].val()/256*(Math.random() - .5));
        velY = (15 + source.stats[Tank.STAT_BULLET_SPEED].val()) * -Math.sin(r + source.stats[Tank.STAT_BULLET_SPREAD].val()/256*(Math.random() - .5));
        velR = 0;
        accX = 0;
        accX = 0;
        accY = 0;
        accR = 0;
        width = 8;
        height = 8;

        health = 5;
    }


    @Override
    void render(Graphics g) {
        g.setColor(new Color(100 + source.stats[Tank.STAT_DAMAGE].val() / 2, 150 - source.stats[Tank.STAT_DAMAGE].val() / 2, 50));
        g.fillOval((int) x - width / 2, (int) y - height / 2, width, height);
        g.setColor(Color.black);
        g.drawOval((int) x - width / 2, (int) y - height / 2, width, height);
    }

    @Override
    void update() {
        applyPhysics();
        if (Math.abs(velX) < 1 && Math.abs(velY) < 1) health = 0;
        if ((x > ARENA_SIZE - BORDER) || (x < BORDER) || (y > ARENA_SIZE - BORDER) || (y < BORDER)) health = 0;
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
        e.health -= source.stats[Tank.STAT_DAMAGE].val();
        health = 0;
    }
}
