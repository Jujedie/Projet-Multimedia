/**
 * Classe gérant l'outil pipette pour prélever une couleur d'une image.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Metier.outils;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Outil pipette pour sélectionner une couleur à partir d'une image.
 */
public class Pipette {

	/**
	 * Prélève la couleur d'un pixel à une position donnée.
	 *
	 * @param image L'image source.
	 * @param x Coordonnée X du pixel.
	 * @param y Coordonnée Y du pixel.
	 * @return La couleur du pixel, ou null si les coordonnées sont invalides.
	 */
	public Color preleverCouleur(BufferedImage image, int x, int y) {
		if (image == null) return null;
		if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) {
			return null;
		}
		
		int rgb = image.getRGB(x, y);
		return new Color(rgb, true);
	}

	/**
	 * Prélève la couleur moyenne d'une zone carrée autour d'un point.
	 *
	 * @param image L'image source.
	 * @param x Coordonnée X du centre.
	 * @param y Coordonnée Y du centre.
	 * @param rayon Rayon de la zone de prélèvement.
	 * @return La couleur moyenne, ou null si invalide.
	 */
	public Color preleverCouleurMoyenne(BufferedImage image, int x, int y, int rayon) {
		if (image == null || rayon < 1) return null;
		
		int sommeRouge = 0;
		int sommeVert = 0;
		int sommeBleu = 0;
		int sommeAlpha = 0;
		int compteur = 0;
		
		for (int dy = -rayon; dy <= rayon; dy++) {
			for (int dx = -rayon; dx <= rayon; dx++) {
				int px = x + dx;
				int py = y + dy;
				
				if (px >= 0 && px < image.getWidth() && py >= 0 && py < image.getHeight()) {
					int rgb = image.getRGB(px, py);
					sommeAlpha += (rgb >> 24) & 0xFF;
					sommeRouge += (rgb >> 16) & 0xFF;
					sommeVert += (rgb >> 8) & 0xFF;
					sommeBleu += rgb & 0xFF;
					compteur++;
				}
			}
		}
		
		if (compteur == 0) return null;
		
		return new Color(
			sommeRouge / compteur,
			sommeVert / compteur,
			sommeBleu / compteur,
			sommeAlpha / compteur
		);
	}
}
