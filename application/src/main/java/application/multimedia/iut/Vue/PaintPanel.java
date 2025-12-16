package application.multimedia.iut.Vue;

import application.multimedia.iut.Vue.barres.MenuBarBuilder;
import application.multimedia.iut.Vue.barres.ToolBarBuilder;
import application.multimedia.iut.Vue.dialogs.TexteImageEditorDialog;
import application.multimedia.iut.Vue.utils.ImageManager;
import application.multimedia.iut.Vue.utils.LucideIconLoader;
import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;

public class PaintPanel extends JPanel {

	private JLabel toile;
	private JScrollPane panneauDeroulement;
	private ImageManager gestionnaireImages;
	
	public PaintPanel() {
		setLayout(new BorderLayout());
		
		new Thread(() -> LucideIconLoader.preloadCommonIcons()).start();
		JPanel panneauHaut = creerPanneauSuperieur();
		
		creerToile();
		
		add(panneauHaut, BorderLayout.NORTH);
		add(panneauDeroulement, BorderLayout.CENTER);
	}
	
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
	
	
	public void ouvrirFichier() {
		gestionnaireImages.ouvrirFichier();
	}
	
	public void enregistrerFichier(boolean nouveauFichier) {
		gestionnaireImages.enregistrerFichier(nouveauFichier);
	}
	
	public void zoomer(double facteur) {
		gestionnaireImages.zoomer(facteur);
	}
	
	public void reinitialiserZoom() {
		gestionnaireImages.reinitialiserZoom();
	}
	
	public BufferedImage obtenirImageCourante() {
		return gestionnaireImages.obtenirImageCourante();
	}
	
	public void definirImageCourante(BufferedImage image) {
		gestionnaireImages.definirImageCourante(image);
	}
	
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
