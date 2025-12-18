/**
 * Classe de test pour les transformations géométriques de la classe {@link Format}.
 * <p>
 * Cette classe valide les fonctionnalités de manipulation de structure d'image, notamment :
 * <ul>
 * <li>Le redimensionnement libre (largeur et hauteur).</li>
 * <li>Le découpage (cropping) d'une zone rectangulaire.</li>
 * <li>La rotation selon un angle arbitraire (ex: 180° et 147°).</li>
 * <li>Les symétries (miroirs) horizontales et verticales.</li>
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

public class FormatTest {

	/**
	 * Fichier image source utilisé pour l'ensemble des tests de format (tortue.png).
	 */
	public final static File inputFile = new File("application/src/main/resources/tortue.png");

	/**
	 * Préfixe du chemin de sortie pour les images générées par les tests.
	 */
	public static final String outputDir = "application/src/test/resources/testFormatResultats_";

	/**
	 * Point d'entrée principal exécutant la suite de tests de transformation.
	 * <p>
	 * La méthode charge l'image originale une seule fois, puis applique
	 * successivement
	 * les transformations de la classe {@link Format} avant d'exporter chaque
	 * résultat
	 * en format PNG.
	 * </p>
	 * 
	 * @param args Arguments de la ligne de commande (non utilisés).
	 */
	public static void main(String[] args) {

		BufferedImage imageOriginale = null;
		try {
			System.out.println("Lecture de l'image source : " + inputFile.getAbsolutePath());
			imageOriginale = ImageIO.read(inputFile);

			if (imageOriginale == null) {
				System.err.println("Impossible de lire l'image source. Vérifiez le chemin et le fichier.");
				return;
			}

			System.out.println("\n--- Test redimension ---");
			BufferedImage imageRedimensionnee = Format.redimensionner(
					imageOriginale, 150, 200);
			File fichierSortieRedim = new File(outputDir + "image_redim_test.png");
			ImageIO.write(imageRedimensionnee, "png", fichierSortieRedim);
			System.out.println("Image redimensionnée sauvegardée : " + fichierSortieRedim.getAbsolutePath());

			System.out.println("\n--- Test couper ---");
			BufferedImage imageCoupee = Format.couper(imageOriginale, 230, 150, 360, 275);
			File fichierCoupe = new File(outputDir + "image_coupee_test.png");
			ImageIO.write(imageCoupee, "png", fichierCoupe);
			System.out.println("Image coupée sauvegardée : " + fichierCoupe.getAbsolutePath());

			System.out.println("\n--- Test rotation ---");
			BufferedImage imagePivotee90 = Format.rotation(imageOriginale, 180.0);
			File fichierRotation90 = new File(outputDir + "image_rotation_90_test.png");
			ImageIO.write(imagePivotee90, "png", fichierRotation90);
			System.out.println("Image pivotée (90°) sauvegardée : " + fichierRotation90.getAbsolutePath());

			BufferedImage imagePivotee147 = Format.rotation(imageOriginale, 147.0);
			File fichierRotation147 = new File(outputDir + "image_rotation_147__test.png");
			ImageIO.write(imagePivotee147, "png", fichierRotation147);
			System.out.println("Image pivotée (, 147°) sauvegardée : " + fichierRotation147.getAbsolutePath());

			System.out.println("\n--- Test symétrie ---");
			BufferedImage imageSymetrieH = Format.symetrieHorizontale(imageOriginale);
			File fichierSymetrieH = new File(outputDir + "image_symetrie_horizontale_test.png");
			ImageIO.write(imageSymetrieH, "png", fichierSymetrieH);
			System.out.println("Image symétrisée horizontalement sauvegardée : " + fichierSymetrieH.getAbsolutePath());

			BufferedImage imageSymetrieV = Format.symetrieVerticale(imageOriginale);
			File fichierSymetrieV = new File(outputDir + "image_symetrie_verticale_test.png");
			ImageIO.write(imageSymetrieV, "png", fichierSymetrieV);
			System.out.println("Image symétrisée verticalement sauvegardée : " + fichierSymetrieV.getAbsolutePath());

		} catch (IOException e) {
			System.err.println(
					"Une erreur d'E/S est survenue lors de la lecture/sauvegarde de l'image. (Vérifiez le chemin du fichier source : "
							+ inputFile.getPath() + ") : " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Une erreur inattendue est survenue : " + e.getMessage());
		}
	}
}