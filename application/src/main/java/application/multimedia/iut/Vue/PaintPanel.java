package application.multimedia.iut.Vue;

import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * Panneau principal de l'application de retouche photo
 * Version refactorisée avec séparation des responsabilités
 */
public class PaintPanel extends JPanel {
	
	private JLabel canvas;
	private JScrollPane scrollPane;
	private ImageManager imageManager;
	
	public PaintPanel() {
		setLayout(new BorderLayout());
		
		// Pré-charger les icônes Lucide en arrière-plan
		new Thread(() -> LucideIconLoader.preloadCommonIcons()).start();

		// Créer le panneau supérieur avec menu et barre d'outils
		JPanel panneauHaut = creerPanneauSuperieur();
		
		// Créer le canvas central
		creerCanvas();
		
		// Assembler les composants
		add(panneauHaut, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	/**
	 * Crée le panneau supérieur avec menu et toolbar
	 */
	private JPanel creerPanneauSuperieur() {
		JPanel panneauHaut = new JPanel(new BorderLayout());
		
		// Créer la barre de menu
		MenuBarBuilder menuBuilder = new MenuBarBuilder(this);
		JMenuBar menuBar = menuBuilder.creerMenuBar();
		
		// Créer la barre d'outils
		ToolBarBuilder toolBarBuilder = new ToolBarBuilder(this);
		JToolBar barreOutils = toolBarBuilder.creerToolBar();
		
		panneauHaut.add(menuBar, BorderLayout.NORTH);
		panneauHaut.add(barreOutils, BorderLayout.SOUTH);
		
		return panneauHaut;
	}
	
	/**
	 * Crée le canvas pour afficher l'image
	 */
	private void creerCanvas() {
		canvas = new JLabel();
		canvas.setHorizontalAlignment(JLabel.CENTER);
		canvas.setVerticalAlignment(JLabel.CENTER);
		canvas.setBackground(Color.WHITE);
		canvas.setOpaque(true);
		canvas.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		
		scrollPane = new JScrollPane(canvas);
		scrollPane.setBackground(Color.DARK_GRAY);
		
		// Initialiser le gestionnaire d'images
		imageManager = new ImageManager(canvas, this);
	}
	
	// Méthodes publiques déléguées au ImageManager
	
	public void ouvrirFichier() {
		imageManager.ouvrirFichier();
	}
	
	public void enregistrerFichier(boolean nouveauFichier) {
		imageManager.enregistrerFichier(nouveauFichier);
	}
	
	public void zoom(double factor) {
		imageManager.zoom(factor);
	}
	
	public void resetZoom() {
		imageManager.resetZoom();
	}
	
	public BufferedImage getCurrentImage() {
		return imageManager.getCurrentImage();
	}
	
	public void setCurrentImage(BufferedImage image) {
		imageManager.setCurrentImage(image);
	}
}
