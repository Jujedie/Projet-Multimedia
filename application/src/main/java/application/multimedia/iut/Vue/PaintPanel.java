/**
 * Classe représentant le panel de dessin principal.
 * Gère l'affichage et l'interaction avec les images.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Vue;

import application.multimedia.iut.Vue.barres.MenuBarBuilder;
import application.multimedia.iut.Vue.barres.ToolBarBuilder;
import application.multimedia.iut.Vue.dialogs.TexteImageEditorDialog;
import application.multimedia.iut.Vue.utils.ImageManager;
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
	private ImageManager gestionnaireImages;
	
	/**
	 * Constructeur du panneau de peinture.
	 * Initialise l'interface et précharge les icônes.
	 */
	public PaintPanel() {
		setLayout(new BorderLayout());
		
		new Thread(() -> LucideIconLoader.preloadCommonIcons()).start();
		JPanel panneauHaut = creerPanneauSuperieur();
		
		creerToile();
		
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
		
		ToolBarBuilder toolBarBuilder = new ToolBarBuilder(this);
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
		
		gestionnaireImages = new ImageManager(toile, this);
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
}
