package com.miolean.arena.framework;

import com.miolean.arena.entities.Field;

import java.awt.*;

/**
 * Created by commandm on 2/16/17.
 */
public class Renderer implements Perpetual{

    private Field field;

    public Renderer(Field field) {
        this.field = field;
    }

    public void tick(Object... args) {
        Graphics g = (Graphics) args[0];
        field.renderAll(g);
    }


    public void stop() {}
    public void start() {}
    public void pause() {}
    public void resume() {}
}
