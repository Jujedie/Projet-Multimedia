package application.multimedia.iut.Metier;

public enum ActionTypeEnum {
	// Actions de type Colorisation
	POT_DE_PEINTURE(6),// false si distance == 0
	TEINTURE(5),
	CONTRASTE(2),
	LUMIERE(2),

	// Actions de type Transformation
	ROTATION(-1),
	REDIMENSIONNEMENT(-1), // true si changement de taille
	COUPER(-1),
	SYMETRIE_HORIZONTALE(-1), // Miroir horizontal
	SYMETRIE_VERTICALE(-1),   // Miroir vertical

	// Actions de type Ajout
	SUPERPOSITION(2),
	FUSION(-1),
	AJOUT_TEXTE_IMAGE(-1);

	private int    nbParametres;

	private ActionTypeEnum(int nbParametres) {
		this.nbParametres = nbParametres;
	}

	public int getNbParametres() {
		return nbParametres;
	}
}