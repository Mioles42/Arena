package com.miolean.arena.entities;

import com.miolean.arena.framework.Option;
import com.miolean.arena.genetics.GeneCommand;
import com.miolean.arena.genetics.Immed;

import java.awt.*;
import java.io.InputStream;

import static com.miolean.arena.entities.Arena.ARENA_SIZE;
import static com.miolean.arena.framework.UByte.ub;


@SuppressWarnings("unused")
public class DefaultGeneticRobot extends GeneticRobot {


    private int viewDistance = 10;

    private boolean equalFlag = false;
    private boolean greaterFlag = false;

    static final int TYPE_TANK = 0x0;
    static final int TYPE_COG = 0x4;
    static final int TYPE_BULLET = 0x8;
    static final int TYPE_WALL = 0x12;


    //This should really be an Option
    static final int MAX_STACK_SIZE = 16;


    public DefaultGeneticRobot(GeneticRobot parent, Arena arena) {
        super(parent, arena);
    }
    public DefaultGeneticRobot(InputStream file, Arena arena) {
        super(file, arena);
    }

    private int typeOf(Entity entity) {
        //It's sort of fun to uncomment these as I add new stuff.
        if (entity instanceof Robot) return TYPE_TANK;
        if (entity instanceof Cog) return TYPE_COG;
        if (entity instanceof Bullet) return TYPE_BULLET;
        //if (entity instanceof Wall) return TYPE_WALL;
        return 0;
    }

    /*-----------------------------------------------------------------
     * Reflected methods (genes) are below.
     * Beware. Not intended for human consumption.
     * All arguments are intended to be within the range [0, 255].
     */

    //TODO Move to the annotation framework. It'll be a pain but definitely worthwhile.

    public void _UNDEF() {}
    public void _UNDEF(int... args) {}
    public void _NO () {} //"Nothing" that is registered as gene #0 (which is the default value)
    public void _SNO() {} //"Nothing" that is registered as gene #1 (not the default value and therefore an actual gene)
    public void _PRINT(int arg0, int arg1, int arg2) {
        //System.out.printf("%s says: %s, %s, %s.\n", getName(), WMEM[arg0].val(), WMEM[arg1].val(), WMEM[arg2].val());
    }

    //TODO Implement test commands after we can disable selected genes
    public void _SCALC() {}
    public void _TCALC() {}
    public void _HURT() {}

    @GeneCommand(args=1, weight=40, cost=0.2)
    public void _GOTO (int reg) {index = WMEM[reg].val() - 2;}
    @GeneCommand(args=1, weight=40, cost=0.2)
    public void _GOE  (int reg) {if(equalFlag) index = WMEM[reg].val() - 2;}
    @GeneCommand(args=1, weight=40, cost=0.2)
    public void _GOG  (int reg) {if(greaterFlag) index = WMEM[reg].val() - 2;}
    @GeneCommand(args=1, weight=50, cost=0.2)
    public void _IGOTO(@Immed int immed) {index = immed - 2;}
    @GeneCommand(args=1, weight=50, cost=0.2)
    public void _IGOE (@Immed int immed) {if(equalFlag) index = immed - 2;}
    @GeneCommand(args=1, weight=50, cost=0.2)
    public void _IGOG (@Immed int immed) {if(greaterFlag) index = immed - 2;}

    @GeneCommand(weight=70, cost=0.0, args=2)
    public void _COMP (int reg1, int reg2) {
        equalFlag = (WMEM[reg1].val() == WMEM[reg2].val());
        greaterFlag = (WMEM[reg1].val() > WMEM[reg2].val());
    }
    @GeneCommand(weight=70, cost=0.0, args=2)
    public void _ICOMP (int reg, @Immed int immed) {
        equalFlag = (WMEM[reg].val() == immed);
        greaterFlag = (WMEM[reg].val() > immed);
    }

