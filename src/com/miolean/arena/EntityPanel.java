package com.miolean.arena;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EntityPanel extends JPanel {

    List<Tank> tanks;
    Entity[] entities;
    JComboBox<String> comboBox;
    JLabel label;
    JList<Entity> list;
    JScrollPane scrollPane;

    private static final int INDEX_TANKS = 0;
    private static final int INDEX_ALL_ENTITIES = 1;

    public EntityPanel(List<Tank> tanks, Entity[] entities) {
        this.tanks = tanks;
        this.entities = entities;

        GridBagConstraints c;
        LayoutManager layout = new GridBagLayout();
        setLayout(layout);

        label = new JLabel();
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = .05;
        c.weightx = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 5, 5, 5);
        label.setText("Showing entities:");
        this.add(label, c);

        comboBox = new JComboBox<>();
        comboBox.addItem("Tanks");
        comboBox.addItem("All Entities");
        comboBox.setSelectedItem("Tanks");
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.weighty = .05;
        c.weightx = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        this.add(comboBox, c);

        scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        list = new JList<>();
        list.setListData(entities);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 1;
        c.weightx = 1;
        c.gridwidth = 3;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(5, 5, 5, 5);
        list.setSize(1000, 1000);
        scrollPane.setViewportView(list);
        this.add(scrollPane, c);
    }

    void updateInfo() {
        if(comboBox.getSelectedIndex() == INDEX_ALL_ENTITIES) {
            list.setListData(entities);
        }

        if(comboBox.getSelectedIndex() == INDEX_TANKS) {
            Tank[] tankArray = new Tank[tanks.size()];
            tanks.toArray(tankArray);
            list.setListData(tankArray);
        }
    }

    void addListSelectionListener(ListSelectionListener lsl) {
        list.addListSelectionListener(lsl);
    }
}
