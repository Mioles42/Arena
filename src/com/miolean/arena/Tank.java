package com.miolean.arena;

import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

import static com.miolean.arena.Global.*;
import static com.miolean.arena.UByte.*;

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

//Tank has all sorts of methods that appear unused but are actually reflected.
@SuppressWarnings("unused")
public class Tank extends Entity {

    //Memories
    private static final Gene[] KMEM;
    private UByte[][] UMEM;
    private UByte[][] PMEM;
    private UByte[][] SMEM;
    private UByte[] WMEM;

    //Program index:
    private int index = 0;

    //Defined memory indexes:
    private UByte sNumber = ub(0);
    private UByte uNumber = ub(0);
    private UByte pNumber = ub(0);

    //Comparison flags:
    private boolean equal = false;
    private boolean greater = false;

    //Stat constants
    private static final int STAT_HASTE = 0x0; //shot speed
    private static final int STAT_DAMAGE = 0x1; //shot damage
    private static final int STAT_REGEN = 0x2; //regeneration
    private static final int STAT_SPEED = 0x3; //acceleration (translates to speed due to drag)
    private static final int STAT_TOUGHNESS = 0x4; //body damage
    private static final int STAT_ROTATE_SPEED = 0x5;

    //Type constant
    private static final int TYPE_TANK = 0x0;
    private static final int TYPE_COG = 0x4;
    private static final int TYPE_BULLET = 0x8;
    private static final int TYPE_WALL = 0x12;


    private UByte[] stats = new UByte[8];

    //Flash color:
    private int flashR = 0x00;
    private int flashG = 0xBB;
    private int flashB = 0x00;

    //General variables:
    int fitness = 0;
    int cogs = 0;
    private int viewDistance = 10;

    //Memory:


    static {
        KMEM = new Gene[256];
        Scanner in = new Scanner(Tank.class.getClassLoader().getResourceAsStream("cfg/origin.txt"));
        in.useDelimiter("/");

        in.next();
        short opcode = (short) Integer.parseInt(in.next().trim(), 16);
        String method;
        String description;
        UByte cost;
        UByte weight;

        while(opcode < 0xFF) {
            method = in.next().trim();
            description = in.next().trim();
            cost = ub(Integer.parseInt(in.next().trim(), 16));
            weight = ub(Integer.parseInt(in.next().trim(), 16));

            KMEM[opcode] = new Gene(method, description, cost, weight);

            in.next();
            opcode = (short) Integer.parseInt(in.next().trim(), 16);
        }

        for(Gene g: KMEM) System.out.println(g);
    }

    Tank() {
        //Todo: This is totally a placeholder
        //0: Initial values for testing.
        x = 100;
        y = 100;
        r = 0;
        width = 40;
        height = 40;
        accX = 1;
        velY = 7;
        velR = 1;
        drag = 1;

        //1: Initialize memories.
        UMEM = new UByte[256][];
        PMEM = new UByte[256][];
        SMEM = new UByte[256][];
        WMEM = new UByte[256];
        UMEM[0] = new UByte[256];
        PMEM[0] = new UByte[256];
        SMEM[0] = new UByte[256];

        for(int i = 0; i < 256; i++) {
            UMEM[0][i] = ub(0);
            SMEM[0][i] = ub(0);
            PMEM[0][i] = ub(0);
            WMEM[i] = ub(0);
        }

        stats[STAT_SPEED] = ub(50);

        //Let's go one deeper.
        PMEM[0][0] = ub(0x51);
        PMEM[0][1] = ub(0x0);
        PMEM[0][2] = ub(0x42);

        WMEM[0x42] = ub(7);

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
        g.setColor(new Color(flashR, flashG, flashB));
        g.fillOval((int) x - width/10, (int) y - height/10, width/5, height/5); //Beacon
    }

