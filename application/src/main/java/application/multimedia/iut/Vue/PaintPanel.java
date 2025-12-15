package application.multimedia.iut.Vue;

import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;

public class PaintPanel extends JPanel {
	
	private JLabel canvas;
	private JScrollPane scrollPane;
	private ImageManager imageManager;
	
	public PaintPanel() {
		setLayout(new BorderLayout());
		
		new Thread(() -> LucideIconLoader.preloadCommonIcons()).start();
		JPanel panneauHaut = creerPanneauSuperieur();
		
		creerCanvas();
		
		add(panneauHaut, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
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
	
	private void creerCanvas() {
		canvas = new JLabel();
		canvas.setHorizontalAlignment(JLabel.CENTER);
		canvas.setVerticalAlignment(JLabel.CENTER);
		canvas.setBackground(Color.WHITE);
		canvas.setOpaque(true);
		canvas.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		
		scrollPane = new JScrollPane(canvas);
		scrollPane.setBackground(Color.DARK_GRAY);
		
		imageManager = new ImageManager(canvas, this);
	}
	
	
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
