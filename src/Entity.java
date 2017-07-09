import java.awt.*;

/**
 * Created by commandm on 2/16/17.
 */
public abstract class Entity {

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

    int width;
    int height;

    Handler handler;

    int uuid;

    void applyPhysics() {

        x -= drag * velX;
        y -= drag * velY;
        r -= drag * velR;

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
    void initialize(Handler handler, int uuid) {
        this.handler = handler;
        this.uuid = uuid;
    }

    public int getUUID() { return uuid; }
}
