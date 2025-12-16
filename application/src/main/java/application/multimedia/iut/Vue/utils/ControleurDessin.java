/**
 * Contrôleur gérant les outils de dessin et leurs interactions.
 * Fait le lien entre la logique métier et l'interface utilisateur.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Vue.utils;

import application.multimedia.iut.Metier.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Contrôleur gérant les outils de dessin de l'application.
 * Coordonne les actions entre la vue et la logique métier.
 */
public class ControleurDessin {
	private OutilDessin outilActif;
	private Pinceau pinceau;
	private Gomme gomme;
	private Pipette pipette;
	private OutilTexte outilTexte;
	
	private Color couleurActive;
	private Point dernierPoint;
	private boolean dessinEnCours;
	
	private List<EcouteurCouleur> ecouteursCouleur;

	/**
	 * Constructeur du contrôleur de dessin.
	 * Initialise tous les outils avec leurs paramètres par défaut.
	 */
	public ControleurDessin() {
		this.outilActif = OutilDessin.SELECTION;
		this.pinceau = new Pinceau();
		this.gomme = new Gomme();
		this.pipette = new Pipette();
		this.outilTexte = new OutilTexte();
		this.couleurActive = Color.BLACK;
		this.dessinEnCours = false;
		this.ecouteursCouleur = new ArrayList<>();
	}

	/**
	 * Démarre une action de dessin à une position donnée.
	 *
	 * @param image L'image sur laquelle dessiner.
	 * @param x Coordonnée X.
	 * @param y Coordonnée Y.
	 */
	public void commencerDessin(BufferedImage image, int x, int y) {
		if (image == null) return;
		
		dernierPoint = new Point(x, y);
		dessinEnCours = true;
		
		switch (outilActif) {
			case PINCEAU:
				pinceau.setCouleur(couleurActive);
				pinceau.dessinerPoint(image, x, y);
				break;
			case GOMME:
				gomme.setCouleurEffacement(Color.WHITE);
				gomme.effacerPoint(image, x, y);
				break;
			case PIPETTE:
				Color couleur = pipette.preleverCouleur(image, x, y);
				if (couleur != null) {
					definirCouleurActive(couleur);
				}
				break;
			default:
				break;
		}
	}

	/**
	 * Continue une action de dessin vers une nouvelle position.
	 *
	 * @param image L'image sur laquelle dessiner.
	 * @param x Coordonnée X.
	 * @param y Coordonnée Y.
	 */
	public void continuerDessin(BufferedImage image, int x, int y) {
		if (image == null || !dessinEnCours || dernierPoint == null) return;
		
		switch (outilActif) {
			case PINCEAU:
				pinceau.setCouleur(couleurActive);
				pinceau.dessinerTrait(image, dernierPoint.x, dernierPoint.y, x, y);
				break;
			case GOMME:
				gomme.setCouleurEffacement(Color.WHITE);
				gomme.effacer(image, dernierPoint.x, dernierPoint.y, x, y);
				break;
			default:
				break;
		}
		
		dernierPoint = new Point(x, y);
	}

	/**
	 * Termine l'action de dessin en cours.
	 */
	public void terminerDessin() {
		dessinEnCours = false;
		dernierPoint = null;
	}

	/**
	 * Dessine du texte sur l'image à une position donnée.
	 *
	 * @param image L'image sur laquelle dessiner.
	 * @param texte Le texte à écrire.
	 * @param x Coordonnée X.
	 * @param y Coordonnée Y.
	 */
	public void dessinerTexte(BufferedImage image, String texte, int x, int y) {
		if (image == null || texte == null || texte.isEmpty()) return;
		outilTexte.dessinerTexte(image, texte, x, y);
	}

	/**
	 * Définit l'outil actif.
	 *
	 * @param outil Le nouvel outil à activer.
	 */
	public void setOutilActif(OutilDessin outil) {
		this.outilActif = outil;
		terminerDessin();
	}

	/**
	 * Obtient l'outil actuellement actif.
	 *
	 * @return L'outil actif.
	 */
	public OutilDessin getOutilActif() {
		return outilActif;
	}

	/**
	 * Définit la couleur active pour le dessin.
	 *
	 * @param couleur La nouvelle couleur.
	 */
	public void definirCouleurActive(Color couleur) {
		this.couleurActive = couleur;
		pinceau.setCouleur(couleur);
		outilTexte.setCouleur(couleur);
		notifierChangementCouleur(couleur);
	}

	/**
	 * Obtient la couleur actuellement active.
	 *
	 * @return La couleur active.
	 */
	public Color getCouleurActive() {
		return couleurActive;
	}

	/**
	 * Définit l'épaisseur du pinceau.
	 *
	 * @param epaisseur La nouvelle épaisseur en pixels.
	 */
	public void setEpaisseurPinceau(int epaisseur) {
		pinceau.setEpaisseur(epaisseur);
	}

	/**
	 * Obtient l'épaisseur actuelle du pinceau.
	 *
	 * @return L'épaisseur en pixels.
	 */
	public int getEpaisseurPinceau() {
		return pinceau.getEpaisseur();
	}

	/**
	 * Définit la taille de la gomme.
	 *
	 * @param taille La nouvelle taille en pixels.
	 */
	public void setTailleGomme(int taille) {
		gomme.setTaille(taille);
	}

	/**
	 * Obtient la taille actuelle de la gomme.
	 *
	 * @return La taille en pixels.
	 */
	public int getTailleGomme() {
		return gomme.getTaille();
	}

	/**
	 * Définit la police pour l'outil texte.
	 *
	 * @param police La nouvelle police.
	 */
	public void setPoliceTexte(Font police) {
		outilTexte.setPolice(police);
	}

	/**
	 * Obtient la police actuelle de l'outil texte.
	 *
	 * @return La police.
	 */
	public Font getPoliceTexte() {
		return outilTexte.getPolice();
	}

	/**
	 * Ajoute un écouteur pour les changements de couleur.
	 *
	 * @param ecouteur L'écouteur à ajouter.
	 */
	public void ajouterEcouteurCouleur(EcouteurCouleur ecouteur) {
		ecouteursCouleur.add(ecouteur);
	}

	/**
	 * Notifie tous les écouteurs d'un changement de couleur.
	 *
	 * @param nouvelleCouleur La nouvelle couleur.
	 */
	private void notifierChangementCouleur(Color nouvelleCouleur) {
		for (EcouteurCouleur ecouteur : ecouteursCouleur) {
			ecouteur.couleurChangee(nouvelleCouleur);
		}
	}

	/**
	 * Vérifie si un dessin est en cours.
	 *
	 * @return true si un dessin est en cours, false sinon.
	 */
	public boolean estEnDessin() {
		return dessinEnCours;
	}

	/**
	 * Interface pour écouter les changements de couleur.
	 */
	public interface EcouteurCouleur {
		void couleurChangee(Color nouvelleCouleur);
	}
}
