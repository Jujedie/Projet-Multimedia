package application.multimedia.iut.Metier;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import application.multimedia.iut.Controleur;

/**
 * Représente une action enregistrée dans l'historique pour le système Undo/Redo.
 */
public class ActionHistorique {
	
	private ActionTypeEnum actionType;
	private Object[]       parametres;

	/**
	 * Usine pour créer une ActionHistorique avec validation des paramètres.
	 * @param actionType Le type d'action.
	 * @param parametres Les paramètres associés à l'action.
	 * @return Une nouvelle instance d'ActionHistorique.
	 */
	public static ActionHistorique creerActionHistorique(ActionTypeEnum actionType, Object[] parametres) {
		if (parametres.length != actionType.getNbParametres() && actionType.getNbParametres() != -1) {
			throw new IllegalArgumentException("Le nombre de paramètres ne correspond pas à l'action.");
		}
		
		return new ActionHistorique(actionType, parametres);
	}

	/**
	 * Constructeur privé pour forcer l'utilisation de l'usine.
	 * @param actionType Le type d'action.
	 * @param parametres Les paramètres associés à l'action.
	 */
	public ActionHistorique(ActionTypeEnum actionType, Object[] parametres) {
		this.actionType = actionType;
		this.parametres = parametres;
	}

	/**
	 * Exécute l'action en utilisant le contrôleur fourni.
	 * @param image L'image sur laquelle l'action est effectuée.
	 * @param controleur Le contrôleur central de l'application.
	 */
	public void faireAction(BufferedImage image, Controleur controleur) {
		switch (this.actionType) {
			case PEINDRE -> {
					Graphics g = (Graphics) this.parametres[0];
					controleur.peindre(g);
				}
			
			default -> throw new UnsupportedOperationException("Action non prise en charge : " + this.actionType);
		}
	}
}
