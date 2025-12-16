/**
 * Classe gérant la création de texte avec une image de fond.
 * Permet de générer un texte dont les lettres sont remplies par une image.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */

package application.multimedia.iut.Metier;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Modèle pour générer du texte rempli avec une image.
 * Combine une police, un texte et une région d'image pour créer un effet visuel.
 */
public class TexteAvecImage {
    private String texte;
    private Font police;
    private Color couleurTexte;
    private BufferedImage imageFond;
    private Rectangle regionSelectionnee;
    
    /**
     * Constructeur de la classe TexteAvecImage.
     * Initialise les valeurs par défaut (texte vide, police Arial 48pt).
     */
    public TexteAvecImage() {
        this.texte = "";
        this.police = new Font("Arial", Font.BOLD, 48);
        this.couleurTexte = Color.WHITE;
        this.imageFond = null;
        this.regionSelectionnee = null;
    }
    
    /**
     * Obtient le texte actuellement configuré.
     *
     * @return Le texte.
     */
    public String getTexte() {
        return texte;
    }
    
    /**
     * Définit le texte à générer.
     *
     * @param texte Le nouveau texte.
     */
    public void setTexte(String texte) {
        this.texte = texte;
    }
    
    /**
     * Obtient la police actuellement utilisée.
     *
     * @return La police de caractères.
     */
    public Font getPolice() {
        return police;
    }
    
    /**
     * Définit la police du texte.
     *
     * @param police La nouvelle police à appliquer.
     */
    public void setPolice(Font police) {
        this.police = police;
    }
    
    /**
     * Obtient la couleur du texte.
     *
     * @return La couleur du texte.
     */
    public Color getCouleurTexte() {
        return couleurTexte;
    }
    
    /**
     * Définit la couleur du texte.
     *
     * @param couleurTexte La nouvelle couleur du texte.
     */
    public void setCouleurTexte(Color couleurTexte) {
        this.couleurTexte = couleurTexte;
    }
    
    /**
     * Obtient l'image de fond utilisée pour remplir le texte.
     *
     * @return L'image de fond, ou null si aucune.
     */
    public BufferedImage getImageFond() {
        return imageFond;
    }
    
    /**
     * Définit l'image de fond pour remplir le texte.
     *
     * @param imageFond L'image à utiliser comme fond.
     */
    public void setImageFond(BufferedImage imageFond) {
        this.imageFond = imageFond;
    }
    
    /**
     * Obtient la région sélectionnée dans l'image de fond.
     *
     * @return La région sélectionnée.
     */
    public Rectangle getRegionSelectionnee() {
        return regionSelectionnee;
    }
    
    /**
     * Définit la région de l'image de fond à utiliser.
     *
     * @param regionSelectionnee La zone rectangulaire à extraire.
     */
    public void setRegionSelectionnee(Rectangle regionSelectionnee) {
        this.regionSelectionnee = regionSelectionnee;
    }
    
    /**
     * Génère l'image finale du texte avec ou sans image de fond.
     * Le texte est rendu avec la police sélectionnée et rempli avec l'image ou la couleur.
     *
     * @return L'image générée, ou null si le texte est vide.
     */
    public BufferedImage genererImage() {
        if (texte == null || texte.isEmpty()) {
            return null;
        }
        
        BufferedImage temp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = temp.createGraphics();
        g.setFont(police);
        FontMetrics fm = g.getFontMetrics();
        int largeur = fm.stringWidth(texte);
        int hauteur = fm.getHeight();
        g.dispose();
        
        BufferedImage texteImage = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = texteImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(police);
        g2d.setColor(Color.WHITE);
        g2d.drawString(texte, 0, fm.getAscent());
        g2d.dispose();
        
        BufferedImage resultat = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);
        
        if (imageFond != null && regionSelectionnee != null) {
            BufferedImage imageRedim = redimensionnerRegion(largeur, hauteur);
            if (imageRedim != null) {
                appliquerImageSurTexte(resultat, texteImage, imageRedim);
            } else {
                copierImageSimple(resultat, texteImage, couleurTexte.getRGB());
            }
        } else {
            copierImageSimple(resultat, texteImage, couleurTexte.getRGB());
        }
        
        return resultat;
    }
    
    /**
     * Applique l'image de fond sur le masque de texte.
     * Copie les pixels de l'image là où le masque est visible.
     *
     * @param dest L'image de destination.
     * @param masque Le masque de texte (alpha channel).
     * @param image L'image source à appliquer.
     */
    private void appliquerImageSurTexte(BufferedImage dest, BufferedImage masque, BufferedImage image) {
        int largeur = dest.getWidth();
        int hauteur = dest.getHeight();
        
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                int coulMasque = masque.getRGB(x, y);
                int alpha = (coulMasque >> 24) & 0xFF;
                
                if (alpha > 0) {
                    int coulImage = image.getRGB(x, y);
                    dest.setRGB(x, y, coulImage);
                }
            }
        }
    }
    
    /**
     * Copie une couleur unie sur le masque de texte.
     * Applique la couleur là où le masque est visible.
     *
     * @param dest L'image de destination.
     * @param masque Le masque de texte (alpha channel).
     * @param couleur La couleur à appliquer (RGB).
     */
    private void copierImageSimple(BufferedImage dest, BufferedImage masque, int couleur) {
        int largeur = dest.getWidth();
        int hauteur = dest.getHeight();
        
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                int coulMasque = masque.getRGB(x, y);
                int alpha = (coulMasque >> 24) & 0xFF;
                
                if (alpha > 0) {
                    dest.setRGB(x, y, couleur | (alpha << 24));
                }
            }
        }
    }
    
    /**
     * Redimensionne la région sélectionnée pour correspondre aux dimensions du texte.
     * Extrait la zone et l'étire/réduit aux dimensions cibles.
     *
     * @param largeur La largeur cible.
     * @param hauteur La hauteur cible.
     * @return L'image redimensionnée, ou null si la région est invalide.
     */
    private BufferedImage redimensionnerRegion(int largeur, int hauteur) {
        int x = Math.max(0, regionSelectionnee.x);
        int y = Math.max(0, regionSelectionnee.y);
        int w = Math.min(regionSelectionnee.width, imageFond.getWidth() - x);
        int h = Math.min(regionSelectionnee.height, imageFond.getHeight() - y);
        
        if (w <= 0 || h <= 0) {
            return null;
        }
        
        BufferedImage region = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                region.setRGB(i, j, imageFond.getRGB(x + i, y + j));
            }
        }
        
        BufferedImage resultat = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);
        
        double ratioX = (double) w / largeur;
        double ratioY = (double) h / hauteur;
        
        for (int j = 0; j < hauteur; j++) {
            for (int i = 0; i < largeur; i++) {
                int srcX = (int) (i * ratioX);
                int srcY = (int) (j * ratioY);
                resultat.setRGB(i, j, region.getRGB(srcX, srcY));
            }
        }
        
        return resultat;
    }
}
