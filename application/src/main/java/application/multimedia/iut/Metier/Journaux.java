/**
 * Classe gérant l'historique des actions (Undo/Redo).
 * Permet d'annuler et de rétablir les modifications apportées aux images.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Metier;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import application.multimedia.iut.Controleur;

/**
 * Gère l'historique des actions pour le système Undo/Redo.
 * Permet d'annuler et de rétablir les modifications d'images.
 */
public class Journaux {
	
	private ArrayList<ActionHistorique> journauxHistorique;
	private BufferedImage imageInitiale;
	private int           indexAction;
	private Controleur    controleur;

	public Journaux(BufferedImage imageInitiale, Controleur controleur) {
		this.journauxHistorique = new ArrayList<ActionHistorique>();
		this.imageInitiale     = imageInitiale;
		this.indexAction       = -1;
		this.controleur        = controleur;
	}

	public ArrayList<ActionHistorique> getJournauxHistorique() { return journauxHistorique;	}
	public BufferedImage getImageInitiale() { return imageInitiale;	}
	public int getIndexAction() { return indexAction; }

	public void setIndexAction(int indexAction) { 
		if (indexAction < -1) {	this.indexAction = -1;	} 
		else {
			if (indexAction >= journauxHistorique.size()) {
				this.indexAction = journauxHistorique.size() - 1;
			}
			else {
				this.indexAction = indexAction; 
			}
		}
	}

	public void ajouterActionHistorique(ActionHistorique actionHistorique) {
		// Supprimer les actions futures si on ajoute une nouvelle action
		while (journauxHistorique.size() > indexAction + 1) {
			journauxHistorique.remove(journauxHistorique.size() - 1);
		}
		journauxHistorique.add(actionHistorique);
		indexAction++;
	}
	public void retirerActionHistorique() {
		if (indexAction >= 0 && indexAction < journauxHistorique.size()) {
			journauxHistorique.remove(indexAction);
			indexAction--;
		}
	}
	public void clearJournaux() {
		journauxHistorique.clear();
		indexAction = -1;
	}

	public BufferedImage retourEnArriere() {
		controleur.ReinitialisationControlleur();
		controleur.updateJournal(imageInitiale);
		
		BufferedImage imageTemporaire = copierImage(imageInitiale);

		for (int i = 0; i < indexAction; i++) {
			journauxHistorique.get(i).faireAction(imageTemporaire, controleur);
		}

		if (indexAction >= 0) {
			indexAction--;
			while (indexAction >= 0 && journauxHistorique.get(indexAction + 1).getActionType().isAutoRetour()) {
				indexAction--;
			}
		}

		return imageTemporaire;
	}

	public BufferedImage retourEnAvant() {
		controleur.ReinitialisationControlleur();
		controleur.updateJournal(imageInitiale);

		BufferedImage imageTemporaire = copierImage(imageInitiale);

		if (indexAction + 1 < journauxHistorique.size()) {
			indexAction++;
			while (indexAction + 1 < journauxHistorique.size() && journauxHistorique.get(indexAction).getActionType().isAutoRetour()) {
				indexAction++;
			}
		}

		for (int i = 0; i <= indexAction; i++) {
			journauxHistorique.get(i).faireAction(imageTemporaire, controleur);
		}

		return imageTemporaire;
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
