package com.miolean.arena;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Window extends JFrame implements ChangeListener ,KeyListener {

    private JSlider slider;
    MainPanel main;
    MemoryPanel memoryPanel;
    EntityPanel entityPanel;


    Window(MainPanel mainPanel, Entity[] entities, java.util.List<Tank> tanks) {
        this.main = mainPanel;
        LayoutManager layout = new GridBagLayout();
        setLayout(layout);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        makeMainLayout(entities, tanks);
    }

    public void makeMainLayout(Entity[] entities, java.util.List<Tank> tanks) {
        JPanel genomePanel = new JPanel();
        memoryPanel = new MemoryPanel(null);
        entityPanel = new EntityPanel(tanks, entities);

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
        mainContainer.add(main, BorderLayout.CENTER);
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
        c.weightx = .2;
        c.weighty = .5;

        //ImageIcon genomeIcon = new ImageIcon(Window.class.getClassLoader().getResource("tex/list.png"));

        makeGenomePanel(genomePanel);

        infoPanel.addTab("Memory", memoryPanel);
        infoPanel.addTab("Entities", entityPanel);
        infoPanel.addTab("Genome", genomePanel);

        JLabel genomeLabel = new JLabel("Genome", JLabel.CENTER);
        genomeLabel.setVerticalTextPosition(JLabel.BOTTOM);
        genomeLabel.setHorizontalTextPosition(JLabel.CENTER);
        infoPanel.setTabComponentAt(2, genomeLabel);

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
        entityPanel.updateInfo();
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
}