    //TODO Assign weights to the run commands (and make sure they actually work)
    public void _RUN (int reg) {
        if(stack.size() < MAX_STACK_SIZE && CURRENT[WMEM[reg].val()] != null) {
            stack.push(new Point(loaded, index));
            loaded = WMEM[reg].val();
            index = 0;
        }
    }
    public void _RUNE  (int reg) {
        if(stack.size() < MAX_STACK_SIZE && equalFlag && CURRENT[WMEM[reg].val()] != null) {
            stack.push(new Point(loaded, index));
            loaded = WMEM[reg].val();
            index = 0;
        }
    }
    public void _RUNG  (int reg) {
        if(stack.size() < MAX_STACK_SIZE && greaterFlag && CURRENT[WMEM[reg].val()] != null) {
            stack.push(new Point(loaded, index));
            loaded = WMEM[reg].val();
            index = 0;
        }
    }
    public void _IRUN(@Immed int immed) {
        if(stack.size() < MAX_STACK_SIZE && CURRENT[immed] != null) {
            stack.push(new Point(loaded, index));
            loaded = immed;
            index = 0;
        }
    }
    public void _IRUNE (@Immed int immed) {
        if(stack.size() < MAX_STACK_SIZE && equalFlag && CURRENT[immed] != null) {
            stack.push(new Point(loaded, index));
            loaded = immed;
            index = 0;
        }
    }
    public void _IRUNG (@Immed int immed) {
        if(stack.size() < MAX_STACK_SIZE && greaterFlag && CURRENT[immed] != null) {
            stack.push(new Point(loaded, index));
            loaded = immed;
            index = 0;
        }
    }

    //Store values in places from existing values in registries
    @GeneCommand(weight=150, cost=0, args=2)
    public void _MOV (int targReg, int sourceReg) {
        WMEM[targReg] = WMEM[sourceReg];
    }
    @GeneCommand(weight=150, cost=0, args=3)
    public void _SSTO (int targSMemReg, int targAddrReg, int sourceReg) {
        if(SMEM[WMEM[targSMemReg].val()] != null) {
            SMEM[WMEM[targSMemReg].val()][WMEM[targAddrReg].val()] = WMEM[sourceReg];
        }
    }
    @GeneCommand(weight=50, cost=0.01, args=3)
    public void _PSTO (int targPMemReg, int targAddrReg, int sourceReg) {
        if(PMEM[WMEM[targPMemReg].val()] != null) {
            PMEM[WMEM[targPMemReg].val()][WMEM[targAddrReg].val()] = WMEM[sourceReg];
        }
    }
    @GeneCommand(weight=20, cost=0.02, args=3)
    public void _USTO (int targUMemReg, int targAddrReg, int sourceReg) {
        if(UMEM[WMEM[targUMemReg].val()] != null) {
            UMEM[WMEM[targUMemReg].val()][WMEM[targAddrReg].val()] = WMEM[sourceReg];
        }
    }

    //Put values in the registry from existing values in memories
    @GeneCommand(weight=150, cost=0, args=3)
    public void _SGET (int targReg, int sourceSMemReg, int sourceAddrReg) {
        if(SMEM[WMEM[sourceSMemReg].val()] != null) {
            WMEM[targReg] = SMEM[WMEM[sourceSMemReg].val()][WMEM[sourceAddrReg].val()];
        }
    }
    @GeneCommand(weight=50, cost=0, args=3)
    public void _PGET (int targReg, int sourcePMemReg, int sourceAddrReg) {
        if(PMEM[WMEM[sourcePMemReg].val()] != null) {
            WMEM[targReg] = PMEM[WMEM[sourcePMemReg].val()][WMEM[sourceAddrReg].val()];
        }
    }
    @GeneCommand(weight=20, cost=0, args=3)
    public void _UGET (int targReg, int sourceUMemReg, int sourceAddrReg) {
        if(UMEM[WMEM[sourceUMemReg].val()] != null) {
            WMEM[targReg] = UMEM[WMEM[sourceUMemReg].val()][WMEM[sourceAddrReg].val()];
        }
    }

    //Put immediate values in immediate locations
    @GeneCommand(weight=200, cost=0, args=2)
    public void _IMOV (int targReg, @Immed int immed) {
        WMEM[targReg] = ub(immed);
    }
    @GeneCommand(weight=100, cost=0, args=3)
    public void _ISSTO(int targSMem, @Immed int targAddr, @Immed int immed) {
        if(SMEM[targSMem] != null) {
            SMEM[targSMem][targAddr] = ub(immed);
        }
    }
    @GeneCommand(weight=50, cost=0.01, args=3)
    public void _IPSTO(int targSMem, @Immed int targAddr, @Immed int immed) {
        if(PMEM[targSMem] != null) {
            PMEM[targSMem][targAddr] = ub(immed);
        }
    }
    @GeneCommand(weight=20, cost=0.02, args=3)
    public void _IUSTO(int targSMem, @Immed int targAddr, @Immed int immed) {
        if(UMEM[targSMem] != null) {
            UMEM[targSMem][targAddr] = ub(immed);
        }
    }

