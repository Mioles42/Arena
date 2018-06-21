package com.miolean.arena.ui;

import javax.swing.*;
import java.awt.*;

public abstract class LivePanel extends JPanel {

    public LivePanel() {
        super.setLayout(new BorderLayout());

        JPanel topBar = new JPanel();
        topBar.add(Box.createHorizontalStrut(40));
        topBar.setBackground(Color.CYAN);
        add(topBar, BorderLayout.NORTH);


    }

    public abstract void display();

    @Override
    protected void addImpl(Component c, Object constraints, int index) {
        super.addImpl(c, BorderLayout.CENTER, index);
    }

}
