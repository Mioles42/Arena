package com.miolean.arena.input;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public abstract class Input<E> {

    private List<ChangeListener> listenerList = new ArrayList<>();

    private String name;
    private String description;
    private E value;

    public Input(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract JPanel open();

    public E getValue() { return value; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public void setValue(E o) {value = o;}

    private void alertListeners() {
        for(ChangeListener l: listenerList) l.stateChanged(new ChangeEvent(this));
    }

    public void addChangeListener(ChangeListener cl) {
        listenerList.add(cl);
    }
    public void removeChangeListener(ChangeListener cl) {
        listenerList.remove(cl);
    }

}
