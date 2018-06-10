package com.miolean.arena.entities;

import com.miolean.arena.framework.Debug;
import com.miolean.arena.framework.Option;
import com.miolean.arena.framework.UByte;
import com.miolean.arena.genetics.*;

import java.awt.*;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;
import java.util.Stack;

import static com.miolean.arena.entities.Arena.ARENA_SIZE;
import static com.miolean.arena.framework.UByte.ub;
import static com.miolean.arena.framework.UByte.ubDeepCopy;

/**
 * Created by commandm on 5/13/17.
 */

/* Memories:
 *
 * K: Genome (all possible genes)
 * U: Super memory; executed rarely
 * P: Program memory; executed regularly
 * S: Storage; mundane
 * W: Registries; used in calculations
 * I: No memory; refers to immediate (literal) values
 */

public abstract class Robot extends Entity implements Comparable<Robot>{

    //General constants
    protected static final int DEFAULT_STAT_VALUE = 10;
    protected static final int MAX_BULLET_RECHARGE = 40;
    protected static final int MAX_HEAL_RECHARGE = 64;
    protected static final int INITIAL_COGS = 40;
    protected static final int HARD_COG_DEFICIT_LIMIT = -40;
    protected static final double DIFFICULTY = 0.05;


    //Stat constants
    static final int STAT_FIRE_SPEED = 0x0; //shot speed
    static final int STAT_DAMAGE = 0x1; //shot damage
    static final int STAT_REGEN = 0x2; //regeneration
    static final int STAT_SPEED = 0x3; //acceleration (translates to speed due to drag)
    static final int STAT_TOUGHNESS = 0x4; //body damage
    static final int STAT_ROTATE_SPEED = 0x5;
    static final int STAT_BULLET_SPEED = 0x6;
    static final int STAT_MAX_HEALTH = 0x7;
    static final int STAT_BULLET_SPREAD = 0x8;


    protected UByte[] stats = new UByte[9];


    //Color:
    private UByte hue = ub(100);

    //State variables:
    private double fitness = 0;
    private int generation = 0;
    private double cogs = 0;



    private String name = "";

    private long lastFireTime = getArena().getTime();
    private long lastHealTime = getArena().getTime();

    //Create a totally blank Robot (for whatever reason)
    Robot(Arena arena) {
        super(Option.robotSize.getValue(), Option.robotSize.getValue(), 10, arena);
        setMass(10);
    }

    @Override
    public void render(Graphics g) {

        int SIZE = getWidth();

        g.setColor(Color.black);
        g.drawOval((int) (getX() - SIZE/2), (int) (getY() - SIZE/2), SIZE, SIZE);


        //Trigonometric functions are expensive so let's use math to minimize
        //how many times we have to calculate them.
        //And yeah, r is rotation. Not radius. Sorry about that, but my keyboard
        //doesn't have a theta. (not that Java would support it probably anyway)
        double sinR = Math.sin(getR());
        double cosR = Math.cos(getR());
        double sind5 = .47943; //As in sine decimal five, or sin(0.5). There is no pi and that is not a mistake.
        double cosd5 = .87758;
        double sind2 = .19867;
        double cosd2 = .98007;
        //sin(r +- u) = sin(r)cos(u) +- sin(u)cos(r)
        //cos(r +- u) = cos(r)cos(u) +- sin(r)sin(u)
        //tan(r +- u) = sin(r +- u) / cos(r +- u) [not that division is so much better]

        double x = getX();
        double y = getY();

        //Wheels!
        int[] wheelXPoints = {
                (int) (x+SIZE*.7*(sinR*cosd5 - sind5*cosR)),
                (int) (x+SIZE*.7*(sinR*cosd5 + sind5*cosR)),
                (int) (x-SIZE*.7*(sinR*cosd5 - sind5*cosR)),
                (int) (x-SIZE*.7*(sinR*cosd5 + sind5*cosR))
        };

        int[] wheelYPoints = {
                (int) (y+SIZE*.7*(cosR*cosd5 + sinR*sind5)),
                (int) (y+SIZE*.7*(cosR*cosd5 - sinR*sind5)),
                (int) (y-SIZE*.7*(cosR*cosd5 + sinR*sind5)),
                (int) (y-SIZE*.7*(cosR*cosd5 - sinR*sind5))
        };

        //Gun barrel!
        int[] gunXPoints = {
                (int) (x+SIZE*.7*(cosR*cosd2 - sinR*sind2)), //y + size * a little bit more * cos(r - .5)
                (int) (x+SIZE*.7*(cosR*cosd2 + sinR*sind2)), //y + size * a little bit more * cos(r - .5)
                (int) (x)
        };

        int[] gunYPoints = {
                (int) (y-SIZE*.7*(sinR*cosd2 + sind2*cosR)),
                (int) (y-SIZE*.7*(sinR*cosd2 - sind2*cosR)),
                (int) (y)

        };


        g.setColor(Color.DARK_GRAY);
        g.fillPolygon(wheelXPoints, wheelYPoints, 4); //Wheels
        g.setColor(Color.BLACK);
        g.drawPolygon(wheelXPoints, wheelYPoints, 4); //Wheel outline

        g.setColor(Color.GRAY);
        g.fillPolygon(gunXPoints, gunYPoints, 3); //Barrel

        double healthPercent = (getHealth() / stats[STAT_MAX_HEALTH].val());
        if(healthPercent < 0) healthPercent = 0;
        if(healthPercent > 1) healthPercent = 1;
        g.setColor(new Color((int) (healthPercent * 100 + 100), (int) (healthPercent * 100 + 100), (int) (healthPercent * 100 + 100)));
        g.fillOval((int) x - SIZE/2, (int) y - SIZE/2, SIZE, SIZE); //Body

        g.setColor(Color.GRAY);
        g.fillOval((int) x - SIZE/8, (int) y - SIZE/8, SIZE/4, SIZE/4); //Beacon
        g.setColor(Color.getHSBColor(hue.val()/256.0f,0.9f,0.5f));
        g.fillOval((int) x - SIZE/10, (int) y - SIZE/10, SIZE/5, SIZE/5); //Beacon

        g.setColor(Color.black);
        g.drawString(name, (int) x - SIZE, (int) y - SIZE);
    }

