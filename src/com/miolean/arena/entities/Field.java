package com.miolean.arena.entities;

import com.miolean.arena.framework.Option;
import com.miolean.arena.framework.UByte;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Field {

    private static final int MAX_ROBOTS = 32*16;
    private static final int MAX_COGS = 200*4;
    public  static final int MAX_ENTITIES = 255*255;
    public  static final int TOP_LIST_LENGTH = 5;
    public static final int ARENA_SIZE = 4*1024;
    public static final int BORDER = 20*2;

    private ConcurrentHashMap<Integer, Entity> entities;
    private List<Robot> robots;
    private List<Cog> cogs;
    private List<Robot> topRobots;

    private int time = 0;

    public Field() {
        entities = new ConcurrentHashMap<>();
        robots = new ArrayList<>();
        cogs = new ArrayList<>();
        topRobots = new ArrayList<>();
        topRobots.add(new Robot(Option.class.getClassLoader().getResourceAsStream("gen/default.ergo"), this));
        topRobots.get(0).setName("Dummy");
    }

    public void updateAll() {

        time++;
        distribute();


        for(Entity e: entities.values()) {

            e.tick();

            //Is it still alive?
            if(entities.get(e.getUUID()) == null) continue;


            for(Entity g: entities.values()) {
                if(e != g && e.quickIntersects(g) && e.intersectsWith(g)) {
                    e.intersect(g);
                }
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
        for(int i = 0; i < topRobots.size(); i++) if(! topRobots.get(i).isAlive()) topRobots.get(i).setUUID(-(i+1+200));
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
        do { uuid = Option.random.nextInt(MAX_ENTITIES-1) + 1; //Don't select 0.
        } while(entities.get(uuid) != null);

        entities.put(uuid, e);
        if(e instanceof Robot) robots.add((Robot)e);
        if(e instanceof Cog) cogs.add((Cog)e);
        e.appear(uuid);
    }

    public void remove(Entity e) {
        if(e instanceof Robot) {
            robots.remove(e);
        }
        if(e instanceof Cog) cogs.remove(e);
        entities.remove(e.getUUID());
    }

    public void remove(int uuid) { remove(fromUUID(uuid)); }

    public Entity fromUUID(int uuid) {
        if(uuid < 0) return topRobots.get(uuid*-1-200-1);
        return entities.get(uuid);
    }
    public Entity fromUUID(UByte great, UByte less) {return entities.get(great.val() * 255 + less.val());}

    public Entity atLocation(int x, int y) {
        TrackerDot location = new TrackerDot(x, y, 4,0,this);

        for(Entity e: entities.values()) {
            if(e != null && e.intersectsWith(location)) return e;
        }
        return null;
    }

    public Entity fromHTML(String html) {
        if(html.contains("ergo_uuid_")) {
            html = html.replaceAll("ergo_uuid_", "");
            return fromUUID(Integer.parseInt(html));
        }
        return null;
    }

    public void distribute() {

        if(Option.random.nextFloat() < 0.05) {
            Cog cog = new Cog(5 + (int) (10 * Option.random.nextFloat()), this);
            cog.setX(Option.random.nextFloat() * ARENA_SIZE);
            cog.setY(Option.random.nextFloat() * ARENA_SIZE);
            cog.setR(Option.random.nextFloat() * ARENA_SIZE);
            add(cog);
        }

        if(Option.random.nextFloat() < 0.01) {
            Robot robot;
            robot = new Robot(getTopRobots().get(Option.random.nextInt(getTopRobots().size())), this);

            robot.setX(Option.random.nextFloat() * ARENA_SIZE);
            robot.setY(Option.random.nextFloat() * ARENA_SIZE);
            robot.setR(Option.random.nextFloat() * ARENA_SIZE);
            add(robot);
        }
    }

    public ConcurrentHashMap<Integer, Entity> getEntities() { return entities;}
    public List<Robot> getTopRobots() { return topRobots;}
    public List<Robot> getRobots() { return robots;}
    public List<Cog> getCogs() { return cogs;}

    public int getTime() {
        return time;
    }
}