    @Override
    void update() {
        applyPhysics();
        //forward(1);
        //Fun time!
        for(int i = 0; i < PMEM.length-3; i++) {
            if(PMEM[0][i].val() == 0x00) continue;
            if(KMEM[PMEM[0][i].val()] == null) continue;
            try {
                System.out.println("[Tank.update()] Attempting to invoke " + KMEM[PMEM[0][i].val()] + " with " + PMEM[0][i+1].val() + ", " + PMEM[0][i+2].val() + ", " + PMEM[0][i+3].val());
                KMEM[PMEM[0][i].val()].getMeaning().invoke(this, PMEM[0][i+1].val(), PMEM[0][i+2].val(), PMEM[0][i+3].val());
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            //Assuming nothing went wrong we've completed a command
            i += 3; //don't want to run the arguments by accident
        }
        System.out.println("[" + x + ", " + y + "]");
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



    public void forward(int force) {
        System.out.println("Forward with force " + force);
        //Essentially, accelerate the tank in the direction it's facing.
        //This will typically take a tank to its max speed (based on drag.)
        //To go slower a tank has to monitor when it's moving forwards.
        if(force > stats[STAT_SPEED].val()) force = stats[STAT_SPEED].val(); //Tanks can't move faster than a certain limit

        //Translate polar force into cartesian vector
        this.accX = force * Math.cos(r);
        this.accY = force * Math.sin(r);
    }
    public void rotate(int force) {
        if(force > stats[STAT_ROTATE_SPEED].val()) force = stats[STAT_ROTATE_SPEED].val(); //Tanks can't move faster than a certain limit

        this.accR = force;
    }

    public void setLEDR(int value) { flashR = value;}
    public void setLEDG(int value) { flashG = value;}
    public void setLEDB(int value) { flashB = value;}

    public void upgrade(UByte stat, int amount) {
        //TODO manage conversion problems with signed UBytes
        stats[Math.abs(stat.val()>>4)] = ub(amount + stats[Math.abs(stat.val()>>4)].val());
    }

    public int typeOf(Entity entity) {
        if (entity instanceof Tank) return TYPE_TANK;
        //if (entity instanceof Cog) return TYPE_COG;
        //if (entity instanceof Bullet) return TYPE_BULLET;
        //if (entity instanceof Wall) return TYPE_WALL;
        return 0;
    }

    /*-----------------------------------------------------------------
     * Reflected methods (genes) are below.
     * Beware. Not intended for human consumption.
     * All arguments are intended to be within the range [0, 255].
     */
    // 1
    public void _GOTO (int regin, int arg1, int arg2) {index = WMEM[arg1].val();}
    public void _GOE  (int regin, int arg1, int arg2) {if(equal) index = WMEM[arg1].val();}
    public void _GOG  (int regin, int arg1, int arg2) {if(greater) index = WMEM[arg1].val();}
    public void _ZGOTO(int regin, int arg1, int arg2) {index = arg1;}
    public void _ZGOE (int regin, int arg1, int arg2) {if(equal) index = arg1;}
    public void _ZGOG (int regin, int arg1, int arg2) {if(greater) index = arg1;}
    public void _COMP (int regin, int arg1, int arg2) {equal = (WMEM[arg1].val() == WMEM[arg2].val()); greater = (WMEM[arg1].val() > WMEM[arg2].val());}
    // 2
    public void _MOV  (int regin, int arg1, int arg2) {WMEM[regin] = WMEM[arg1];}
    public void _SSTO (int regin, int arg1, int arg2) {SMEM[arg1][regin] = WMEM[arg2];}
    public void _PSTO (int regin, int arg1, int arg2) {PMEM[arg1][regin] = WMEM[arg2];}
    public void _USTO (int regin, int arg1, int arg2) {UMEM[arg1][regin] = WMEM[arg2];}
    public void _SGET (int regin, int arg1, int arg2) {WMEM[regin] = SMEM[arg2][arg1];}
    public void _PGET (int regin, int arg1, int arg2) {WMEM[regin] = PMEM[arg2][arg1];}
    public void _UGET (int regin, int arg1, int arg2) {WMEM[regin] = UMEM[arg2][arg1];}
    public void _ZMOV (int regin, int arg1, int arg2) {WMEM[regin] = ub(arg1);}
    public void _ZSSTO(int regin, int arg1, int arg2) {SMEM[arg1][regin] = ub(arg2);}
    public void _ZPSTO(int regin, int arg1, int arg2) {PMEM[arg1][regin] = ub(arg2);}
    public void _ZUSTO(int regin, int arg1, int arg2) {UMEM[arg1][regin] = ub(arg2);}
    public void _WCLR (int regin, int arg1, int arg2) {for(; arg1 < arg2 && arg1 < 256; arg1++) WMEM[arg1] = ub(0);}
    public void _SCLR (int regin, int arg1, int arg2) {for(; arg1 < arg2 && arg1 < 256; arg1++) SMEM[arg1][regin] = ub(0);}
    public void _PCLR (int regin, int arg1, int arg2) {for(; arg1 < arg2 && arg1 < 256; arg1++) PMEM[arg1][regin] = ub(0);}
    public void _UCLR (int regin, int arg1, int arg2) {for(; arg1 < arg2 && arg1 < 256; arg1++) UMEM[arg1][regin] = ub(0);}
    // 3
    public void _POSX (int regin, int arg1, int arg2) {WMEM[regin] = ub((int) (x/ARENA_SIZE*255));}
    public void _VELX (int regin, int arg1, int arg2) {WMEM[regin] = ub((int) velX);}
    public void _ACCX (int regin, int arg1, int arg2) {WMEM[regin] = ub((int) accX);}
    public void _POSY (int regin, int arg1, int arg2) {WMEM[regin] = ub((int) (y/ARENA_SIZE*255));}
    public void _VELY (int regin, int arg1, int arg2) {WMEM[regin] = ub((int) velY);}
    public void _ACCY (int regin, int arg1, int arg2) {WMEM[regin] = ub((int) accY);}
    public void _POSR (int regin, int arg1, int arg2) {WMEM[regin] = ub((int) (r/2*Math.PI*255));}
    public void _VELR (int regin, int arg1, int arg2) {WMEM[regin] = ub((int) velR);}
    public void _ACCR (int regin, int arg1, int arg2) {WMEM[regin] = ub((int) accR);}
    // 4
    public void _VIEW (int regin, int arg1, int arg2) {viewDistance = WMEM[arg1].val();}
    public void _NEAR (int regin, int arg1, int arg2) {}
    // 5
    public void _HEAL (int regin, int arg1, int arg2) {if(health + WMEM[arg1].val() < 256) health += WMEM[arg1].val();}
    public void _FORWD(int regin, int arg1, int arg2) {forward(WMEM[arg1].val());}
    public void _REVRS(int regin, int arg1, int arg2) {forward(-WMEM[arg1].val());}
    public void _FIRE (int regin, int arg1, int arg2) {} //TODO Implmement _FIRE()
    public void _TURNL(int regin, int arg1, int arg2) {rotate(WMEM[arg1].val());}
    public void _TURNR(int regin, int arg1, int arg2) {rotate(-WMEM[arg1].val());}
    // 6
    public void _ADD  (int regin, int arg1, int arg2) {WMEM[regin] = ub(arg1 + arg2);}
    public void _SUB  (int regin, int arg1, int arg2) {WMEM[regin] = ub(arg1 - arg2);}
    public void _PROD (int regin, int arg1, int arg2) {WMEM[regin] = ub(arg1 * arg2);}
    public void _QUOT (int regin, int arg1, int arg2) {WMEM[regin] = ub(arg1 / arg2);}
    public void _BWOR (int regin, int arg1, int arg2) {WMEM[regin] = ub(arg1 | arg2);}
    public void _BWAND(int regin, int arg1, int arg2) {WMEM[regin] = ub(arg1 & arg2);}
    public void _BWXOR(int regin, int arg1, int arg2) {WMEM[regin] = ub(arg1 ^ arg2);}
    public void _INCR (int regin, int arg1, int arg2) {WMEM[regin] = ub(WMEM[regin].val() + 1);}
    // 7
    public void _OTYPE(int regin, int arg1, int arg2) {WMEM[regin] = ub(typeOf(handler.getByUUID(WMEM[arg1],WMEM[arg1])));}
    public void _OHP  (int regin, int arg1, int arg2) {WMEM[regin] = ub(handler.getByUUID(WMEM[arg1],WMEM[arg1]).health);}
    public void _OCOG (int regin, int arg1, int arg2) {WMEM[regin] = ub(((Tank) handler.getByUUID(WMEM[arg1],WMEM[arg1])).cogs);}
    public void _OSTAT(int regin, int arg1, int arg2) {WMEM[regin] = (((Tank) handler.getByUUID(WMEM[arg1],WMEM[arg1])).stats[WMEM[regin].val()]);}
    // 8
    public void _WALL (int regin, int arg1, int arg2) {} //TODO Implement _WALL() [when Walls exist]
    public void _SPIT (int regin, int arg1, int arg2) {} //TODO Implement _SPIT() [when Cogs exist]
    public void _LEDR (int regin, int arg1, int arg2) {setLEDR(WMEM[arg1].val());}
    public void _LEDG (int regin, int arg1, int arg2) {setLEDG(WMEM[arg1].val());}
    public void _LEDB (int regin, int arg1, int arg2) {setLEDB(WMEM[arg1].val());}
    public void _OLEDR(int regin, int arg1, int arg2) {WMEM[regin] = ub(((Tank) handler.getByUUID(WMEM[arg1],WMEM[arg1])).flashR);}
    public void _OLEDG(int regin, int arg1, int arg2) {WMEM[regin] = ub(((Tank) handler.getByUUID(WMEM[arg1],WMEM[arg1])).flashG);}
    public void _OLEDB(int regin, int arg1, int arg2) {WMEM[regin] = ub(((Tank) handler.getByUUID(WMEM[arg1],WMEM[arg1])).flashB);}
    // 9
    public void _UUIDM(int regin, int arg1, int arg2) {WMEM[regin] = ub(uuidMost);}
    public void _UUIDL(int regin, int arg1, int arg2) {WMEM[regin] = ub(uuidLeast);}
    public void _HP   (int regin, int arg1, int arg2) {WMEM[regin] = ub(health);}
    public void _COG  (int regin, int arg1, int arg2) {WMEM[regin] = ub(cogs);}
    public void _PNT  (int regin, int arg1, int arg2) {WMEM[regin] = ub(fitness);}
    // A
    public void _UPG  (int regin, int arg1, int arg2) {upgrade(WMEM[arg1], WMEM[arg2].val());}
    public void _STAT (int regin, int arg1, int arg2) {WMEM[regin] = stats[Math.abs(arg1>>4)];}
    // B TODO: Implement B (Space) block
    public void _WFULL(int regin, int arg1, int arg2) {}
    public void _SFULL(int regin, int arg1, int arg2) {}
    public void _PFULL(int regin, int arg1, int arg2) {}
    public void _UFULL(int regin, int arg1, int arg2) {}
    // C
    public void _OPOSX(int regin, int arg1, int arg2) {}
    public void _OVELX(int regin, int arg1, int arg2) {}
    public void _OACCX(int regin, int arg1, int arg2) {}
    public void _OPOSY(int regin, int arg1, int arg2) {}
    public void _OVELY(int regin, int arg1, int arg2) {}
    public void _OACCY(int regin, int arg1, int arg2) {}
    public void _OPOSR(int regin, int arg1, int arg2) {}
    public void _OVELR(int regin, int arg1, int arg2) {}
    public void _OACCR(int regin, int arg1, int arg2) {}
    // D
    public void _LSMEM(int regin, int arg1, int arg2) {}
    public void _SMEMX(int regin, int arg1, int arg2) {}
    public void _DEFS (int regin, int arg1, int arg2) {}
    public void _SDEL (int regin, int arg1, int arg2) {}
    public void _LPMEM(int regin, int arg1, int arg2) {}
    public void _PMEMX(int regin, int arg1, int arg2) {}
    public void _DEFP (int regin, int arg1, int arg2) {}
    public void _PDEL (int regin, int arg1, int arg2) {}
    public void _LUMEM(int regin, int arg1, int arg2) {}
    public void _UMEMX(int regin, int arg1, int arg2) {}
    public void _DEFU (int regin, int arg1, int arg2) {}
    public void _UDEP (int regin, int arg1, int arg2) {}
    public void _SSWAP(int regin, int arg1, int arg2) {}
    public void _PSWAP(int regin, int arg1, int arg2) {}
    public void _USWAP(int regin, int arg1, int arg2) {}
    // E
    public void _KWGT (int regin, int arg1, int arg2) {}
    public void _COST (int regin, int arg1, int arg2) {}
    // F
    public void _REP  (int regin, int arg1, int arg2) {}
    public void _TWK  (int regin, int arg1, int arg2) {}
    public void _KRAND(int regin, int arg1, int arg2) {}
    public void _URAND(int regin, int arg1, int arg2) {}
    public void _PRAND(int regin, int arg1, int arg2) {}
    public void _SRAND(int regin, int arg1, int arg2) {}
    public void _WRAND(int regin, int arg1, int arg2) {}
    public void _IRAND(int regin, int arg1, int arg2) {}
}
