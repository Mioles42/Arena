package com.miolean.arena;

import com.miolean.arena.entities.Entity;
import com.miolean.arena.entities.Field;
import com.miolean.arena.entities.Gene;
import com.miolean.arena.entities.Robot;

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
import java.util.ArrayList;

public class GeneralDisplayPanel extends JPanel implements ChangeListener, ListSelectionListener, HyperlinkListener, ActiveRobotListener {

    private JSlider slider;
    private JTabbedPane tabbedPane;
    MemoryPanel memoryPanel;
    EvolutionPanel evolutionPanel;
    EntityPanel entityPanel;

    java.util.List<ActiveRobotListener> listenerList = new ArrayList<ActiveRobotListener>();
    Entity viewholder;
    Entity infoholder;

    Field field;


    GeneralDisplayPanel(Field field) {
        this.field = field;

        LayoutManager layout = new GridBagLayout();
        setLayout(layout);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        makeMainLayout();
    }

    public void makeMainLayout() {
        JPanel genomePanel = new JPanel();
        memoryPanel = new MemoryPanel(null);
        evolutionPanel = new EvolutionPanel(field);
        entityPanel = new EntityPanel(field);

        evolutionPanel.addHyperlinkListener(this);
        entityPanel.addHyperlinkListener(this);

        GridBagConstraints c;

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

        //ImageIcon genomeIcon = new ImageIcon(GeneralDisplayPanel.class.getClassLoader().getResource("tex/list.png"));

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


        for(int i = 0; i < com.miolean.arena.entities.Robot.KMEM.length; i++) {
            if(i/16 > category) {
                if(branch != null) root.add(branch);
                category = i/16;
                branch = new DefaultMutableTreeNode(Integer.toHexString(category).toUpperCase() + "  " + Gene.GENE_CATEGORIES[category]);
                //Add a new section!
            }
            if(com.miolean.arena.entities.Robot.KMEM[i] != null) {
                 twig = new DefaultMutableTreeNode(Integer.toHexString(i).toUpperCase() + "|  " + com.miolean.arena.entities.Robot.KMEM[i]);
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
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
            infoholder = field.fromHTML(e.getDescription());
            alertInfoholderChange(((Robot)infoholder));
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        Entity n = (Entity) ((JList)e.getSource()).getSelectedValue();
        if(n != null) {
            viewholder = n;
            alertViewholderChange(n);
        }
    }

    public void addActiveRobotListener(ActiveRobotListener l) {
        listenerList.add(l);
    }

    public void removeActiveRobotListener(ActiveRobotListener l) {
        listenerList.remove(l);
    }

    @Override
    public void viewholderChanged(Entity e) {
        viewholder = e;
    }

    @Override
    public void infoholderChanged(Robot e) {

        memoryPanel.source = e;
    }

    public void alertViewholderChange(Entity e) {
        for(ActiveRobotListener arl: listenerList) arl.viewholderChanged(e);
    }
    public void alertInfoholderChange(Robot e) {
        for(ActiveRobotListener arl: listenerList) arl.infoholderChanged(e);
        memoryPanel.source = e;
    }
}
