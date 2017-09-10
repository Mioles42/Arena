package com.miolean.arena;

import java.awt.*;
import static com.miolean.arena.Global.*;

/**
 * Created by commandm on 2/16/17.
 */
public class Renderer {

    private Entity[] entities;

    public Renderer(Entity[] entities) {
        this.entities = entities;
    }

    public void render(Graphics g) {

        g.setColor(Color.GRAY);
        for(int i = 0; i < ARENA_SIZE / 64; i++) {
            g.drawLine(i*64, BORDER, i*64, ARENA_SIZE -BORDER);
            g.drawLine(BORDER, i*64, ARENA_SIZE - BORDER, i*64);
        }

        for(Entity e: entities) {
            if(e == null) continue;
            e.render(g);
        }
        g.setColor(Color.RED);
        g.drawRect(10, 10, ARENA_SIZE - BORDER, ARENA_SIZE - BORDER);


    }
}
