package com.miolean.arena.entities;

import com.miolean.arena.ui.FieldDisplayPanel;
import javafx.scene.control.ScrollPane;

import javax.swing.*;
import java.awt.*;

public class EntityDecorator {

    public static void drawCircularGlow(Graphics2D g, Entity e, int x, int y) {
        float[] dist = {0.0f, 0.45f};
        Color[] colors = {Color.WHITE, new Color(0, 0, 0, 0)};

        final double BORDER_FACTOR = 1.6;

        g.setPaint(new RadialGradientPaint((float) x, (float) y, (float) (e.getWidth() * BORDER_FACTOR), dist, colors, MultipleGradientPaint.CycleMethod.NO_CYCLE));
        g.fillOval((int) (x - (e.getWidth() * BORDER_FACTOR / 2)), (int) (y - (e.getHeight() * BORDER_FACTOR) / 2), (int) (e.getWidth() * BORDER_FACTOR), (int) (e.getHeight() * BORDER_FACTOR));
    }

    public static JComponent mergePanels(final JPanel... panels) {

        JTabbedPane pane = new JTabbedPane();

        for(JPanel p: panels) {
            if(p.getName() == null) pane.addTab("Other", p);
            pane.addTab(p.getName(), toScrollPane(p));
        }


//        JPanel result = new JPanel() {
//
//            int currentPane = 0;
//            Rectangle[] tabBounds = new Rectangle[panels.length];
//
//            @Override
//            public void paintComponent(Graphics g) {
//                super.paintComponent(g);
//
//
//                Point mouse = new Point(
//                        MouseInfo.getPointerInfo().getLocation().x - getLocationOnScreen().x,
//                        MouseInfo.getPointerInfo().getLocation().y - getLocationOnScreen().y
//                );
//
//                //Draw the name and the grey background
//
//                System.out.println();
//
//                g.setFont(g.getFont().deriveFont(Font.BOLD));
//                FontMetrics metrics = g.getFontMetrics();
//
//                g.setColor(Color.lightGray);
//                g.fillRect(-1, -1, getWidth(), 5+2*metrics.getHeight());
//
//                g.setColor(Color.black);
//                g.drawString(getName(), 2, metrics.getHeight() -2);
//
//                int lastTabEnd = 0;
//
//                for(int i = 0; i < panels.length; i++) {
//
//                    if(panels[i].getName() == null) panels[i].setName("Other");
//
//                    if(currentPane == i) {
//                        g.clearRect(lastTabEnd, metrics.getHeight() + 2, metrics.stringWidth(panels[i].getName())+10, 5+metrics.getHeight());
//                    }
//                    g.setColor(Color.black);
//                    g.drawLine(lastTabEnd, metrics.getHeight() + 2, lastTabEnd, 5+2*metrics.getHeight());
//
//                    tabBounds[i] = new Rectangle(lastTabEnd, metrics.getHeight(), metrics.stringWidth(panels[i].getName())+10, 9+metrics.getHeight());
//
//                    if(tabBounds[i].contains(mouse) && currentPane != i) {
//                        g.setColor(new Color(225, 225, 225));
//                        g.fillRect(lastTabEnd+1, metrics.getHeight() + 2, metrics.stringWidth(panels[i].getName())+10, 5+metrics.getHeight());
//                        g.setColor(Color.blue);
//                        g.drawString(panels[i].getName(), lastTabEnd + 5, 2*metrics.getHeight() + 2);
//                    } else {
//                        g.setColor(Color.black);
//                        g.drawString(panels[i].getName(), lastTabEnd + 5, 2*metrics.getHeight() + 2);
//                    }
//                    lastTabEnd += metrics.stringWidth(panels[i].getName())+10;
//                }
//
//
//                g.setColor(Color.black);
//                g.drawLine(0, 5+2*metrics.getHeight(), getWidth(), 5+2*metrics.getHeight());
//                g.setFont(g.getFont().deriveFont(Font.PLAIN));
//            }
//        };


        return pane;
    }

    public static JScrollPane toScrollPane(JPanel panel) {
        JScrollPane scroll = new JScrollPane();
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setViewportView(panel);

        return scroll;
    }

    public static JPanel quickStatusPanel(final Entity e) {
        return new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.drawRoundRect(15, 15, e.getWidth() + 30, e.getHeight() + 30, 4, 4);
                e.renderBody(g, 15 + (e.getWidth() + 30) / 2, 15 + (e.getHeight() + 30) / 2, e.RENDER_GLOWING);
                e.renderStatus(g, e.getWidth() + 70, 15, e.RENDER_LOW_QUALITY);

                g.drawLine(5, e.getHeight() + 50, getWidth()-5, e.getHeight() + 50);
            }
        };
    }
}