    //Clear an area of memories
    @GeneCommand(weight=20, cost=0, args=2)
    public void _WCLR (int start, int end) {
        while(start < end && start < 256) {
            WMEM[start] = ub(0);
            start++;
        }
    }
    @GeneCommand(weight=10, cost=0, args=3)
    public void _SCLR (int targSMemreg, int startReg, int endReg) {
        if(SMEM[WMEM[targSMemreg].val()] != null) {
            int counter = WMEM[startReg].val();
            while(counter < WMEM[endReg].val() && counter < 256) {
                SMEM[WMEM[targSMemreg].val()][counter] = ub(0);
                counter++;
            }
        }
    }
    @GeneCommand(weight=6, cost=0, args=3)
    public void _PCLR (int targSMemreg, int startReg, int endReg) {
        if(PMEM[WMEM[targSMemreg].val()] != null) {
            int counter = WMEM[startReg].val();
            while(counter < WMEM[endReg].val() && counter < 256) {
                PMEM[WMEM[targSMemreg].val()][counter] = ub(0);
                counter++;
            }
        }
    }
    @GeneCommand(weight=2, cost=0, args=3)
    public void _UCLR (int targSMemreg, int startReg, int endReg) {
        if(UMEM[WMEM[targSMemreg].val()] != null) {
            int counter = WMEM[startReg].val();
            while(counter < WMEM[endReg].val() && counter < 256) {
                UMEM[WMEM[targSMemreg].val()][counter] = ub(0);
                counter++;
            }
        }
    }

    //Get information about where the Robot is and how fast it's going.
    @GeneCommand(weight=10, cost=0, args=1)
    public void _POSX (int targReg) {WMEM[targReg] = ub((int) (getX()/ARENA_SIZE*255));}
    @GeneCommand(weight=10, cost=0, args=1)
    public void _VELX (int targReg) {WMEM[targReg] = ub((int) getVelX());}
    @GeneCommand(weight=10, cost=0, args=1)
    public void _ACCX (int targReg) {WMEM[targReg] = ub((int) getAccX());}
    @GeneCommand(weight=10, cost=0, args=1)
    public void _POSY (int targReg) {WMEM[targReg] = ub((int) (getY()/ARENA_SIZE*255));}
    @GeneCommand(weight=10, cost=0, args=1)
    public void _VELY (int targReg) {WMEM[targReg] = ub((int) getVelY());}
    @GeneCommand(weight=10, cost=0, args=1)
    public void _ACCY (int targReg) {WMEM[targReg] = ub((int) getAccY());}
    @GeneCommand(weight=10, cost=0, args=1)
    public void _POSR (int targReg) {WMEM[targReg] = ub((int) (getR()/(2*Math.PI)*255));}
    @GeneCommand(weight=10, cost=0, args=1)
    public void _VELR (int targReg) {WMEM[targReg] = ub((int) getVelR());}
    @GeneCommand(weight=10, cost=0, args=1)
    public void _ACCR (int targReg) {WMEM[targReg] = ub((int) getAccR());}

    //TODO Find other nearby entities
    @GeneCommand(weight=30, cost=0.1, args=1)
    public void _VIEW (int viewReg) {viewDistance = WMEM[viewReg].val();}
    @GeneCommand(weight=30, cost=0, args=1)
    public void _NEAR (int targReg) {}
    public void _CNEAR (int targReg) {}
    public void _RNEAR (int targReg) {}
    public void _BNEAR (int targReg) {}
    public void _WNEAR (int targReg) {}
    public void _NST (int targReg) {}
    public void _CNST (int targReg) {}
    public void _RNST (int targReg) {}
    public void _BNST (int targReg) {}
    public void _WNST (int targReg) {}

    //General actions
    @GeneCommand(weight=100, cost=0, args=0)
    public void _HEAL () {repair();}
    @GeneCommand(weight=250, cost=0, args=1)
    public void _FORWD(int forceReg) {forward(WMEM[forceReg].val());}
    @GeneCommand(weight=100, cost=0, args=1)
    public void _REVRS(int forceReg) {forward(-WMEM[forceReg].val());}
    @GeneCommand(weight=110, cost=0, args=0)
    public void _FIRE () {fire();}
    @GeneCommand(weight=400, cost=0, args=1)
    public void _TURNL(int forceReg) {rotate(WMEM[forceReg].val());}
    @GeneCommand(weight=400, cost=0, args=1)
    public void _TURNR(int forceReg) {rotate(-WMEM[forceReg].val());}

