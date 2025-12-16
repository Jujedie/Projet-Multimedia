package application.multimedia.iut.Metier.image;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

public class RenduToile {
	public void peindre(Graphics g, PileCouches pile, SessionPlacement placement) {
		if (pile.estVide()) return;
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		Rectangle base = pile.limitesBase();
		List<CoucheImage> couches = pile.couches();
		double zoom = pile.niveauZoom();

		for (int i = 0; i < couches.size(); i++) {
			CoucheImage couche = couches.get(i);
			int largeur = couche.largeurRedimensionnee(zoom);
			int hauteur = couche.hauteurRedimensionnee(zoom);
			if (i > 0 && base != null) {
				Shape clipAncien = g2d.getClip();
				g2d.setClip(base);
				g2d.drawImage(couche.image, couche.x, couche.y, largeur, hauteur, null);
				g2d.setClip(clipAncien);
			} else {
				g2d.drawImage(couche.image, couche.x, couche.y, largeur, hauteur, null);
			}
		}

		if (placement != null && placement.estActive()) {
			CoucheImage enAttente = placement.coucheEnAttente();
			int largeur = enAttente.largeurRedimensionnee(zoom);
			int hauteur = enAttente.hauteurRedimensionnee(zoom);
			Shape clipAncien = g2d.getClip();
			if (base != null) g2d.setClip(base);
			Composite compAncien = g2d.getComposite();
			g2d.drawImage(enAttente.image, enAttente.x, enAttente.y, largeur, hauteur, null);
			g2d.setComposite(compAncien);
			g2d.setColor(Color.RED);
			g2d.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{6f,6f},0));
			g2d.drawRect(enAttente.x, enAttente.y, largeur, hauteur);
			g2d.setClip(clipAncien);
		}
	}

	public BufferedImage construireComposite(PileCouches pile) {
		if (pile.estVide()) return null;
		Rectangle base = pile.limitesBase();
		int minX;
		int minY;
		int largeurImg;
		int hauteurImg;
		if (base != null) {
			minX = base.x;
			minY = base.y;
			largeurImg = base.width;
			hauteurImg = base.height;
		} else {
			minX = 0;
			minY = 0;
			int maxX = 0;
			int maxY = 0;
			for (CoucheImage couche : pile.couches()) {
				int largeur = couche.largeurRedimensionnee(pile.niveauZoom());
				int hauteur = couche.hauteurRedimensionnee(pile.niveauZoom());
				minX = Math.min(minX, couche.x);
				minY = Math.min(minY, couche.y);
				maxX = Math.max(maxX, couche.x + largeur);
				maxY = Math.max(maxY, couche.y + hauteur);
			}
			largeurImg = Math.max(1, maxX - minX);
			hauteurImg = Math.max(1, maxY - minY);
		}
		BufferedImage composite = new BufferedImage(largeurImg, hauteurImg, BufferedImage.TYPE_INT_ARGB);
		
		List<CoucheImage> couches = pile.couches();
		double zoom = pile.niveauZoom();
		
		for (int i = 0; i < couches.size(); i++) {
			CoucheImage couche = couches.get(i);
			int largeur = couche.largeurRedimensionnee(zoom);
			int hauteur = couche.hauteurRedimensionnee(zoom);
			int posX = couche.x - minX;
			int posY = couche.y - minY;
			
			BufferedImage imageRedim = redimensionnerImage(couche.image, largeur, hauteur);
			
			if (i > 0 && base != null) {
				superposerAvecClip(composite, imageRedim, posX, posY, base.width, base.height);
			} else {
				superposerImage(composite, imageRedim, posX, posY);
			}
		}
		
		return composite;
	}
	
	private BufferedImage redimensionnerImage(BufferedImage source, int largeur, int hauteur) {
		if (source.getWidth() == largeur && source.getHeight() == hauteur) {
			return source;
		}
		
		BufferedImage resultat = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);
		double ratioX = (double) source.getWidth() / largeur;
		double ratioY = (double) source.getHeight() / hauteur;
		
		for (int y = 0; y < hauteur; y++) {
			for (int x = 0; x < largeur; x++) {
				int srcX = Math.min((int) (x * ratioX), source.getWidth() - 1);
				int srcY = Math.min((int) (y * ratioY), source.getHeight() - 1);
				resultat.setRGB(x, y, source.getRGB(srcX, srcY));
			}
		}
		
		return resultat;
	}
	
	private void superposerImage(BufferedImage dest, BufferedImage source, int posX, int posY) {
		int largeurDest = dest.getWidth();
		int hauteurDest = dest.getHeight();
		int largeurSource = source.getWidth();
		int hauteurSource = source.getHeight();
		
		for (int y = 0; y < hauteurSource; y++) {
			for (int x = 0; x < largeurSource; x++) {
				int destX = posX + x;
				int destY = posY + y;
				
				if (destX >= 0 && destX < largeurDest && destY >= 0 && destY < hauteurDest) {
					int coulSource = source.getRGB(x, y);
					int alphaSource = (coulSource >> 24) & 0xFF;
					
					if (alphaSource == 255) {
						dest.setRGB(destX, destY, coulSource);
					} else if (alphaSource > 0) {
						int coulDest = dest.getRGB(destX, destY);
						int nouvelleCoul = melangerCouleurs(coulDest, coulSource, alphaSource);
						dest.setRGB(destX, destY, nouvelleCoul);
					}
				}
			}
		}
	}
	
	private void superposerAvecClip(BufferedImage dest, BufferedImage source, int posX, int posY, int clipW, int clipH) {
		int largeurSource = source.getWidth();
		int hauteurSource = source.getHeight();
		
		for (int y = 0; y < hauteurSource; y++) {
			for (int x = 0; x < largeurSource; x++) {
				int destX = posX + x;
				int destY = posY + y;
				
				if (destX >= 0 && destX < clipW && destY >= 0 && destY < clipH) {
					int coulSource = source.getRGB(x, y);
					int alphaSource = (coulSource >> 24) & 0xFF;
					
					if (alphaSource == 255) {
						dest.setRGB(destX, destY, coulSource);
					} else if (alphaSource > 0) {
						int coulDest = dest.getRGB(destX, destY);
						int nouvelleCoul = melangerCouleurs(coulDest, coulSource, alphaSource);
						dest.setRGB(destX, destY, nouvelleCoul);
					}
				}
			}
		}
	}
	
	private int melangerCouleurs(int coulDest, int coulSource, int alphaSource) {
		int alphaDest = (coulDest >> 24) & 0xFF;
		int rougeDest = (coulDest >> 16) & 0xFF;
		int vertDest = (coulDest >> 8) & 0xFF;
		int bleuDest = coulDest & 0xFF;
		
		int rougeSource = (coulSource >> 16) & 0xFF;
		int vertSource = (coulSource >> 8) & 0xFF;
		int bleuSource = coulSource & 0xFF;
		
		float ratioSource = alphaSource / 255f;
		float ratioDest = 1f - ratioSource;
		
		int rouge = (int) (rougeSource * ratioSource + rougeDest * ratioDest);
		int vert = (int) (vertSource * ratioSource + vertDest * ratioDest);
		int bleu = (int) (bleuSource * ratioSource + bleuDest * ratioDest);
		int alpha = Math.max(alphaDest, alphaSource);
		
		return (alpha << 24) | (rouge << 16) | (vert << 8) | bleu;
	}
}
