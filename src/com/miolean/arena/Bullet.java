package com.miolean.arena;

import java.awt.*;

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
        accY = 0;
        accR = 0;
        width = 8;
        height = 8;

        health = 5;
    }


    @Override
    void render(Graphics g) {
        g.setColor(new Color(200, 150, 0));
        g.fillOval((int) x - width/2, (int) y - height/2, width, height);

        g.drawOval((int) (x - width/2), (int) (y - height/2), width*2, height*2);
    }

    @Override
    void update() {
        applyPhysics();
        if(Math.abs(velX) < 1 && Math.abs(velY) < 1) health = 0;
        if((x > ARENA_SIZE - BORDER) || (x < BORDER) || (y > ARENA_SIZE - BORDER) || (y < BORDER)) health = 0;
    }

    @Override
    boolean intersectsWith(Entity e) {
        if( e instanceof TrackerDot) return false;
        if(e == null || e == source) return false; //Don't interact with your own source
        double distanceSquared = (x - e.x)*(x - e.x)+(y - e.y)*(y - e.y);
        double minDistance = (width/2.0 + e.width/2.0) * (width/2.0 + e.width/2.0);
        if(e instanceof Tank) {
            System.out.println(minDistance);
        }

        return distanceSquared <= minDistance;

    }

    @Override
    void intersect(Entity e) {
        System.out.println("Bullet collision with " + e);
        e.health -= source.stats[Tank.STAT_DAMAGE].val();
        health = 0;
    }
}
