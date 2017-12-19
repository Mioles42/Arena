package com.miolean.arena;

/**
 * Created by commandm on 5/21/17.
 *
 */


class Global {

    //Number of ticks in-simulation
    static int time;

    //Seconds per tick, for each of the different cycles
    static int updateCycle = 50;
    static int displayCycle = 100;
    static int renderCycle = 20;
    static int distributeCycle = 20;

    //1 pixel= 1 tank-meter, I suppose
    static final int ARENA_SIZE = 1024;

    static final int BORDER = 20;

    static boolean[] KEY = new boolean[9];

    //Key constants
    static final int KEY_Q = 0;
    static final int KEY_W = 1;
    static final int KEY_E = 2;
    static final int KEY_R = 3;
    static final int KEY_A = 4;
    static final int KEY_S = 5;
    static final int KEY_D = 6;
    static final int KEY_F = 7;
    static final int KEY_SPACE = 8;

}
