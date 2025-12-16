package application.multimedia.iut.Metier;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TexteAvecImage {
    private String texte;
    private Font police;
    private Color couleurTexte;
    private BufferedImage imageFond;
    private Rectangle regionSelectionnee;
    
    public TexteAvecImage() {
        this.texte = "";
        this.police = new Font("Arial", Font.BOLD, 48);
        this.couleurTexte = Color.WHITE;
        this.imageFond = null;
        this.regionSelectionnee = null;
    }
    
    public String getTexte() {
        return texte;
    }
    
    public void setTexte(String texte) {
        this.texte = texte;
    }
    
    public Font getPolice() {
        return police;
    }
    
    public void setPolice(Font police) {
        this.police = police;
    }
    
    public Color getCouleurTexte() {
        return couleurTexte;
    }
    
    public void setCouleurTexte(Color couleurTexte) {
        this.couleurTexte = couleurTexte;
    }
    
    public BufferedImage getImageFond() {
        return imageFond;
    }
    
    public void setImageFond(BufferedImage imageFond) {
        this.imageFond = imageFond;
    }
    
    public Rectangle getRegionSelectionnee() {
        return regionSelectionnee;
    }
    
    public void setRegionSelectionnee(Rectangle regionSelectionnee) {
        this.regionSelectionnee = regionSelectionnee;
    }
    
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
