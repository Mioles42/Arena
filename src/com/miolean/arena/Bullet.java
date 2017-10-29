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
        velX = source.stats[Tank.STAT_BULLET_SPEED].val()/2 * Math.cos(r + 0.2*(Math.random() - .5));
        velY = source.stats[Tank.STAT_BULLET_SPEED].val()/2 * -Math.sin(r + 0.2*(Math.random() - .5));
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

        g.fillOval((int) x - width/2, (int) y - height/2, width, height);
    }

    @Override
    void update() {
        applyPhysics();
        if(velX < 1 && velY < 1) health = 0;
        if((x > ARENA_SIZE - BORDER) || (x < BORDER) || (y > ARENA_SIZE - BORDER) || (y < BORDER)) health = 0;
    }

    @Override
    boolean intersectsWith(Entity e) {
        if(e == null || e == source) return false; //Don't interact with your own source
        double distanceSquared = (x - e.x)*(x - e.x)+(y - e.y)*(y - e.y);
        return distanceSquared <= (width/2 - e.width/2)*(width/2 - e.width/2);
    }

    @Override
    void intersect(Entity e) {
        e.health -= source.stats[Tank.STAT_DAMAGE].val();
        health = 0;
    }
}
