package application.multimedia.iut.Vue.image;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PileCouches {
	private final List<CoucheImage> couches = new ArrayList<>();
	private int indexActif = -1;
	private double niveauZoom = 1.0;

	public List<CoucheImage> couches() {
		return Collections.unmodifiableList(couches);
}

	public boolean estVide() {
		return couches.isEmpty();
	}

	public double niveauZoom() {
		return niveauZoom;
	}

	public void zoomer(double facteur) {
		niveauZoom *= facteur;
		if (niveauZoom < 0.1) niveauZoom = 0.1;
		if (niveauZoom > 5.0) niveauZoom = 5.0;
	}

	public void reinitialiserZoom() {
		niveauZoom = 1.0;
	}

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

	public CoucheImage ajouterCouche(CoucheImage couche) {
		couches.add(couche);
		indexActif = couches.size() - 1;
		return couche;
	}

	public void vider() {
		couches.clear();
		indexActif = -1;
		niveauZoom = 1.0;
	}

	public CoucheImage coucheActive() {
		if (indexActif >= 0 && indexActif < couches.size()) {
			return couches.get(indexActif);
		}
		return null;
	}

	public CoucheImage coucheBase() {
		return couches.isEmpty() ? null : couches.get(0);
	}

	public Rectangle limitesBase() {
		CoucheImage base = coucheBase();
		if (base == null) return null;
		return new Rectangle(base.x, base.y, base.largeurRedimensionnee(niveauZoom), base.hauteurRedimensionnee(niveauZoom));
	}
}
