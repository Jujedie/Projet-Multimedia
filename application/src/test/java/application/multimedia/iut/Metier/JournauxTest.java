/**
 * Classe de test pour la gestion de l'historique et des journaux d'actions via
 * la classe {@link Journaux}.
 * <p>
 * Ce test vérifie le cycle de vie des modifications apportées à une image :
 * <ul>
 * <li>L'initialisation du journal avec une image source.</li>
 * <li>L'empilement d'actions historiques (Luminosité, Contraste).</li>
 * <li>Le mécanisme de retour en arrière (Undo) pour restaurer un état précédent
 * de l'image.</li>
 * </ul>
 * </p>
 * * @author VotreNom
 * 
 * @version 1.0
 */

package application.multimedia.iut.Metier;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class JournauxTest {

	/**
	 * Point d'entrée principal pour tester la pile d'historique.
	 * <p>
	 * Le scénario de test suit ces étapes :
	 * <ol>
	 * <li>Chargement de l'image "chat.png".</li>
	 * <li>Création d'une instance de {@link Journaux} pour suivre les
	 * modifications.</li>
	 * <li>Simulaton d'une modification de lumière (-10) et de contraste (150) via
	 * {@link ActionHistorique}.</li>
	 * <li>Exécution d'un {@code retourEnArriere()} pour annuler la dernière
	 * action.</li>
	 * <li>Sauvegarde de l'image résultante pour vérification visuelle.</li>
	 * </ol>
	 * </p>
	 * @param args Arguments de la ligne de commande (non utilisés).
	 */
	public static void main(String[] args) {
		System.out.println("Test Journaux");

		BufferedImage image;
		try {
			image = ImageIO.read(new File("./application/src/main/resources/chat.png"));
		} catch (Exception e) {
			System.err.println("Erreur de lecture de l'image : " + e.getMessage());
			return;
		}

		Journaux journaux = new Journaux(image);
		System.out.println(
				"Journaux initialisé avec une image de taille : " + image.getWidth() + "x" + image.getHeight());

		// Ajouter une action fictive
		ActionHistorique action = ActionHistorique.creerActionHistorique(
				ActionTypeEnum.LUMIERE,
				new Object[] { -10 });

		journaux.ajouterActionHistorique(action);
		System.out.println(
				"Action ajoutée. Nombre d'actions dans le journal : " + journaux.getJournauxHistorique().size());

		// Ajouter une autre action fictive
		ActionHistorique action2 = ActionHistorique.creerActionHistorique(
				ActionTypeEnum.CONTRASTE,
				new Object[] { 150 });

		journaux.ajouterActionHistorique(action2);
		System.out.println("Deuxième action ajoutée. Nombre d'actions dans le journal : "
				+ journaux.getJournauxHistorique().size());

		image = journaux.retourEnArriere();
		System.out.println("Retour en arrière effectué.");

		try {
			ImageIO.write(image, "png", new File("./application/src/test/resources/chat_journaux.png"));
		} catch (Exception e) {
			System.err.println("Erreur d'écriture de l'image : " + e.getMessage());
			return;
		}
	}
}
