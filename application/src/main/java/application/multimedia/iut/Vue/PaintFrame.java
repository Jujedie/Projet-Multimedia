package application.multimedia.iut.Vue;

import javax.swing.*;

public class PaintFrame extends JFrame {
    private PaintPanel paintPanel;

    public PaintFrame() {
        setTitle("Projet Multimédia");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);

        paintPanel = new PaintPanel();
        add(paintPanel);
    }

    public PaintPanel getPaintPanel() {
        return paintPanel;
    }
}
