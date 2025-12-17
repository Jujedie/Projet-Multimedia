/**
 * Classe représentant le panel de dessin principal.
 * Gère l'affichage et l'interaction avec les images.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Vue;

import application.multimedia.iut.MainControlleur;
import application.multimedia.iut.Metier.GestionnaireOutils;
import application.multimedia.iut.Vue.barres.MenuBarBuilder;
import application.multimedia.iut.Vue.barres.ToolBarBuilder;
import application.multimedia.iut.Vue.dialogs.TexteImageEditorDialog;
import application.multimedia.iut.Vue.utils.ImageManagerVue;
import application.multimedia.iut.Vue.utils.LucideIconLoader;
import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * Panneau de dessin principal affichant et gérant les images.
 * Coordonne l'affichage du canevas et les interactions utilisateur.
 */
public class PaintPanel extends JPanel {

	private JLabel toile;
	private JScrollPane panneauDeroulement;
	private ImageManagerVue gestionnaireImages;
	private MainControlleur.Controleur controleur;
	
	/**
	 * Constructeur du panneau de peinture.
	 * Initialise l'interface et précharge les icônes.
	 * @param controleur Le contrôleur central de l'application.
	 */
	private ToolBarBuilder toolBarBuilder;

	public PaintPanel(MainControlleur.Controleur controleur) {
		this.controleur = controleur;
		setLayout(new BorderLayout());
		
		new Thread(() -> LucideIconLoader.preloadCommonIcons()).start();
		JPanel panneauHaut = creerPanneauSuperieur();
		
		creerToile();
		
		// Connecter l'écouteur de couleur maintenant que gestionnaireImages est initialisé
		if (toolBarBuilder != null) {
			toolBarBuilder.connecterEcouteurCouleur();
		}
		
		add(panneauHaut, BorderLayout.NORTH);
		add(panneauDeroulement, BorderLayout.CENTER);
	}
	
	/**
	 * Crée le panneau supérieur contenant la barre de menu et la barre d'outils.
	 *
	 * @return Le panneau supérieur assemblé.
	 */
	private JPanel creerPanneauSuperieur() {
		JPanel panneauHaut = new JPanel(new BorderLayout());
		
		MenuBarBuilder menuBuilder = new MenuBarBuilder(this);
		JMenuBar menuBar = menuBuilder.creerMenuBar();
		
		toolBarBuilder = new ToolBarBuilder(this);
		JToolBar barreOutils = toolBarBuilder.creerToolBar();
		
		panneauHaut.add(menuBar, BorderLayout.NORTH);
		panneauHaut.add(barreOutils, BorderLayout.SOUTH);
		
		return panneauHaut;
	}
	
	/**
	 * Crée et configure la zone de dessin (toile) avec son gestionnaire d'images.
	 */
	private void creerToile() {
		toile = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (gestionnaireImages != null) {
					gestionnaireImages.dessinerImage(g);
				}
			}
		};
		toile.setHorizontalAlignment(JLabel.CENTER);
		toile.setVerticalAlignment(JLabel.CENTER);
		toile.setBackground(Color.WHITE);
		toile.setOpaque(true);
		toile.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		
		panneauDeroulement = new JScrollPane(toile);
		panneauDeroulement.setBackground(Color.DARK_GRAY);
		
		gestionnaireImages = new ImageManagerVue(toile, this, controleur);
		gestionnaireImages.activerDeplacementImage();
	}
	
	
	/**
	 * Ouvre une boîte de dialogue pour charger une ou plusieurs images.
	 */
	public void ouvrirFichier() {
		gestionnaireImages.ouvrirFichier();
	}
	
	/**
	 * Enregistre l'image composite actuelle.
	 *
	 * @param nouveauFichier True pour "Enregistrer sous", false pour "Enregistrer".
	 */
	public void enregistrerFichier(boolean nouveauFichier) {
		gestionnaireImages.enregistrerFichier(nouveauFichier);
	}
	
	/**
	 * Applique un facteur de zoom sur l'image courante.
	 *
	 * @param facteur Le facteur multiplicatif (ex: 1.2 pour +20%, 0.8 pour -20%).
	 */
	public void zoomer(double facteur) {
		gestionnaireImages.zoomer(facteur);
	}
	
	/**
	 * Réinitialise le niveau de zoom à 100%.
	 */
	public void reinitialiserZoom() {
		gestionnaireImages.reinitialiserZoom();
	}
	
	/**
	 * Obtient l'image de la couche actuellement active.
	 *
	 * @return L'image courante, ou null si aucune.
	 */
	public BufferedImage obtenirImageCourante() {
		return gestionnaireImages.obtenirImageCourante();
	}
	
	/**
	 * Définit une nouvelle image comme couche active.
	 * Remplace toutes les couches existantes.
	 *
	 * @param image La nouvelle image à définir.
	 */
	public void definirImageCourante(BufferedImage image) {
		gestionnaireImages.definirImageCourante(image);
	}
	
	/**
	 * Ouvre la boîte de dialogue de création de texte avec image de fond.
	 * Ajoute l'image générée si l'utilisateur valide.
	 */
	public void ouvrirEditeurTexteImage() {
		Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
		TexteImageEditorDialog dialogue = new TexteImageEditorDialog(frame);
		dialogue.setVisible(true);
		
		if (dialogue.estValide()) {
			BufferedImage imageGeneree = dialogue.getImageGeneree();
			if (imageGeneree != null) {
				gestionnaireImages.ajouterImageAvecChoix(imageGeneree);
			}
		}
	}
	
	/**
	 * Active un outil de dessin.
	 *
	 * @param outil L'outil à activer.
	 */
	public void activerOutilDessin(application.multimedia.iut.Metier.outils.OutilDessin outil) {
		gestionnaireImages.activerOutil(outil);
	}
	
	/**
	 * Définit la couleur de dessin active.
	 *
	 * @param couleur La nouvelle couleur.
	 */
	public void definirCouleurDessin(java.awt.Color couleur) {
		gestionnaireImages.definirCouleur(couleur);
	}
	
	/**
	 * Supprime tout le contenu de l'affichage.
	 */
	public void supprimerTout() {
		controleur.suppressionTotale();
		gestionnaireImages.rafraichirAffichage();
	}
	
	/**
	 * Enregistre un écouteur pour les changements de couleur (pipette).
	 *
	 * @param ecouteur L'écouteur à enregistrer.
	 */
	public void enregistrerEcouteurCouleur(GestionnaireOutils.EcouteurCouleur ecouteur) {
		controleur.ajouterEcouteurCouleur(ecouteur);
	}
	
	// ========================================
	// MÉTHODES DE COLORISATION
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
		controleur.appliquerTeinte(red, green, blue, alpha);
		gestionnaireImages.rafraichirAffichage();
	}
	
	/**
	 * Ajuste le contraste de l'image courante.
	 * 
	 * @param contraste Le niveau de contraste (-100 à +100).
	 */
	public void appliquerContraste(int contraste) {
		controleur.appliquerContraste(contraste);
		gestionnaireImages.rafraichirAffichage();
	}
	
	/**
	 * Ajuste la luminosité de l'image courante.
	 * 
	 * @param luminosite Le niveau de luminosité (-255 à +255).
	 */
	public void appliquerLuminosite(int luminosite) {
		controleur.appliquerLuminosite(luminosite);
		gestionnaireImages.rafraichirAffichage();
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
		controleur.appliquerPotDePeinture(couleurDest, distance, estContinue, xOrig, yOrig);
		gestionnaireImages.rafraichirAffichage();
	}
}
