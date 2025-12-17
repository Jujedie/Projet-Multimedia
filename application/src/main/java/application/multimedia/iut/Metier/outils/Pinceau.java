/**
 * Classe gérant l'outil pinceau pour dessiner sur une image.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Metier.outils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Outil de dessin au pinceau sur une image.
 * Permet de tracer des traits avec une couleur et une épaisseur configurables.
 */
public class Pinceau {
	private Color couleur;
	private int epaisseur;

	/**
	 * Constructeur par défaut du pinceau.
	 * Initialise avec une couleur noire et une épaisseur de 2 pixels.
	 */
	public Pinceau() {
		this.couleur = Color.BLACK;
		this.epaisseur = 2;
	}

	/**
	 * Constructeur avec paramètres.
	 *
	 * @param couleur La couleur du pinceau.
	 * @param epaisseur L'épaisseur du trait en pixels.
	 */
	public Pinceau(Color couleur, int epaisseur) {
		this.couleur = couleur;
		this.epaisseur = Math.max(1, epaisseur);
	}

	/**
	 * Dessine un trait entre deux points sur l'image.
	 *
	 * @param image L'image sur laquelle dessiner.
	 * @param x1 Coordonnée X du point de départ.
	 * @param y1 Coordonnée Y du point de départ.
	 * @param x2 Coordonnée X du point d'arrivée.
	 * @param y2 Coordonnée Y du point d'arrivée.
	 */
	public void dessinerTrait(BufferedImage image, int x1, int y1, int x2, int y2) {
		if (image == null) return;
		
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(couleur);
		g2d.setStroke(new java.awt.BasicStroke(epaisseur, 
			java.awt.BasicStroke.CAP_ROUND, 
			java.awt.BasicStroke.JOIN_ROUND));
		g2d.drawLine(x1, y1, x2, y2);
		g2d.dispose();
	}

	/**
	 * Dessine un point sur l'image.
	 *
	 * @param image L'image sur laquelle dessiner.
	 * @param x Coordonnée X du point.
	 * @param y Coordonnée Y du point.
	 */
	public void dessinerPoint(BufferedImage image, int x, int y) {
		if (image == null) return;
		
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(couleur);
		int rayon = epaisseur / 2;
		g2d.fillOval(x - rayon, y - rayon, epaisseur, epaisseur);
		g2d.dispose();
	}

	/**
	 * Définit la couleur du pinceau.
	 *
	 * @param couleur La nouvelle couleur.
	 */
	public void setCouleur(Color couleur) {
		this.couleur = couleur;
	}

	/**
	 * Obtient la couleur actuelle du pinceau.
	 *
	 * @return La couleur du pinceau.
	 */
	public Color getCouleur() {
		return couleur;
	}

	/**
	 * Définit l'épaisseur du pinceau.
	 *
	 * @param epaisseur La nouvelle épaisseur en pixels (minimum 1).
	 */
	public void setEpaisseur(int epaisseur) {
		this.epaisseur = Math.max(1, epaisseur);
	}

	/**
	 * Obtient l'épaisseur actuelle du pinceau.
	 *
	 * @return L'épaisseur en pixels.
	 */
	public int getEpaisseur() {
		return epaisseur;
	}
}
