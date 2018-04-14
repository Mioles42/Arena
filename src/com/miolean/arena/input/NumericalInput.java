package com.miolean.arena.input;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class NumericalInput extends Input implements ChangeListener {

    private Integer value = 0;
    private List<WeakReference<JSlider>> sliders = new ArrayList<WeakReference<JSlider>>();
    private int min;
    private int max;


    public NumericalInput(final String name, final String description, int min, int max, int init) {
        super(name, description);
        value = init;
        this.min = min;
        this.max = max;
    }

    @Override
    public JPanel open() {

        JPanel panel = new JPanel();
        JLabel label = new JLabel(getName());
        JSlider slider = new JSlider();
        WeakReference weak = new WeakReference<>(slider);
        sliders.add(weak);
        slider.setMaximum(1000);
        slider.setMinimum(1);
        slider.setValue(20);
        slider.addChangeListener(this);


        panel.add(label);
        panel.add(slider);
        slider.setValue(value);

        return panel;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (Integer) value;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        value = ((JSlider)e.getSource()).getValue();
        System.out.println(value);
        for(WeakReference w: sliders) {
            if(w.get() == null) {
                sliders.remove(w);
                continue;
            }
            ((JSlider)w.get()).setValue(value);
        }
    }
}