    @GeneCommand(weight=40, cost=1, args=2)
    public void _WALL (int lengthReg, int widthReg) {} //TODO Implement _WALL() [when Walls exist]
    @GeneCommand(weight=20, cost=0, args=1)
    public void _SPIT (int valueReg) {} //TODO Implement _SPIT() [when Cogs exist]


    public void _HUE (int sourceReg) {setHue(WMEM[sourceReg].val());}
    public void _FACE(int uuidReg) {} //TODO Face towards an entity

    //Math
    @GeneCommand(weight=20, cost=0, args=2)
    public void _ADD  (int arg0, int arg1) {WMEM[arg0] = ub(WMEM[arg0].val() + WMEM[arg1].val());}
    @GeneCommand(weight=20, cost=0, args=2)
    public void _SUB  (int arg0, int arg1) {WMEM[arg0] = ub(WMEM[arg0].val() + WMEM[arg1].val());}
    @GeneCommand(weight=10, cost=0, args=2)
    public void _PROD (int arg0, int arg1) {WMEM[arg0] = ub(WMEM[arg0].val() + WMEM[arg1].val());}
    @GeneCommand(weight=10, cost=0, args=2)
    public void _QUOT (int arg0, int arg1) {if(WMEM[arg1].val() != 0) WMEM[arg0] = ub(WMEM[arg0].val() + WMEM[arg1].val());}
    @GeneCommand(weight=50, cost=0, args=1)
    public void _INCR (int arg0) {WMEM[arg0] = ub(WMEM[arg0].val() + 1);}

    //Identity of other Robots
    @GeneCommand(weight=30, cost=0, args=2)
    public void _OTYPE(int targReg, int uuidReg) {if(uuidReg < 255 && getArena().fromUUID(WMEM[uuidReg].val(),WMEM[uuidReg+1].val()) != null) WMEM[targReg] = ub(typeOf(getArena().fromUUID(WMEM[uuidReg].val(),WMEM[uuidReg+1].val())));}
    @GeneCommand(weight=30, cost=0, args=2)
    public void _OHP  (int targReg, int uuidReg) {if(uuidReg < 255 && getArena().fromUUID(WMEM[uuidReg].val(),WMEM[uuidReg+1].val()) != null) WMEM[targReg] = ub((int) (getArena().fromUUID(WMEM[uuidReg].val(),WMEM[uuidReg+1].val()).getHealth()));}
    @GeneCommand(weight=30, cost=0, args=2)
    public void _OCOG (int targReg, int uuidReg) {if(uuidReg < 255 && getArena().fromUUID(WMEM[uuidReg].val(),WMEM[uuidReg+1].val()) instanceof Robot) WMEM[targReg] = ub((int)((Robot) getArena().fromUUID(WMEM[uuidReg].val(),WMEM[uuidReg+1].val())).getCogs());}
    @GeneCommand(weight=30, cost=0, args=2)
    public void _OHUE (int targReg, int uuidReg) {if(uuidReg < 255 && getArena().fromUUID(WMEM[uuidReg].val(),WMEM[uuidReg+1].val()) instanceof Robot) WMEM[targReg] = ub((int)((Robot) getArena().fromUUID(WMEM[uuidReg].val(),WMEM[uuidReg+1].val())).getHue());}
    @GeneCommand(weight=15, cost=0, args=2)
    public void _OFIT (int targReg, int uuidReg) {if(uuidReg < 255 && getArena().fromUUID(WMEM[uuidReg].val(),WMEM[uuidReg+1].val()) instanceof Robot) WMEM[targReg] = ub((int)((GeneticRobot) getArena().fromUUID(WMEM[uuidReg].val(),WMEM[uuidReg+1].val())).getFitness());}

    @GeneCommand(weight=5, cost=0, args=1)
    public void _UUID(int targReg) {if(targReg < 255) { WMEM[targReg] = ub(getUUID()>>8); WMEM[targReg] = ub(getUUID());}}
    @GeneCommand(weight=20, cost=0, args=1)
    public void _HP   (int targReg) {WMEM[targReg] = ub((int) getHealth());}
    @GeneCommand(weight=20, cost=0, args=1)
    public void _COG  (int targReg) {WMEM[targReg] = ub((int) getCogs());}
    @GeneCommand(weight=10, cost=0, args=1)
    public void _PNT  (int targReg) {WMEM[targReg] = ub((int) getFitness());}

