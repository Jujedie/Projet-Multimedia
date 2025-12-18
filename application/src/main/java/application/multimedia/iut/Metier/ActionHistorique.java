package application.multimedia.iut.Metier;

import java.awt.image.BufferedImage;

public class ActionHistorique {

	private ActionTypeEnum actionType;
	private Object[] parametres;

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

	public void faireAction(BufferedImage image) {
		switch (this.actionType) {
			case TEINTURE -> {
				int red = (int) this.parametres[0];
				int green = (int) this.parametres[1];
				int blue = (int) this.parametres[2];
				int alpha = (int) this.parametres[3];
				Colorisation.teinter(image, red, green, blue, alpha);
			}
			case CONTRASTE -> {
				int contraste = (int) this.parametres[0];
				Colorisation.contraste(image, contraste);
			}
			case LUMIERE -> {
				int luminosite = (int) this.parametres[0];
				Colorisation.luminosite(image, luminosite);
			}
			case POT_DE_PEINTURE -> {
				int couleurDestination = (int) this.parametres[0];
				int distance = (int) this.parametres[1];
				boolean estContinue = (boolean) this.parametres[2];
				int xOrigine = (int) this.parametres[3];
				int yOrigine = (int) this.parametres[4];
				Colorisation.potDePeinture(image, couleurDestination, distance, estContinue, xOrigine, yOrigine);
			}

			case ROTATION -> {
				double angle = (double) this.parametres[0];
				Format.rotation(image, angle);
			}
			case REDIMENSIONNEMENT -> {
				int largeur = (int) this.parametres[0];
				int hauteur = (int) this.parametres[1];
				Format.redimensionner(image, largeur, hauteur);
			}
			case COUPER -> {
				int x1 = (int) this.parametres[0];
				int y1 = (int) this.parametres[1];
				int x2 = (int) this.parametres[2];
				int y2 = (int) this.parametres[3];
				Format.couper(image, x1, y1, x2, y2);
			}
			case SYMETRIE_HORIZONTALE -> Format.symetrieHorizontale(image);
			case SYMETRIE_VERTICALE -> Format.symetrieVerticale(image);

			case SUPERPOSITION -> {
				// À implémenter après pull request
			}
			case FUSION -> {
				// À implémenter après pull request
			}
			case AJOUT_TEXTE_IMAGE -> {
				// À implémenter après pull request
			}
			default -> throw new UnsupportedOperationException("Action non prise en charge : " + this.actionType);
		}
	}
}
