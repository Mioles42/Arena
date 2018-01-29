package com.miolean.arena.entities;

import com.miolean.arena.Global;
import com.miolean.arena.UByte;
import com.miolean.arena.entities.Cog;
import com.miolean.arena.entities.Entity;
import com.miolean.arena.entities.Robot;
import sun.plugin.dom.exception.InvalidStateException;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Field {

    private static final int MAX_ROBOTS = 32;
    private static final int MAX_COGS = 200;
    public  static final int MAX_ENTITIES = 255*255;
    public  static final int TOP_LIST_LENGTH = 10;

    private ConcurrentHashMap<Integer, Entity> entities;
    private List<Robot> robots;
    private List<Cog> cogs;
    private List<Robot> topRobots;

    public Field() {
        entities = new ConcurrentHashMap<>();
        robots = new ArrayList<>();
        cogs = new ArrayList<>();
        topRobots = new ArrayList<>();
        topRobots.add(new Robot(Global.class.getClassLoader().getResourceAsStream("gen/default.ergo"), this));
        topRobots.get(0).setName("Dummy");
    }

    public void updateAll() {
        for(Entity e: entities.values()) {

            e.tick();

            //Is it still alive?
            if(entities.get(e.getUUID()) == null) continue;

            for(Entity g: entities.values()) {
                if(e.intersectsWith(g)) e.intersect(g);
            }

            //Is it still alive?
            if(entities.get(e.getUUID()) == null) continue;


            if(e instanceof Robot && ((Robot) e).getFitness() > 0 && (((Robot) e).getFitness() > topRobots.get(0).getFitness()) || e == topRobots.get(0)) {
                Robot r = (Robot) e;
                if(! topRobots.contains(r)) {
                    topRobots.add(r);
                }
            }
        }
        Collections.sort(topRobots);
        while(topRobots.size() > TOP_LIST_LENGTH) topRobots.remove(0);
    }

    public void renderAll(Graphics g) {
        for(Entity e: entities.values()) {
            e.render(g);
        }
    }

    public void add(Entity e) {

        if(e instanceof Robot && robots.size() >= MAX_ROBOTS) return;
        if(e instanceof Cog && cogs.size() >= MAX_COGS) return;

        entities.remove(e.getUUID());

        int uuid;
        do { uuid = Global.random.nextInt(MAX_ENTITIES-1) + 1; //Don't select 0.
        } while(entities.get(uuid) != null);

        entities.put(uuid, e);
        if(e instanceof Robot) robots.add((Robot)e);
        if(e instanceof Cog) cogs.add((Cog)e);
        e.appear(uuid);
    }

    public void remove(Entity e) {
        if(e instanceof Robot) {
            robots.remove(e);
            if(robots.contains(e)) {
            }

        }
        if(e instanceof Cog) cogs.remove(e);

        e.die();
        entities.remove(e.getUUID());
    }

    public void remove(int uuid) { remove(fromUUID(uuid)); }

    public Entity fromUUID(int uuid) {
        return entities.get(uuid);
    }
    public Entity fromUUID(UByte great, UByte less) {return entities.get(great.val() * 255 + less.val());}

    public Entity atLocation(int x, int y) {
        TrackerDot location = new TrackerDot(x, y, 0,this);

        for(Entity e: entities.values()) {
            if(e != null && e.intersectsWith(location)) return e;
        }
        return null;
    }

    public Entity fromHTML(String html) {
        if(html.contains("tank_greatest")) {
            html = html.replaceAll("tank_greatest_", "");
            return topRobots.get(Integer.parseInt(html));
        }
        return null;
    }

    public ConcurrentHashMap<Integer, Entity> getEntities() { return entities;}
    public List<Robot> getTopRobots() { return topRobots;}
    public List<Robot> getRobots() { return robots;}
    public List<Cog> getCogs() { return cogs;}
}
