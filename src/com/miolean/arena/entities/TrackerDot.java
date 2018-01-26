package com.miolean.arena.entities;

import com.miolean.arena.Handler;

import java.awt.*;

public class TrackerDot extends Entity {


    public TrackerDot(double x, double y, int persistence, Handler handler) {
        super(4, 4, persistence, handler);
        setX(x);
        setY(y);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.black);
        g.fillOval((int) getX() - 2, (int) getY() - 2, 4, 4);
    }

    @Override
    public void update() {
        damage(1);
    }

    @Override
    public boolean intersectsWith(Entity e) {
        return false;
    }

    @Override
    public void intersect(Entity e) {}

    @Override
    public void onBirth() {}

    @Override
    public void onDeath() {}

    @Override
    public String toHTML() {
        //TODO
        return null;
    }
}
