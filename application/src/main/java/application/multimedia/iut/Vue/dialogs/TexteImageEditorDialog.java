/**
 * Classe principale pour lancer l'application de retouche d'images.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Vue.dialogs;

import application.multimedia.iut.Metier.TexteAvecImage;
import application.multimedia.iut.Vue.panels.*;
import application.multimedia.iut.Vue.utils.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class TexteImageEditorDialog extends JDialog {
    private TexteAvecImage modele;
    private TextConfigPanel panneauConfig;
    private PreviewPanel panneauApercu;
    private SelectionPanel panneauSelection;
    private boolean valide = false;
    
    public TexteImageEditorDialog(Frame parent) {
        super(parent, "Texte avec image de fond", true);
        modele = new TexteAvecImage();
        initComponents();
        mettreAJourModele();
        setSize(1000, 750);
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        JPanel panneauPrincipal = new JPanel(new BorderLayout(15, 15));
        panneauPrincipal.setBackground(StyleHelper.BACKGROUND_LIGHT);
        panneauPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        panneauConfig = new TextConfigPanel(v -> mettreAJourModele(), this::chargerImage);
        panneauPrincipal.add(panneauConfig, BorderLayout.NORTH);
        
        JSplitPane splitPane = creerSplitPane();
        panneauPrincipal.add(splitPane, BorderLayout.CENTER);
        
        ActionButtonsPanel panneauBoutons = new ActionButtonsPanel(this::valider, this::annuler);
        panneauPrincipal.add(panneauBoutons, BorderLayout.SOUTH);
        
        add(panneauPrincipal);
    }
    
    private JSplitPane creerSplitPane() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        panneauSelection = new SelectionPanel(selection -> {
            modele.setRegionSelectionnee(selection);
            panneauApercu.repaint();
        });
        
        JScrollPane scrollSelection = new JScrollPane(panneauSelection);
        scrollSelection.setBorder(StyleHelper.createTitledBorder(
            "Sélection de la région de l'image",
            StyleHelper.TEXT_LIGHT
        ));
        scrollSelection.setBackground(Color.WHITE);
        splitPane.setLeftComponent(scrollSelection);
        
        panneauApercu = new PreviewPanel(this::genererApercu);
        splitPane.setRightComponent(panneauApercu);
        
        splitPane.setDividerLocation(480);
        return splitPane;
    }
    
    private BufferedImage genererApercu() {
        if (modele.getTexte() == null || modele.getTexte().isEmpty()) {
            return null;
        }
        return modele.genererImage();
    }
    
    private void valider() {
        if (panneauConfig.getTexte().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Veuillez saisir un texte.",
                "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }
        valide = true;
        dispose();
    }
    
    private void annuler() {
        valide = false;
        dispose();
    }
    
    private void chargerImage() {
        File[] fichiers = ImageDialogs.selectImages(this);
        if (fichiers == null || fichiers.length == 0) return;

        try {
            BufferedImage img = ImageIO.read(fichiers[0]);
            if (img == null) throw new IllegalArgumentException("Image illisible");
            modele.setImageFond(img);
            panneauSelection.setImage(img);
            panneauApercu.repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors du chargement de l'image : " + ex.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void mettreAJourModele() {
        modele.setTexte(panneauConfig.getTexte());
        modele.setPolice(panneauConfig.getPolice());
        modele.setCouleurTexte(panneauConfig.getCouleurTexte());
        panneauApercu.repaint();
    }
    
    public boolean estValide() {
        return valide;
    }
    
    public TexteAvecImage getModele() {
        return modele;
    }
    
    public BufferedImage getImageGeneree() {
        return modele.genererImage();
    }
}
