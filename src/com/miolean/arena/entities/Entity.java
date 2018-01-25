package com.miolean.arena.entities;

import com.miolean.arena.Handler;

import java.awt.*;

import static com.miolean.arena.Global.ARENA_SIZE;
import static com.miolean.arena.Global.BORDER;

/**
 * Created by commandm on 2/16/17.
 * Anything that moves according to set physics is an Entity.
 * Entities can update themselves every tick and render themselves.
 * They can also interact with Entities with which they intersect.
 */

public abstract class Entity {

    //Motion components:
    private double x; //X position, in pixels
    private double y; //Y position, in pixels
    private double r; //Rotation, in radians
    private double velX; //X velocity, in pixels per tick.
    private double velY; //Y velocity, in pixels per tick.
    private double velR; //Rotational velocity, in degrees per tick.
    private double accX; //X acceleration, in pixels per tick per tick.
    private double accY; //Y acceleration, in pixels per tick per tick.
    private double accR; //Rotational acceleration, in degrees per tick per tick.

    private final static double DRAG = 0.1; //The amount that an Entity naturally slows down each tick, per unit of velocity.
    private final static double RDRAG = 0.5;

    //Size components:
    private int width;
    private int height;

    //Entities can also be destroyed:
    private double health = 1;

    //Other things:
    private Handler handler;
    private int uuid;


    Entity(int width, int height, int health, Handler handler) {
        this.width = width;
        this.height = height;
        this.health = health;
        this.handler = handler;
        this.uuid = handler.add(this);
    }


    void applyPhysics() {
        r %= 6.28;

        velX -= DRAG * velX;
        velY -= DRAG * velY;
        velR -= RDRAG * velR;

        velX += accX;
        velY += accY;
        velR += accR;

        x += velX;
        y += velY;
        r += velR;

        if(x > ARENA_SIZE - BORDER) x = ARENA_SIZE - BORDER;
        if(x < BORDER) x = BORDER;
        if(y > ARENA_SIZE - BORDER) y = ARENA_SIZE - BORDER;
        if(y < BORDER) y = BORDER;
    }

    public abstract void render(Graphics g);
    public abstract void update();
    public abstract boolean intersectsWith(Entity e);
    public abstract void intersect(Entity e);
    public abstract void onBirth();
    public abstract void onDeath();
    public abstract String toHTML();

    boolean isAlive() {return ! (handler == null);}
    public double getX() { return x; }
    public double getY() { return y; }
    public double getR() { return r; }
    public double getVelX() { return velX; }
    public double getVelY() { return velY; }
    public double getVelR() { return velR; }
    public double getAccX() { return accX; }
    public double getAccY() { return accY; }
    public double getAccR() { return accR; }
    public double getHealth() { return health; }
    public int getUUID() { return uuid; }
    public int getWidth() { return width; }
    public int getHeight() { return height;}
    public Handler getHandler() { return handler; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setR(double r) { this.r = r; }
    public void setVelX(double velX) { this.velX = velX; }
    public void setVelY(double velY) { this.velY = velY; }
    public void setVelR(double velR) { this.velR = velR; }
    public void setAccX(double accX) { this.accX = accX; }
    public void setAccY(double accY) { this.accY = accY; }
    public void setAccR(double accR) { this.accR = accR; }
    public void setHealth(double health) { this.health = health;}

    public void die() {
        health = 0;
        handler = null;
        onDeath();
    }
    public void damage(double amount) {health -= amount;}
    public void heal(double amount) {health += amount;}
    public void add(Entity e) {handler.add(e);}

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
