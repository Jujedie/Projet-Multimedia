/**
 * Classe représentant une couche d'image individuelle.
 * Contient les données d'une image avec sa position et ses propriétés.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Metier.image;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Représente une couche d'image avec sa position dans le canevas.
 * Chaque couche contient une BufferedImage et des coordonnées x, y.
 */
public class CoucheImage {
	public final BufferedImage image;
	public int x;
	public int y;

	/**
	 * Crée une nouvelle couche d'image.
	 * Initialise l'image avec sa position sur le canevas.
	 *
	 * @param image L'image source de la couche.
	 * @param x La coordonnée horizontale (coin supérieur gauche).
	 * @param y La coordonnée verticale (coin supérieur gauche).
	 */
	public CoucheImage(BufferedImage image, int x, int y) {
		this.image = image;
		this.x = x;
		this.y = y;
	}

	/**
	 * Calcule la largeur de l'image après application du zoom.
	 *
	 * @param zoom Le facteur d'échelle à appliquer.
	 * @return La largeur redimensionnée en pixels.
	 */
	public int largeurRedimensionnee(double zoom) {
		return (int) Math.round(image.getWidth() * zoom);
	}

	/**
	 * Calcule la hauteur de l'image après application du zoom.
	 *
	 * @param zoom Le facteur d'échelle à appliquer.
	 * @return La hauteur redimensionnée en pixels.
	 */
	public int hauteurRedimensionnee(double zoom) {
		return (int) Math.round(image.getHeight() * zoom);
	}

	/**
	 * Convertit la couche en rectangle englobant.
	 * Combine la position et les dimensions avec le zoom appliqué.
	 *
	 * @param zoom Le facteur d'échelle actuel.
	 * @return Le rectangle définissant la zone occupée par la couche.
	 */
	public Rectangle enRectangle(double zoom) {
		return new Rectangle(x, y, largeurRedimensionnee(zoom), hauteurRedimensionnee(zoom));
	}
}
