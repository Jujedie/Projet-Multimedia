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
				int red   = (int) parametres[0];
				int green = (int) parametres[1];
				int blue  = (int) parametres[2];
				int alpha = (int) parametres[3];
				Colorisation.teinter(image, red, green, blue,  alpha);
				break;
			case CONTRASTE:
				int contraste = (int) parametres[0];
				Colorisation.contraste(image, contraste);
				break;
			case LUMIERE:
				int luminosite = (int) parametres[0];
				Colorisation.luminosite(image, luminosite);
				break;
			case POT_DE_PEINTURE:
				int couleurDestination = (int) parametres[0];
				int distance		   = (int) parametres[1];
				boolean estContinue    = (boolean) parametres[2];
				int xOrigine		   = (int) parametres[3];
				int yOrigine		   = (int) parametres[4];
				Colorisation.potDePeinture(image, couleurDestination, distance, estContinue, xOrigine, yOrigine);
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
