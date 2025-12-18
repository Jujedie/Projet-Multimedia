package application.multimedia.iut.Metier;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import application.multimedia.iut.Controleur;

public class ActionHistorique {
	
	private ActionTypeEnum actionType;
	private Object[]       parametres;

	public static ActionHistorique creerActionHistorique(ActionTypeEnum actionType, Object[] parametres) {
		if (parametres.length != actionType.getNbParametres() && actionType.getNbParametres() != -1) {
			throw new IllegalArgumentException("Le nombre de paramètres ne correspond pas à l'action.");
		}
		
		return new ActionHistorique(actionType, parametres);
	}

	public ActionHistorique(ActionTypeEnum actionType, Object[] parametres) {
		this.actionType = actionType;
		this.parametres = parametres;
	}

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
