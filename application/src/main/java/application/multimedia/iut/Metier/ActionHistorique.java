package application.multimedia.iut.Metier;

public class ActionHistorique {
	
	private ActionTypeEnum actionType;
	private Object[]       parametres;

	public ActionHistorique(ActionTypeEnum actionType) {
		this.actionType = actionType;
		this.parametres = new Object[actionType.getNbParametres()];
	}
}
