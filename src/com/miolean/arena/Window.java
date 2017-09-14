package com.miolean.arena;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Window extends JFrame implements ChangeListener ,KeyListener {

    private JSlider slider;
    MainPanel main;


    Window(MainPanel mainPanel) {
        this.main = mainPanel;
        LayoutManager layout = new GridBagLayout();
        setLayout(layout);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JPanel genomePanel = new JPanel();
        JPanel activeSetPanel = new JPanel();
        JPanel usedSetPanel = new JPanel();


        //Add the main panel:
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BorderLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 2;
        c.gridwidth = 1;
        c.ipadx = 5;
        c.ipady = 5;
        c.weightx = .7;
        c.weighty = .5;
        mainContainer.add(mainPanel, BorderLayout.CENTER);
        mainContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLoweredBevelBorder()
            ));
        this.add(mainContainer, c);

        //Add the info panel:
        //Because of the way borders work we have to use multiple panels...
        JTabbedPane infoPanel = new JTabbedPane();
        JPanel infoPanelPanel = new JPanel();
        infoPanelPanel.setLayout(new BorderLayout());
        infoPanelPanel.add(infoPanel, BorderLayout.CENTER);
        infoPanel.setBackground(new Color(0, 155, 0));
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.ipadx = 5;
        c.ipady = 5;
        c.weightx = .4;
        c.weighty = .5;

        infoPanel.addTab("Genome", genomePanel);
        infoPanel.addTab("Active Set", activeSetPanel);
        infoPanel.addTab("Used Set", usedSetPanel);

        infoPanelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.add(infoPanelPanel, c);

        //Add the control panel:
        JPanel controlPanel = new JPanel();
        JPanel controlPanelPanel = new JPanel();
        controlPanel.setBackground(new Color(100, 150, 250));

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 1;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.ipadx = 5;
        c.ipady = 5;
        c.weightx = .4;
        c.weighty = .3;
        controlPanelPanel.setLayout(new BorderLayout());
        controlPanelPanel.add(controlPanel, BorderLayout.CENTER);
        controlPanelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.add(controlPanelPanel, c);

        slider = new JSlider();
        slider.setMaximum(1000);
        slider.setMinimum(1);
        slider.setValue(20);
        slider.setFocusable(false);
        slider.addChangeListener(this);
        controlPanel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        controlPanel.add(slider, c);

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if(e.getSource() == slider) {
            Global.tickSpeed = slider.getValue();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        main.keyTyped(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        main.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        main.keyReleased(e);

    }
}
