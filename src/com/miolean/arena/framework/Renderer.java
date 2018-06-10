package com.miolean.arena.framework;

import com.miolean.arena.entities.Arena;

import java.awt.*;

/**
 * Created by commandm on 2/16/17.
 */
public class Renderer implements Perpetual{

    private Arena arena;

    public Renderer(Arena arena) {
        this.arena = arena;
    }

    public void tick(Object... args) {
        Graphics g = (Graphics) args[0];
        arena.renderAll(g);
    }


    public void stop() {}
    public void start() {}
    public void pause() {}
    public void resume() {}
}
