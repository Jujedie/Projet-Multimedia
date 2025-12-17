package application.multimedia.iut.Metier;

import application.multimedia.iut.Metier.image.*;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

public class AjoutContenu {

	public void peindre(Graphics g, PileCouches pile, SessionPlacement placement) {
		if (pile.estVide())
			return;
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		Rectangle base = pile.limitesBase();
		List<CoucheImage> couches = pile.couches();
		double zoom = pile.niveauZoom();

		for (int i = 0; i < couches.size(); i++) {
			CoucheImage couche = couches.get(i);
			int largeur = couche.largeurRedimensionnee(zoom);
			int hauteur = couche.hauteurRedimensionnee(zoom);
			if (i > 0 && base != null) {
				Shape clipAncien = g2d.getClip();
				g2d.setClip(base);
				g2d.drawImage(couche.image, couche.x, couche.y, largeur, hauteur, null);
				g2d.setClip(clipAncien);
			} else {
				g2d.drawImage(couche.image, couche.x, couche.y, largeur, hauteur, null);
			}
		}

		if (placement != null && placement.estActive()) {
			CoucheImage enAttente = placement.coucheEnAttente();
			int largeur = enAttente.largeurRedimensionnee(zoom);
			int hauteur = enAttente.hauteurRedimensionnee(zoom);
			Shape clipAncien = g2d.getClip();
			if (base != null)
				g2d.setClip(base);
			Composite compAncien = g2d.getComposite();
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
			g2d.drawImage(enAttente.image, enAttente.x, enAttente.y, largeur, hauteur, null);
			g2d.setComposite(compAncien);
			g2d.setColor(Color.RED);
			g2d.setStroke(
					new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[] { 6f, 6f }, 0));
			g2d.drawRect(enAttente.x, enAttente.y, largeur, hauteur);
			g2d.setClip(clipAncien);
		}
	}

	public BufferedImage construireComposite(PileCouches pile) {
		if (pile.estVide())
			return null;
		Rectangle base = pile.limitesBase();
		int minX;
		int minY;
		int largeurImg;
		int hauteurImg;
		if (base != null) {
			minX = base.x;
			minY = base.y;
			largeurImg = base.width;
			hauteurImg = base.height;
		} else {
			minX = 0;
			minY = 0;
			int maxX = 0;
			int maxY = 0;
			for (CoucheImage couche : pile.couches()) {
				int largeur = couche.largeurRedimensionnee(pile.niveauZoom());
				int hauteur = couche.hauteurRedimensionnee(pile.niveauZoom());
				minX = Math.min(minX, couche.x);
				minY = Math.min(minY, couche.y);
				maxX = Math.max(maxX, couche.x + largeur);
				maxY = Math.max(maxY, couche.y + hauteur);
			}
			largeurImg = Math.max(1, maxX - minX);
			hauteurImg = Math.max(1, maxY - minY);
		}
		BufferedImage composite = new BufferedImage(largeurImg, hauteurImg, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = composite.createGraphics();
		g2d.setComposite(AlphaComposite.Src);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		List<CoucheImage> couches = pile.couches();
		double zoom = pile.niveauZoom();
		for (int i = 0; i < couches.size(); i++) {
			CoucheImage couche = couches.get(i);
			int largeur = couche.largeurRedimensionnee(zoom);
			int hauteur = couche.hauteurRedimensionnee(zoom);
			if (i > 0 && base != null) {
				Shape clipAncien = g2d.getClip();
				g2d.setClip(0, 0, base.width, base.height);
				g2d.drawImage(couche.image, couche.x - minX, couche.y - minY, largeur, hauteur, null);
				g2d.setClip(clipAncien);
			} else {
				g2d.drawImage(couche.image, couche.x - minX, couche.y - minY, largeur, hauteur, null);
			}
		}
		g2d.dispose();
		return composite;
	}

	public static BufferedImage fusionHorizontale(BufferedImage imgGauche, BufferedImage imgDroite, int fondu) {

		if (fondu < 0 || fondu > imgGauche.getWidth() || fondu > imgDroite.getWidth()) {
			throw new IllegalArgumentException(
					"La largeur du fondu (" + fondu
							+ ") doit être positive et inférieure aux largeurs des deux images.");
		}

		if (imgGauche.getHeight() != imgDroite.getHeight()) {

			if (imgGauche.getHeight() > imgDroite.getHeight()) {
				double ratio = (double) imgDroite.getHeight() / imgGauche.getHeight();
				int newWidth = (int) (imgGauche.getWidth() * ratio);
				imgGauche = Format.redimensionner(imgGauche, newWidth, imgDroite.getHeight());
			}

			if (imgGauche.getHeight() < imgDroite.getHeight()) {
				double ratio = (double) imgGauche.getHeight() / imgDroite.getHeight();
				int newWidth = (int) (imgDroite.getWidth() * ratio);
				imgDroite = Format.redimensionner(imgDroite, newWidth, imgGauche.getHeight());
			}
		}

		int width = imgGauche.getWidth() + imgDroite.getWidth() - fondu;
		int height = Math.max(imgGauche.getHeight(), imgDroite.getHeight());

		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = result.getGraphics();

		int xDebutFondu = imgGauche.getWidth() - fondu;

		g.drawImage(imgGauche,
				0, 0, xDebutFondu, height,
				0, 0, xDebutFondu, imgGauche.getHeight(),
				null);

		g.drawImage(imgDroite,
				imgGauche.getWidth(), 0, width, height,
				fondu, 0, imgDroite.getWidth(), imgDroite.getHeight(),
				null);

		for (int x = 0; x < fondu; x++) {
			float alpha = x / (float) fondu;

			for (int y = 0; y < height; y++) {

				int color1 = y < imgGauche.getHeight() ? imgGauche.getRGB(xDebutFondu + x, y) : Color.TRANSLUCENT;
				int color2 = y < imgDroite.getHeight() ? imgDroite.getRGB(x, y) : Color.TRANSLUCENT;

				int a1 = (color1 >> 24) & 0xff;
				int r1 = (color1 >> 16) & 0xff;
				int g1 = (color1 >> 8) & 0xff;
				int b1 = color1 & 0xff;

				int a2 = (color2 >> 24) & 0xff;
				int r2 = (color2 >> 16) & 0xff;
				int g2 = (color2 >> 8) & 0xff;
				int b2 = color2 & 0xff;

				int a = (int) (a1 * (1 - alpha) + a2 * alpha);
				int r = (int) (r1 * (1 - alpha) + r2 * alpha);
				int g_ = (int) (g1 * (1 - alpha) + g2 * alpha);
				int b = (int) (b1 * (1 - alpha) + b2 * alpha);

				int argb = (a << 24) | (r << 16) | (g_ << 8) | b;

				result.setRGB(xDebutFondu + x, y, argb);
			}
		}

		g.dispose();
		return result;
	}

public static BufferedImage fusionVerticale(BufferedImage imgHaut, BufferedImage imgBas, int fondu) {

        if (fondu < 0 || fondu > imgHaut.getHeight() || fondu > imgBas.getHeight()) {
            throw new IllegalArgumentException(
                    "La hauteur du fondu (" + fondu
                            + ") doit être positive et inférieure aux hauteurs des deux images.");
        }

        if (imgHaut.getWidth() != imgBas.getWidth()) {

            if (imgHaut.getWidth() > imgBas.getWidth()) {
                double ratio = (double) imgBas.getWidth() / imgHaut.getWidth();
                int newHeight = (int) (imgHaut.getHeight() * ratio);
                imgHaut = Format.redimensionner(imgHaut, imgBas.getWidth(), newHeight);
            }

            if (imgHaut.getWidth() < imgBas.getWidth()) {
                double ratio = (double) imgHaut.getWidth() / imgBas.getWidth();
                int newHeight = (int) (imgBas.getHeight() * ratio);
                imgBas = Format.redimensionner(imgBas, imgHaut.getWidth(), newHeight);
            }
        }

        int width = Math.max(imgHaut.getWidth(), imgBas.getWidth());
        int height = imgHaut.getHeight() + imgBas.getHeight() - fondu;

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = result.getGraphics();

        int yDebutFondu = imgHaut.getHeight() - fondu;

        g.drawImage(imgHaut,
                0, 0, width, yDebutFondu,
                0, 0, imgHaut.getWidth(), yDebutFondu,
                null);

        g.drawImage(imgBas,
                0, imgHaut.getHeight(), width, height,
                0, fondu, imgBas.getWidth(), imgBas.getHeight(),
                null);

        for (int y = 0; y < fondu; y++) {
            float alpha = y / (float) fondu;

            for (int x = 0; x < width; x++) {

                int color1 = x < imgHaut.getWidth() ? imgHaut.getRGB(x, yDebutFondu + y) : Color.TRANSLUCENT;
                int color2 = x < imgBas.getWidth() ? imgBas.getRGB(x, y) : Color.TRANSLUCENT;

                int a1 = (color1 >> 24) & 0xff;
                int r1 = (color1 >> 16) & 0xff;
                int g1 = (color1 >> 8) & 0xff;
                int b1 = color1 & 0xff;

                int a2 = (color2 >> 24) & 0xff;
                int r2 = (color2 >> 16) & 0xff;
                int g2 = (color2 >> 8) & 0xff;
                int b2 = color2 & 0xff;

                int a = (int) (a1 * (1 - alpha) + a2 * alpha);
                int r = (int) (r1 * (1 - alpha) + r2 * alpha);
                int g_ = (int) (g1 * (1 - alpha) + g2 * alpha);
                int b = (int) (b1 * (1 - alpha) + b2 * alpha);

                int argb = (a << 24) | (r << 16) | (g_ << 8) | b;

                result.setRGB(x, yDebutFondu + y, argb);
            }
        }

        g.dispose();
        return result;
    }
}