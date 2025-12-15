package application.multimedia.iut.Vue;

import java.awt.*;
import javax.swing.*;

public class PaintPanel extends JPanel {
    private JLabel canvas;
    private JButton sauvegarderBtn, chargerBtn, effacerBtn, mergerBtn, flipHBBtn, flipVBBtn, rotationBtn;
    private JButton rediBtn, contrasteBtn, texteBtn, remplirBtn;
    private JButton undoBtn, redoBtn;
    private Boolean remplirMode = false;
    private int remplirTolerence;
    private Color remplirCouleur;

    public PaintPanel() {
        setLayout(new BorderLayout());

        // Canvas
        canvas = new JLabel();
        canvas.setHorizontalAlignment(JLabel.CENTER);
        canvas.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JScrollPane scrollPane = new JScrollPane(canvas);
        add(scrollPane, BorderLayout.CENTER);

        // Barre d'outils
        JPanel toolBar = new JPanel(new FlowLayout());
        sauvegarderBtn = new JButton("Sauvegarder");
        chargerBtn = new JButton("Charger");
        effacerBtn = new JButton("Effacer");
        mergerBtn = new JButton("Fusionner");
        flipHBBtn = new JButton("Flip Horizontal");
        flipVBBtn = new JButton("Flip Vertical");
        rotationBtn = new JButton("Rotation");
        rediBtn = new JButton("Redimensionner");
        contrasteBtn = new JButton("Contraste");
        texteBtn = new JButton("Ajouter Texte");
        remplirBtn = new JButton("Remplir");
        undoBtn = new JButton("Annuler");
        redoBtn = new JButton("Refaire");

        toolBar.add(sauvegarderBtn);
        toolBar.add(chargerBtn);
        toolBar.add(effacerBtn);
        toolBar.add(mergerBtn);
        toolBar.add(new JSeparator(SwingConstants.VERTICAL));
        toolBar.add(flipHBBtn);
        toolBar.add(flipVBBtn);
        toolBar.add(rotationBtn);
        toolBar.add(rediBtn);
        toolBar.add(new JSeparator(SwingConstants.VERTICAL));
        toolBar.add(contrasteBtn);
        toolBar.add(texteBtn);
        toolBar.add(remplirBtn);
        toolBar.add(new JSeparator(SwingConstants.VERTICAL));
        toolBar.add(undoBtn);
        toolBar.add(redoBtn);

        add(toolBar, BorderLayout.NORTH);
    }
}
