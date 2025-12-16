/**
 * Classe principale pour lancer l'application de retouche d'images.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Metier;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Colorisation {
	static public final int VALEUR_MOYENNE = 127;

	public static void teinter( BufferedImage image, int red, int green, int blue, int alpha) {
		red   = clamp(red);
        green = clamp(green);
        blue  = clamp(blue);
        alpha = clamp(alpha);

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
				
				int a = (rgb >> 24) & 0xFF;
                int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = rgb & 0xFF;
				

                // alpha blending : résultat = tint*alpha + origine*(255-alpha)
                int nr = (red * alpha + r * (255 - alpha)) / 255;
                int ng = (green * alpha + g * (255 - alpha)) / 255;
                int nb = (blue * alpha + b * (255 - alpha)) / 255;

                int teinteCouleur = (a << 24) | (nr << 16) | (ng << 8) | nb;
                image.setRGB(x, y, teinteCouleur);
			}
		}
	}

	public static int clamp(int v) {
        return Math.max(0, Math.min(255, v));
    }

	public static void contraste( BufferedImage image, int contraste) {
		for(int x = 0; x < image.getWidth(); x++){
			for (int y = 0; y < image.getHeight(); y++){
				Color coul = new Color(image.getRGB(x,y));

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

	public static void luminosite(BufferedImage image, int luminosite) {
		for(int x = 0; x < image.getWidth(); x++){
			for (int y = 0; y < image.getHeight(); y++){
				Color coul = new Color(image.getRGB(x,y));
				
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

		
				image.setRGB(x,y, luminositeCouleur);
			}
		}
	}

	public static void potDePeinture (BufferedImage image, int coulDest, int distance, Boolean estContinue, int xOrig, int yOrig) {
		if (estContinue) {
			potDePeintureRec (image, xOrig, yOrig, image.getRGB( xOrig, yOrig ) & 0xFFFFFF, coulDest, distance);
		}
		else {
			int coulOrig = image.getRGB( xOrig, yOrig ) & 0xFFFFFF;
			for(int x = 0; x < image.getWidth(); x++){
				for (int y = 0; y < image.getHeight(); y++){
					if (distance(image.getRGB( x, y ),coulOrig) <= distance)
						image.setRGB(x,y, coulDest);
				}
			}
		}
	}

	public static void potDePeintureRec  (BufferedImage image, int x, int y, int coulOrig, int coul, double distance) {

        int w = image.getWidth();
        int h = image.getHeight();

		boolean[][] visited = new boolean[w][h];

		java.util.ArrayDeque<int[]> stack = new java.util.ArrayDeque<>();
        stack.push(new int[] { x, y });

        while (!stack.isEmpty()) {

            int[] p = stack.pop();
            int px = p[0];
            int py = p[1];

            if (px < 0 || px >= w || py < 0 || py >= h) continue;
            if (visited[px][py]) continue;
            visited[px][py] = true;

            if (distance(image.getRGB(px, py), coulOrig) > distance) continue;

            image.setRGB(px, py, coul);

            stack.push(new int[] { px + 1, py });
            stack.push(new int[] { px - 1, py });
            stack.push(new int[] { px, py + 1 });
            stack.push(new int[] { px, py - 1 });
        }
    }

	public static double distance(int c1, int c2) {
        // retirer l'alpha
        c1 &= 0xFFFFFF;
        c2 &= 0xFFFFFF;

        int r1 = (c1 >> 16) & 0xFF;
        int g1 = (c1 >> 8) & 0xFF;
        int b1 = c1 & 0xFF;

        int r2 = (c2 >> 16) & 0xFF;
        int g2 = (c2 >> 8) & 0xFF;
        int b2 = c2 & 0xFF;

        int dr = r1 - r2;
        int dg = g1 - g2;
        int db = b1 - b2;

        return Math.sqrt((double)(dr * dr + dg * dg + db * db));
    }
}