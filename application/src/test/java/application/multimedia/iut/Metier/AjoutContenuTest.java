/**
 * Classe de test pour les fonctionnalités de manipulation d'images de la classe {@link AjoutContenu}.
 * <p>
 * Cette classe valide les algorithmes de fusion d'images en générant des fichiers de sortie
 * basés sur des ressources locales. Elle teste spécifiquement :
 * <ul>
 * <li>La fusion horizontale de deux images avec un décalage (offset).</li>
 * <li>La fusion verticale de deux images avec un décalage (offset).</li>
 * </ul>
 * </p>
 * * @author VotreNom
 * @version 1.0
 */

package application.multimedia.iut.Metier;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class AjoutContenuTest {
	/**
	 * Chemin vers l'image source utilisée pour la partie gauche (ou supérieure) de
	 * la fusion.
	 */
	public final static File inputFileGauche = new File("application/src/main/resources/chat.png");

	/**
	 * Chemin vers l'image source utilisée pour la partie droite (ou inférieure) de
	 * la fusion.
	 */
	public final static File inputFileDroite = new File("application/src/main/resources/chat.png");

	/**
	 * Répertoire de destination pour les images résultant des tests de fusion.
	 */
	public static final String outputDir = "application/src/test/resources/testAjoutContenuResultats";

	/**
	 * Point d'entrée principal pour l'exécution des tests unitaires manuels.
	 * <p>
	 * Le processus suit les étapes suivantes :
	 * <ol>
	 * <li>Chargement des images sources via {@link ImageIO}.</li>
	 * <li>Exécution de la fusion horizontale avec un décalage de 250 pixels.</li>
	 * <li>Exécution de la fusion verticale avec un décalage de 250 pixels.</li>
	 * <li>Sauvegarde des fichiers résultants dans le répertoire spécifié par
	 * {@code outputDir}.</li>
	 * </ol>
	 * </p>
	 */

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
				System.out.println(
						"Image fusionneé horizontalement sauvegardée : : " + fichierSortieFusionH.getAbsolutePath());

				System.out.println("\n--- Test Fusion Verticale---");
				BufferedImage resultatFusionV = AjoutContenu.fusionVerticale(
						imageOriginaleGauche, imageOriginaleDroite, 250);
				File fichierSortieFusionV = new File(outputDir + "image_fusion_V_250px.png");
				ImageIO.write(resultatFusionV, "png", fichierSortieFusionV);
				System.out.println(
						"Image fusionneé verticalement sauvegardée : : " + fichierSortieFusionV.getAbsolutePath());

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