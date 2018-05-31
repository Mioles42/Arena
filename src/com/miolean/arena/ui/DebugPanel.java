package com.miolean.arena.ui;

import com.miolean.arena.framework.Debug;

import javax.swing.*;
import java.awt.*;

public class DebugPanel extends JPanel {
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Debug.drawDebugLog(g);
    }

    public void updateInfo() {
        Debug.refresh();
        repaint();
    }
}
