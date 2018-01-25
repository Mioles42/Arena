package com.miolean.arena;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

class Cog extends Entity {

    private int value;

    Cog(int value) {
        this.value = value;
        width = (int) (5*Math.sqrt(value));
        height = width;
        health = 1;
    }

    @Override
    void render(Graphics g) {
        g.setColor(new Color(256 - value*4, 128 + value*4, 150));
        g.fillRect((int) x - width/2, (int) y - height/2, width, height);
        g.setColor(Color.black);
        g.drawRect((int) (x - width/2), (int) (y - height/2), width, height);
    }

    @Override
    void update() {
        applyPhysics();
    }

    @Override
    boolean intersectsWith(Entity e) {
        if(! (e instanceof Robot)) return false; //Don't interact with anything but Tanks

        //Assume an elliptical collision
        Ellipse2D.Double bounds = new Ellipse2D.Double(e.x - e.width/2, e.y - e.height/2, e.width, e.height);
        return bounds.intersects(new Rectangle2D.Double(x - width/2, y - height/2, width, height));
    }

    @Override
    void intersect(Entity e) {
        ((Robot) e).cogs += value;
        this.health = 0;
    }

    @Override
    void onBirth() {

    }

    @Override
    void onDeath() {

    }
}
