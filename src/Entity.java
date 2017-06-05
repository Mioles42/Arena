import java.awt.*;

/**
 * Created by commandm on 2/16/17.
 */
public abstract class Entity {

    protected int x;
    protected int y;
    protected int r;
    protected int vel;
    protected int velLimit = 201;
    protected int drag;
    protected int acc;
    protected int velR;

    protected int width;
    protected int height;

    protected Handler handler;

    protected int uuid;

    protected void applyPhysics() {

        System.out.println("Applying physics to " + this);

        vel -= drag;
        vel += acc;
        if(vel > velLimit) vel = velLimit;
        if(vel < -velLimit) vel = -velLimit;

        x += vel * (int) Math.cos(r);
        y += vel * (int) Math.sin(r);
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
