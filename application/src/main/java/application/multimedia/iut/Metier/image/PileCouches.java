/**
 * Classe gérant la pile de couches d'images (layers).
 * Permet la superposition et la gestion de plusieurs images.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Metier.image;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Pile de couches d'images pour la gestion multi-calques.
 * Permet d'empiler, zoomer et manipuler plusieurs images superposées.
 */
public class PileCouches {
	private final List<CoucheImage> couches = new ArrayList<>();
	private int indexActif = -1;
	private double niveauZoom = 1.0;

	/**
	 * Obtient la liste des couches d'images.
	 * Retourne une vue non modifiable pour protéger l'intégrité de la pile.
	 *
	 * @return La liste des couches en lecture seule.
	 */
	public List<CoucheImage> couches() {
		return Collections.unmodifiableList(couches);
}

	/**
	 * Vérifie si la pile ne contient aucune couche.
	 *
	 * @return true si la pile est vide, false sinon.
	 */
	public boolean estVide() {
		return couches.isEmpty();
	}

	/**
	 * Obtient le niveau de zoom actuel.
	 *
	 * @return Le facteur de zoom (1.0 = 100%).
	 */
	public double niveauZoom() {
		return niveauZoom;
	}

	/**
	 * Applique un facteur de zoom multiplicatif.
	 * Le zoom est limité entre 0.1 (10%) et 5.0 (500%).
	 *
	 * @param facteur Le multiplicateur à appliquer au zoom actuel.
	 */
	public void zoomer(double facteur) {
		niveauZoom *= facteur;
		if (niveauZoom < 0.1) niveauZoom = 0.1;
		if (niveauZoom > 5.0) niveauZoom = 5.0;
	}

	/**
	 * Réinitialise le zoom à sa valeur par défaut (100%).
	 */
	public void reinitialiserZoom() {
		niveauZoom = 1.0;
	}

	/**
	 * Ajoute une nouvelle couche à partir d'une image.
	 * L'image peut être centrée automatiquement sur la toile.
	 *
	 * @param img L'image source à ajouter.
	 * @param tailleToile Les dimensions du canevas pour le centrage.
	 * @param centrer true pour centrer l'image, false pour position (0,0).
	 * @return La couche créée et ajoutée.
	 */
	public CoucheImage ajouterCouche(BufferedImage img, Dimension tailleToile, boolean centrer) {
		int x = 0;
		int y = 0;
		if (centrer) {
			int largeurToile = Math.max(1, tailleToile.width);
			int hauteurToile = Math.max(1, tailleToile.height);
			x = (largeurToile - img.getWidth()) / 2;
			y = (hauteurToile - img.getHeight()) / 2;
		}
		return ajouterCouche(new CoucheImage(img, x, y));
	}

	/**
	 * Ajoute une couche existante à la pile.
	 * La couche devient automatiquement la couche active.
	 *
	 * @param couche La couche à ajouter.
	 * @return La couche ajoutée.
	 */
	public CoucheImage ajouterCouche(CoucheImage couche) {
		couches.add(couche);
		indexActif = couches.size() - 1;
		return couche;
	}

	/**
	 * Supprime toutes les couches de la pile.
	 * Réinitialise également l'index actif et le zoom.
	 */
	public void vider() {
		couches.clear();
		indexActif = -1;
		niveauZoom = 1.0;
	}

	/**
	 * Obtient la couche actuellement sélectionnée.
	 *
	 * @return La couche active, ou null si aucune couche ou index invalide.
	 */
	public CoucheImage coucheActive() {
		if (indexActif >= 0 && indexActif < couches.size()) {
			return couches.get(indexActif);
		}
		return null;
	}

	/**
	 * Obtient la première couche de la pile (couche de fond).
	 *
	 * @return La couche de base, ou null si la pile est vide.
	 */
	public CoucheImage coucheBase() {
		return couches.isEmpty() ? null : couches.get(0);
	}

	/**
	 * Calcule les limites de la couche de base avec le zoom appliqué.
	 *
	 * @return Le rectangle englobant de la couche de base, ou null si vide.
	 */
	public Rectangle limitesBase() {
		CoucheImage base = coucheBase();
		if (base == null) return null;
		return new Rectangle(base.x, base.y, base.largeurRedimensionnee(niveauZoom), base.hauteurRedimensionnee(niveauZoom));
	}
}
