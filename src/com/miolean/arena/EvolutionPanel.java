package com.miolean.arena;

import com.miolean.arena.entities.Robot;

import javax.swing.*;
import javax.swing.event.HyperlinkListener;
import java.awt.*;

public class EvolutionPanel extends JPanel{

    JTextPane textPane;
    JLabel label;

    JScrollPane scrollPane;

    java.util.List<com.miolean.arena.entities.Robot> robots;

    private static final int INDEX_UMEM = 0;
    private static final int INDEX_PMEM = 1;
    private static final int INDEX_SMEM = 2;
    private static final int INDEX_WMEM = 3;

    public EvolutionPanel(java.util.List<com.miolean.arena.entities.Robot> robots) {

        this.robots = robots;

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
        label.setText("Top robots:");
        this.add(label, c);

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
        c.gridwidth = 3;
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

        for(int i = 0; i < robots.size()-1; i++) {
            Robot t = robots.get(i);

            result += "<p><b>[" + (i+1) + "]</b>  ";
            if(!t.isAlive()) result += "<s><font color=\"red\">";
            result += "<a href=tank_greatest_"+i + ">";
            result += t.name + " [Fitness: " + String.format("%.2f", t.fitness) + "]</a>";
            if(!t.isAlive()) result += "</font></s>";
            result += "</p>";

        }


        textPane.setText(result);

    }

    public void addHyperlinkListener(HyperlinkListener l) {
        textPane.addHyperlinkListener(l);
    }
}