    @Override
    public boolean intersectsWith(Entity e) {
        long marker = System.nanoTime();

        if(e == null) return false;
        if(! (e instanceof Robot || e instanceof TrackerDot)) return false;

        boolean result = (Math.sqrt(getWidth() * getWidth() / 2 + e.getWidth() * e.getWidth() / 2)
            > Math.sqrt((getX() - e.getX())*(getX() - e.getX())+(getY() - e.getY())*(getY() - e.getY())));
        Debug.logTime("Robot intersections", marker - System.nanoTime());
        return result;
    }

    @Override
    public void intersect(Entity e) {
        repel(e);
        e.damage(getMass());
    }

    @Override
    public String toString() {
        return name + getUUID();
    }

    protected void fire() {
        if(lastFireTime + MAX_BULLET_RECHARGE - stats[STAT_FIRE_SPEED].val() < getArena().getTime()) {
            Bullet bullet = new Bullet(this, getArena());
            add(bullet);
            lastFireTime = getArena().getTime();
        }
    }
    protected void repair() {
        if(lastHealTime + MAX_HEAL_RECHARGE - stats[STAT_REGEN].val()/4 < getArena().getTime() ) {
            cogs--;
            heal(1);
            lastHealTime= getArena().getTime();
        }
    }
    protected void forward(int force) {
        //Essentially, accelerate the tank in the direction it's facing.
        //This will typically take a tank to its max speed (based on drag.)
        //To go slower a tank has to monitor when it's moving forwards.
        if(force > stats[STAT_SPEED].val()) force = stats[STAT_SPEED].val(); //Tanks can't move faster than a certain limit
        if(force < -stats[STAT_SPEED].val()) force = -stats[STAT_SPEED].val(); //Tanks can't move faster than a certain limit

        //Translate polar force into cartesian vector
        setAccX(force *  Math.cos(getR()) / 16); //Scaling!
        setAccY(force * -Math.sin(getR()) / 16);
    }
    protected void rotate(int force) {
        if(force > stats[STAT_ROTATE_SPEED].val()) force = stats[STAT_ROTATE_SPEED].val(); //Robots can't rotate faster than a certain limit
        if(force < -stats[STAT_ROTATE_SPEED].val()) force = -stats[STAT_ROTATE_SPEED].val();

        setAccR( ((double) force )/512 );
    }
    protected void upgrade(UByte stat, int amount) {
        //TODO manage conversion problems with signed UBytes
        cogs -= amount;
        int newValue = amount + stats[Math.abs(stat.val()>>5)].val();
        stats[Math.abs(stat.val()>>5)] = (amount > 255)? ub(255):ub(amount);
    }

    public void onDeath() {
        if(cogs <= 5) cogs=5;
        Cog cog;
        int value;
        int maxValue = (int)(cogs/4)+1;
        while(cogs > 1) {
            value = (int) Math.min(Option.random.nextInt(maxValue-1)+1, cogs);
            cog = new Cog(value, getArena());
            cogs -= value;
            cog.setX(getX());
            cog.setY(getY());
            cog.setVelX(10*(Option.random.nextFloat()-0.5));
            cog.setVelY(10*(Option.random.nextFloat()-0.5));
            add(cog);
        }
    }

    @Override
    public String toHTML() {
        String result = "";

        result += "<a href=ergo_uuid_"+getUUID() + ">";
        if(!isAlive()) result += "<font color=\"red\">";
        else result += "<font color=\"blue\">";
        result += getName() + " [Fitness: " + String.format("%.2f", getFitness()) + "]";
        if(!isAlive()) result += "</font>";
        return result;
    }

    public double getFitness() { return fitness; }
    public void setFitness(double fitness) { this.fitness = fitness; }
    public int getGeneration() { return generation; }
    public void setGeneration(int generation) { this.generation = generation; }
    public double getCogs() { return cogs; }
    public void setCogs(double cogs) { this.cogs = cogs; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public UByte getHue() {return hue;}
    public void setHue(UByte hue) {this.hue = hue;}

    @Override
    public int compareTo(Robot o) {
        return (int) (fitness - o.getFitness());
    }
}
