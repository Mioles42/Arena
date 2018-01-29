package com.miolean.arena;

import com.miolean.arena.entities.Entity;
import com.miolean.arena.entities.Field;

import java.awt.*;

/**
 * Created by commandm on 2/16/17.
 */
public class Renderer {

    private Field field;

    Renderer(Field field) {
        this.field = field;
    }

    void render(Graphics g) {


        field.renderAll(g);
    }
}
