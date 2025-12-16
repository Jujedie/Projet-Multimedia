package application.multimedia.iut.Metier.image;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class CoucheImage {
	public final BufferedImage image;
	public int x;
	public int y;

	public CoucheImage(BufferedImage image, int x, int y) {
		this.image = image;
		this.x = x;
		this.y = y;
	}

	public int largeurRedimensionnee(double zoom) {
		return (int) Math.round(image.getWidth() * zoom);
	}

	public int hauteurRedimensionnee(double zoom) {
		return (int) Math.round(image.getHeight() * zoom);
	}

	public Rectangle enRectangle(double zoom) {
		return new Rectangle(x, y, largeurRedimensionnee(zoom), hauteurRedimensionnee(zoom));
	}
}
