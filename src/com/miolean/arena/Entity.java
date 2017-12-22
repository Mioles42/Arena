package com.miolean.arena;

import java.awt.*;

/**
 * Created by commandm on 2/16/17.
 * Anything that moves according to set physics is an Entity.
 * Entities can update themselves every tick and render themselves.
 * They can also interact with Entities with which they intersect.
 */
@SuppressWarnings("unused")
abstract class Entity {

    //Motion components:
    double x; //X position, in pixels
    double y; //Y position, in pixels
    double r; //Rotation, in degrees
    double velX; //X velocity, in pixels per tick.
    double velY; //Y velocity, in pixels per tick.
    double velR; //Rotational velocity, in degrees per tick.
    double accX; //X acceleration, in pixels per tick per tick.
    double accY; //Y acceleration, in pixels per tick per tick.
    double accR; //Rotational acceleration, in degrees per tick per tick.

    private final static double DRAG = 0.1; //The amount that an Entity naturally slows down each tick, per unit of velocity.
    private final static double RDRAG = 0.5;

    //Size components:
    int width;
    int height;

    //Entities can also be destroyed:
    int health = 1;

    //Other things:
    Handler handler; //The Handler which manages this Entity and which can be asked to destroy it. Null if dead.
    int uuidMost; //A Universally Unique ID for use by other Entities (namely Tanks) to reference it. Can totally be negative.
    int uuidLeast;

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

    }

    abstract void render(Graphics g);
    abstract void update();
    abstract boolean intersectsWith(Entity e);
    abstract void intersect(Entity e);
    //abstract void onCreate();
    //abstract void onDestroy();

    short getUUID() {
        short uuid = (short) (((short) uuidMost) << 8);
        uuid += uuidLeast;
        return uuid;
    }

    abstract void onBirth();
    abstract void onDeath();
}
