package com.miolean.arena.entities;

import java.awt.*;

import static com.miolean.arena.framework.Option.ARENA_SIZE;
import static com.miolean.arena.framework.Option.BORDER;

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
    private double mass = 1;

    //Entities can also be destroyed:
    private double health = 1;
    private boolean alive = true;
    private long age = 0;

    //Other things:
    private Field field;

    //ID management
    private int uuid = -1;


    Entity(int width, int height, int health, Field field) {
        this.width = width;
        this.height = height;
        this.health = health;
        this.field = field;
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

    void repel(Entity e) {
        double compoundVel = Math.sqrt(velX*velX + velY*velY);
        double rposX = x - e.getX();
        double rposY = y  - e.getY();

        //Lay down the law for impossibly direct collisions
        double velX = (Math.abs(this.velX) < 1)? this.velX+1:this.velX;
        double velY = (Math.abs(this.velY) < 1)? this.velY+1:this.velY;
        if(rposX == 0) rposX = 1;
        if(rposY == 0) rposY = 1;


        double collisionAngle = Math.atan(rposY/rposX);
        double forceAngle = ((velX > 0)? Math.PI:0) + Math.atan(velY/velX);
        double reflectAngle = 2*collisionAngle-forceAngle;

        double percentMass = mass / (mass + e.getMass()); //basically for translating momentum into force

        move(reflectAngle,compoundVel * (1-percentMass));
        e.move( reflectAngle + Math.PI,compoundVel * (percentMass));
    }

    void accelerate(double direction, double magnitude) {
        accX += magnitude * Math.cos(direction) / mass;
        accY += magnitude * Math.sin(direction) / mass;
    }

    void move(double direction, double magnitude) {
        velX += magnitude * Math.cos(direction) / mass;
        velY += magnitude * Math.sin(direction) / mass;
    }

    void tick() {
        if(health <= 0 || field == null) die();
        else if(age > 0) update();
        age++;
    }

    public abstract void render(Graphics g);
    protected abstract void update();
    public abstract boolean intersectsWith(Entity e);
    public abstract void intersect(Entity e);
    protected abstract void onBirth();
    protected abstract void onDeath();
    public abstract String toHTML();

    public boolean isAlive() {return alive;}
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
    public double getMass() { return mass; }
    public long getAge() { return age; }
    public int getUUID() { return uuid; }
    public int getWidth() { return width; }
    public int getHeight() { return height;}
    public Field getField() { return field; }
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
    public void setWidth(int width) { this.width = width;}
    public void setHeight(int height) { this.height = height;}
    public void setMass(double mass) { this.mass = mass;}
    protected void setUUID(int uuid) {
        if(isAlive()) throw new IllegalStateException("Cannot change UUID of live entity");
        else this.uuid = uuid;
    }


    public final void die() {
        alive = false;
        onDeath();
        field.remove(this);
    }

    public final void appear(int uuid) {
        alive = true;
        this.uuid = uuid;
        onBirth();
    }

    public void damage(double amount) {health -= amount;}
    public void heal(double amount) {health += amount;}
    public void add(Entity e) {field.add(e);}

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
