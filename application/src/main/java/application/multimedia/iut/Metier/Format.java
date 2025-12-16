/**
 * Classe principale pour lancer l'application de retouche d'images.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Metier;

import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

public class Format {
	public static BufferedImage redimensionner(BufferedImage image, int largeur, int hauteur) {

		Image imageMiseAEchelle = image.getScaledInstance(largeur, hauteur, Image.SCALE_SMOOTH);

		BufferedImage imageRedimensionnee = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = imageRedimensionnee.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		g2d.drawImage(imageMiseAEchelle, 0, 0, null);

		g2d.dispose();

		return imageRedimensionnee;
	}

	public static BufferedImage couper(BufferedImage source, int x1, int y1, int x2, int y2) {

		int startX = Math.min(x1, x2);
		int startY = Math.min(y1, y2);

		int width = Math.abs(x2 - x1) + 1;

		int height = Math.abs(y2 - y1) + 1;

		if (startX < 0 || startY < 0 || (startX + width) > source.getWidth()
				|| (startY + height) > source.getHeight()) {
			throw new IllegalArgumentException(
					"Les coordonnées (" + x1 + ", " + y1 + ") à (" + x2 + ", " + y2
							+ ") sont en dehors des limites de l'image source (" + source.getWidth() + "x"
							+ source.getHeight() + ").");
		}

		BufferedImage subImage = source.getSubimage(startX, startY, width, height);

		return subImage;
	}

	public static BufferedImage rotation(BufferedImage source, double angle) {

		double angleRad = Math.toRadians(angle);
		int largeur = source.getWidth();
		int hauteur = source.getHeight();

		int nouvelleLargeur = largeur;
		int nouvelleHauteur = hauteur;

		boolean angleDroit = angle % 180 != 0 && angle % 90 == 0;
		if (angleDroit) {
			nouvelleLargeur = hauteur;
			nouvelleHauteur = largeur;
		}

		BufferedImage imageRotation = new BufferedImage(nouvelleLargeur, nouvelleHauteur, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = imageRotation.createGraphics();

		AffineTransform tx = new AffineTransform();

		if (angleDroit) {
			if (angle % 360 == 90 || angle % 360 == -270) {
				tx.translate(nouvelleLargeur, 0);
				tx.rotate(angleRad);
			} else if (angle % 360 == 270 || angle % 360 == -90) {
				tx.translate(0, nouvelleHauteur);
				tx.rotate(angleRad);
			} else if (angle % 360 == 180 || angle % 360 == -180) {
				tx.translate(nouvelleLargeur, nouvelleHauteur);
				tx.rotate(angleRad);
			}

		} else {
			tx.translate(largeur / 2.0, hauteur / 2.0);
			tx.rotate(angleRad);
			tx.translate(-largeur / 2.0, -hauteur / 2.0);
		}

		g2d.setTransform(tx);
		g2d.drawImage(source, 0, 0, null);
		g2d.dispose();

		return imageRotation;
	}

	public static BufferedImage symetrieHorizontale(BufferedImage source) {
		int largeur = source.getWidth();
		int hauteur = source.getHeight();

		BufferedImage imageSymetrique = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = imageSymetrique.createGraphics();

		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-largeur, 0);
		g2d.setTransform(tx);
		g2d.drawImage(source, 0, 0, null);

		g2d.dispose();

		return imageSymetrique;
	}

	public static BufferedImage symetrieVerticale(BufferedImage source) {
		int largeur = source.getWidth();
		int hauteur = source.getHeight();

		BufferedImage imageSymetrique = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = imageSymetrique.createGraphics();

		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -hauteur);
		g2d.setTransform(tx);
		g2d.drawImage(source, 0, 0, null);

		g2d.dispose();

		return imageSymetrique;
	}
}