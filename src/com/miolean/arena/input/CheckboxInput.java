package com.miolean.arena.input;


import javax.swing.*;

public class CheckboxInput<E extends Boolean> extends Input<E> {


    public CheckboxInput(String name, String description) {
        super(name, description);
    }

    @Override
    public JPanel open() {
        return null;
    }
}
