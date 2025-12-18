package application.multimedia.iut.Metier;

/**
 * Enumération des types d'actions pouvant être enregistrées dans l'historique.
 */
public enum ActionTypeEnum {
	PEINDRE(1, false),
	SUPPRESSION_TOTALE(0, false),
	DEBUT_PLACEMENT(2, false),
	SESSION_PLACEMENT_VALIDE(0, false),

	ZOOMER(1, false),
	REINITIALISER_ZOOM(0, false),

	AJOUTER_IMAGE_NOUV_COUCHE(2, false),
	AJOUTER_IMAGE_AVEC_CHOIX(3, false),

	DEBUT_DESSIN(3, true),
	CONTINUER_DESSIN(3, true),
	TERMINER_DESSIN(0, true),
	DESSINER_TEXTE(3, false),
	
	CHANGER_OUTIL(1, true),
	CHANGER_COULEUR(1, true),
	CHANGER_EPAISSEUR(1, true),
	CHANGER_GOMME(1, true),
	CHANGER_POLICE_TEXTE(1, true),
	AJOUTER_ECOUTEUR(1, true),
	
	APPLIQUER_TEINTE(4, false),
	APPLIQUER_CONTRASTE(1, false),
	APPLIQUER_LUMINOSITE(1, false);

	private int   nbParametres;
	private boolean autoRetour;

	/**
	 * Constructeur de l'énumération.
	 * @param nbParametres Le nombre de paramètres attendus pour cette action.
	 * @param autoRetour Indique si l'action peut être automatiquement annulée ou rétablie.
	 */
	private ActionTypeEnum(int nbParametres, boolean autoRetour) {
		this.nbParametres = nbParametres;
		this.autoRetour = autoRetour;
	}

	public int getNbParametres() {
		return nbParametres;
	}

	public boolean isAutoRetour() {
		return autoRetour;
	}
}