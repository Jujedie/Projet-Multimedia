package application.multimedia.iut.Metier;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class JournauxTest {
	public static void main(String[] args) {
		System.out.println("Test Journaux");

		BufferedImage image;
		try {
			 image = ImageIO.read(new File("./application/src/main/resources/chat.png"));
		}
		catch (Exception e) {
			System.err.println("Erreur de lecture de l'image : " + e.getMessage());
			return;
		}
		
		Journaux journaux = new Journaux(image);
		System.out.println("Journaux initialisé avec une image de taille : " + image.getWidth() + "x" + image.getHeight());

		// Ajouter une action fictive
		ActionHistorique action = ActionHistorique.creerActionHistorique(
			ActionTypeEnum.LUMIERE,
			new Object[] { -10 }
		);

		journaux.ajouterActionHistorique(action);
		System.out.println("Action ajoutée. Nombre d'actions dans le journal : " + journaux.getJournauxHistorique().size());

		// Ajouter une autre action fictive
		ActionHistorique action2 = ActionHistorique.creerActionHistorique(
			ActionTypeEnum.CONTRASTE,
			new Object[] { 150 }
		);

		journaux.ajouterActionHistorique(action2);
		System.out.println("Deuxième action ajoutée. Nombre d'actions dans le journal : " + journaux.getJournauxHistorique().size());

		image = journaux.retourEnArriere();
		System.out.println("Retour en arrière effectué.");

		try {
			ImageIO.write(image, "png", new File("./application/src/test/resources/chat_journaux.png"));
		}
		catch (Exception e) {
			System.err.println("Erreur d'écriture de l'image : " + e.getMessage());
			return;
		}
	}
}
