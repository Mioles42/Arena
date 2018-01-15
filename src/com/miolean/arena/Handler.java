package com.miolean.arena;

/**
 * Created by commandm on 2/19/17.
 */
class Handler {

    final static int MAX_COGS = 128;
    final static int MAX_TANKS = 32;

    private Entity[] entities;
    int numEntities;

    int numCogs;
    int numTanks;

    private int lastUUIDUsed = 0;

    Handler(Entity[] entities) {
        this.entities = entities;
    }

    void update() {
        Global.time++;

        for(Entity e: entities) {
            if(e == null) continue;
            e.update();
            for(Entity j: entities) { if (j == null) continue;
                if(e.intersectsWith(j) && e != j) e.intersect(j);
            }
            if(e.health <= 0) remove(e.getUUID());
        }
    }

    void remove(int uuid) {
        if(entities[uuid] instanceof Cog) numCogs--;
        if(entities[uuid] instanceof Tank) numTanks--;

        entities[uuid].onDeath();
        entities[uuid].handler = null;
        entities[uuid] = null;
        numEntities--;


    }

    void add(Entity e) {

        if(e instanceof Cog && numCogs >= MAX_COGS) return;
        if(e instanceof Tank && numTanks >= MAX_TANKS) return;

        while(entities[lastUUIDUsed] != null) {
            lastUUIDUsed++;
            lastUUIDUsed %= entities.length;
        }
        entities[lastUUIDUsed] = e;
        e.uuidMost = lastUUIDUsed >> 8;
        e.uuidLeast = lastUUIDUsed % 0b100000000;
        e.handler = this;
        numEntities++;
        e.onBirth();

        if(e instanceof Cog) numCogs++;
        if(e instanceof Tank) numTanks++;
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
            if ((x - e.x) * (x - e.x) + (y - e.y) * (y - e.y) < distance * distance) count++;
        }
        return count;
    }

    //TODO more than 256 entity support
    Entity getByUUID(UByte uuidLeast, UByte uuidMost) {
        return entities[uuidLeast.val()];
    }

    Entity entityAtLocation(int x, int y) {

        TrackerDot location = new TrackerDot(x, y, 1);

        for(Entity e: entities) {
            if(e != null && e.intersectsWith(location)) return e;
        }

        return null;
    }

    void distribute() {

        if(Global.random.nextFloat() < 0.05) {
            Cog cog = new Cog(5 + (int) (10 * Global.random.nextFloat()));
            cog.x = Global.random.nextFloat() * Global.ARENA_SIZE;
            cog.y = Global.random.nextFloat() * Global.ARENA_SIZE;
            cog.r = Global.random.nextFloat() * Global.ARENA_SIZE;
            add(cog);
        }

        if(Global.random.nextFloat() < 0.01) {
            Tank tank = new Tank(Global.BASE_TANK);
            tank.x = Global.random.nextFloat() * Global.ARENA_SIZE;
            tank.y = Global.random.nextFloat() * Global.ARENA_SIZE;
            tank.r = Global.random.nextFloat() * Global.ARENA_SIZE;
            add(tank);
        }
    }
}
