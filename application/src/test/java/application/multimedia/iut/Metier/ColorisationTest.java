package application.multimedia.iut.Metier;

// Exemple simple montrant comment importer et utiliser une classe du répertoire `main`.
// Le package de la classe `Colorisation` est `application.multimedia.iut.Metier` —
// depuis ce fichier de test (même package) vous pouvez accéder directement à la classe
// sans écrire d'import explicite. Si vous étiez dans un autre package, ajoutez :
// import application.multimedia.iut.Metier.Colorisation;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ColorisationTest {

	// Exemple autonome : méthode main montrant l'utilisation d'une méthode statique
	// de la classe `Colorisation` située dans `src/main/java`.
	public static void main(String[] args) {

		// Affiche le répertoire courant (working directory)
		System.out.println("Répertoire courant = " + System.getProperty("user.dir"));

		BufferedImage image;
		try {
			 image = ImageIO.read(new File("./application/src/main/resources/chat.png"));
		}
		catch (Exception e) {
			System.err.println("Erreur de lecture de l'image : " + e.getMessage());
			return;
		}

		// Test Luminance
		Colorisation.luminosite(image, 50);

		try {
			ImageIO.write(image, "png", new File("./application/src/test/resources/chat_luminosite.png"));
		}
		catch (Exception e) {
			System.err.println("Erreur d'écriture de l'image : " + e.getMessage());
			return;
		}


		try {
			 image = ImageIO.read(new File("./application/src/main/resources/chat.png"));
		}
		catch (Exception e) {
			System.err.println("Erreur de lecture de l'image : " + e.getMessage());
			return;
		}


		// Test Contraste
		Colorisation.contraste(image , 50);

		try {
			ImageIO.write(image, "png", new File("./application/src/test/resources/chat_contraste.png"));
		}
		catch (Exception e) {
			System.err.println("Erreur d'écriture de l'image : " + e.getMessage());
			return;
		}


		try {
			 image = ImageIO.read(new File("./application/src/main/resources/chat.png"));
		}
		catch (Exception e) {
			System.err.println("Erreur de lecture de l'image : " + e.getMessage());
			return;
		}


		// Test Teinter
		Colorisation.teinter(image, 100, 0, 0, 75);

		try {
			ImageIO.write(image, "png", new File("./application/src/test/resources/chat_teinter.png"));
		}
		catch (Exception e) {
			System.err.println("Erreur d'écriture de l'image : " + e.getMessage());
			return;
		}

		try {
			 image = ImageIO.read(new File("./application/src/main/resources/chat.png"));
		}
		catch (Exception e) {
			System.err.println("Erreur de lecture de l'image : " + e.getMessage());
			return;
		}


		// Test Pot de peinture
		Colorisation.potDePeinture(image, (new java.awt.Color(255, 255, 255)).getRGB(), 50, false, 150, 5);

		try {
			ImageIO.write(image, "png", new File("./application/src/test/resources/chat_potDePeinture.png"));
		}
		catch (Exception e) {
			System.err.println("Erreur d'écriture de l'image : " + e.getMessage());
			return;
		}
	}
}
