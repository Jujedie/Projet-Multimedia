/**
 * Classe principale pour lancer l'application de retouche d'images.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Metier.image;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class SessionPlacement {
	private CoucheImage coucheEnAttente;
	private boolean active;
	private Rectangle limitesBase;

	public void demarrer(BufferedImage img, Dimension tailleToile, double zoom, Rectangle limitesBase) {
		int largeurToile = Math.max(1, tailleToile.width);
		int hauteurToile = Math.max(1, tailleToile.height);
		int largeur = (int) Math.round(img.getWidth() * zoom);
		int hauteur = (int) Math.round(img.getHeight() * zoom);
		int x = (largeurToile - largeur) / 2;
		int y = (hauteurToile - hauteur) / 2;
		coucheEnAttente = new CoucheImage(img, x, y);
		active = true;
		this.limitesBase = limitesBase;
	}

	public boolean estActive() {
		return active && coucheEnAttente != null;
	}

	public CoucheImage coucheEnAttente() {
		return coucheEnAttente;
	}

	public Rectangle limitesCouche(double zoom) {
		return coucheEnAttente == null ? null : coucheEnAttente.enRectangle(zoom);
	}

	public void deplacerAu(Point point, double zoom) {
		if (coucheEnAttente == null) return;
		int largeur = coucheEnAttente.largeurRedimensionnee(zoom);
		int hauteur = coucheEnAttente.hauteurRedimensionnee(zoom);
		coucheEnAttente.x = point.x - largeur / 2;
		coucheEnAttente.y = point.y - hauteur / 2;
	}

	public void translater(int dx, int dy) {
		if (coucheEnAttente == null) return;
		coucheEnAttente.x += dx;
		coucheEnAttente.y += dy;
	}

	public boolean intersecteBase(double zoom) {
		if (coucheEnAttente == null) return false;
		if (limitesBase == null) return true;
		Rectangle inter = limitesCouche(zoom).intersection(limitesBase);
		return !inter.isEmpty();
	}

	public CoucheImage valider() {
		if (!estActive()) return null;
		CoucheImage placee = coucheEnAttente;
		coucheEnAttente = null;
		active = false;
		limitesBase = null;
		return placee;
	}

	public void annuler() {
		coucheEnAttente = null;
		active = false;
		limitesBase = null;
	}

	public Rectangle limitesBase() {
		return limitesBase;
	}
}
