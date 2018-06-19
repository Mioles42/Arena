package com.miolean.arena.entities;

import com.miolean.arena.framework.Debug;

import javax.sound.midi.Track;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Cog extends Entity {

    private int value;

    public Cog(int value, Arena arena) {
        super((int) (5*Math.sqrt(value)), (int) (5*Math.sqrt(value)), 1, arena);
        this.value = value;
    }

    @Override
    public void renderBody(Graphics g, int x, int y) {
        g.setColor(Color.GRAY);
        try { g.setColor(new Color(256 - value*4, 128 + value*4, 150));}
        catch (Exception e) {System.out.println("Tried to set a cog's color with value " + value);}
        g.fillRect(x - getWidth()/2, y - getHeight()/2, getWidth(), getHeight());
        g.setColor(Color.black);
        g.drawRect( x - getWidth()/2, y - getHeight()/2, getWidth(), getHeight());
    }

    @Override
    public void update() {
        applyPhysics();
    }

    @Override
    public boolean intersectsWith(Entity e) {
        long marker = System.nanoTime();
        if(! (e instanceof Robot || e instanceof TrackerDot)) return false; //Don't interact with anything but Tanks

        //Assume an elliptical collision
        Ellipse2D.Double bounds = new Ellipse2D.Double(e.getX() - e.getWidth()/2, e.getY() - e.getHeight()/2, e.getWidth(), e.getHeight());
        boolean result = bounds.intersects(new Rectangle2D.Double(getX() - getWidth()/2, getY() - getHeight()/2, getWidth(), getHeight()));

        Debug.logTime("Cog intersections", marker - System.nanoTime());
        return result;
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
