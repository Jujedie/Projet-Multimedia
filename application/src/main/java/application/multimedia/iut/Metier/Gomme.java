/**
 * Classe gérant l'outil gomme pour effacer sur une image.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Metier;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Outil gomme pour effacer des parties d'une image.
 * Dessine en blanc pour effacer les traits de crayon.
 */
public class Gomme {
	private int taille;
	private Color couleurEffacement;

	/**
	 * Constructeur par défaut de la gomme.
	 * Initialise avec une taille de 10 pixels et efface en blanc.
	 */
	public Gomme() {
		this.taille = 10;
		this.couleurEffacement = Color.WHITE;
	}

	/**
	 * Constructeur avec paramètres.
	 *
	 * @param taille La taille de la gomme en pixels.
	 */
	public Gomme(int taille) {
		this.taille = Math.max(1, taille);
		this.couleurEffacement = Color.WHITE;
	}

	/**
	 * Efface une zone entre deux points sur l'image.
	 * Dessine en blanc pour effacer les coups de crayon.
	 *
	 * @param image L'image sur laquelle effacer.
	 * @param x1 Coordonnée X du point de départ.
	 * @param y1 Coordonnée Y du point de départ.
	 * @param x2 Coordonnée X du point d'arrivée.
	 * @param y2 Coordonnée Y du point d'arrivée.
	 */
	public void effacer(BufferedImage image, int x1, int y1, int x2, int y2) {
		if (image == null) return;
		
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(couleurEffacement);
		g2d.setStroke(new java.awt.BasicStroke(taille, 
			java.awt.BasicStroke.CAP_ROUND, 
			java.awt.BasicStroke.JOIN_ROUND));
		g2d.drawLine(x1, y1, x2, y2);
		g2d.dispose();
	}

	/**
	 * Efface un point sur l'image.
	 * Dessine en blanc pour effacer.
	 *
	 * @param image L'image sur laquelle effacer.
	 * @param x Coordonnée X du point.
	 * @param y Coordonnée Y du point.
	 */
	public void effacerPoint(BufferedImage image, int x, int y) {
		if (image == null) return;
		
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(couleurEffacement);
		int rayon = taille / 2;
		g2d.fillOval(x - rayon, y - rayon, taille, taille);
		g2d.dispose();
	}

	/**
	 * Définit la taille de la gomme.
	 *
	 * @param taille La nouvelle taille en pixels (minimum 1).
	 */
	public void setTaille(int taille) {
		this.taille = Math.max(1, taille);
	}

	/**
	 * Obtient la taille actuelle de la gomme.
	 *
	 * @return La taille en pixels.
	 */
	public int getTaille() {
		return taille;
	}

	/**
	 * Définit la couleur d'effacement.
	 *
	 * @param couleur La couleur à utiliser pour effacer (généralement blanc).
	 */
	public void setCouleurEffacement(Color couleur) {
		this.couleurEffacement = couleur;
	}

	/**
	 * Obtient la couleur d'effacement actuelle.
	 *
	 * @return La couleur d'effacement.
	 */
	public Color getCouleurEffacement() {
		return couleurEffacement;
	}
}
