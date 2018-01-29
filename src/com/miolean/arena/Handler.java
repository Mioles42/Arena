package com.miolean.arena;

import com.miolean.arena.entities.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by commandm on 2/19/17.
 */
public class Handler {

    Field field;
    private int lastUUIDUsed = 0;

    public static Robot BASE_ROBOT;

    Handler(Field field) {

        this.field = field;
    }

    public void update() {
        field.updateAll();
    }


//    public Entity closestToDistance(double x, double y, int distance) {
//        double record = 0xFFFF;
//        Entity result = null;
//        for(Entity e: entities) {
//            if(e == null) continue;
//            double closeness = ((x - e.x) * (x - e.x) + (y - e.y) * (y - e.y) - distance * distance);
//            if(closeness < record) {
//                record = closeness;
//                result = e;
//            }
//        }
//        return result;
//    }



    public void distribute() {

        if(Global.random.nextFloat() < 0.05) {
            Cog cog = new Cog(5 + (int) (10 * Global.random.nextFloat()), field);
            cog.setX(Global.random.nextFloat() * Global.ARENA_SIZE);
            cog.setY(Global.random.nextFloat() * Global.ARENA_SIZE);
            cog.setR(Global.random.nextFloat() * Global.ARENA_SIZE);
            field.add(cog);
        }

        if(Global.random.nextFloat() < 0.01) {
            Robot robot;
            robot = new Robot(field.getTopRobots().get(Global.random.nextInt(field.getTopRobots().size())), field);

            robot.setX(Global.random.nextFloat() * Global.ARENA_SIZE);
            robot.setY(Global.random.nextFloat() * Global.ARENA_SIZE);
            robot.setR(Global.random.nextFloat() * Global.ARENA_SIZE);
            field.add(robot);
        }
    }
}
