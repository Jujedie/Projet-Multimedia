package application.multimedia.iut.Metier;

import application.multimedia.iut.Metier.image.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class AjoutContenuTest {

	public static final String outputDir = "application/src/test/resources/testAjoutResultats/";


	public static void main(String[] args) {

		new File(outputDir).mkdirs();

		try {
			System.out.println("--- Démarrage des Tests AjoutContenu ---");

			System.out.println("\n--- Test fusion (AjoutContenu.fusion) ---");

			BufferedImage imgGauche = creerImageFactice(100, 100, Color.RED);
			BufferedImage imgDroite = creerImageFactice(120, 100, Color.BLUE);
			int largeurFondu = 30;

			BufferedImage imageFusionnee = AjoutContenu.fusion(imgGauche, imgDroite, largeurFondu);
			File fichierFusion = new File(outputDir + "fusion_test.png");
			ImageIO.write(imageFusionnee, "png", fichierFusion);
			System.out.println("Image fusionnée (" + imageFusionnee.getWidth() + "x" + imageFusionnee.getHeight()
					+ ") sauvegardée : " + fichierFusion.getAbsolutePath());

			System.out.println("\n--- Test construireComposite (Deux couches, sans limites) ---");

			CoucheImage couche1 = creerCouche(50, 50, Color.GREEN, 10, 10); // Fin à (60, 60)
			CoucheImage couche2 = creerCouche(70, 30, Color.MAGENTA, 50, 50); // Fin à (120, 80)

			List<CoucheImage> couches = new ArrayList<>();
			couches.add(couche1);
			couches.add(couche2);

			PileCouches pileSansBase = new PileCouches(couches, 1.0, null);

			BufferedImage imageCompositeSansBase = new AjoutContenu().construireComposite(pileSansBase);

			File fichierCompositeSansBase = new File(outputDir + "composite_sans_base_test.png");
			ImageIO.write(imageCompositeSansBase, "png", fichierCompositeSansBase);
			System.out
					.println("Image composite (Sans base) sauvegardée : " + fichierCompositeSansBase.getAbsolutePath());
			System.out.println("Dimensions : " + imageCompositeSansBase.getWidth() + "x"
					+ imageCompositeSansBase.getHeight() + " (Attendu: 110x70)");

			System.out.println("\n--- Test construireComposite (Deux couches, avec limites) ---");

			Rectangle base = new Rectangle(0, 0, 150, 150);

			PileCouches pileAvecBase = new PileCouches(couches, 1.0, base);

			BufferedImage imageCompositeAvecBase = new AjoutContenu().construireComposite(pileAvecBase);

			File fichierCompositeAvecBase = new File(outputDir + "composite_avec_base_test.png");
			ImageIO.write(imageCompositeAvecBase, "png", fichierCompositeAvecBase);
			System.out
					.println("Image composite (Avec base) sauvegardée : " + fichierCompositeAvecBase.getAbsolutePath());
			System.out.println("Dimensions : " + imageCompositeAvecBase.getWidth() + "x"
					+ imageCompositeAvecBase.getHeight() + " (Attendu: 150x150)");

		} catch (IOException e) {
			System.err.println("Une erreur d'E/S est survenue lors de la sauvegarde d'image. Vérifiez les chemins : "
					+ e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Une erreur inattendue est survenue : " + e.getMessage());
			e.printStackTrace();
		}
	}
}