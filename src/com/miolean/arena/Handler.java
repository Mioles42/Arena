package com.miolean.arena;

import org.junit.jupiter.api.Test;

/**
 * Created by commandm on 2/19/17.
 */
public class Handler {

    private Entity[] entities;
    int numEntities;

    int lastUUIDUsed = 0;

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
            if(e.health <= 0) remove(e.getUUID());
        }
    }

    public boolean remove(int uuid) {
        if(entities[uuid] == null) return false;
        entities[uuid] = null;
        numEntities--;
        return true;
    }

    public boolean add(Entity e) {
        while(entities[lastUUIDUsed] != null) {
            lastUUIDUsed++;
            lastUUIDUsed %= entities.length;
        }
        entities[lastUUIDUsed] = e;
        e.uuidMost = lastUUIDUsed >> 8;
        e.uuidLeast = lastUUIDUsed % 0b100000000;
        e.handler = this;
        numEntities++;
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

    public Entity getByUUID(UByte uuidLeast, UByte uuidMost) {
        return entities[uuidMost.val()*256 + uuidLeast.val()];
    }
}
