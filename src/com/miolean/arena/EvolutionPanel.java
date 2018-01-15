package com.miolean.arena;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.*;
import java.awt.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class EvolutionPanel extends JPanel implements HyperlinkListener{

    JTextPane textPane;
    JLabel label;

    JScrollPane scrollPane;

    java.util.List<Tank> tanks;

    private static final int INDEX_UMEM = 0;
    private static final int INDEX_PMEM = 1;
    private static final int INDEX_SMEM = 2;
    private static final int INDEX_WMEM = 3;

    public EvolutionPanel(java.util.List<Tank> tanks) {

        this.tanks = tanks;

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
        label.setText("Top tanks:");
        this.add(label, c);

        scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);


        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setHighlighter(null);
        textPane.addHyperlinkListener(this);
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

        for(int i = 0; i < tanks.size()-1; i++) {
            Tank t = tanks.get(i);

            result += "<p><b>[" + (i+1) + "]</b>  ";
            if(!t.isAlive()) result += "<s>";
            result += t.name + " [Fitness: " + t.fitness + "]";
            if(!t.isAlive()) result += "</s>";
            result += "</p>";

        }


        textPane.setText(result);

    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {

    }
}
