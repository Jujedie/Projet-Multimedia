package application.multimedia.iut.Metier;

import java.awt.image.BufferedImage;

public class ActionHistorique {
	
	private ActionTypeEnum actionType;
	private Object[]       parametres;

	public ActionHistorique creerActionHistorique(ActionTypeEnum actionType, Object[] parametres) {
		if (parametres.length != actionType.getNbParametres() && actionType.getNbParametres() != -1) {
			throw new IllegalArgumentException("Le nombre de paramètres ne correspond pas à l'action.");
		}
		
		return new ActionHistorique(actionType, parametres);
	}

	public ActionHistorique(ActionTypeEnum actionType, Object[] parametres) {
		this.actionType = actionType;
		this.parametres = parametres;
	}

	public void faireAction(BufferedImage image) {
		switch (this.actionType) {
			case TEINTURE:
				// Implémentation de l'action TEINTURE
				break;
			case CONTRASTE:
				// Implémentation de l'action CONTRASTE
				break;
			case LUMIERE:
				
				break;
			case POT_DE_PEINTURE:
				
				break;

			case ROTATION:
				
				break;
			case REDIMENSIONNEMENT:
				
				break;
			case COUPER:
				
				break;
			case SYMETRIE_HORIZONTALE:	
				
				break;
			case SYMETRIE_VERTICALE:
				
				break;

			case SUPERPOSITION:
				
				break;
			case FUSION:
				
				break;
			case AJOUT_TEXTE_IMAGE:
				
				break;
			default:
				throw new UnsupportedOperationException("Action non prise en charge : " + this.actionType);
		}
	}
}
