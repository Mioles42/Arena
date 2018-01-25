package com.miolean.arena;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Window extends JFrame implements ChangeListener ,KeyListener, ListSelectionListener, HyperlinkListener {

    private JSlider slider;
    private JTabbedPane tabbedPane;
    MainPanel main;
    MemoryPanel memoryPanel;
    EvolutionPanel evolutionPanel;
    EntityPanel entityPanel;

    java.util.List<Robot> topRobots;
    java.util.List<Robot> robots;

    Window(MainPanel mainPanel, java.util.List<Robot> topRobots, Entity[] entities, java.util.List<Robot> robots) {
        this.main = mainPanel;
        this.topRobots = topRobots;
        this.robots = robots;

        LayoutManager layout = new GridBagLayout();
        setLayout(layout);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        makeMainLayout(topRobots, entities, robots);
    }

    public void makeMainLayout(java.util.List<Robot> topRobots, Entity[] entities, java.util.List<Robot> robots) {
        JPanel genomePanel = new JPanel();
        memoryPanel = new MemoryPanel(null);
        evolutionPanel = new EvolutionPanel(topRobots);
        entityPanel = new EntityPanel(robots, entities);

        JPanel usedSetPanel = new JPanel();

        evolutionPanel.addHyperlinkListener(this);
        entityPanel.addHyperlinkListener(this);

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
        mainContainer.add(main, BorderLayout.CENTER);
        mainContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLoweredBevelBorder()
        ));
        this.add(mainContainer, c);

        //Add the info panel:
        //Because of the way borders work we have to use multiple panels...
        tabbedPane = new JTabbedPane();
        JPanel infoPanelPanel = new JPanel();
        infoPanelPanel.setLayout(new BorderLayout());
        infoPanelPanel.add(tabbedPane, BorderLayout.CENTER);
        tabbedPane.setBackground(new Color(0, 155, 0));

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.ipadx = 5;
        c.ipady = 5;
        c.weightx = .2;
        c.weighty = .5;

        //ImageIcon genomeIcon = new ImageIcon(Window.class.getClassLoader().getResource("tex/list.png"));

        makeGenomePanel(genomePanel);

        tabbedPane.addTab("Program Memory", memoryPanel);
        tabbedPane.addTab("Entities", entityPanel);
        tabbedPane.addTab("Genome", genomePanel);
        tabbedPane.addTab("Evolution", evolutionPanel);

        JLabel genomeLabel = new JLabel("Genome", JLabel.CENTER);
        genomeLabel.setVerticalTextPosition(JLabel.BOTTOM);
        genomeLabel.setHorizontalTextPosition(JLabel.CENTER);
        tabbedPane.setTabComponentAt(2, genomeLabel);

        infoPanelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.add(infoPanelPanel, c);

        //Add the control panel:
        JPanel controlPanel = new JPanel();
        JPanel controlPanelPanel = new JPanel();

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 1;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.ipadx = 5;
        c.ipady = 5;
        c.weightx = .2;
        c.weighty = .3;
        controlPanelPanel.setLayout(new BorderLayout());
        controlPanelPanel.add(controlPanel, BorderLayout.CENTER);
        controlPanelPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createEtchedBorder()
        ));
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

    public void display() {

        memoryPanel.updateInfo();
        evolutionPanel.updateInfo();
        entityPanel.updateInfo();
    }

    public void makeGenomePanel(JPanel genomePanel) {

        genomePanel.setLayout(new GridBagLayout());

        int category = -1;

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("");
        DefaultMutableTreeNode branch = null;
        DefaultMutableTreeNode twig;


        for(int i = 0; i < Robot.KMEM.length; i++) {
            if(i/16 > category) {
                if(branch != null) root.add(branch);
                category = i/16;
                branch = new DefaultMutableTreeNode(Integer.toHexString(category).toUpperCase() + "  " + Gene.GENE_CATEGORIES[category]);
                //Add a new section!
            }
            if(Robot.KMEM[i] != null) {
                 twig = new DefaultMutableTreeNode(Integer.toHexString(i).toUpperCase() + "|  " + Robot.KMEM[i]);
                 branch.add(twig);
            }
        }

        root.add(branch);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.ipadx = 5;
        c.ipady = 5;
        c.weightx = .1;
        c.weighty = .1;

        JTree tree = new JTree(root);
        tree.setEditable(false);
        tree.setFocusable(false);
        JScrollPane scrollPane = new JScrollPane(tree);
        genomePanel.add(scrollPane, c);
    }


    @Override
    public void stateChanged(ChangeEvent e) {
        if(e.getSource() == slider) {
            Global.updateCycle = (int) (1000.0/slider.getValue());
            Global.distributeCycle = (int) (Global.distributeRatio * 1000.0/slider.getValue());
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        main.requestFocus();
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

    public void setActiveTank(Robot robot) {
        memoryPanel.source = robot;
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {

            if(e.getDescription().contains("tank_greatest_")) {
                int i = Integer.parseInt(e.getDescription().replace("tank_greatest_", ""));
                setActiveTank(topRobots.get(i));
                if (topRobots.get(i).isAlive()) main.viewholder = topRobots.get(i);
                tabbedPane.setSelectedIndex(0);
            } else if(e.getDescription().contains("tank_num_")) {
                int i = Integer.parseInt(e.getDescription().replace("tank_num_", ""));
                setActiveTank(robots.get(i));
                if (robots.get(i).isAlive()) main.viewholder = robots.get(i);
                tabbedPane.setSelectedIndex(0);
            }
        }
    }
  
    @Override
    public void valueChanged(ListSelectionEvent e) {
        System.out.println("Viewholder selected from entities list, set to " + main.viewholder);
        Entity n = (Entity) ((JList)e.getSource()).getSelectedValue();
        if(n != null) main.setViewholder(n);
    }
}