    @GeneCommand(weight=70, cost=.5, args=2)
    public void _UPG  (int statReg, int amountReg) {upgrade(WMEM[statReg], WMEM[amountReg].val());}
    @GeneCommand(weight=20, cost=0, args=2)
    public void _STAT (int targReg, int statReg) {WMEM[targReg] = stats[Math.abs(statReg>>5)];}
    @GeneCommand(weight=5, cost=0, args=2)
    public void _KWGT (int targReg, int kAddrReg) {if(KMEM[WMEM[kAddrReg].val()] != null) WMEM[targReg] = ub(KMEM[WMEM[kAddrReg].val()].getWeight());}
    @GeneCommand(weight=40, cost=0, args=2)
    public void _COST (int targReg, int kAddrReg) {if(KMEM[WMEM[kAddrReg].val()] != null) WMEM[targReg] = ub((int)(KMEM[WMEM[kAddrReg].val()].getCost()*4));}

    @GeneCommand(weight=20, cost=0, args=2)
    public void _OPOSX(int targReg, int uuidReg) {if(uuidReg < 255 && getArena().fromUUID(WMEM[uuidReg].val(),  WMEM[uuidReg+1].val()) != null) WMEM[targReg] = ub((int) getArena().fromUUID(WMEM[uuidReg].val(),  WMEM[uuidReg].val()+1).getX());}
    @GeneCommand(weight=20, cost=0, args=2)
    public void _OVELX(int targReg, int uuidReg) {if(uuidReg < 255 && getArena().fromUUID(WMEM[uuidReg].val(),  WMEM[uuidReg+1].val()) != null) WMEM[targReg] = ub((int) getArena().fromUUID(WMEM[uuidReg].val(),  WMEM[uuidReg].val()+1).getVelX());}
    @GeneCommand(weight=20, cost=0, args=2)
    public void _OACCX(int targReg, int uuidReg) {if(uuidReg < 255 && getArena().fromUUID(WMEM[uuidReg].val(),  WMEM[uuidReg+1].val()) != null) WMEM[targReg] = ub((int) getArena().fromUUID(WMEM[uuidReg].val(),  WMEM[uuidReg].val()+1).getAccX());}
    @GeneCommand(weight=20, cost=0, args=2)
    public void _OPOSY(int targReg, int uuidReg) {if(uuidReg < 255 && getArena().fromUUID(WMEM[uuidReg].val(),  WMEM[uuidReg+1].val()) != null) WMEM[targReg] = ub((int) getArena().fromUUID(WMEM[uuidReg].val(),  WMEM[uuidReg].val()+1).getY());}
    @GeneCommand(weight=20, cost=0, args=2)
    public void _OVELY(int targReg, int uuidReg) {if(uuidReg < 255 && getArena().fromUUID(WMEM[uuidReg].val(),  WMEM[uuidReg+1].val()) != null) WMEM[targReg] = ub((int) getArena().fromUUID(WMEM[uuidReg].val(),  WMEM[uuidReg].val()+1).getVelY());}
    @GeneCommand(weight=20, cost=0, args=2)
    public void _OACCY(int targReg, int uuidReg) {if(uuidReg < 255 && getArena().fromUUID(WMEM[uuidReg].val(),  WMEM[uuidReg+1].val()) != null) WMEM[targReg] = ub((int) getArena().fromUUID(WMEM[uuidReg].val(),  WMEM[uuidReg].val()+1).getAccY());}
    @GeneCommand(weight=20, cost=0, args=2)
    public void _OPOSR(int targReg, int uuidReg) {if(uuidReg < 255 && getArena().fromUUID(WMEM[uuidReg].val(),  WMEM[uuidReg+1].val()) != null) WMEM[targReg] = ub((int) getArena().fromUUID(WMEM[uuidReg].val(),  WMEM[uuidReg].val()+1).getR());}
    @GeneCommand(weight=20, cost=0, args=2)
    public void _OVELR(int targReg, int uuidReg) {if(uuidReg < 255 && getArena().fromUUID(WMEM[uuidReg].val(),  WMEM[uuidReg+1].val()) != null) WMEM[targReg] = ub((int) getArena().fromUUID(WMEM[uuidReg].val(),  WMEM[uuidReg].val()+1).getVelR());}
    @GeneCommand(weight=20, cost=0, args=2)
    public void _OACCR(int targReg, int uuidReg) {if(uuidReg < 255 && getArena().fromUUID(WMEM[uuidReg].val(),  WMEM[uuidReg+1].val()) != null) WMEM[targReg] = ub((int) getArena().fromUUID(WMEM[uuidReg].val(),  WMEM[uuidReg].val()+1).getAccR());}

