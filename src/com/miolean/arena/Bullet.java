package com.miolean.arena;

import java.awt.*;

public class Bullet extends Entity {

    Tank source;

    public Bullet(Tank source) {
        this.source = source;

        x = source.x;
        y = source.y;
        r = source.r;
        velX = source.stats[Tank.STAT_BULLET_SPEED].val() * Math.cos(r);
        velY = source.stats[Tank.STAT_BULLET_SPEED].val() * -Math.sin(r);
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

        g.fillOval((int) x, (int) y, width, height);
    }

    @Override
    void update() {
        applyPhysics();
        if(velX < 1 && velY < 1) health = 0;
    }

    @Override
    boolean intersectsWith(Entity e) {
        return false;
    }

    @Override
    void intersect(Entity e) {

    }
}
