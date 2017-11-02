package com.miolean.arena;
import static com.miolean.arena.Global.*;
import static com.miolean.arena.UByte.ub;

public class ControlledTank extends Tank {

    public ControlledTank(int x, int y) {
        this.x = x;
        this.y = y;
        health = 100;

        //Do better default stats.
        stats[STAT_SPEED] = ub(100);
        stats[STAT_BULLET_SPEED] = ub(50);
        stats[STAT_ROTATE_SPEED] = ub(100);
        stats[STAT_HASTE] = ub(100);
        stats[STAT_DAMAGE] = ub(30);
        stats[STAT_MAX_HEALTH] = ub(100);
        stats[STAT_BULLET_SPREAD] = ub(100);
    }

    @Override
    public void update() {
        applyPhysics();
        if(KEY[KEY_W]) forward(8);
        else if(KEY[KEY_S]) forward(-8);
        else forward(0);

        if(KEY[KEY_A]) rotate(8);
        else if(KEY[KEY_D]) rotate(-8);
        else rotate(0);

        if(KEY[KEY_SPACE]) fire();
        if(KEY[KEY_F]) health--;
    }
}
