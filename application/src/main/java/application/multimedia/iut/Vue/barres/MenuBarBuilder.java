/**
 * Classe construisant la barre de menu de l'application.
 * Gère les menus Fichier, Édition, Image, etc.
 *
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Vue.barres;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import application.multimedia.iut.Vue.PaintPanel;

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
	 * Contient les menus Fichier, Édition, Image, Outils et Filtres.
	 *
	 * @return La barre de menu construite.
	 */
	public JMenuBar creerMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		menuBar.add(creerMenuFichier());
		menuBar.add(creerMenuEdition());
		menuBar.add(creerMenuImage());
		menuBar.add(creerMenuOutils());
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
		JMenuItem texteImageItem = new JMenuItem("Texte avec image");
		JMenuItem effacerItem = new JMenuItem("Effacer tout");

		annulerItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, menuShortcut));
		refaireItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, menuShortcut));
		texteImageItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, menuShortcut));
		effacerItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, menuShortcut | KeyEvent.SHIFT_DOWN_MASK));

		annulerItem.setMnemonic(KeyEvent.VK_A);
		refaireItem.setMnemonic(KeyEvent.VK_R);
		texteImageItem.setMnemonic(KeyEvent.VK_T);
		effacerItem.setMnemonic(KeyEvent.VK_E);

		annulerItem.addActionListener(e -> panneau.annulerDerniereAction());
		refaireItem.addActionListener(e -> panneau.refaireDerniereAction());
		texteImageItem.addActionListener(e -> panneau.ouvrirDialogueTexteImage());
		effacerItem.addActionListener(e -> panneau.effacerTout());

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
		imageMenu.setMnemonic(KeyEvent.VK_I);

		JMenuItem fusionnerItem = new JMenuItem("Fusion Horizontale");
		JMenuItem fusionVerticale = new JMenuItem("Fusion Verticale");
		JMenuItem flipHItem = new JMenuItem("Retourner horizontalement");
		JMenuItem flipVItem = new JMenuItem("Retourner verticalement");
		JMenuItem rotationItem = new JMenuItem("Rotation");
		JMenuItem redimensionnerItem = new JMenuItem("Redimensionner");

		fusionnerItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, menuShortcut));
		fusionVerticale.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, menuShortcut | KeyEvent.SHIFT_DOWN_MASK));
		flipHItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, menuShortcut));
		flipVItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, menuShortcut));
		rotationItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, menuShortcut));
		redimensionnerItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, menuShortcut));

		fusionnerItem.setMnemonic(KeyEvent.VK_F);
		fusionVerticale.setMnemonic(KeyEvent.VK_V);
		flipHItem.setMnemonic(KeyEvent.VK_H);
		flipVItem.setMnemonic(KeyEvent.VK_J);
		rotationItem.setMnemonic(KeyEvent.VK_R);
		redimensionnerItem.setMnemonic(KeyEvent.VK_D);

		fusionnerItem.addActionListener(e -> panneau.fusionHorizontale());
		fusionVerticale.addActionListener(e -> panneau.fusionVerticale());
		flipHItem.addActionListener(e -> panneau.flipH());
		flipVItem.addActionListener(e -> panneau.flipV());
		rotationItem.addActionListener(e -> panneau.rotation());
		redimensionnerItem.addActionListener(e -> panneau.redimensionner());

		imageMenu.add(fusionnerItem);
		imageMenu.add(fusionVerticale);
		imageMenu.addSeparator();
		imageMenu.add(flipHItem);
		imageMenu.add(flipVItem);
		imageMenu.addSeparator();
		imageMenu.add(rotationItem);
		imageMenu.add(redimensionnerItem);
		return imageMenu;
	}

	/**
	 * Crée le menu Outils avec tous les outils de dessin disponibles.
	 * Contient : Sélection, Pinceau, Gomme, Pipette, Remplissage, Texte.
	 *
	 * @return Le menu Outils.
	 */
	private JMenu creerMenuOutils() {
		JMenu outilsMenu = new JMenu("Outils");
		outilsMenu.setMnemonic(KeyEvent.VK_O);

		JRadioButtonMenuItem selectionItem = new JRadioButtonMenuItem("Sélection");
		JRadioButtonMenuItem pinceauItem = new JRadioButtonMenuItem("Pinceau");
		JRadioButtonMenuItem gommeItem = new JRadioButtonMenuItem("Gomme");
		JRadioButtonMenuItem pipetteItem = new JRadioButtonMenuItem("Pipette");
		JRadioButtonMenuItem remplissageItem = new JRadioButtonMenuItem("Remplissage");
		JRadioButtonMenuItem texteItem = new JRadioButtonMenuItem("Texte");

		selectionItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, menuShortcut));
		pinceauItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, menuShortcut));
		gommeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, menuShortcut));
		pipetteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, menuShortcut));
		remplissageItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, menuShortcut));
		texteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_6, menuShortcut));

		selectionItem.setMnemonic(KeyEvent.VK_S);
		pinceauItem.setMnemonic(KeyEvent.VK_P);
		gommeItem.setMnemonic(KeyEvent.VK_G);
		pipetteItem.setMnemonic(KeyEvent.VK_I);
		remplissageItem.setMnemonic(KeyEvent.VK_R);
		texteItem.setMnemonic(KeyEvent.VK_T);

		ButtonGroup groupeOutils = new ButtonGroup();
		groupeOutils.add(selectionItem);
		groupeOutils.add(pinceauItem);
		groupeOutils.add(gommeItem);
		groupeOutils.add(pipetteItem);
		groupeOutils.add(remplissageItem);
		groupeOutils.add(texteItem);

		selectionItem.setSelected(true);

		selectionItem.addActionListener(e -> panneau.activerOutilDessin(application.multimedia.iut.Metier.outils.OutilDessin.SELECTION));
		pinceauItem.addActionListener(e -> panneau.activerOutilDessin(application.multimedia.iut.Metier.outils.OutilDessin.PINCEAU));
		gommeItem.addActionListener(e -> panneau.activerOutilDessin(application.multimedia.iut.Metier.outils.OutilDessin.GOMME));
		pipetteItem.addActionListener(e -> panneau.activerOutilDessin(application.multimedia.iut.Metier.outils.OutilDessin.PIPETTE));
		remplissageItem.addActionListener(e -> panneau.activerOutilDessin(application.multimedia.iut.Metier.outils.OutilDessin.REMPLISSAGE));
		texteItem.addActionListener(e -> panneau.activerOutilDessin(application.multimedia.iut.Metier.outils.OutilDessin.TEXTE));

		outilsMenu.add(selectionItem);
		outilsMenu.add(pinceauItem);
		outilsMenu.add(gommeItem);
		outilsMenu.add(pipetteItem);
		outilsMenu.add(remplissageItem);
		outilsMenu.add(texteItem);

		return outilsMenu;
	}

	/**
	 * Crée le menu Filtres avec les effets applicables.
	 * Contient : Contraste, Luminosité, Teinture.
	 *
	 * @return Le menu Filtres.
	 */
	private JMenu creerMenuFiltres() {
		JMenu filtresMenu = new JMenu("Filtres");
		filtresMenu.setMnemonic(KeyEvent.VK_L);

		JMenuItem contrasteItem = new JMenuItem("Contraste");
		JMenuItem luminositeItem = new JMenuItem("Luminosité");
		JMenuItem teintureItem = new JMenuItem("Teinture");

		contrasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, menuShortcut));
		luminositeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, menuShortcut));
		teintureItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, menuShortcut));

		contrasteItem.setMnemonic(KeyEvent.VK_C);
		luminositeItem.setMnemonic(KeyEvent.VK_L);
		teintureItem.setMnemonic(KeyEvent.VK_T);

		contrasteItem.addActionListener(e -> ouvrirDialogueContraste());
		luminositeItem.addActionListener(e -> ouvrirDialogueLuminosite());
		teintureItem.addActionListener(e -> ouvrirDialogueTeinture());

		filtresMenu.add(contrasteItem);
		filtresMenu.add(luminositeItem);
		filtresMenu.add(teintureItem);

		return filtresMenu;
	}

	/**
	 * Ouvre un dialogue pour ajuster le contraste.
	 */
	private void ouvrirDialogueContraste() {
		String input = JOptionPane.showInputDialog(panneau, "Niveau de contraste (-100 à +100):", "Contraste", JOptionPane.QUESTION_MESSAGE);
		if (input != null && !input.trim().isEmpty()) {
			try {
				int contraste = Integer.parseInt(input.trim());
				if (contraste >= -100 && contraste <= 100) {
					panneau.appliquerContraste(contraste);
				} else {
					JOptionPane.showMessageDialog(panneau, "La valeur doit être entre -100 et 100.", "Erreur", JOptionPane.ERROR_MESSAGE);
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(panneau, "Veuillez entrer un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Ouvre un dialogue pour ajuster la luminosité.
	 */
	private void ouvrirDialogueLuminosite() {
		String input = JOptionPane.showInputDialog(panneau, "Niveau de luminosité (-255 à +255):", "Luminosité", JOptionPane.QUESTION_MESSAGE);
		if (input != null && !input.trim().isEmpty()) {
			try {
				int luminosite = Integer.parseInt(input.trim());
				if (luminosite >= -255 && luminosite <= 255) {
					panneau.appliquerLuminosite(luminosite);
				} else {
					JOptionPane.showMessageDialog(panneau, "La valeur doit être entre -255 et 255.", "Erreur", JOptionPane.ERROR_MESSAGE);
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(panneau, "Veuillez entrer un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Ouvre un dialogue pour appliquer une teinture.
	 */
	private void ouvrirDialogueTeinture() {
		JOptionPane.showMessageDialog(panneau, "Fonctionnalité de teinture à implémenter.", "Info", JOptionPane.INFORMATION_MESSAGE);
	}
}
