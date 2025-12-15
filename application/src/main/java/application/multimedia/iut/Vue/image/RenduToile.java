package application.multimedia.iut.Vue.image;

import java.awt.AlphaComposite;
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
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
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
		int minX = base != null ? base.x : 0;
		int minY = base != null ? base.y : 0;
		int maxX = base != null ? base.x + base.width : 0;
		int maxY = base != null ? base.y + base.height : 0;
		if (base == null) {
			for (CoucheImage couche : pile.couches()) {
				int largeur = couche.largeurRedimensionnee(pile.niveauZoom());
				int hauteur = couche.hauteurRedimensionnee(pile.niveauZoom());
				minX = Math.min(minX, couche.x);
				minY = Math.min(minY, couche.y);
				maxX = Math.max(maxX, couche.x + largeur);
				maxY = Math.max(maxY, couche.y + hauteur);
			}
		}
		int largeurImg = Math.max(1, maxX - minX);
		int hauteurImg = Math.max(1, maxY - minY);
		BufferedImage composite = new BufferedImage(largeurImg, hauteurImg, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = composite.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, largeurImg, hauteurImg);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		List<CoucheImage> couches = pile.couches();
		double zoom = pile.niveauZoom();
		for (int i = 0; i < couches.size(); i++) {
			CoucheImage couche = couches.get(i);
			int largeur = couche.largeurRedimensionnee(zoom);
			int hauteur = couche.hauteurRedimensionnee(zoom);
			if (i > 0 && base != null) {
				Shape clipAncien = g2d.getClip();
				g2d.setClip(0, 0, base.width, base.height);
				g2d.drawImage(couche.image, couche.x - minX, couche.y - minY, largeur, hauteur, null);
				g2d.setClip(clipAncien);
			} else {
				g2d.drawImage(couche.image, couche.x - minX, couche.y - minY, largeur, hauteur, null);
			}
		}
		g2d.dispose();
		return composite;
	}
}
