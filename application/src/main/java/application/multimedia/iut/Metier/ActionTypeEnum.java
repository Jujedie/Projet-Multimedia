package application.multimedia.iut.Metier;

public enum ActionTypeEnum {
	PEINDRE(1),
	SUPPRESSION_TOTALE(0),
	DEBUT_PLACEMENT(2);

	private int nbParametres;

	private ActionTypeEnum(int nbParametres) {
		this.nbParametres = nbParametres;
	}

	public int getNbParametres() {
		return nbParametres;
	}
}