    //TODO Better manage multiple memories
    @GeneCommand(weight=6, cost=8, args=1)
    public void _DEFS (int newMemReg) {if(SMEM[WMEM[newMemReg].val()] == null) createMemory(SMEM, WMEM[newMemReg].val());}
    @GeneCommand(weight=7, cost=4, args=1)
    public void _DELS (int memReg) {if(SMEM[WMEM[memReg].val()] != null && WMEM[memReg].val() != 0) SMEM[WMEM[memReg].val()] = null;}
    @GeneCommand(weight=4, cost=16, args=1)
    public void _DEFP (int newMemReg) {if(PMEM[WMEM[newMemReg].val()] == null) createMemory(PMEM, WMEM[newMemReg].val());}
    @GeneCommand(weight=2, cost=8, args=1)
    public void _DELP (int memReg) {if(SMEM[WMEM[memReg].val()] != null && WMEM[memReg].val() != 0 && !(WMEM[memReg].val() == loaded && CURRENT == PMEM)) PMEM[WMEM[memReg].val()] = null;}
    @GeneCommand(weight=1, cost=32, args=1)
    public void _DEFU (int newMemReg) {if(UMEM[WMEM[newMemReg].val()] == null) createMemory(UMEM, WMEM[newMemReg].val());}
    @GeneCommand(weight=1, cost=64, args=1)
    public void _DELU (int memReg) {if(SMEM[WMEM[memReg].val()] != null && WMEM[memReg].val() != 0 && !(WMEM[memReg].val() == loaded && CURRENT == UMEM)) UMEM[WMEM[memReg].val()] = null;}


    public void _LOADED(int targReg) {WMEM[targReg] = ub(loaded);}

    //TODO Get rid of this awful, awful command
    @Deprecated
    @GeneCommand(weight=10, cost=0.2, args=1)
    public void _SWAP(int arg0, int arg1, int arg2) {
        if(CURRENT[arg0] != null) {
            loaded = arg0;
            index = arg1 - 4;
        }
    }

    //TODO Mass gene exchange and crossing commands
    @GeneCommand(weight=15, cost=0, args=1)
    public void _TARG() {}
    @GeneCommand(weight=10, cost=0, args=1)
    public void _SCOPY() {}
    @GeneCommand(weight=30, cost=0, args=1)
    public void _PCOPY() {}
    @GeneCommand(weight=1, cost=0, args=1)
    public void _UCOPY() {}

    @GeneCommand(weight=42, cost=42, args=0)
    public void _REP  () {reproduce();}
    @GeneCommand(weight=42, cost=16, args=2)
    public void _TWK  (int geneToTweak, int sourceReg) {
        if(KMEM[geneToTweak] != null) KMEM[geneToTweak].setWeight(WMEM[sourceReg].val());
    }
    @GeneCommand(weight=255, cost=0, args=1)
    public void _KRAND(int targReg) {
        WMEM[targReg] = randomGene();
    }
    @GeneCommand(weight=70, cost=0, args=2)
    public void _URAND(int targReg, int sourceUMem) {
        if(UMEM[sourceUMem] != null) WMEM[targReg] = randomAddress(UMEM[sourceUMem]);
    }
    @GeneCommand(weight=70, cost=0, args=2)
    public void _PRAND(int targReg, int sourceUMem) {
        if(PMEM[sourceUMem] != null) WMEM[targReg] = randomAddress(PMEM[sourceUMem]);
    }
    @GeneCommand(weight=255, cost=0, args=2)
    public void _SRAND(int targReg, int sourceUMem) {
        if(SMEM[sourceUMem] != null) WMEM[targReg] = randomAddress(SMEM[sourceUMem]);
    }
    @GeneCommand(weight=255, cost=0, args=1)
    public void _WRAND(int targReg) {
        WMEM[targReg] = randomAddress(WMEM);
    }
    @GeneCommand(weight=255, cost=0, args=1)
    public void _IRAND(int targReg) {
        WMEM[targReg] = ub((int) (Option.random.nextFloat() * 255));
    }

}
