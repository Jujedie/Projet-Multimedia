package application.multimedia.iut.Metier;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class AjoutContenuTest {

	public final static File inputFileGauche = new File("application/src/main/resources/chat.png");
	public final static File inputFileDroite = new File("application/src/main/resources/chat.png");
	public static final String outputDir = "application/src/test/resources/testAjoutContenuResultats";

	public static void main(String[] args) {

		BufferedImage imageOriginaleGauche = null;
		BufferedImage imageOriginaleDroite = null;
		try {
			System.out.println("Lecture des images sources :\ngauche : " + inputFileGauche.getAbsolutePath()
					+ "\n droite : " + inputFileDroite.getAbsolutePath());
			imageOriginaleGauche = ImageIO.read(inputFileGauche);
			imageOriginaleDroite = ImageIO.read(inputFileDroite);

			if (imageOriginaleGauche == null) {
				System.err.println("Impossible de lire l'image source de gauche. Vérifiez le chemin et le fichier.");
				return;
			}
			if (imageOriginaleDroite == null) {
				System.err.println("Impossible de lire l'image source de droite. Vérifiez le chemin et le fichier.");
				return;
			}

			try {

				System.out.println("\n--- Test Fusion Horizontale---");
				BufferedImage resultatFusionH = AjoutContenu.fusionHorizontale(
						imageOriginaleGauche, imageOriginaleDroite, 250);
				File fichierSortieFusionH = new File(outputDir + "image_fusion_H_250px.png");
				ImageIO.write(resultatFusionH, "png", fichierSortieFusionH);
				System.out.println("Image fusionneé horizontalement sauvegardée : : " + fichierSortieFusionH.getAbsolutePath());

				System.out.println("\n--- Test Fusion Verticale---");
				BufferedImage resultatFusionV = AjoutContenu.fusionVerticale(
						imageOriginaleGauche, imageOriginaleDroite, 250);
				File fichierSortieFusionV = new File(outputDir + "image_fusion_V_250px.png");
				ImageIO.write(resultatFusionV, "png", fichierSortieFusionV);
				System.out.println("Image fusionneé verticalement sauvegardée : : " + fichierSortieFusionV.getAbsolutePath());

			} catch (IllegalArgumentException e) {
				System.err.println("Erreur paramètre fusion : " + e.getMessage());
			}

		} catch (IOException e) {
			System.err.println("Erreur d'E/S : " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}