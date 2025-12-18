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

import application.multimedia.iut.Metier.Colorisation;
import application.multimedia.iut.Metier.Format;
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
 * Contrôleur central - Connecteur simple entre le modèle et la vue.
 * Délègue la logique métier aux classes appropriées.
 */
public class Controleur {
	// ========== Journaux ==========
	private Journaux historiqueModification;

	// ========== MODÈLE - Gestion des images ==========
	private PileCouches pileCouches;
	private SessionPlacement sessionPlacement;
	private RenduToile renduToile;
	private ImageManagerMetier imageManagerMetier;
	
	// ========== MODÈLE - Gestion des outils ==========
	private GestionnaireOutils gestionnaireOutils;
	
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

	// ========================================
	// GESTION DU JOURNAL D'UNDO/REDO
	// ========================================
	
	public void retourEnArriere(){
		if (this.historiqueModification != null){
			BufferedImage image = this.historiqueModification.retourEnArriere();
			if (image != null) {
				imageManagerMetier.definirImageCourante(image, new Dimension(image.getWidth(), image.getHeight()));
			}
		}
	}

	public void retourEnAvant(){
		if (this.historiqueModification != null){
			BufferedImage image = this.historiqueModification.retourEnAvant();
			if (image != null) {
				imageManagerMetier.definirImageCourante(image, new Dimension(image.getWidth(), image.getHeight()));
			}
		}
	}

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

	public ImageManagerMetier getImageManagerMetier() {
		return imageManagerMetier;
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

		historiqueModification.ajouterImage(obtenirImageCourante());
	}

	public void demarrerPlacement(BufferedImage img, Dimension tailleToile) {
		imageManagerMetier.demarrerPlacement(img, tailleToile);

		historiqueModification.ajouterImage(obtenirImageCourante());
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

		historiqueModification.ajouterImage(obtenirImageCourante());
	}
	
	public void ajouterImageAvecChoix(BufferedImage image, LoadChoice choix, Dimension tailleToile) {
		imageManagerMetier.ajouterImageAvecChoix(image, choix, tailleToile);

		historiqueModification.ajouterImage(obtenirImageCourante());
	}

	public CoucheImage coucheAuPoint(Point p) {
		return imageManagerMetier.coucheAuPoint(p);
	}

	public void creerImageVide(int largeur, int hauteur, Dimension tailleToile) {
		imageManagerMetier.creerImageVide(largeur, hauteur, tailleToile);

		historiqueModification = new Journaux(this);
		historiqueModification.ajouterImage(obtenirImageCourante());
	}
	
	public void enregistrerFichier(File fichier) throws IOException {
		imageManagerMetier.enregistrerFichier(fichier);
	}

	public BufferedImage ouvrirFichier(File fichier, LoadChoice choix, Dimension tailleToile) throws IOException {
		imageManagerMetier.ouvrirFichier(fichier, choix, tailleToile);

		historiqueModification = new Journaux(this);
		historiqueModification.ajouterImage(obtenirImageCourante());

		return obtenirImageCourante();
	}

	public boolean isImageInitialeBlanchePresente() {
		return imageManagerMetier.isImageInitialeBlanchePresente();
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

		historiqueModification.ajouterImage(obtenirImageCourante());
	}
	
	public void dessinerTexte(BufferedImage image, String texte, int x, int y) {
		gestionnaireOutils.dessinerTexte(image, texte, x, y);

		historiqueModification.ajouterImage(obtenirImageCourante());
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
	
	// ========================================
	// DÉLÉGATION - Colorisation
	// ========================================
	
	/**
	 * Applique une teinte de couleur sur l'image courante.
	 * 
	 * @param red La composante rouge de la teinte (0-255).
	 * @param green La composante verte de la teinte (0-255).
	 * @param blue La composante bleue de la teinte (0-255).
	 * @param alpha L'intensité de la teinte (0-255).
	 */
	public void appliquerTeinte(int red, int green, int blue, int alpha) {
		BufferedImage image = imageManagerMetier.obtenirImageCourante();
		if (image != null) {
			Colorisation.teinter(image, red, green, blue, alpha);

			historiqueModification.ajouterImage(obtenirImageCourante());
		}
	}
	
	/**
	 * Ajuste le contraste de l'image courante.
	 * 
	 * @param contraste Le niveau de contraste (-100 à +100).
	 */
	public void appliquerContraste(int contraste) {
		BufferedImage image = imageManagerMetier.obtenirImageCourante();
		if (image != null) {
			Colorisation.contraste(image, contraste);

			historiqueModification.ajouterImage(obtenirImageCourante());
		}
	}
	
	/**
	 * Ajuste la luminosité de l'image courante.
	 * 
	 * @param luminosite Le niveau de luminosité (-255 à +255).
	 */
	public void appliquerLuminosite(int luminosite) {
		BufferedImage image = imageManagerMetier.obtenirImageCourante();
		if (image != null) {
			Colorisation.luminosite(image, luminosite);

			historiqueModification.ajouterImage(obtenirImageCourante());
		}
	}
	
	/**
	 * Applique l'outil pot de peinture sur l'image courante.
	 * 
	 * @param couleurDest La couleur de remplissage (RGB).
	 * @param distance La tolérance de couleur (0-441).
	 * @param estContinue true pour remplissage continu, false pour global.
	 * @param xOrig La coordonnée X du point de départ.
	 * @param yOrig La coordonnée Y du point de départ.
	 */
	public void appliquerPotDePeinture(int couleurDest, int distance, boolean estContinue, int xOrig, int yOrig) {
		BufferedImage image = imageManagerMetier.obtenirImageCourante();
		if (image != null) {
			Colorisation.potDePeinture(image, couleurDest, distance, estContinue, xOrig, yOrig);
		}
	}

	// ========================================
	// DÉLÉGATION - Gestion du format
	// ========================================
	
	/**
	 * Applique un retournement horizontal à l'image.
	 * 
	 * @param image L'image à retourner.
	 * @return L'image retournée.
	 */
	public BufferedImage flipH(BufferedImage image) {
		if (image != null) {
			BufferedImage img = Format.symetrieHorizontale(image);
			return img;
			
		}
		return null;
	}
	
	/**
	 * Applique un retournement vertical à l'image.
	 * 
	 * @param image L'image à retourner.
	 * @return L'image retournée.
	 */
	public BufferedImage flipV(BufferedImage image) {
		if (image != null) {
			BufferedImage img = Format.symetrieVerticale(image);
			historiqueModification.ajouterImage(obtenirImageCourante());
			return img;
		}
		return null;
	}
	
	/**
	 * Applique une rotation à l'image.
	 * 
	 * @param image L'image à pivoter.
	 * @param angle L'angle de rotation en degrés.
	 * @return L'image pivotée.
	 */
	public BufferedImage rotation(BufferedImage image, double angle) {
		if (image != null) {
			BufferedImage img = Format.rotation(image, angle);
			return img;
		}
		return null;
	}
	
	/**
	 * Redimensionne l'image aux dimensions spécifiées.
	 * 
	 * @param image L'image à redimensionner.
	 * @param largeur La nouvelle largeur.
	 * @param hauteur La nouvelle hauteur.
	 * @return L'image redimensionnée.
	 */
	public BufferedImage redimensionner(BufferedImage image, int largeur, int hauteur) {
		if (image != null && largeur > 0 && hauteur > 0) {
			BufferedImage img = Format.redimensionner(image, largeur, hauteur);
			historiqueModification.ajouterImage(obtenirImageCourante());
			return img;
		}
		return null;
	}

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
}