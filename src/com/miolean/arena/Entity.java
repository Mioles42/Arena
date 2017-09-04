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

    double drag; //The amount that an Entity naturally slows down each tick, per unit of velocity.

    //Size components:
    int width;
    int height;

    //Other things:
    private Handler handler; //The Handler which manages this Entity and which can be asked to destroy it.
    private int uuidMost; //A Universally Unique ID for use by other Entities (namely Tanks) to reference it.
    private int uuidLeast;

    void applyPhysics() {

        velX -= drag * velX;
        velY -= drag * velY;
        velR -= drag * velR;

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

    void initialize(Handler handler, byte uuidLeast, byte uuidMost) {
        this.handler = handler;
        this.uuidLeast = uuidLeast;
        this.uuidMost = uuidMost;
    }

    short getUUID() {
        short uuid = (short) (((short) uuidMost) << 8);
        uuid += uuidLeast;
        return uuid;
    }
}
