/**
 * Classe gérant l'historique des actions (Undo/Redo).
 * Permet d'annuler et de rétablir les modifications apportées aux images.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Metier;

import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.util.ArrayList;

import application.multimedia.iut.Controleur;

import application.multimedia.iut.Controleur;

/**
 * Gère l'historique des actions pour le système Undo/Redo.
 * Permet d'annuler et de rétablir les modifications d'images.
 */
public class Journaux {
	public  final int MAX_HISTORIQUE = 20;

	private final ArrayList<BufferedImage> historique;
	private int                            indexImage;

	public Journaux(Controleur controleur) {
		this.historique = new ArrayList<BufferedImage>();
		this.indexImage = -1;
	}

	public ArrayList<BufferedImage> getHistorique() { return historique;	}
	public int getIndexImage() { return indexImage; }

	public void setIndexAction(int indexImage) { 
		if (indexImage < -1) {	this.indexImage = -1;	} 
		else {
			if (indexImage >= historique.size()) {
				this.indexImage = historique.size() - 1;
			}
			else {
				this.indexImage = indexImage; 
			}
		}
	}

	public void ajouterImage(BufferedImage image) {
		image = copierImage(image);
		if (indexImage < historique.size() - 1) {
			historique.subList(indexImage + 1, historique.size()).clear();
		}

		if ( historique.size() >= MAX_HISTORIQUE ) {
			historique.remove(0);
			if (indexImage > 0) {
				indexImage--;
			}
		}
		historique.add(image);
		indexImage++;
	}
	public void clearJournaux() {
		historique.clear();
		indexImage = -1;
	}

	public BufferedImage retourEnArriere() {
		if (indexImage > 0) {
			indexImage--;
			return copierImage(historique.get(indexImage));
		}
		return null;
	}

	public BufferedImage retourEnAvant() {
		if (indexImage < historique.size() - 1) {
			indexImage++;
			return copierImage(historique.get(indexImage));
		}
		return null;
	}

	private BufferedImage copierImage(BufferedImage image) {
		BufferedImage copie = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				copie.setRGB(x, y, image.getRGB(x, y));
			}
		}

		return copie;
	}
}
