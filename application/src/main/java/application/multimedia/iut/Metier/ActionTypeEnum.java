package application.multimedia.iut.Metier;

/**
 * Enumération des types d'actions pouvant être enregistrées dans l'historique.
 */
public enum ActionTypeEnum {
	PEINDRE(1, false),
	SUPPRESSION_TOTALE(0, false),
	DEBUT_PLACEMENT(2, false),
	SESSION_PLACEMENT_VALIDE(0, true),
	;

	private int nbParametres;
	private boolean autoRetourArriere;

	/**
	 * Constructeur de l'énumération.
	 * @param nbParametres Le nombre de paramètres attendus pour cette action.
	 * @param autoRetourArriere Indique si l'action peut être automatiquement annulée.
	 */
	private ActionTypeEnum(int nbParametres, boolean autoRetourArriere) {
		this.nbParametres = nbParametres;
		this.autoRetourArriere = autoRetourArriere;
	}

	public int getNbParametres() {
		return nbParametres;
	}

	public boolean isAutoRetourArriere() {
		return autoRetourArriere;
	}
}