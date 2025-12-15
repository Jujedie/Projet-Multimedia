package application.multimedia.iut.Vue;

import javax.swing.*;
import java.awt.*;

public class ToolBarBuilder {
    
    private PaintPanel panel;
    private Color iconColor = new Color(60, 60, 60);
    private int iconSize = 20;
    
    public ToolBarBuilder(PaintPanel panel) {
        this.panel = panel;
    }
    
    public JToolBar creerToolBar() {
        JToolBar barreOutils = new JToolBar();
        barreOutils.setFloatable(false);
        barreOutils.setBackground(new Color(240, 240, 240));
        barreOutils.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        
        ajouterOutilsDessin(barreOutils);
        barreOutils.addSeparator();
        
        ajouterOutilsFichier(barreOutils);
        barreOutils.addSeparator();
        
        ajouterOutilsTransformation(barreOutils);
        barreOutils.addSeparator();
        
        ajouterOutilsFiltres(barreOutils);
        barreOutils.addSeparator();
        
        ajouterOutilsHistorique(barreOutils);
        barreOutils.addSeparator();
        
        ajouterOutilsZoom(barreOutils);
        barreOutils.addSeparator();
        
        ajouterSelecteurCouleurs(barreOutils);
        
        return barreOutils;
    }
    
    private void ajouterOutilsDessin(JToolBar barre) {
        ButtonGroup groupeOutils = new ButtonGroup();
        
        JToggleButton selectionBtn = creerBoutonToggle("square-dashed", "Sélection");
        JToggleButton pinceauBtn = creerBoutonToggle("pencil", "Pinceau");
        JToggleButton gommeBtn = creerBoutonToggle("eraser", "Gomme");
        JToggleButton pipetteBtn = creerBoutonToggle("pipette", "Pipette");
        JToggleButton remplissageBtn = creerBoutonToggle("paint-bucket", "Remplissage");
        JButton texteBtn = creerBouton("type", "Texte");
        
        groupeOutils.add(selectionBtn);
        groupeOutils.add(pinceauBtn);
        groupeOutils.add(gommeBtn);
        groupeOutils.add(pipetteBtn);
        groupeOutils.add(remplissageBtn);
        
        barre.add(selectionBtn);
        barre.add(pinceauBtn);
        barre.add(gommeBtn);
        barre.add(pipetteBtn);
        barre.add(remplissageBtn);
        barre.add(texteBtn);
    }
    
    private void ajouterOutilsFichier(JToolBar barre) {
        JButton sauvegarderBtn = creerBouton("save", "Sauvegarder");
        JButton chargerBtn = creerBouton("folder-open", "Ouvrir");
        JButton effacerBtn = creerBouton("trash-2", "Effacer tout");
        
        chargerBtn.addActionListener(e -> panel.ouvrirFichier());
        sauvegarderBtn.addActionListener(e -> panel.enregistrerFichier(true));
        
        barre.add(sauvegarderBtn);
        barre.add(chargerBtn);
        barre.add(effacerBtn);
    }
    
    private void ajouterOutilsTransformation(JToolBar barre) {
        JButton flipHBtn = creerBouton("flip-horizontal", "Flip Horizontal");
        JButton flipVBtn = creerBouton("flip-vertical", "Flip Vertical");
        JButton rotationBtn = creerBouton("rotate-cw", "Rotation");
        JButton rediBtn = creerBouton("maximize", "Redimensionner");
        JButton mergerBtn = creerBouton("layers", "Fusionner");
        
        barre.add(flipHBtn);
        barre.add(flipVBtn);
        barre.add(rotationBtn);
        barre.add(rediBtn);
        barre.add(mergerBtn);
    }
    
    private void ajouterOutilsFiltres(JToolBar barre) {
        JButton contrasteBtn = creerBouton("contrast", "Contraste");
        barre.add(contrasteBtn);
    }
    
    private void ajouterOutilsHistorique(JToolBar barre) {
        JButton undoBtn = creerBouton("undo", "Annuler");
        JButton redoBtn = creerBouton("redo", "Refaire");
        
        barre.add(undoBtn);
        barre.add(redoBtn);
    }
    
    private void ajouterOutilsZoom(JToolBar barre) {
        JButton zoomInBtn = creerBouton("zoom-in", "Zoom +");
        JButton zoomOutBtn = creerBouton("zoom-out", "Zoom -");
        JButton zoomResetBtn = creerBouton("maximize-2", "Zoom 100%");
        
        zoomInBtn.addActionListener(e -> panel.zoom(1.2));
        zoomOutBtn.addActionListener(e -> panel.zoom(0.8));
        zoomResetBtn.addActionListener(e -> panel.resetZoom());
        
        barre.add(zoomInBtn);
        barre.add(zoomOutBtn);
        barre.add(zoomResetBtn);
    }
    
    private void ajouterSelecteurCouleurs(JToolBar barre) {
        JPanel miniCouleurPanel = new JPanel(new GridLayout(1, 2, 2, 0));
        miniCouleurPanel.setMaximumSize(new Dimension(50, 30));
        miniCouleurPanel.setPreferredSize(new Dimension(50, 30));
        
        JPanel couleurPrincipale = new JPanel();
        couleurPrincipale.setBackground(Color.BLACK);
        couleurPrincipale.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        couleurPrincipale.setToolTipText("Couleur principale");
        
        JPanel couleurSecondaire = new JPanel();
        couleurSecondaire.setBackground(Color.WHITE);
        couleurSecondaire.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        couleurSecondaire.setToolTipText("Couleur secondaire");
        
        miniCouleurPanel.add(couleurPrincipale);
        miniCouleurPanel.add(couleurSecondaire);
        barre.add(miniCouleurPanel);
    }
    
    private JToggleButton creerBoutonToggle(String iconName, String tooltip) {
        JToggleButton btn = new JToggleButton(LucideIconLoader.loadIcon(iconName, iconSize, iconColor));
        configurerBouton(btn, tooltip);
        return btn;
    }
    
    private JButton creerBouton(String iconName, String tooltip) {
        JButton btn = new JButton(LucideIconLoader.loadIcon(iconName, iconSize, iconColor));
        configurerBouton(btn, tooltip);
        return btn;
    }
    
    private void configurerBouton(AbstractButton btn, String tooltip) {
        btn.setToolTipText(tooltip);
        btn.setPreferredSize(new Dimension(40, 40));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(240, 240, 240));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Effet hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = btn.getBackground();
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(220, 220, 220));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(originalColor);
            }
        });
    }
}
