package com.miolean.arena;

import com.miolean.arena.entities.Cog;
import com.miolean.arena.entities.Entity;
import com.miolean.arena.entities.Robot;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Field {

    private static final int MAX_ROBOTS = 32;
    private static final int MAX_COGS = 200;
    private static final int MAX_ENTITIES = 255*255;
    private static final int BLOCK_SIZE = 16;

    private HashMap<Integer, Entity> entities;
    private List<Robot> robots;
    private List<Cog> cogs;
    private List<Robot> topRobots;

    public Field() {

    }

    public void updateAll() {
        while(entities.entrySet().iterator().hasNext()) {
            Entity e = entities.entrySet().iterator().next().getValue();
            e.update();
        }
    }

    public void renderAll(Graphics g) {
        while(entities.entrySet().iterator().hasNext()) {
            Entity e = entities.entrySet().iterator().next().getValue();
            e.render(g);
        }
    }

    public void add(Entity e) {
        entities.remove(e.getUUID());

        int uuid;
        do { uuid = Global.random.nextInt(MAX_ENTITIES);
        } while(entities.get(uuid) != null);

        entities.put(uuid, e);
        e.appear(uuid);
    }

    public void remove(Entity e) {
        e.die();
        entities.remove(e.getUUID());
    }

    public Entity fromUUID(int uuid) {
        return entities.get(uuid);
    }

    public Collection<Entity> getEntities() { return entities.values();}
    public Collection<Robot> getTopRobots() { return topRobots;}
    public Collection<Robot> getRobots() { return robots;}
    public Collection<Cog> getCogs() { return cogs;}
}
