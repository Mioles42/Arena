package com.miolean.arena;

import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

import static com.miolean.arena.Global.*;

/**
 * Created by commandm on 5/13/17.
 */

//Tank has all sorts of methods that appear unused but are actually reflected.
@SuppressWarnings("unused")
public class Tank extends Entity {

    private static final Gene[] KMEM;


    private static final int STAT_HASTE = 0; //shot speed
    private static final int STAT_DAMAGE = 1; //shot damage
    private static final int STAT_REGEN = 2; //regeneration
    private static final int STAT_SPEED = 3; //acceleration (translates to speed due to drag)
    private static final int STAT_TOUGHNESS = 4; //body damage

    private byte[] stats = new byte[8];

    //Flash color:
    private int flashR = 0x0000;
    private int flashG = 0xBBBB;
    private int flashB = 0x0000;

    //Fitness:
    int fitness = 0;
    protected int viewDistance = 10;

    //Memory:


    static {
        KMEM = new Gene[256];
        Scanner in = new Scanner(Tank.class.getClassLoader().getResourceAsStream("cfg/origin.txt"));
        in.useDelimiter("/");

        in.next();
        short opcode = (short) Integer.parseInt(in.next().trim(), 16);
        String method;
        String description;
        byte cost;
        byte weight;

        while(opcode < 0xFF) {
            method = in.next().trim();
            description = in.next().trim();
            cost = (byte) (Integer.parseInt(in.next().trim(), 16));
            weight = (byte) (Integer.parseInt(in.next().trim(), 16));

            KMEM[opcode] = new Gene(method, description, cost, weight);

            in.next();
            opcode = (short) Integer.parseInt(in.next().trim(), 16);
        }

        for(Gene g: KMEM) System.out.println(g);
    }

    Tank() {
        //Todo: This is totally a placeholder
        x = 100;
        y = 100;
        r = 0;
        width = 40;
        height = 40;
        velX = 7;
        velY = 7;
        velR = 1;
        drag = .05;

        try {
            KMEM[0x10].meaning.invoke(this, (byte) 42, (byte) 0, (byte) 0);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    void render(Graphics g) {
        g.setColor(new Color(100, 100, 90));

        //Body! (This part's easy)
        //(There's actually no calculation necessary for this.)

        //Trigonometric functions are expensive so let's use math to minimize
        //how many times we have to calculate them.
        //And yeah, r is rotation. Not radius. Sorry about that, but my keyboard
        //doesn't have a theta. (not that Java would support it probably anyway)
        double sinR = Math.sin(r);
        double cosR = Math.cos(r);
        double sind5 = .47943; //As in sine decimal five, or sin(0.5). There is no pi and that is not a mistake.
        double cosd5 = .87758;
        double sind2 = .19867;
        double cosd2 = .98007;
        //sin(r +- u) = sin(r)cos(u) +- sin(u)cos(r)
        //cos(r +- u) = cos(r)cos(u) +- sin(r)sin(u)
        //tan(r +- u) = sin(r +- u) / cos(r +- u) [not that division is so much better]

        //Wheels!
        int[] wheelXPoints = {
                (int) (x+width*.7*(sinR*cosd5 - sind5*cosR)),
                (int) (x+width*.7*(sinR*cosd5 + sind5*cosR)),
                (int) (x-width*.7*(sinR*cosd5 - sind5*cosR)),
                (int) (x-width*.7*(sinR*cosd5 + sind5*cosR))
        };

        int[] wheelYPoints = {
                (int) (y+width*.7*(cosR*cosd5 + sinR*sind5)),
                (int) (y+width*.7*(cosR*cosd5 - sinR*sind5)),
                (int) (y-width*.7*(cosR*cosd5 + sinR*sind5)),
                (int) (y-width*.7*(cosR*cosd5 - sinR*sind5))
        };

        //Gun barrel!
        int[] gunXPoints = {
                (int) (x+width*.7*(cosR*cosd2 - sinR*sind2)), //y + size * a little bit more * cos(r - .5)
                (int) (x+width*.7*(cosR*cosd2 + sinR*sind2)), //y + size * a little bit more * cos(r - .5)
                (int) (x)
        };

        int[] gunYPoints = {
                (int) (y-width*.7*(sinR*cosd2 + sind2*cosR)),
                (int) (y-width*.7*(sinR*cosd2 - sind2*cosR)),
                (int) (y)

        };


        g.setColor(Color.DARK_GRAY);
        g.fillPolygon(wheelXPoints, wheelYPoints, 4); //Wheels
        g.setColor(Color.BLACK);
        g.drawPolygon(wheelXPoints, wheelYPoints, 4); //Wheel outline

        g.setColor(Color.GRAY);
        g.fillPolygon(gunXPoints, gunYPoints, 3); //Barrel

        g.setColor(Color.LIGHT_GRAY);
        g.fillOval((int) x - width/2, (int) y - height/2, width, height); //Body

        g.setColor(Color.GRAY);
        g.fillOval((int) x - width/8, (int) y - height/8, width/4, height/4); //Beacon
        g.setColor(new Color(flashR*255/0xFFFF, flashG*255/0xFFFF, flashB*255/0xFFFF));
        g.fillOval((int) x - width/10, (int) y - height/10, width/5, height/5); //Beacon
    }

    @Override
    void update() {
        applyPhysics();
        //forward(1);
    }

    @Override
    boolean intersectsWith(Entity e) {
        if(e == null) return false;
        double distanceSquared = (x - e.x)*(x - e.x)+(y - e.y)*(y - e.y);
        return distanceSquared <= (width/2 - e.width/2)*(width/2 - e.width/2);
    }

    @Override
    void intersect(Entity e) {

    }

    @Override
    public String toString() {
        return super.toString() + "{x: " + x + ", y: " + y + "}";
    }

    public void applyPhysics() {
        super.applyPhysics();
        if(x > ARENA_SIZE - BORDER) x = ARENA_SIZE - BORDER;
        if(x < BORDER) x = BORDER;
        if(y > ARENA_SIZE - BORDER) y = ARENA_SIZE - BORDER;
        if(y < BORDER) y = BORDER;
    }



    public int forward(int force) {
        //Essentially, accelerate the tank in the direction it's facing.
        //This will typically take a tank to its max speed (based on drag.)
        //To go slower a tank has to monitor when it's moving forwards.
        if(force > stats[STAT_SPEED]) force = stats[STAT_SPEED]; //Tanks can't move faster than a certain limit

        //Translate polar force into cartesian vector
        this.accX = force * Math.cos(r);
        this.accY = force * Math.sin(r);

        return force;
    }
    public int backward(int force) {
        //Same as forward, but reversed
        if(force > stats[STAT_SPEED]) force = stats[STAT_SPEED]; //Tanks can't move faster than a certain limit

        //Translate polar force into cartesian vector
        this.accX = -force * Math.cos(r);
        this.accY = -force * Math.sin(r);

        return force;
    }

    public void setLEDR(byte value) { flashR = value;}
    public void setLEDG(byte value) { flashG = value;}
    public void setLEDB(byte value) { flashB = value;}

    public void upgrade(byte stat, byte amount) {
        //TODO manage conversion problems with signed bytes
        stats[stat>>4] += amount;
    }

    public void _GOTO(byte regin, byte arg1, byte arg2) {
        System.out.println(regin);
    }
}
