package com.miolean.arena.entities;

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

    public static JComponent mergeComponents(final JComponent... components) {

        JTabbedPane pane = new JTabbedPane();

        for(JComponent p: components) {
            if(p.getName() == null) pane.addTab("Other", p);
            pane.addTab(p.getName(), p);
        }

        pane.setBackground(new Color(220, 220, 220));

        return pane;
    }

    public static JScrollPane toScrollPane(JPanel panel) {
        JScrollPane scroll = new JScrollPane();
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setViewportView(panel);
        scroll.setName(panel.getName());

        return scroll;
    }

    public static JPanel quickStatusPanel(final Entity e) {
        return new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.drawRoundRect(15, 15, e.getWidth() + 30, e.getHeight() + 30, 4, 4);
                e.renderBody(g, 15 + (e.getWidth() + 30) / 2, 15 + (e.getHeight() + 30) / 2, Entity.RENDER_GLOWING);
                e.renderStatus(g, e.getWidth() + 70, 15, Entity.RENDER_LOW_QUALITY);

                g.drawLine(5, e.getHeight() + 50, getWidth()-5, e.getHeight() + 50);
            }
        };
    }

}
