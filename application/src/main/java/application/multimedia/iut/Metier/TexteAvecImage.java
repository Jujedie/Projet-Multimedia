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
        
        int largeurTexte = calculerLargeurTexte();
        int hauteurTexte = calculerHauteurTexte();
        
        BufferedImage image = creerImageVide(largeurTexte, hauteurTexte);
        Graphics2D g2d = image.createGraphics();
        activerAntialiasing(g2d);
        
        if (imageFond != null && regionSelectionnee != null) {
            dessinerTexteAvecImageFond(g2d, largeurTexte, hauteurTexte);
        } else {
            dessinerTexteSimple(g2d);
        }
        
        g2d.dispose();
        return image;
    }
    
    private int calculerLargeurTexte() {
        BufferedImage temp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = temp.createGraphics();
        g.setFont(police);
        FontMetrics fm = g.getFontMetrics();
        int largeur = fm.stringWidth(texte);
        g.dispose();
        return largeur;
    }
    
    private int calculerHauteurTexte() {
        BufferedImage temp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = temp.createGraphics();
        g.setFont(police);
        FontMetrics fm = g.getFontMetrics();
        int hauteur = fm.getHeight();
        g.dispose();
        return hauteur;
    }
    
    private BufferedImage creerImageVide(int largeur, int hauteur) {
        return new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);
    }
    
    private void activerAntialiasing(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
    
    private void dessinerTexteSimple(Graphics2D g2d) {
        g2d.setFont(police);
        g2d.setColor(couleurTexte);
        
        FontMetrics fm = g2d.getFontMetrics();
        int y = fm.getAscent();
        
        g2d.drawString(texte, 0, y);
    }
    
    private void dessinerTexteAvecImageFond(Graphics2D g2d, int largeurTexte, int hauteurTexte) {
        BufferedImage imageFondRedimensionnee = extraireEtRedimensionnerImage(largeurTexte, hauteurTexte);
        
        if (imageFondRedimensionnee == null) {
            dessinerTexteSimple(g2d);
            return;
        }
        
        dessinerMasqueTexte(g2d);
        appliquerImageSurTexte(g2d, imageFondRedimensionnee);
        ajouterContourTexte(g2d);
    }
    
    private BufferedImage extraireEtRedimensionnerImage(int largeurCible, int hauteurCible) {
        int x = Math.max(0, regionSelectionnee.x);
        int y = Math.max(0, regionSelectionnee.y);
        int largeur = Math.min(regionSelectionnee.width, imageFond.getWidth() - x);
        int hauteur = Math.min(regionSelectionnee.height, imageFond.getHeight() - y);
        
        if (largeur <= 0 || hauteur <= 0) {
            return null;
        }
        
        BufferedImage region = imageFond.getSubimage(x, y, largeur, hauteur);
        
        BufferedImage imageRedim = new BufferedImage(largeurCible, hauteurCible, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imageRedim.createGraphics();
        g.drawImage(region, 0, 0, largeurCible, hauteurCible, null);
        g.dispose();
        
        return imageRedim;
    }
    
    private void dessinerMasqueTexte(Graphics2D g2d) {
        g2d.setFont(police);
        g2d.setColor(Color.WHITE);
        
        FontMetrics fm = g2d.getFontMetrics();
        int y = fm.getAscent();
        
        g2d.drawString(texte, 0, y);
    }
    
    private void appliquerImageSurTexte(Graphics2D g2d, BufferedImage imageFond) {
        g2d.setComposite(AlphaComposite.SrcIn);
        g2d.drawImage(imageFond, 0, 0, null);
        g2d.setComposite(AlphaComposite.SrcOver);
    }
    
    private void ajouterContourTexte(Graphics2D g2d) {
    }
}
