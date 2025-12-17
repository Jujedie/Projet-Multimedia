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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;

/**
 * Constructeur de la barre de menu de l'application.
 * Crée les menus Fichier, Édition, Image avec leurs actions.
 */
public class MenuBarBuilder {

	private PaintPanel panneau;
	private final int menuShortcut = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();

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
		fichierMenu.setMnemonic(KeyEvent.VK_F);

		JMenuItem nouveauItem = new JMenuItem("Nouveau");
		JMenuItem ouvrirItem = new JMenuItem("Ouvrir");
		JMenuItem enregistrerItem = new JMenuItem("Enregistrer");
		JMenuItem enregistrerSousItem = new JMenuItem("Enregistrer sous...");
		JMenuItem quitterItem = new JMenuItem("Quitter");

		nouveauItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, menuShortcut));
		ouvrirItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, menuShortcut));
		enregistrerItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, menuShortcut));
		enregistrerSousItem
				.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, menuShortcut | KeyEvent.SHIFT_DOWN_MASK));
		quitterItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, menuShortcut));

		nouveauItem.setMnemonic(KeyEvent.VK_N);
		ouvrirItem.setMnemonic(KeyEvent.VK_O);
		enregistrerItem.setMnemonic(KeyEvent.VK_E);
		enregistrerSousItem.setMnemonic(KeyEvent.VK_A);
		quitterItem.setMnemonic(KeyEvent.VK_Q);

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
		editionMenu.setMnemonic(KeyEvent.VK_E);

		JMenuItem annulerItem = new JMenuItem("Annuler");
		JMenuItem refaireItem = new JMenuItem("Refaire");
		JMenuItem effacerItem = new JMenuItem("Effacer tout");

		annulerItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, menuShortcut));
		refaireItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, menuShortcut));
		effacerItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, menuShortcut | KeyEvent.SHIFT_DOWN_MASK));

		annulerItem.setMnemonic(KeyEvent.VK_A);
		refaireItem.setMnemonic(KeyEvent.VK_R);
		effacerItem.setMnemonic(KeyEvent.VK_E);

		editionMenu.add(annulerItem);
		editionMenu.add(refaireItem);
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

		JMenuItem fusionnerItem = new JMenuItem("Fusion Horizontale");
		JMenuItem fusionVerticale = new JMenuItem("Fusion Verticale");
		JMenuItem flipHItem = new JMenuItem("Retourner horizontalement");
		JMenuItem flipVItem = new JMenuItem("Retourner verticalement");
		JMenuItem rotationItem = new JMenuItem("Rotation");
		JMenuItem redimensionnerItem = new JMenuItem("Redimensionner");

		fusionnerItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, menuShortcut));
		fusionVerticale.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, menuShortcut | KeyEvent.SHIFT_DOWN_MASK));
		flipHItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, menuShortcut));
		flipVItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, menuShortcut));
		rotationItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, menuShortcut));
		redimensionnerItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, menuShortcut));

		fusionnerItem.setMnemonic(KeyEvent.VK_F);
		flipHItem.setMnemonic(KeyEvent.VK_H);
		flipVItem.setMnemonic(KeyEvent.VK_V);
		rotationItem.setMnemonic(KeyEvent.VK_R);
		redimensionnerItem.setMnemonic(KeyEvent.VK_D);

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
