package com.miolean.arena;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Window extends JFrame implements ChangeListener ,KeyListener, HyperlinkListener {

    private JSlider slider;
    private JTabbedPane tabbedPane;
    MainPanel main;
    MemoryPanel memoryPanel;
    EvolutionPanel evolutionPanel;

    java.util.List<Tank> topTanks;


    Window(MainPanel mainPanel, java.util.List<Tank> topTanks) {
        this.main = mainPanel;
        this.topTanks = topTanks;

        LayoutManager layout = new GridBagLayout();
        setLayout(layout);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        makeMainLayout(topTanks);
    }

    public void makeMainLayout(java.util.List<Tank> topTanks) {
        JPanel genomePanel = new JPanel();
        memoryPanel = new MemoryPanel(null);
        evolutionPanel = new EvolutionPanel(topTanks);
        JPanel usedSetPanel = new JPanel();

        evolutionPanel.addHyperlinkListener(this);

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
        tabbedPane.addTab("Used Set", usedSetPanel);
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
    }

    public void makeGenomePanel(JPanel genomePanel) {

        genomePanel.setLayout(new GridBagLayout());

        int category = -1;

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("");
        DefaultMutableTreeNode branch = null;
        DefaultMutableTreeNode twig;


        for(int i = 0; i < Tank.KMEM.length; i++) {
            if(i/16 > category) {
                if(branch != null) root.add(branch);
                category = i/16;
                branch = new DefaultMutableTreeNode(Integer.toHexString(category).toUpperCase() + "  " + Gene.GENE_CATEGORIES[category]);
                //Add a new section!
            }
            if(Tank.KMEM[i] != null) {
                 twig = new DefaultMutableTreeNode(Integer.toHexString(i).toUpperCase() + "|  " + Tank.KMEM[i]);
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
            Global.updateCycle = 1000/slider.getValue();
            Global.distributeCycle = 1000/slider.getValue();
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

    public void setActiveTank(Tank tank) {
        memoryPanel.source = tank;
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            int i = Integer.parseInt(e.getDescription().replace("tank_greatest_", ""));
            System.out.println("Active tank is now high scorer " + i);
            setActiveTank(topTanks.get(i));
            if(topTanks.get(i).isAlive()) main.viewholder = topTanks.get(i);
            tabbedPane.setSelectedIndex(0);
        }
    }
}
