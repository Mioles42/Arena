package com.miolean.arena;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MemoryPanel extends JPanel {

    JTextPane textPane;
    JSpinner spinner;
    JComboBox<String> comboBox;
    JLabel label;

    Tank source;

    private static final int INDEX_KMEM = 0;
    private static final int INDEX_UMEM = 1;
    private static final int INDEX_PMEM = 2;
    private static final int INDEX_SMEM = 3;
    private static final int INDEX_WMEM = 4;

    public MemoryPanel(Tank source) {

        this.source = source;

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
        label.setText("Memory and address:");
        this.add(label, c);

        comboBox = new JComboBox<>();
        comboBox.addItem("KMEM");
        comboBox.addItem("UMEM");
        comboBox.addItem("PMEM");
        comboBox.addItem("SMEM");
        comboBox.addItem("WMEM");
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.weighty = .05;
        c.weightx = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        this.add(comboBox, c);

        SpinnerModel model = new SpinnerNumberModel(0, 0, 255, 1);
        spinner = new JSpinner(model);
        spinner.getEditor().setFocusable(false);
        c.gridx = 2;
        c.gridy = 0;
        c.weighty = .05;
        c.weightx = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        this.add(spinner, c);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);


        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setHighlighter(null);
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
        if(source != null) {

            StyledDocument doc = textPane.getStyledDocument();
            try {doc.remove(0, doc.getLength());}
            catch (BadLocationException e) {}
            String text = "§";
            switch (comboBox.getSelectedIndex()) {
                case INDEX_KMEM: text = ("[KMEM text unimplemented so far]"); break;
                case INDEX_UMEM: text = (source.stringUMEM((int) spinner.getValue())); break;
                case INDEX_PMEM: text = (source.stringPMEM((int) spinner.getValue())); break;
                case INDEX_SMEM: text = ("[SMEM text unimplemented so far]"); break;
                case INDEX_WMEM: text = ("[WMEM text unimplemented so far]"); break;
            }

            Scanner scanner = new Scanner(text);
            scanner.useDelimiter(Pattern.compile("§"));
            while(scanner.hasNext()) {

                Style style = textPane.addStyle("current", null);

                String next = scanner.next();
                switch(next.substring(0,1)){
                    case "r": StyleConstants.setForeground(style, Color.RED.darker()); break;
                    case "b": StyleConstants.setForeground(style, Color.BLUE.darker()); break;
                    case "g": StyleConstants.setForeground(style, Color.GREEN.darker()); break;
                    case "y": StyleConstants.setForeground(style, Color.YELLOW.darker()); break;
                    case "m": StyleConstants.setForeground(style, Color.MAGENTA.darker()); break;
                    case "c": StyleConstants.setForeground(style, Color.CYAN.darker()); break;
                    case "k": StyleConstants.setForeground(style, Color.BLACK.darker()); break;

                }

                try { doc.insertString(doc.getLength(), next.substring(1), style); }
                catch (BadLocationException e){}
            }
        }

        repaint();
    }
}
