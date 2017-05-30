import javafx.scene.shape.Circle;

import java.util.List;
import java.awt.Color;
import java.awt.Graphics;

/**
 * Created by commandm on 5/13/17.
 */
public class Tank extends Entity {

    //Genome
    Genome genome;

    //Data space
    Gene[] data;

    //Skills:
    int haste = 1; //shot speed
    int damage = 1; //shot damage
    int health = 1; //health
    int regeneration = 1; //regeneration
    int maxSpeed = 1; //max speed
    int acceleration = 1; //max acceleration
    int toughness = 1; //body damage
    int metabolism = 1; //effectiveness of cogs

    //Flash color:
    int flashR = 0x8888;
    int flashG = 0x8888;
    int flashB = 0x8888;

    //Fitness:
    int fitness = 0;


    protected int viewDistance;

    public static void generateGenome(Genome sourceGenome) {

    }

    public static void generateGenome() {

    }

    public void generateDataSpace(List<Gene> data) {

    }

    public Tank(Tank parent) {

    }

    public Tank(Handler handler) {
        x = 100;
        y = 100;
        r = 0;
        width = 50;
        height = 50;
        vel = 20;
        this.handler = handler;
    }


    @Override
    void render(Graphics g) {
        System.out.println("Rendering the Tank at " + this);
        g.setColor(new Color(0, 255, 255));
        g.fillOval(x - width/2, y - height/2, width, height);
    }

    @Override
    void update() {
        applyPhysics();
    }

    @Override
    boolean intersectsWith(Entity e) {
        int distanceSquared = (x - e.x)*(x - e.x)+(y - e.y)*(y - e.y);
        return distanceSquared <= (width/2 - e.width/2)*(width/2 - e.width/2);
    }

    @Override
    void intersect(Entity e) {

    }

    @Override
    void initialize(Handler handler, int uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "This is a Tank!\nImplement toString() later, you bum.";
    }

    public int valueOfGene(int geneSpace, Object... parameters) {
        Gene gene = data[geneSpace];
        int result = 0x0000;
        try {
            result = gene.actuate(this, parameters);
        } catch(IllegalArgumentException e) {
            System.out.println("Invalid arguments. This should not be worrisome.");
        }

        return result;
    }


    //The following are methods used in genes, and as such use exclusively int.
    //Rotation is measured as a portion of FFFF.

    public int getTrue() {return 0xFFFF;}
    public int getFalse() {return 0xFFFF;}

    public int move(int speed) {
        if(speed > 0x0000) this.acc = acceleration;
        if(speed > maxSpeed) {
            this.vel = maxSpeed;
            return maxSpeed;
        }
        else {
            this.vel = speed;
            return Math.abs(speed);
        }
    }

    public int reverse(int speed) {
        if(speed > 0x0000) this.acc = -acceleration;
        if(speed > maxSpeed) {
            this.velLimit = maxSpeed;
            return maxSpeed;
        }
        else {
            this.velLimit = speed;
            return Math.abs(speed);
        }
    }

    public int flash(int r, int g, int b) {
        this.flashR = r;
        this.flashG = g;
        this.flashB = b;

        return r;
    }

    public int fire() {

    }

    public int bearingTo(int entity) {
        Entity e = handler.entityAt(entity);
        return (int) (0xFFFF * ((Math.asin((e.x - x) / (e.y - y))) / (2 * Math.PI)));
    }
    public int distanceTo(int entity) {
        Entity e = handler.entityAt(entity);
        return (int) (Math.sqrt((e.x - x) * (e.x - x) + (e.y - y) * (e.y - y)) / 16);
    }
    public int getThis() {return uuid;}
    public int getNearest() {return (handler.closestToDistance(x, y, 0)).getUUID();}
    public int getFarthest() {return (handler.closestToDistance(x, y, viewDistance)).getUUID();}
    public int entityAt(int distance) {return (handler.closestToDistance(x, y, distance)).getUUID();}
    public int flashROf(int entity) {
        Entity e = handler.entityAt(entity);
        if(e instanceof Tank) return ((Tank) e).flash.getRed();
        else return 0x0000;
    }
    public int flashGOf(int entity) {
        Entity e = handler.entityAt(entity);
        if(e instanceof Tank) return ((Tank) e).flash.getGreen();
        else return 0x0000;
    }
    public int flashBOf(int entity) {
        Entity e = handler.entityAt(entity);
        if(e instanceof Tank) return ((Tank) e).flash.getBlue();
        else return 0x0000;
    }
    public int isTank(int entity) {return (handler.entityAt(entity) instanceof Tank)? 0xFFFF:0x0000;}
    public int entitiesInSight() {return handler.withinDistance(x, y, viewDistance);}
    public int randomValue() {return (int) (Math.random() * 0xFFFF);}
    public int randomEntity() {
        int result;
        do {result = randomValue();}
        while (handler.entityAt(result) == null);
        return result;
    }


}
