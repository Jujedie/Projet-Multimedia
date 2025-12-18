/**
 * Classe gérant l'historique des actions (Undo/Redo).
 * Permet d'annuler et de rétablir les modifications apportées aux images.
 * * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Metier;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Gère l'historique des actions pour le système Undo/Redo.
 * Permet d'annuler et de rétablir les modifications d'images.
 */
public class Journaux {

	/** Liste ordonnée des actions effectuées sur l'image. */
	private ArrayList<ActionHistorique> journauxHistorique;

	/** État original de l'image avant toute modification. */
	private BufferedImage imageInitiale;

	/** Curseur pointant vers la dernière action appliquée dans la liste. */
	private int indexAction;

	/**
	 * Initialise un nouvel historique avec une image de base.
	 * * @param imageInitiale L'image source qui servira de point de départ.
	 */
	public Journaux(BufferedImage imageInitiale) {
		this.journauxHistorique = new ArrayList<ActionHistorique>();
		this.imageInitiale = imageInitiale;
		this.indexAction = -1;
	}

	/** @return La liste complète des actions enregistrées. */
	public ArrayList<ActionHistorique> getJournauxHistorique() {
		return journauxHistorique;
	}

	/** @return L'image source non modifiée. */
	public BufferedImage getImageInitiale() {
		return imageInitiale;
	}

	/** @return La position actuelle dans l'historique. */
	public int getIndexAction() {
		return indexAction;
	}

	/**
	 * Définit manuellement la position du curseur dans l'historique avec
	 * vérification des bornes.
	 * * @param indexAction Le nouvel index (doit être compris entre -1 et
	 * taille-1).
	 */
	public void setIndexAction(int indexAction) {
		if (indexAction < -1) {
			this.indexAction = -1;
		} else {
			if (indexAction >= journauxHistorique.size()) {
				this.indexAction = journauxHistorique.size() - 1;
			} else {
				this.indexAction = indexAction;
			}
		}
	}

	/**
	 * Enregistre une nouvelle action dans l'historique.
	 * <p>
	 * Si le curseur n'est pas à la fin de la liste (après des "Undo"),
	 * les actions futures sont supprimées pour créer une nouvelle branche.
	 * </p>
	 * * @param actionHistorique L'action à ajouter.
	 */
	public void ajouterActionHistorique(ActionHistorique actionHistorique) {
		// Supprimer les actions futures si on ajoute une nouvelle action
		while (journauxHistorique.size() > indexAction + 1) {
			journauxHistorique.remove(journauxHistorique.size() - 1);
		}
		journauxHistorique.add(actionHistorique);
		indexAction++;
	}

	/**
	 * Supprime l'action pointée par l'index actuel et décrémente le curseur.
	 */
	public void retirerActionHistorique() {
		if (indexAction >= 0 && indexAction < journauxHistorique.size()) {
			journauxHistorique.remove(indexAction);
			indexAction--;
		}
	}

	/**
	 * Réinitialise complètement l'historique des actions.
	 */
	public void clearJournaux() {
		journauxHistorique.clear();
		indexAction = -1;
	}

	/**
	 * Annule la dernière action effectuée.
	 * <p>
	 * Recrée une image à partir de l'original en rejouant toutes les actions
	 * jusqu'à l'avant-dernière.
	 * </p>
	 * * @return Une nouvelle {@link BufferedImage} représentant l'état précédent.
	 */
	public BufferedImage retourEnArriere() {
		BufferedImage imageTemporaire = copierImage(imageInitiale);

		for (int i = 0; i < indexAction; i++) {
			journauxHistorique.get(i).faireAction(imageTemporaire);
		}

		if (indexAction >= 0) {
			indexAction--;
		}

		return imageTemporaire;
	}

	/**
	 * Rétablit une action précédemment annulée.
	 * <p>
	 * Recrée une image à partir de l'original en rejouant les actions
	 * en incluant la suivante dans la pile.
	 * </p>
	 * * @return Une nouvelle {@link BufferedImage} représentant l'état rétabli.
	 */
	public BufferedImage retourEnAvant() {
		BufferedImage imageTemporaire = copierImage(imageInitiale);

		if (indexAction + 1 < journauxHistorique.size()) {
			indexAction++;
		}

		for (int i = 0; i <= indexAction; i++) {
			journauxHistorique.get(i).faireAction(imageTemporaire);
		}

		return imageTemporaire;
	}

	/**
	 * Crée une copie physique profonde (pixel par pixel) d'une image.
	 * * @param image L'image source à copier.
	 * 
	 * @return Une nouvelle instance de {@link BufferedImage} identique à la source.
	 */
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