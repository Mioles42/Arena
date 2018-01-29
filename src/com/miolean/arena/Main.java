package com.miolean.arena;

import com.miolean.arena.entities.Field;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Main implements Runnable, WindowListener {

    private FieldDisplayPanel fieldDisplayPanel;
    private GeneralDisplayPanel generalDisplayPanel;

    Field field;
    Thread ergoThread;
    private Handler handler;
    private boolean isRunning = true;

    public static void main(String[] args) {
       Main main = new Main();

        Thread ergoThread = new Thread(main, "ergoloop");
        ergoThread.run();
    }

    private Main() {
        field = new Field();
        fieldDisplayPanel = new FieldDisplayPanel(field);
        generalDisplayPanel = new GeneralDisplayPanel(field);
        initializeGUI();

        handler = new Handler(field);

    }

    public void initializeGUI() {
        JFrame window = new JFrame("Ergo");
        window.setSize(1200, 700);
        window.setLocation(20, 200);
        window.setVisible(true);
        window.setLayout(new GridBagLayout());
        window.addWindowListener(this);

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
        mainContainer.add(fieldDisplayPanel, BorderLayout.CENTER);
        mainContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLoweredBevelBorder()
        ));
        window.add(mainContainer, c);

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
        window.add(generalDisplayPanel, c);

        generalDisplayPanel.addActiveRobotListener(fieldDisplayPanel);
        fieldDisplayPanel.addActiveRobotListener(generalDisplayPanel);
    }

    public void run() {
        System.out.println("Running...");

        long lastUpdate = System.currentTimeMillis();
        long lastRender = System.currentTimeMillis();
        long lastDisplay = System.currentTimeMillis();
        long lastDistribute = System.currentTimeMillis();

        while(isRunning) {

            long time = System.currentTimeMillis();

            if(time > lastUpdate + Global.updateCycle) {
                handler.update();
                lastUpdate = time;
            }
            time = System.currentTimeMillis();

            if(time > lastRender + Global.renderCycle) {
                fieldDisplayPanel.repaint();
                lastRender = time;
            }
            time = System.currentTimeMillis();

            if(time > lastDistribute + Global.distributeCycle) {
                handler.distribute();
                lastDistribute = time;
            }

            if(time > lastDisplay + Global.displayCycle) {
                generalDisplayPanel.display();
                lastDisplay = time;
            }
        }
    }

    @Override public void windowOpened(WindowEvent e) {}
    @Override public void windowClosing(WindowEvent e) {isRunning = false; System.exit(0);}
    @Override public void windowClosed(WindowEvent e) {    }
    @Override public void windowIconified(WindowEvent e) {}
    @Override public void windowDeiconified(WindowEvent e) {}
    @Override public void windowActivated(WindowEvent e) {}
    @Override public void windowDeactivated(WindowEvent e) {}
}
