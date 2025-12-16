/**
 * Panneau d'aperçu en temps réel.
 * Affiche le résultat généré du texte avec image.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Vue.panels;

import application.multimedia.iut.Vue.utils.StyleHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Supplier;

/**
 * Panneau d'aperçu dynamique pour afficher le résultat en temps réel.
 * Se met à jour automatiquement à chaque modification.
 */
public class PreviewPanel extends JPanel {
    
    private Supplier<BufferedImage> imageSupplier;
    
    /**
     * Constructeur du panneau d'aperçu.
     * Crée un panneau qui affiche l'image fournie par le supplier.
     *
     * @param imageSupplier Le fournisseur d'image à afficher.
     */
    public PreviewPanel(Supplier<BufferedImage> imageSupplier) {
        this.imageSupplier = imageSupplier;
        setBackground(StyleHelper.BACKGROUND_DARK);
        setBorder(StyleHelper.createTitledBorder("Aperçu", Color.WHITE));
    }
    
    /**
     * Dessine l'image dans le panneau.
     * Centre l'image si elle est présente.
     *
     * @param g Le contexte graphique.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (imageSupplier == null) return;
        
        BufferedImage image = imageSupplier.get();
        if (image != null) {
            int x = (getWidth() - image.getWidth()) / 2;
            int y = (getHeight() - image.getHeight()) / 2;
            g.drawImage(image, x, y, null);
        }
    }
}
