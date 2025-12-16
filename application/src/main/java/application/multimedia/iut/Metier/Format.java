package application.multimedia.iut.Metier;

import java.awt.image.BufferedImage;

public class Format {

	public static BufferedImage redimensionner(BufferedImage source, int largeurCible, int hauteurCible) {
		int largeurSource = source.getWidth();
		int hauteurSource = source.getHeight();

		BufferedImage imageRedimensionnee = new BufferedImage(largeurCible, hauteurCible, BufferedImage.TYPE_INT_ARGB);

		double ratioX = (double) (largeurSource - 1) / largeurCible;
		double ratioY = (double) (hauteurSource - 1) / hauteurCible;

		for (int y = 0; y < hauteurCible; y++) {
			for (int x = 0; x < largeurCible; x++) {

				double sourceX = x * ratioX;
				double sourceY = y * ratioY;

				int xSol = (int) sourceX;
				int ySol = (int) sourceY;

				int xPlafond = Math.min(xSol + 1, largeurSource - 1);
				int yPlafond = Math.min(ySol + 1, hauteurSource - 1);

				int rgbA = source.getRGB(xSol, ySol);
				int rgbB = source.getRGB(xPlafond, ySol);
				int rgbC = source.getRGB(xSol, yPlafond);
				int rgbD = source.getRGB(xPlafond, yPlafond);

				double poidsX = sourceX - xSol;
				double poidsY = sourceY - ySol;

				int alpha = interpoler(getAlpha(rgbA), getAlpha(rgbB), getAlpha(rgbC), getAlpha(rgbD),
						poidsX, poidsY);
				int rouge = interpoler(getRouge(rgbA), getRouge(rgbB), getRouge(rgbC), getRouge(rgbD), poidsX,
						poidsY);
				int vert = interpoler(getVert(rgbA), getVert(rgbB), getVert(rgbC), getVert(rgbD),
						poidsX, poidsY);
				int bleu = interpoler(getBleu(rgbA), getBleu(rgbB), getBleu(rgbC), getBleu(rgbD), poidsX,
						poidsY);

				int rgbFinal = (alpha << 24) | (rouge << 16) | (vert << 8) | bleu;
				imageRedimensionnee.setRGB(x, y, rgbFinal);
			}
		}

		return imageRedimensionnee;
	}

	private static int interpoler(int a, int b, int c, int d, double poidsX, double poidsY) {

		double haut = a * (1 - poidsX) + b * poidsX;
		double bas = c * (1 - poidsX) + d * poidsX;

		double valeurFinale = haut * (1 - poidsY) + bas * poidsY;
		return (int) Math.round(valeurFinale);
	}

	private static int getAlpha(int rgb) {
		return (rgb >> 24) & 0xFF;
	}

	private static int getRouge(int rgb) {
		return (rgb >> 16) & 0xFF;
	}

	private static int getVert(int rgb) {
		return (rgb >> 8) & 0xFF;
	}

	private static int getBleu(int rgb) {
		return rgb & 0xFF;
	}

	public static BufferedImage couper(BufferedImage source, int x1, int y1, int x2, int y2) {

		int debutX = Math.min(x1, x2);
		int debutY = Math.min(y1, y2);
		int largeur = Math.abs(x2 - x1) + 1;
		int hauteur = Math.abs(y2 - y1) + 1;

		if (debutX < 0 || debutY < 0 || (debutX + largeur) > source.getWidth()
				|| (debutY + hauteur) > source.getHeight()) {
			throw new IllegalArgumentException(
					"Les coordonnées (" + x1 + ", " + y1 + ") à (" + x2 + ", " + y2
							+ ") sont en dehors des limites de l'image source (" + source.getWidth() + "x"
							+ source.getHeight() + ").");
		}
		BufferedImage sousImage = source.getSubimage(debutX, debutY, largeur, hauteur);
		return sousImage;
	}

	public static BufferedImage rotation(BufferedImage src, double angleDeg) {

		angleDeg = ((angleDeg % 360) + 360) % 360;

		int w = src.getWidth();
		int h = src.getHeight();

		if (angleDeg == 90) {
			BufferedImage dest = new BufferedImage(h, w, BufferedImage.TYPE_INT_ARGB);
			for (int y = 0; y < h; y++)
				for (int x = 0; x < w; x++)
					dest.setRGB(h - 1 - y, x, src.getRGB(x, y));
			return dest;

		} else if (angleDeg == 180) {
			BufferedImage dest = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			for (int y = 0; y < h; y++)
				for (int x = 0; x < w; x++)
					dest.setRGB(w - 1 - x, h - 1 - y, src.getRGB(x, y));
			return dest;

		} else if (angleDeg == 270) {
			BufferedImage dest = new BufferedImage(h, w, BufferedImage.TYPE_INT_ARGB);
			for (int y = 0; y < h; y++)
				for (int x = 0; x < w; x++)
					dest.setRGB(y, w - 1 - x, src.getRGB(x, y));
			return dest;

		} else {
			return rotationRSamp(src, angleDeg);
		}
	}

	public static BufferedImage rotationRSamp(BufferedImage src, double angleDeg) {
		int srcWidth = src.getWidth();
		int srcHeight = src.getHeight();

		double angle = Math.toRadians(angleDeg);
		double cosA = Math.abs(Math.cos(angle));
		double sinA = Math.abs(Math.sin(angle));

		int newWidth = (int) Math.ceil(srcWidth * cosA + srcHeight * sinA);
		int newHeight = (int) Math.ceil(srcHeight * cosA + srcWidth * sinA);
		BufferedImage dest = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

		int cxSrc = srcWidth / 2;
		int cySrc = srcHeight / 2;

		int cxDest = newWidth / 2;
		int cyDest = newHeight / 2;

		double cos = Math.cos(angle);
		double sin = Math.sin(angle);

		for (int yPrime = 0; yPrime < newHeight; yPrime++) {
			for (int xPrime = 0; xPrime < newWidth; xPrime++) {
				double xC = xPrime - cxDest;
				double yC = yPrime - cyDest;

				double x = xC * cos + yC * sin;
				double y = -xC * sin + yC * cos;

				int xSrc = (int) Math.round(x + cxSrc);
				int ySrc = (int) Math.round(y + cySrc);

				if (xSrc >= 0 && xSrc < srcWidth && ySrc >= 0 && ySrc < srcHeight) {
					dest.setRGB(xPrime, yPrime, src.getRGB(xSrc, ySrc));
				} else {
					dest.setRGB(xPrime, yPrime, 0x000000); // fond noir
				}
			}
		}
		return dest;
	}

	public static BufferedImage symetrieHorizontale(BufferedImage source) {
		int largeur = source.getWidth();
		int hauteur = source.getHeight();

		BufferedImage imageSymetrique = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);

		for (int y = 0; y < hauteur; y++) {
			for (int x = 0; x < largeur; x++) {
				int rgb = source.getRGB(x, y);
				int nouveauX = largeur - 1 - x;

				imageSymetrique.setRGB(nouveauX, y, rgb);
			}
		}
		return imageSymetrique;
	}

	public static BufferedImage symetrieVerticale(BufferedImage source) {
		int largeur = source.getWidth();
		int hauteur = source.getHeight();

		BufferedImage imageSymetrique = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);

		for (int y = 0; y < hauteur; y++) {
			for (int x = 0; x < largeur; x++) {
				int rgb = source.getRGB(x, y);
				int nouveauY = hauteur - 1 - y;

				imageSymetrique.setRGB(x, nouveauY, rgb);
			}
		}
		return imageSymetrique;
	}
}