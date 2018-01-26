package com.miolean.arena;

import com.miolean.arena.entities.Entity;

import java.awt.*;

/**
 * Created by commandm on 2/16/17.
 */
public class Renderer {

    private Entity[] entities;

    Renderer(Entity[] entities) {
        this.entities = entities;
    }

    void render(Graphics g) {


        for(Entity e: entities) {
            if(e == null) continue;
            e.render(g);
        }


    }
}
