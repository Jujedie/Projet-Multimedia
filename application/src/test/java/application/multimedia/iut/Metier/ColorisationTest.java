/**
 * Classe de test pour les fonctionnalités de traitement d'image de la classe {@link Colorisation}.
 * <p>
 * Cette classe permet de tester et de visualiser les effets des différents algorithmes
 * de colorisation en chargeant des images sources et en sauvegardant les résultats
 * après application de filtres (luminosité, contraste, teinte et pot de peinture).
 * </p>
 * * <p>Remarque : La classe recharge l'image originale entre chaque test pour éviter 
 * l'accumulation des effets sur une même instance.</p>
 * * @author Antoine LECHASLES, Martin RAVENEL, Julien OYER
 * @version 1.0
 */

package application.multimedia.iut.Metier;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ColorisationTest {

	/**
	 * Point d'entrée principal pour tester les méthodes de traitement de la classe
	 * {@link Colorisation}.
	 * <p>
	 * Le déroulement des tests est le suivant :
	 * <ol>
	 * <li><b>Luminosité :</b> Augmente la luminosité de l'image "chat.png" de
	 * 50.</li>
	 * <li><b>Contraste :</b> Modifie le contraste de l'image "chat.png" de 50.</li>
	 * <li><b>Teinte :</b> Applique une coloration (filtre rouge) à l'image
	 * "chat.png".</li>
	 * <li><b>Pot de Peinture :</b> Applique un remplissage de couleur bleue sur
	 * l'image "tortue.png"
	 * avec une tolérance spécifique.</li>
	 * </ol>
	 * </p>
	 */
	public static void main(String[] args) {

		// Affiche le répertoire courant (working directory)
		System.out.println("Répertoire courant = " + System.getProperty("user.dir"));

		BufferedImage image;
		try {
			image = ImageIO.read(new File("./application/src/main/resources/chat.png"));
		} catch (Exception e) {
			System.err.println("Erreur de lecture de l'image : " + e.getMessage());
			return;
		}

		// Test Luminance
		Colorisation.luminosite(image, 50);

		try {
			ImageIO.write(image, "png", new File("./application/src/test/resources/chat_luminosite.png"));
		} catch (Exception e) {
			System.err.println("Erreur d'écriture de l'image : " + e.getMessage());
			return;
		}

		try {
			image = ImageIO.read(new File("./application/src/main/resources/chat.png"));
		} catch (Exception e) {
			System.err.println("Erreur de lecture de l'image : " + e.getMessage());
			return;
		}

		// Test Contraste
		Colorisation.contraste(image, 50);

		try {
			ImageIO.write(image, "png", new File("./application/src/test/resources/chat_contraste.png"));
		} catch (Exception e) {
			System.err.println("Erreur d'écriture de l'image : " + e.getMessage());
			return;
		}

		try {
			image = ImageIO.read(new File("./application/src/main/resources/chat.png"));
		} catch (Exception e) {
			System.err.println("Erreur de lecture de l'image : " + e.getMessage());
			return;
		}

		// Test Teinter
		Colorisation.teinter(image, 100, 0, 0, 75);

		try {
			ImageIO.write(image, "png", new File("./application/src/test/resources/chat_teinter.png"));
		} catch (Exception e) {
			System.err.println("Erreur d'écriture de l'image : " + e.getMessage());
			return;
		}

		try {
			image = ImageIO.read(new File("./application/src/main/resources/tortue.png"));
		} catch (Exception e) {
			System.err.println("Erreur de lecture de l'image : " + e.getMessage());
			return;
		}

		// Test Pot de peinture
		Colorisation.potDePeinture(image, (new Color(0, 20, 120)).getRGB(), 100, true, 0, 0);

		try {
			ImageIO.write(image, "png", new File("./application/src/test/resources/tortue_potDePeinture.png"));
		} catch (Exception e) {
			System.err.println("Erreur d'écriture de l'image : " + e.getMessage());
			return;
		}
	}
}
