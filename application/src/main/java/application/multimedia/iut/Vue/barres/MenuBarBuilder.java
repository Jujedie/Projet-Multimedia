/**
 * Classe construisant la barre de menu de l'application.
 * Gère les menus Fichier, Édition, Image, etc.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Vue.barres;

import application.multimedia.iut.Vue.PaintPanel;
import javax.swing.*;

/**
 * Constructeur de la barre de menu de l'application.
 * Crée les menus Fichier, Édition, Image avec leurs actions.
 */
public class MenuBarBuilder {

	private PaintPanel panneau;

	/**
	 * Constructeur du constructeur de barre de menu.
	 *
	 * @param panneau Le panneau de peinture associé.
	 */
	public MenuBarBuilder(PaintPanel panneau) {
		this.panneau = panneau;
	}

	/**
	 * Crée et assemble la barre de menu complète.
	 * Contient les menus Fichier, Édition, Image et Filtres.
	 *
	 * @return La barre de menu construite.
	 */
	public JMenuBar creerMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		menuBar.add(creerMenuFichier());
		menuBar.add(creerMenuEdition());
		menuBar.add(creerMenuImage());
		menuBar.add(creerMenuFiltres());

		return menuBar;
	}

	/**
	 * Crée le menu Fichier avec les opérations de gestion de fichiers.
	 * Contient : Nouveau, Ouvrir, Enregistrer, Enregistrer sous, Quitter.
	 *
	 * @return Le menu Fichier.
	 */
	private JMenu creerMenuFichier() {
		JMenu fichierMenu = new JMenu("Fichier");

		JMenuItem nouveauItem = new JMenuItem("Nouveau");
		JMenuItem ouvrirItem = new JMenuItem("Ouvrir");
		JMenuItem enregistrerItem = new JMenuItem("Enregistrer");
		JMenuItem enregistrerSousItem = new JMenuItem("Enregistrer sous...");
		JMenuItem quitterItem = new JMenuItem("Quitter");

		ouvrirItem.addActionListener(e -> panneau.ouvrirFichier());
		enregistrerItem.addActionListener(e -> panneau.enregistrerFichier(false));
		enregistrerSousItem.addActionListener(e -> panneau.enregistrerFichier(true));
		quitterItem.addActionListener(e -> System.exit(0));

		fichierMenu.add(nouveauItem);
		fichierMenu.add(ouvrirItem);
		fichierMenu.add(enregistrerItem);
		fichierMenu.add(enregistrerSousItem);
		fichierMenu.addSeparator();
		fichierMenu.add(quitterItem);

		return fichierMenu;
	}

	/**
	 * Crée le menu Édition avec les opérations d'édition.
	 * Contient : Annuler, Refaire, Texte avec image, Effacer tout.
	 *
	 * @return Le menu Édition.
	 */
	private JMenu creerMenuEdition() {
		JMenu editionMenu = new JMenu("Édition");

		JMenuItem annulerItem = new JMenuItem("Annuler");
		JMenuItem refaireItem = new JMenuItem("Refaire");
		JMenuItem effacerItem = new JMenuItem("Effacer tout");
		JMenuItem texteImageItem = new JMenuItem("Texte avec image...");
		
		texteImageItem.addActionListener(e -> panneau.ouvrirEditeurTexteImage());

		editionMenu.add(annulerItem);
		editionMenu.add(refaireItem);
		editionMenu.addSeparator();
		editionMenu.add(texteImageItem);
		editionMenu.addSeparator();
		editionMenu.add(effacerItem);

		return editionMenu;
	}

	/**
	 * Crée le menu Image avec les transformations d'images.
	 * Contient : Fusionner, Retourner, Rotation, Redimensionner.
	 *
	 * @return Le menu Image.
	 */
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

	/**
	 * Crée le menu Filtres avec les effets applicables.
	 * Contient : Contraste.
	 *
	 * @return Le menu Filtres.
	 */
	private JMenu creerMenuFiltres() {
		JMenu filtresMenu = new JMenu("Filtres");

		JMenuItem contrasteItem = new JMenuItem("Contraste");
		filtresMenu.add(contrasteItem);

		return filtresMenu;
	}
}
