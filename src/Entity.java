import java.awt.*;

/**
 * Created by commandm on 2/16/17.
 */
public abstract class Entity {

    protected int x;
    protected int velX;
    protected int accX;
    protected int y;
    protected int velY;
    protected int accY;
    protected int r;
    protected int velR;
    protected int accR;

    protected int width;
    protected int height;

    protected Handler handler;

    protected int location;

    protected void applyPhysics() {
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
    void initialize(Handler handler, int location) {
        this.handler = handler;
        this.location = location;
    }

    public int getLocation() { return location; }
}
