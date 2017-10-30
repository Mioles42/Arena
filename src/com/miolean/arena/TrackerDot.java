package com.miolean.arena;

import java.awt.*;

public class TrackerDot extends Entity {

    int persistence;

    public TrackerDot(double x, double y, int persistence) {
        health = 42424242;
        this.x = x;
        this.y = y;
        this.persistence = persistence;
    }

    @Override
    void render(Graphics g) {
        g.setColor(Color.black);
        g.fillOval((int) x - 2, (int) y - 2, 4, 4);
    }

    @Override
    void update() {

        persistence--;
        if(persistence <= 0) handler.remove(getUUID());
    }

    @Override
    boolean intersectsWith(Entity e) {
        return false;
    }

    @Override
    void intersect(Entity e) {

    }
}
