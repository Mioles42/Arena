package com.miolean.arena;

import com.miolean.arena.entities.Cog;
import com.miolean.arena.entities.Entity;
import com.miolean.arena.entities.Robot;
import com.miolean.arena.entities.TrackerDot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by commandm on 2/19/17.
 */
public class Handler {

    final static int MAX_COGS = 200;
    final static int MAX_TANKS = 32;

    private Entity[] entities;
    List<Robot> topRobots = new ArrayList<>();
    List<Robot> robots; //This will come in handy sooner or later

    int numEntities;

    int numCogs;
    int numTanks;

    private int lastUUIDUsed = 0;

    Handler(Entity[] entities) {
        this.entities = entities;
        robots = new ArrayList<>();
    }

    public void update() {
        Global.time++;

        for(Entity e: entities) {
            if(e == null) continue;
            e.update();
            for(Entity j: entities) { if (j == null) continue;
                if(e.intersectsWith(j) && e != j) e.intersect(j);
            }
            if(e.getHealth() <= 0) remove(e.getUUID());

            //If we're holding a Robot that has beaten a record (or there are no records)
            if(topRobots.size() == 0 && e instanceof Robot) topRobots.add((Robot) e);
            else if(e instanceof Robot && ((Robot) e).getFitness() > topRobots.get(topRobots.size()-1).getFitness()) {
                Robot robot = (Robot) e;
                topRobots.remove(robot); //Just recategorize it

                //We should really do a binary search here, but the time it would save us is pretty limited tbh
                for(int i = 0; i < topRobots.size(); i++) {
                    if(robot.getFitness() > topRobots.get(i).getFitness()) {
                        topRobots.add(i, robot);
                        break;
                    }
                }
                if(topRobots.size() > 11) topRobots.remove(11);
            }
        }
    }

    public void remove(int uuid) {
        if(entities[uuid] instanceof Cog) numCogs--;
        if(entities[uuid] instanceof Robot) {
            numTanks--;
            robots.remove( entities[uuid]);
        }

        entities[uuid].onDeath();
        entities[uuid] = null;
        numEntities--;


    }

    public int add(Entity e) {

        if(e instanceof Cog && numCogs >= MAX_COGS) return 0;
        if(e instanceof Robot && numTanks >= MAX_TANKS) return 0;

        if(e instanceof Cog) numCogs++;
        if(e instanceof Robot) {
            numTanks++;
            robots.add((Robot)e);
        }
        numEntities++;

        while(entities[lastUUIDUsed] != null) {
            lastUUIDUsed++;
            lastUUIDUsed %= entities.length;
        }
        entities[lastUUIDUsed] = e;
        e.onBirth();
        return lastUUIDUsed;
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

    public int withinDistance(double x, double y, int distance) {

        int count = 0x0000;
        for(Entity e: entities) {
            if(e == null) continue;
            if ((x - e.getX()) * (x - e.getX()) + (y - e.getY()) * (y - e.getY()) < distance * distance) count++;
        }
        return count;
    }

    //TODO more than 256 entity support
    public Entity getByUUID(UByte uuidLeast, UByte uuidMost) {
        return entities[uuidLeast.val()];
    }

    public Entity entityAtLocation(int x, int y) {

        TrackerDot location = new TrackerDot(x, y, 1, this);

        for(Entity e: entities) {
            if(e != null && e.intersectsWith(location)) return e;
        }

        return null;
    }

    public void distribute() {

        if(Global.random.nextFloat() < 0.05) {
            Cog cog = new Cog(5 + (int) (10 * Global.random.nextFloat()), this);
            cog.setX(Global.random.nextFloat() * Global.ARENA_SIZE);
            cog.setY(Global.random.nextFloat() * Global.ARENA_SIZE);
            cog.setR(Global.random.nextFloat() * Global.ARENA_SIZE);
            add(cog);
        }

        if(Global.random.nextFloat() < 0.01) {
            Robot robot;
            if(topRobots.size() > 9) robot = new Robot(topRobots.get(Global.random.nextInt(5)), this);
            else robot = new Robot(Global.class.getResourceAsStream("default.ergo"), this);

            robot.setX(Global.random.nextFloat() * Global.ARENA_SIZE);
            robot.setY(Global.random.nextFloat() * Global.ARENA_SIZE);
            robot.setR(Global.random.nextFloat() * Global.ARENA_SIZE);
            add(robot);
        }
    }

    public List<Robot> getRobots() {
        return robots;
    }

}
