/**
 * Dialogue pour créer un texte avec une image de fond.
 * Permet de configurer le texte, la police et la région de l'image.
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

/**
 * Dialogue d'édition de texte avec image de fond.
 * Permet de configurer texte, police, couleur et région d'image.
 */
public class TexteImageEditorDialog extends JDialog {
    private TexteAvecImage modele;
    private TextConfigPanel panneauConfig;
    private PreviewPanel panneauApercu;
    private SelectionPanel panneauSelection;
    private boolean valide = false;
    
    /**
     * Constructeur de la boîte de dialogue d'édition de texte avec image.
     * Initialise l'interface et le modèle.
     *
     * @param parent La fenêtre parente.
     */
    public TexteImageEditorDialog(Frame parent) {
        super(parent, "Texte avec image de fond", true);
        modele = new TexteAvecImage();
        initComponents();
        mettreAJourModele();
        setSize(1000, 750);
        setLocationRelativeTo(parent);
    }
    
    /**
     * Initialise et agence les composants de l'interface.
     * Crée les panneaux de configuration, sélection, aperçu et boutons.
     */
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
    
    /**
     * Crée le panneau divisé contenant la sélection et l'aperçu.
     * Côte à côte avec un diviseur ajustable.
     *
     * @return Le JSplitPane configuré.
     */
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
    
    /**
     * Génère l'aperçu du texte avec l'image sélectionnée.
     * Appelée par le panneau d'aperçu pour afficher le résultat.
     *
     * @return L'image générée, ou null si le texte est vide.
     */
    private BufferedImage genererApercu() {
        if (modele.getTexte() == null || modele.getTexte().isEmpty()) {
            return null;
        }
        return modele.genererImage();
    }
    
    /**
     * Valide la création du texte avec image.
     * Vérifie que le texte n'est pas vide avant de fermer.
     */
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
    
    /**
     * Annule la création et ferme la boîte de dialogue.
     */
    private void annuler() {
        valide = false;
        dispose();
    }
    
    /**
     * Ouvre un sélecteur de fichier pour charger une image de fond.
     * Affiche l'image dans le panneau de sélection après chargement.
     */
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
    
    /**
     * Met à jour le modèle avec les valeurs actuelles de l'interface.
     * Synchronise le texte, la police et la couleur.
     */
    private void mettreAJourModele() {
        modele.setTexte(panneauConfig.getTexte());
        modele.setPolice(panneauConfig.getPolice());
        modele.setCouleurTexte(panneauConfig.getCouleurTexte());
        panneauApercu.repaint();
    }
    
    /**
     * Indique si l'utilisateur a validé la création.
     *
     * @return true si validé, false sinon.
     */
    public boolean estValide() {
        return valide;
    }
    
    /**
     * Obtient le modèle de texte avec image configuré.
     *
     * @return Le modèle TexteAvecImage.
     */
    public TexteAvecImage getModele() {
        return modele;
    }
    
    /**
     * Génère et retourne l'image finale du texte.
     * Appelle la méthode de génération du modèle.
     *
     * @return L'image générée.
     */
    public BufferedImage getImageGeneree() {
        return modele.genererImage();
    }
}
