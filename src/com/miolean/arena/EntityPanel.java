package com.miolean.arena;

import com.miolean.arena.entities.Bullet;
import com.miolean.arena.entities.Cog;
import com.miolean.arena.entities.ControlledRobot;
import com.miolean.arena.entities.Entity;
import com.miolean.arena.entities.Robot;

import javax.swing.*;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EntityPanel extends JPanel implements ActionListener{

    JTextPane textPane;
    JLabel label;
    JSpinner spinner;
    JButton refresh = new JButton("Go!");
    Point scrollPosition = new Point(0, 0);

    JScrollPane scrollPane;

    java.util.List<com.miolean.arena.entities.Robot> robots;
    Entity[] entities;


    JComboBox<String> comboBox;
    private static final int INDEX_TANKS = 1;
    private static final int INDEX_ENTITIES = 0;

    public EntityPanel(java.util.List<com.miolean.arena.entities.Robot> robots, Entity[] entities) {

        this.entities = entities;
        this.robots = robots;

        GridBagConstraints c;
        LayoutManager layout = new GridBagLayout();
        setLayout(layout);


        label = new JLabel();
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = .03;
        c.weightx = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 5, 5, 5);
        label.setText("Active Entities");
        label.setHorizontalAlignment(JLabel.CENTER);
        this.add(label, c);

        SpinnerModel model = new SpinnerNumberModel(0, 0, 255, 1);
        spinner = new JSpinner(model);
        spinner.setEditor(new JSpinner.NumberEditor(spinner, "Block ###"));
        spinner.getEditor().setFocusable(false);
        ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().setEditable(false);
        c.gridx = 1;
        c.gridy = 0;
        c.weighty = .03;
        c.weightx = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        this.add(spinner, c);

        comboBox = new JComboBox<>();
        comboBox.addItem("All Entities");
        comboBox.addItem("Robots");
        comboBox.setSelectedItem("All Entities");
        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 0;
        c.weighty = .03;
        c.weightx = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        this.add(comboBox, c);

        refresh.addActionListener(this);
        c = new GridBagConstraints();
        c.gridx = 3;
        c.gridy = 0;
        c.weighty = .03;
        c.weightx = .1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        this.add(refresh, c);

        scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);


        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setHighlighter(null);
        textPane.setContentType("text/html");
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 1;
        c.weightx = 1;
        c.gridwidth = 4;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(5, 5, 5, 5);
        textPane.setSize(1000, 1000);
        scrollPane.setViewportView(textPane);
        this.add(scrollPane, c);

    }

    void updateInfo() {
        String result = "";

        if(comboBox.getSelectedIndex() == INDEX_TANKS) {
            result += "<p>Total Robots: " + robots.size() + "</p>";

            for (int i = 0; i < robots.size(); i++) {
                com.miolean.arena.entities.Robot t = robots.get(i);

                result += "<b>[" + t.getClass().getSimpleName() + "]</b><font color=\"blue\">  ";
                result += "<a href=tank_num_" + i + ">";
                result += t.getName() + " [Fitness: " + String.format("%.2f", t.getFitness()) + "]</a>";
                result += "</font>";
                result += "<br />";

            }
        } else if(comboBox.getSelectedIndex() == INDEX_ENTITIES) {

            for (int i = 0; i < entities.length; i++) {
                if(entities[i] == null) continue;
                Entity e = entities[i];

                if(e instanceof com.miolean.arena.entities.Robot && ! (e instanceof ControlledRobot)) result += "<font color=\"blue\">";
                else if(e instanceof Cog) result += "<font color=\"orange\">";
                else if(e instanceof ControlledRobot) result += "<font color=\"grey\">";
                else if(e instanceof Bullet) result += "<font color=\"red\">";
                else result += "<font color=\"black\">";

                result += "<b>[" + e.getClass().getSimpleName() + "]</b>";

                if(e instanceof com.miolean.arena.entities.Robot && ! (e instanceof ControlledRobot)) {
                    result += "<a href=tank_num_" + i + ">";
                    result += ((com.miolean.arena.entities.Robot)e).getName() + " [Fitness: " + String.format("%.2f", ((Robot)e).getFitness()) + "]</a>";
                }
                result += "</font>";
                result += "<br />";

            }
        }

        scrollPosition = scrollPane.getViewport().getViewPosition();
        textPane.setText(result);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                scrollPane.getViewport().setViewPosition(scrollPosition);
            }
        };

        SwingUtilities.invokeLater(runnable);

    }

    public void addHyperlinkListener(HyperlinkListener l) {
        textPane.addHyperlinkListener(l);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == refresh) updateInfo();
    }
}
