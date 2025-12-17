/**
 * Classe gérant le placement interactif d'images sur le canevas.
 * Permet de positionner une image avant de la valider.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Metier.image;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Gère le placement interactif d'une image avant validation.
 * Permet de positionner et déplacer une couche en prévisualisation.
 */
public class SessionPlacement {
	private CoucheImage coucheEnAttente;
	private boolean active;
	private Rectangle limitesBase;

	/**
	 * Démarre une nouvelle session de placement d'image.
	 * L'image est initialement centrée sur la toile, après application du zoom.
	 *
	 * @param img L'image source à placer.
	 * @param tailleToile Les dimensions du canevas.
	 * @param zoom Le facteur d'échelle à appliquer à l'image.
	 * @param limitesBase Les limites de la zone de base (première image/canevas).
	 */
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

	/**
	 * Vérifie si une session de placement est active.
	 *
	 * @return true si une session est en cours, false sinon.
	 */
	public boolean estActive() {
		return active && coucheEnAttente != null;
	}

	/**
	 * Obtient la couche d'image en attente de placement.
	 *
	 * @return La couche en attente, ou null si aucune session active.
	 */
	public CoucheImage coucheEnAttente() {
		return coucheEnAttente;
	}

	/**
	 * Obtient les limites de la couche en attente de placement.
	 * Calcule le rectangle englobant en tenant compte du zoom.
	 *
	 * @param zoom Le facteur de zoom actuel.
	 * @return Le rectangle des limites, ou null si aucune couche.
	 */
	public Rectangle limitesCouche(double zoom) {
		return coucheEnAttente == null ? null : coucheEnAttente.enRectangle(zoom);
	}

	/**
	 * Déplace la couche en attente vers un point spécifique.
	 * Le centre de l'image est positionné sur le point donné.
	 *
	 * @param point Le point de destination (coordonnées du centre).
	 * @param zoom Le facteur de zoom actuel pour calculer les dimensions.
	 */
	public void deplacerAu(Point point, double zoom) {
		if (coucheEnAttente == null) return;
		int largeur = coucheEnAttente.largeurRedimensionnee(zoom);
		int hauteur = coucheEnAttente.hauteurRedimensionnee(zoom);
		coucheEnAttente.x = point.x - largeur / 2;
		coucheEnAttente.y = point.y - hauteur / 2;
	}

	/**
	 * Déplace la couche en attente par un décalage relatif.
	 * Applique une translation à la position actuelle.
	 *
	 * @param dx Le décalage horizontal en pixels.
	 * @param dy Le décalage vertical en pixels.
	 */
	public void translater(int dx, int dy) {
		if (coucheEnAttente == null) return;
		coucheEnAttente.x += dx;
		coucheEnAttente.y += dy;
	}

	/**
	 * Vérifie si la couche en attente intersecte les limites de l'image de base.
	 * Utilisé pour valider qu'une superposition est possible.
	 *
	 * @param zoom Le facteur de zoom actuel.
	 * @return true si les zones se chevauchent, false sinon.
	 */
	public boolean intersecteBase(double zoom) {
		if (coucheEnAttente == null) return false;
		if (limitesBase == null) return true;
		Rectangle inter = limitesCouche(zoom).intersection(limitesBase);
		return !inter.isEmpty();
	}

	/**
	 * Valide le placement de la couche en attente.
	 * Termine la session et retourne la couche placée.
	 *
	 * @return La couche image validée, ou null si aucune session active.
	 */
	public CoucheImage valider() {
		if (!estActive()) return null;
		CoucheImage placee = coucheEnAttente;
		coucheEnAttente = null;
		active = false;
		limitesBase = null;
		return placee;
	}

	/**
	 * Annule la session de placement en cours.
	 * Supprime la couche en attente et réinitialise l'état.
	 */
	public void annuler() {
		coucheEnAttente = null;
		active = false;
		limitesBase = null;
	}

	/**
	 * Obtient les limites de l'image de base.
	 *
	 * @return Le rectangle définissant la zone de base.
	 */
	public Rectangle limitesBase() {
		return limitesBase;
	}
}
