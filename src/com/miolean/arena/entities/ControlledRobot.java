package com.miolean.arena.entities;
import com.miolean.arena.Handler;

import static com.miolean.arena.Global.*;
import static com.miolean.arena.UByte.ub;

public class ControlledRobot extends Robot {

    public ControlledRobot(int x, int y, Handler handler) {
        super(handler);
        setX(x);
        setY(y);
        setHealth(100);

        //Do better default stats.
        stats[STAT_SPEED] = ub(20);
        stats[STAT_BULLET_SPEED] = ub(20);
        stats[STAT_ROTATE_SPEED] = ub(40);
        stats[STAT_HASTE] = ub(120);
        stats[STAT_DAMAGE] = ub(10);
        stats[STAT_MAX_HEALTH] = ub(100);
        stats[STAT_BULLET_SPREAD] = ub(30);
    }

    @Override
    public void update() {
        applyPhysics();
        if(KEY[KEY_W]) forward(100);
        else if(KEY[KEY_S]) forward(-100);
        else forward(0);

        if(KEY[KEY_A]) rotate(100);
        else if(KEY[KEY_D]) rotate(-100);
        else rotate(0);

        if(KEY[KEY_SPACE]) fire();
        if(KEY[KEY_F]) damage(1);
        if(KEY[KEY_R]) heal(1);
    }

    @Override
    public void onBirth() {

    }

    @Override
    public void onDeath() {

    }
}