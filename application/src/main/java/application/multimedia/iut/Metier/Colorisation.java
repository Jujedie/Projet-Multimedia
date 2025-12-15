package application.multimedia.iut.Metier;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Colorisation {
	static public final int VALEUR_MOYENNE = 127;

	public static void teinter( BufferedImage image, int red, int green, int blue ) {
		
	}

	public static void contraste( BufferedImage image, Color coul, int contraste) {
		if (contraste < -100) contraste = -100;
		if (contraste >  100) contraste =  100;

		int r = coul.getRed();
		int g = coul.getGreen();
		int b = coul.getBlue();
		
		r = (int)(r + contraste / 100f * (r - VALEUR_MOYENNE));
		g = (int)(g + contraste / 100f * (g - VALEUR_MOYENNE));
		b = (int)(b + contraste / 100f * (b - VALEUR_MOYENNE));

		if (r < 0)   r = 0;   
		if (r > 255) r = 255;
		
		if (g < 0)   g = 0;   
		if (g > 255) g = 255;
		
		if (b < 0)   b = 0;   
		if (b > 255) b = 255;

		int contrasteCouleur = new Color(r,g,b).getRGB();

		for(int x = 0; x < image.getWidth(); x++){
			for (int y = 0; y < image.getHeight(); y++){
				image.setRGB(x,y, contrasteCouleur);
			}
		}
	}

	public static int luminance (Color coul) {
		int r = coul.getRed();
		int g = coul.getGreen();
		int b = coul.getBlue();

		int min = Math.min ( r, Math.min(g,b));
		int max = Math.max ( r, Math.max(g,b));

		return (max + min) / 2;
	}

	public static void luminosite(BufferedImage image, Color coul, int luminosite) {
		if (luminosite < -255) luminosite = -255;
		if (luminosite >  255) luminosite =  255;

		int r = coul.getRed();
		int g = coul.getGreen();
		int b = coul.getBlue();

		r = r + luminosite;
		g = g + luminosite;
		b = b + luminosite;

		if (r < 0)   r = 0;   
		if (r > 255) r = 255;
		
		if (g < 0)   g = 0;   
		if (g > 255) g = 255;
		
		if (b < 0)   b = 0;   
		if (b > 255) b = 255;

		int luminositeCouleur = new Color(r,g,b).getRGB();

		for(int x = 0; x < image.getWidth(); x++){
			for (int y = 0; y < image.getHeight(); y++){
				image.setRGB(x,y, luminositeCouleur);
			}
		}
	}

	private void potDePeinture (BufferedImage image, int coulDest, int distance, Boolean estContinue, int xOrig, int yOrig) {
		if (estContinue) {
			potDePeintureRec (image, xOrig, yOrig, image.getRGB( xOrig, yOrig ) & 0xFFFFFF, coulDest, distance);
		}
		else {
			int coulOrig = image.getRGB( xOrig, yOrig ) & 0xFFFFFF;
			for(int x = 0; x < image.getWidth(); x++){
				for (int y = 0; y < image.getHeight(); y++){
					if (distance(image.getRGB( x, y ),coulOrig) < distance)
						image.setRGB(x,y, coulDest);
				}
			}
		}
	}

	private void potDePeintureRec  (BufferedImage image, int x, int y, int coulOrig, int coul, int distance) {
		if ( x < 0 || x >= image.getWidth() ) return;
		if ( y < 0 || y >= image.getHeight()) return;

		if ( !(distance(image.getRGB( x, y ),coulOrig) < distance) ){ return;}

		image.setRGB(x, y, coul);

		potDePeintureRec(image, x+1, y, coulOrig, coul, distance);
		potDePeintureRec(image, x-1, y, coulOrig, coul, distance);
		potDePeintureRec(image, x, y+1, coulOrig, coul, distance);
		potDePeintureRec(image, x, y-1, coulOrig, coul, distance);
	}

	public static double distance(int c1, int c2) {
		int r1 = c1 / (256*256);
		int g1 = c1 / 256 % 256;
		int b1 = c1 % 256;

		int r2 = c2 / (256*256);
		int g2 = c2 / 256 % 256;
		int b2 = c2 % 256;

		int dr = r1 - r2;
		int dg = g1 - g2;
		int db = b1 - b2;

		return Math.sqrt((double)(dr * dr + dg * dg + db * db));
	}
}