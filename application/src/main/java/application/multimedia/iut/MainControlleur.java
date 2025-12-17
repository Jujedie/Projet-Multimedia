/**
 * Classe principale pour lancer l'application de retouche d'images.
 * Point d'entrée de l'application.
 * Contient le contrôleur central qui fait le lien entre le modèle (Métier) et la vue (Vue).
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */

package application.multimedia.iut;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.SwingUtilities;

import application.multimedia.iut.Metier.ActionHistorique;
import application.multimedia.iut.Metier.GestionnaireOutils;
import application.multimedia.iut.Metier.Journaux;
import application.multimedia.iut.Metier.image.CoucheImage;
import application.multimedia.iut.Metier.image.ImageManagerMetier;
import application.multimedia.iut.Metier.image.PileCouches;
import application.multimedia.iut.Metier.image.RenduToile;
import application.multimedia.iut.Metier.image.SessionPlacement;
import application.multimedia.iut.Metier.outils.OutilDessin;
import application.multimedia.iut.Vue.PaintFrame;
import application.multimedia.iut.Vue.utils.ImageDialogs.LoadChoice;

/**
 * Point d'entrée de l'application de retouche d'images.
 * Contient le contrôleur central et initialise l'interface graphique Swing.
 */
public class MainControlleur {
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
		// ========== Journaux ==========
		private Journaux historiqueModification;

		// ========== MODÈLE - Gestion des images ==========
		private final PileCouches pileCouches;
		private final SessionPlacement sessionPlacement;
		private final RenduToile renduToile;
		private final ImageManagerMetier imageManagerMetier;
		
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
			this.imageManagerMetier = new ImageManagerMetier(pileCouches, sessionPlacement, renduToile);
			
			// Initialisation du gestionnaire d'outils
			this.gestionnaireOutils = new GestionnaireOutils();
		}

		public void updateJournal(BufferedImage image){ this.historiqueModification = new Journaux(image);}
		
		// ========================================
		// ACCÈS AU MODÈLE - Gestion des images
		// ========================================
		
		public PileCouches getPileCouches() {
			return pileCouches;
		}
		
		public boolean pileCouchesEstVide() {
			return pileCouches.estVide();
		}
		
		
		public SessionPlacement getSessionPlacement() {
			return sessionPlacement;
		}

		
		public RenduToile getRenduToile() {
			return renduToile;
		}

		public void peindre(Graphics g) {
			renduToile.peindre(g, pileCouches, sessionPlacement);
		}

		
		public void suppressionTotale() {
			pileCouches.vider();
			sessionPlacement.annuler();
			gestionnaireOutils.terminerDessin();
		}


		public void definirImageCourante(BufferedImage image, Dimension tailleToile) {
			imageManagerMetier.definirImageCourante(image, tailleToile);
		}

		public void demarrerPlacement(BufferedImage img, Dimension tailleToile) {
			imageManagerMetier.demarrerPlacement(img, tailleToile);
		}

		public boolean sessionPlacementValide() {
			return imageManagerMetier.validerPlacement();
		}

		public void zoomer(double facteur) {
			imageManagerMetier.zoomer(facteur);
		}

		public void reinitialiserZoom() {
			imageManagerMetier.reinitialiserZoom();
		}

		public BufferedImage obtenirImageCourante() {
			return imageManagerMetier.obtenirImageCourante();
		}

		public void ajouterImageCommeNouvelleCouche(BufferedImage image, Dimension tailleToile) {
			imageManagerMetier.ajouterImageCommeNouvelleCouche(image, tailleToile);
		}
		
		public void ajouterImageAvecChoix(BufferedImage image, LoadChoice choix, Dimension tailleToile) {
			imageManagerMetier.ajouterImageAvecChoix(image, choix, tailleToile);
		}

		public CoucheImage coucheAuPoint(Point p) {
			return imageManagerMetier.coucheAuPoint(p);
		}

		public void creerImageVide(int largeur, int hauteur, Dimension tailleToile) {
			imageManagerMetier.creerImageVide(largeur, hauteur, tailleToile);
		}
		
		public void enregistrerFichier(File fichier) throws IOException {
			imageManagerMetier.enregistrerFichier(fichier);
		}

		public BufferedImage ouvrirFichier(File fichier, LoadChoice choix, Dimension tailleToile) throws IOException {
			imageManagerMetier.ouvrirFichier(fichier, choix, tailleToile);
			return obtenirImageCourante();
		}

		public boolean imageInitialePresente() {
			return imageManagerMetier.imageInitialePresente();
		}

		// ========================================
		// DÉLÉGATION - Gestion des outils
		// ========================================
		
		public void commencerDessin(BufferedImage image, int x, int y) {
			if (this.historiqueModification != null){ 
				gestionnaireOutils.commencerDessin(image, x, y); 
			}
		}
		
		public void continuerDessin(BufferedImage image, int x, int y) {
			if (this.historiqueModification != null){ 
				gestionnaireOutils.continuerDessin(image, x, y); 
			}
		}
		
		public void terminerDessin() {
			if (this.historiqueModification != null){  
				gestionnaireOutils.terminerDessin(); 
			}
		}
		
		public void dessinerTexte(BufferedImage image, String texte, int x, int y) {
			if (this.historiqueModification != null){ 
				gestionnaireOutils.dessinerTexte(image, texte, x, y);
				var actionHistorique = new ActionHistorique(null, new Object[]{texte,x,y});
				this.historiqueModification.ajouterActionHistorique(actionHistorique);
			}
		}
		
		public void setOutilActif(OutilDessin outil) {
			if (this.historiqueModification != null){ 
				gestionnaireOutils.setOutilActif(outil);
			}
		}
		
		public OutilDessin getOutilActif() {
			return gestionnaireOutils.getOutilActif();
		}
		
		// ========================================
		// DÉLÉGATION - Gestion de la couleur
		// ========================================
		
		public void definirCouleurActive(Color couleur) {
			if (this.historiqueModification != null){ 
				gestionnaireOutils.definirCouleurActive(couleur);
			}
		}
		
		public Color getCouleurActive() {
			return gestionnaireOutils.getCouleurActive();
		}
		
		// ========================================
		// DÉLÉGATION - Configuration des outils
		// ========================================
		
		public void setEpaisseurPinceau(int epaisseur) {
			if (this.historiqueModification != null){ 
				gestionnaireOutils.setEpaisseurPinceau(epaisseur);
			}
		}
		
		public int getEpaisseurPinceau() {
			return gestionnaireOutils.getEpaisseurPinceau();
		}
		
		public void setTailleGomme(int taille) {
			if (this.historiqueModification != null){ 
				gestionnaireOutils.setTailleGomme(taille);
			}
		}
		
		public int getTailleGomme() {
			return gestionnaireOutils.getTailleGomme();
		}
		
		public void setPoliceTexte(Font police) {
			if (this.historiqueModification != null){ 
				gestionnaireOutils.setPoliceTexte(police);
			}
		}
		
		public Font getPoliceTexte() {
			return gestionnaireOutils.getPoliceTexte();
		}
		
		// ========================================
		// DÉLÉGATION - Gestion des écouteurs
		// ========================================
		
		public void ajouterEcouteurCouleur(GestionnaireOutils.EcouteurCouleur ecouteur) {
			if (this.historiqueModification != null){ 
				gestionnaireOutils.ajouterEcouteurCouleur(ecouteur);
			}
		}
		
		public boolean estEnDessin() {
			return gestionnaireOutils.estEnDessin();
		}
	}
}