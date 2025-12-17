/**
 * Classe principale pour lancer l'application de retouche d'images.
 * Point d'entrée de l'application.
 * Contient le contrôleur central qui fait le lien entre le modèle (Métier) et la vue (Vue).
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */

package application.multimedia.iut;

import application.multimedia.iut.Metier.GestionnaireOutils;
import application.multimedia.iut.Metier.image.PileCouches;
import application.multimedia.iut.Metier.image.RenduToile;
import application.multimedia.iut.Metier.image.SessionPlacement;
import application.multimedia.iut.Metier.outils.OutilDessin;
import application.multimedia.iut.Vue.PaintFrame;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

/**
 * Point d'entrée de l'application de retouche d'images.
 * Contient le contrôleur central et initialise l'interface graphique Swing.
 */
public class Main {
	/**
	 * Point d'entrée de l'application.
	 * Crée le contrôleur central qui gère le modèle,
	 * puis lance l'interface graphique dans le thread de l'EDT en lui passant le contrôleur.
	 *
	 * @param args Arguments de la ligne de commande (non utilisés).
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			// Création du contrôleur central qui fait le lien Modèle-Vue
			Controleur controleur = new Controleur();
			
			// Création de la vue en lui passant le contrôleur
			PaintFrame view = new PaintFrame(controleur);
			view.setVisible(true);
		});
	}
	
	/**
	 * Contrôleur central - Connecteur simple entre le modèle et la vue.
	 * Délègue la logique métier aux classes appropriées.
	 */
	public static class Controleur {
		
		// ========== MODÈLE - Gestion des images ==========
		private final PileCouches pileCouches;
		private final SessionPlacement sessionPlacement;
		private final RenduToile renduToile;
		
		// ========== MODÈLE - Gestion des outils ==========
		private final GestionnaireOutils gestionnaireOutils;
		
		/**
		 * Constructeur du contrôleur.
		 * Initialise les composants du modèle.
		 */
		public Controleur() {
			// Initialisation du modèle de gestion d'images
			this.pileCouches = new PileCouches();
			this.sessionPlacement = new SessionPlacement();
			this.renduToile = new RenduToile();
			
			// Initialisation du gestionnaire d'outils
			this.gestionnaireOutils = new GestionnaireOutils();
		}
		
		// ========================================
		// ACCÈS AU MODÈLE - Gestion des images
		// ========================================
		
		public PileCouches getPileCouches() {
			return pileCouches;
		}
		
		public SessionPlacement getSessionPlacement() {
			return sessionPlacement;
		}
		
		public RenduToile getRenduToile() {
			return renduToile;
		}
		
		public void suppressionTotale() {
			pileCouches.vider();
			sessionPlacement.annuler();
			gestionnaireOutils.terminerDessin();
		}
		
		// ========================================
		// DÉLÉGATION - Gestion des outils
		// ========================================
		
		public void commencerDessin(BufferedImage image, int x, int y) {
			gestionnaireOutils.commencerDessin(image, x, y);
		}
		
		public void continuerDessin(BufferedImage image, int x, int y) {
			gestionnaireOutils.continuerDessin(image, x, y);
		}
		
		public void terminerDessin() {
			gestionnaireOutils.terminerDessin();
		}
		
		public void dessinerTexte(BufferedImage image, String texte, int x, int y) {
			gestionnaireOutils.dessinerTexte(image, texte, x, y);
		}
		
		public void setOutilActif(OutilDessin outil) {
			gestionnaireOutils.setOutilActif(outil);
		}
		
		public OutilDessin getOutilActif() {
			return gestionnaireOutils.getOutilActif();
		}
		
		// ========================================
		// DÉLÉGATION - Gestion de la couleur
		// ========================================
		
		public void definirCouleurActive(Color couleur) {
			gestionnaireOutils.definirCouleurActive(couleur);
		}
		
		public Color getCouleurActive() {
			return gestionnaireOutils.getCouleurActive();
		}
		
		// ========================================
		// DÉLÉGATION - Configuration des outils
		// ========================================
		
		public void setEpaisseurPinceau(int epaisseur) {
			gestionnaireOutils.setEpaisseurPinceau(epaisseur);
		}
		
		public int getEpaisseurPinceau() {
			return gestionnaireOutils.getEpaisseurPinceau();
		}
		
		public void setTailleGomme(int taille) {
			gestionnaireOutils.setTailleGomme(taille);
		}
		
		public int getTailleGomme() {
			return gestionnaireOutils.getTailleGomme();
		}
		
		public void setPoliceTexte(Font police) {
			gestionnaireOutils.setPoliceTexte(police);
		}
		
		public Font getPoliceTexte() {
			return gestionnaireOutils.getPoliceTexte();
		}
		
		// ========================================
		// DÉLÉGATION - Gestion des écouteurs
		// ========================================
		
		public void ajouterEcouteurCouleur(GestionnaireOutils.EcouteurCouleur ecouteur) {
			gestionnaireOutils.ajouterEcouteurCouleur(ecouteur);
		}
		
		public boolean estEnDessin() {
			return gestionnaireOutils.estEnDessin();
		}
	}
}