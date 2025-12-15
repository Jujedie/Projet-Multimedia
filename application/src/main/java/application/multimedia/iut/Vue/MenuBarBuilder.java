package application.multimedia.iut.Vue;

import javax.swing.*;
import java.awt.event.ActionListener;

public class MenuBarBuilder {
	
	private PaintPanel panel;
	
	public MenuBarBuilder(PaintPanel panel) {
		this.panel = panel;
	}
	
	public JMenuBar creerMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		menuBar.add(creerMenuFichier());
		menuBar.add(creerMenuEdition());
		menuBar.add(creerMenuImage());
		menuBar.add(creerMenuFiltres());
		
		return menuBar;
	}
	
	private JMenu creerMenuFichier() {
		JMenu fichierMenu = new JMenu("Fichier");
		
		JMenuItem nouveauItem = new JMenuItem("Nouveau");
		JMenuItem ouvrirItem = new JMenuItem("Ouvrir");
		JMenuItem enregistrerItem = new JMenuItem("Enregistrer");
		JMenuItem enregistrerSousItem = new JMenuItem("Enregistrer sous...");
		JMenuItem quitterItem = new JMenuItem("Quitter");
		
		ouvrirItem.addActionListener(e -> panel.ouvrirFichier());
		enregistrerItem.addActionListener(e -> panel.enregistrerFichier(false));
		enregistrerSousItem.addActionListener(e -> panel.enregistrerFichier(true));
		quitterItem.addActionListener(e -> System.exit(0));
		
		fichierMenu.add(nouveauItem);
		fichierMenu.add(ouvrirItem);
		fichierMenu.add(enregistrerItem);
		fichierMenu.add(enregistrerSousItem);
		fichierMenu.addSeparator();
		fichierMenu.add(quitterItem);
		
		return fichierMenu;
	}
	
	private JMenu creerMenuEdition() {
		JMenu editionMenu = new JMenu("Édition");
		
		JMenuItem annulerItem = new JMenuItem("Annuler");
		JMenuItem refaireItem = new JMenuItem("Refaire");
		JMenuItem effacerItem = new JMenuItem("Effacer tout");
		
		editionMenu.add(annulerItem);
		editionMenu.add(refaireItem);
		editionMenu.addSeparator();
		editionMenu.add(effacerItem);
		
		return editionMenu;
	}
	
	private JMenu creerMenuImage() {
		JMenu imageMenu = new JMenu("Image");
		
		JMenuItem fusionnerItem = new JMenuItem("Fusionner");
		JMenuItem flipHItem = new JMenuItem("Retourner horizontalement");
		JMenuItem flipVItem = new JMenuItem("Retourner verticalement");
		JMenuItem rotationItem = new JMenuItem("Rotation");
		JMenuItem redimensionnerItem = new JMenuItem("Redimensionner");
		
		imageMenu.add(fusionnerItem);
		imageMenu.addSeparator();
		imageMenu.add(flipHItem);
		imageMenu.add(flipVItem);
		imageMenu.add(rotationItem);
		imageMenu.add(redimensionnerItem);
		
		return imageMenu;
	}
	
	private JMenu creerMenuFiltres() {
		JMenu filtresMenu = new JMenu("Filtres");
		
		JMenuItem contrasteItem = new JMenuItem("Contraste");
		filtresMenu.add(contrasteItem);
		
		return filtresMenu;
	}
}
