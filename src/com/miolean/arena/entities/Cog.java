package com.miolean.arena.entities;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Cog extends Entity {

    private int value;

    public Cog(int value, Field field) {
        super((int) (5*Math.sqrt(value)), (int) (5*Math.sqrt(value)), 1, field);
        this.value = value;
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.GRAY);
        try { g.setColor(new Color(256 - value*4, 128 + value*4, 150));}
        catch (Exception e) {System.out.println("Tried to set a cog's color with value " + value);}
        g.fillRect((int) getX() - getWidth()/2, (int) getY() - getHeight()/2, getWidth(), getHeight());
        g.setColor(Color.black);
        g.drawRect((int) (getX() - getWidth()/2), (int) (getY() - getHeight()/2), getWidth(), getHeight());
    }

    @Override
    public void update() {
        applyPhysics();
    }

    @Override
    public boolean intersectsWith(Entity e) {
        if(! (e instanceof Robot)) return false; //Don't interact with anything but Tanks

        //Assume an elliptical collision
        Ellipse2D.Double bounds = new Ellipse2D.Double(e.getX() - e.getWidth()/2, e.getY() - e.getHeight()/2, e.getWidth(), e.getHeight());
        return bounds.intersects(new Rectangle2D.Double(getX() - getWidth()/2, getY() - getHeight()/2, getWidth(), getHeight()));
    }

    @Override
    public void intersect(Entity e) {
        if(e instanceof Robot) {
            ((Robot) e).setCogs(((Robot) e).getCogs() + value);
            die();
        }
    }

    @Override
    public void onBirth() {

    }

    @Override
    public void onDeath() {

    }

    @Override
    public String toHTML() {
        return "<font color=\"orange\">" + value + "-Cog";
    }
}
