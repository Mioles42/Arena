package com.miolean.arena;

import com.miolean.arena.entities.Cog;
import com.miolean.arena.entities.Entity;
import com.miolean.arena.entities.Robot;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Field {

    private static final int MAX_ROBOTS = 32;
    private static final int MAX_COGS = 200;
    private static final int MAX_ENTITIES = 255;
    private static final int BLOCK_SIZE = 16;

    private HashMap<UByte, Entity> entities;
    private List<Robot> robots;
    private List<Cog> cogs;
    private List<Robot> topRobots;

    public Field() {

    }

    public void updateAll() {
        for(Entity e: entities) {
            e.update();
        }
    }

    public void renderAll(Graphics g) {
        for (Entity e: entities) {
            e.render(g);
        }
    }

    public List<Entity> getEntities() { return entities; }
    public List<Robot> getTopRobots() { return topRobots;}
    public List<Robot> getRobots() { return robots;}
    public List<Cog> getCogs() { return cogs;}
}
