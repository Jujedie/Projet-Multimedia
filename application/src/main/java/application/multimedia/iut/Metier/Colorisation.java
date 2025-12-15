package application.multimedia.iut.Metier;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Colorisation {
	public static void teinter( BufferedImage image, int red, int green, int blue ) {
		
	}

	public static void contraste( BufferedImage image) {

	}

	public static int luminance (Color coul) {
		int r = coul.getRed();
		int g = coul.getGreen();
		int b = coul.getBlue();

		int min = Math.min ( r, Math.min(g,b));
		int max = Math.max ( r, Math.max(g,b));

		return (max + min) / 2;
	}

	public static int luminosite(Color coul, int luminosite) {
		
	}

	public static void potDePeinture (BufferedImage image, int coulOrig, int coulDest, Boolean estContinue ) {

	}
}