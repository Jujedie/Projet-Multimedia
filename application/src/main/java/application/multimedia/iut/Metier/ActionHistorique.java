package application.multimedia.iut.Metier;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Font;

import application.multimedia.iut.Controleur;
import application.multimedia.iut.Metier.outils.OutilDessin;
import application.multimedia.iut.Vue.utils.ImageDialogs.LoadChoice;

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
		if (parametres.length != actionType.getNbParametres() && actionType.getNbParametres() <= -1) {
			throw new IllegalArgumentException("Le nombre de paramètres ne correspond pas à l'action.");
		}
		
		return new ActionHistorique(actionType, parametres);
	}

	public ActionTypeEnum getActionType() {
		return actionType;
	}

	/**
	 * Constructeur privé pour forcer l'utilisation de l'usine.
	 * @param actionType Le type d'action.
	 * @param parametres Les paramètres associés à l'action.
	 */
	private ActionHistorique(ActionTypeEnum actionType, Object[] parametres) {
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

			case SUPPRESSION_TOTALE -> {
					controleur.suppressionTotale();
				}

			case DEBUT_PLACEMENT -> {
					controleur.demarrerPlacement((BufferedImage) this.parametres[0], (Dimension) this.parametres[1]);
				}

			case SESSION_PLACEMENT_VALIDE -> {
				controleur.sessionPlacementValide();
				}

			case ZOOMER -> {
					controleur.zoomer((double) this.parametres[0]);
				}

			case REINITIALISER_ZOOM -> {
					controleur.reinitialiserZoom();
				}

			case AJOUTER_IMAGE_NOUV_COUCHE -> {
					controleur.ajouterImageCommeNouvelleCouche((BufferedImage) this.parametres[0], (Dimension) this.parametres[1]);
				}

			case AJOUTER_IMAGE_AVEC_CHOIX -> {
					controleur.ajouterImageAvecChoix((BufferedImage) this.parametres[0], (LoadChoice) this.parametres[1], (Dimension) this.parametres[2]);
				}

			case DEBUT_DESSIN -> {
					controleur.commencerDessin((BufferedImage) this.parametres[0], (int) this.parametres[1], (int) this.parametres[2]);
				}

			case CONTINUER_DESSIN -> {
					controleur.continuerDessin((BufferedImage) this.parametres[0], (int) this.parametres[1], (int) this.parametres[2]);
				}

			case TERMINER_DESSIN -> {
					controleur.terminerDessin();
				}

			case DESSINER_TEXTE -> {
					controleur.dessinerTexte((BufferedImage) this.parametres[0], (String) this.parametres[1], (int) this.parametres[2], (int) this.parametres[3]);
				}

			case CHANGER_OUTIL -> {
					controleur.setOutilActif((OutilDessin) this.parametres[0]);
				}

			case CHANGER_COULEUR -> {
				controleur.definirCouleurActive((Color) this.parametres[0]);
			}

			case CHANGER_EPAISSEUR -> {
					controleur.setEpaisseurPinceau((int) this.parametres[0]);
				}

			case CHANGER_GOMME -> {
					controleur.setTailleGomme((int) this.parametres[0]);
				}

			case CHANGER_POLICE_TEXTE -> {
					controleur.setPoliceTexte((Font) this.parametres[0]);
				}

			case AJOUTER_ECOUTEUR -> {
					controleur.ajouterEcouteurCouleur((GestionnaireOutils.EcouteurCouleur) this.parametres[0]);
				}

			case APPLIQUER_TEINTE -> {
					controleur.appliquerTeinte((int) this.parametres[0],(int) this.parametres[1],(int) this.parametres[2],(int) this.parametres[3]);
				}

			case APPLIQUER_CONTRASTE -> {
					controleur.appliquerContraste((int) this.parametres[0]);
				}

			case APPLIQUER_LUMINOSITE -> {
					controleur.appliquerLuminosite((int) this.parametres[0]);
				}
			
			default -> throw new UnsupportedOperationException("Action non prise en charge : " + this.actionType);
		}
	}
}
