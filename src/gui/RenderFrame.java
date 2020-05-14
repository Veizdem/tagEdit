package gui;

import intrpr.Interpr;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RenderFrame extends JFrame {
    private String title;
    private Color bgColor;
    private ArrayList<Component> elements;

    public RenderFrame(String document) {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(800, 800);

        Interpr interpr = new Interpr(document);

        elements = interpr.getItems();
        title = interpr.getTitle();
        bgColor = interpr.getBackgroundColor();

        this.setTitle(title);

        Container panel = this.getContentPane();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.setBackground(bgColor);

        if (elements != null && !elements.isEmpty()) {
            for (Component element : elements) {
                panel.add(element);
            }
        }
    }
}
