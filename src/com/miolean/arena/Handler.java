package com.miolean.arena;

import org.junit.jupiter.api.Test;

/**
 * Created by commandm on 2/19/17.
 */
public class Handler {

    private Entity[] entities;

    public Handler(Entity[] entities) {
        this.entities = entities;
    }

    public void update() {
        for(Entity e: entities) {
            if(e == null) continue;
            e.update();
            for(Entity j: entities) {
                if(e.intersectsWith(j) && e != j) e.intersect(j);
            }
        }
    }

    public Entity entityAt(byte uuidMost, byte uuidLeast) {
        short uuid = (short) (((short) uuidMost) << 8);
        uuid += uuidLeast;
        return entities[uuid];
    }

    public boolean remove(int uuid) {
        if(entities[uuid] == null) return false;
        entities[uuid] = null;
        return true;
    }

    public Entity closestToDistance(double x, double y, int distance) {
        double record = 0xFFFF;
        Entity result = null;
        for(Entity e: entities) {
            if(e == null) continue;
            double closeness = ((x - e.x) * (x - e.x) + (y - e.y) * (y - e.y) - distance * distance);
            if(closeness < record) {
                record = closeness;
                result = e;
            }
        }
        return result;
    }

    public int withinDistance(double x, double y, int distance) {
        int count = 0x0000;
        for(Entity e: entities) {
            if(e == null) continue;
            if ((x - e.x) * (x - e.x) + (y - e.y) * (y - e.y) < distance * distance) count++;
        }
        return count;
    }
}
