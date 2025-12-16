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
        
        BufferedImage image = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(police);
        
        if (imageFond != null && regionSelectionnee != null) {
            g2d.setColor(Color.WHITE);
            g2d.drawString(texte, 0, fm.getAscent());
            
            BufferedImage imageRedim = redimensionnerRegion(largeur, hauteur);
            if (imageRedim != null) {
                g2d.setComposite(AlphaComposite.SrcIn);
                g2d.drawImage(imageRedim, 0, 0, null);
            }
        } else {
            g2d.setColor(couleurTexte);
            g2d.drawString(texte, 0, fm.getAscent());
        }
        
        g2d.dispose();
        return image;
    }
    
    private BufferedImage redimensionnerRegion(int largeur, int hauteur) {
        int x = Math.max(0, regionSelectionnee.x);
        int y = Math.max(0, regionSelectionnee.y);
        int w = Math.min(regionSelectionnee.width, imageFond.getWidth() - x);
        int h = Math.min(regionSelectionnee.height, imageFond.getHeight() - y);
        
        if (w <= 0 || h <= 0) {
            return null;
        }
        
        BufferedImage region = imageFond.getSubimage(x, y, w, h);
        BufferedImage resultat = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resultat.createGraphics();
        g.drawImage(region, 0, 0, largeur, hauteur, null);
        g.dispose();
        return resultat;
    }
}
