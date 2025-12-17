/**
 * Classe construisant la barre d'outils de l'application.
 * Contient les boutons d'accès rapide aux outils de dessin.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Vue.barres;

import application.multimedia.iut.Main;
import application.multimedia.iut.Vue.utils.LucideIconLoader;
import application.multimedia.iut.Vue.PaintPanel;
import javax.swing.*;
import java.awt.*;

/**
 * Constructeur de la barre d'outils de l'application.
 * Fournit les boutons d'accès rapide aux outils de dessin et transformations.
 */
public class ToolBarBuilder {

	private PaintPanel panneau;
	private Color couleurIcone = new Color(60, 60, 60);
	private int tailleIcone = 20;
	private JPanel couleurPrincipale;
	private Main.Controleur.EcouteurCouleur ecouteurCouleurPipette;

	/**
	 * Constructeur du constructeur de barre d'outils.
	 *
	 * @param panneau Le panneau de peinture associé.
	 */
	public ToolBarBuilder(PaintPanel panneau) {
		this.panneau = panneau;
	}
	
	/**
	 * Crée et assemble la barre d'outils complète.
	 * Contient tous les outils de dessin, fichiers, transformations, etc.
	 *
	 * @return La barre d'outils construite.
	 */
	public JToolBar creerToolBar() {
		JToolBar barreOutils = new JToolBar();
		barreOutils.setFloatable(false);
		barreOutils.setBackground(new Color(240, 240, 240));
		barreOutils.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		
		ajouterOutilsDessin(barreOutils);
		barreOutils.addSeparator();
		
		ajouterOutilsFichier(barreOutils);
		barreOutils.addSeparator();
		
		ajouterOutilsTransformation(barreOutils);
		barreOutils.addSeparator();
		
		ajouterOutilsFiltres(barreOutils);
		barreOutils.addSeparator();
		
		ajouterOutilsHistorique(barreOutils);
		barreOutils.addSeparator();
		
		ajouterOutilsZoom(barreOutils);
		barreOutils.addSeparator();
		
		ajouterSelecteurCouleurs(barreOutils);
		
		return barreOutils;
	}
	
	/**
	 * Ajoute les outils de dessin à la barre d'outils.
	 * Contient : Sélection, Pinceau, Gomme, Pipette, Remplissage, Texte.
	 *
	 * @param barre La barre d'outils à modifier.
	 */
	private void ajouterOutilsDessin(JToolBar barre) {
		ButtonGroup groupeOutils = new ButtonGroup();
		
		JToggleButton selectionBtn = creerBoutonToggle("square-dashed", "Sélection");
		JToggleButton pinceauBtn = creerBoutonToggle("pencil", "Pinceau");
		JToggleButton gommeBtn = creerBoutonToggle("eraser", "Gomme");
		JToggleButton pipetteBtn = creerBoutonToggle("pipette", "Pipette");
		JToggleButton remplissageBtn = creerBoutonToggle("paint-bucket", "Remplissage");
		JButton texteBtn = creerBouton("type", "Texte");
		JButton texteImageBtn = creerBouton("image", "Texte avec image");
		
		// Connecter les boutons aux outils
		selectionBtn.addActionListener(e -> panneau.activerOutilDessin(application.multimedia.iut.Metier.OutilDessin.SELECTION));
		pinceauBtn.addActionListener(e -> panneau.activerOutilDessin(application.multimedia.iut.Metier.OutilDessin.PINCEAU));
		gommeBtn.addActionListener(e -> panneau.activerOutilDessin(application.multimedia.iut.Metier.OutilDessin.GOMME));
		pipetteBtn.addActionListener(e -> panneau.activerOutilDessin(application.multimedia.iut.Metier.OutilDessin.PIPETTE));
		texteBtn.addActionListener(e -> panneau.activerOutilDessin(application.multimedia.iut.Metier.OutilDessin.TEXTE));
		texteImageBtn.addActionListener(e -> panneau.ouvrirEditeurTexteImage());
		
		selectionBtn.setSelected(true);
		
		groupeOutils.add(selectionBtn);
		groupeOutils.add(pinceauBtn);
		groupeOutils.add(gommeBtn);
		groupeOutils.add(pipetteBtn);
		groupeOutils.add(remplissageBtn);
		
		barre.add(selectionBtn);
		barre.add(pinceauBtn);
		barre.add(gommeBtn);
		barre.add(pipetteBtn);
		barre.add(remplissageBtn);
		barre.add(texteBtn);
		barre.add(texteImageBtn);
	}
	
	/**
	 * Ajoute les outils de gestion de fichiers à la barre d'outils.
	 * Contient : Sauvegarder, Ouvrir, Effacer tout.
	 *
	 * @param barre La barre d'outils à modifier.
	 */
	private void ajouterOutilsFichier(JToolBar barre) {
		JButton sauvegarderBtn = creerBouton("save", "Sauvegarder");
		JButton chargerBtn = creerBouton("folder-open", "Ouvrir");
		JButton effacerBtn = creerBouton("trash-2", "Effacer tout");

		chargerBtn.addActionListener(e -> panneau.ouvrirFichier());
		sauvegarderBtn.addActionListener(e -> panneau.enregistrerFichier(true));
		effacerBtn.addActionListener(e -> panneau.supprimerTout());
		
		barre.add(sauvegarderBtn);
		barre.add(chargerBtn);
		barre.add(effacerBtn);
	}
	
	/**
	 * Ajoute les outils de transformation d'images à la barre d'outils.
	 * Contient : Flip H/V, Rotation, Redimensionner, Fusionner.
	 *
	 * @param barre La barre d'outils à modifier.
	 */
	private void ajouterOutilsTransformation(JToolBar barre) {
		JButton flipHBtn = creerBouton("flip-horizontal", "Flip Horizontal");
		JButton flipVBtn = creerBouton("flip-vertical", "Flip Vertical");
		JButton rotationBtn = creerBouton("rotate-cw", "Rotation");
		JButton rediBtn = creerBouton("maximize", "Redimensionner");
		JButton mergerBtn = creerBouton("layers", "Fusionner");
		
		barre.add(flipHBtn);
		barre.add(flipVBtn);
		barre.add(rotationBtn);
		barre.add(rediBtn);
		barre.add(mergerBtn);
	}
	
	/**
	 * Ajoute les outils de filtres d'images à la barre d'outils.
	 * Contient : Contraste.
	 *
	 * @param barre La barre d'outils à modifier.
	 */
	private void ajouterOutilsFiltres(JToolBar barre) {
		JButton contrasteBtn = creerBouton("contrast", "Contraste");
		barre.add(contrasteBtn);
	}
	
	/**
	 * Ajoute les outils d'historique à la barre d'outils.
	 * Contient : Annuler, Refaire.
	 *
	 * @param barre La barre d'outils à modifier.
	 */
	private void ajouterOutilsHistorique(JToolBar barre) {
		JButton undoBtn = creerBouton("undo", "Annuler");
		JButton redoBtn = creerBouton("redo", "Refaire");
		
		barre.add(undoBtn);
		barre.add(redoBtn);
	}
	
	/**
	 * Ajoute les contrôles de zoom à la barre d'outils.
	 * Contient : Zoom+, Zoom-, Réinitialiser.
	 *
	 * @param barre La barre d'outils à modifier.
	 */
	private void ajouterOutilsZoom(JToolBar barre) {
		JButton zoomInBtn = creerBouton("zoom-in", "Zoom +");
		JButton zoomOutBtn = creerBouton("zoom-out", "Zoom -");
		JButton zoomResetBtn = creerBouton("maximize-2", "Zoom 100%");
		
		zoomInBtn.addActionListener(e -> panneau.zoomer(1.2));
		zoomOutBtn.addActionListener(e -> panneau.zoomer(0.8));
		zoomResetBtn.addActionListener(e -> panneau.reinitialiserZoom());
		
		barre.add(zoomInBtn);
		barre.add(zoomOutBtn);
		barre.add(zoomResetBtn);
	}
	
	/**
	 * Ajoute le sélecteur de couleurs à la barre d'outils.
	 * Affiche deux panneaux pour les couleurs principale et secondaire.
	 *
	 * @param barre La barre d'outils à modifier.
	 */
	private void ajouterSelecteurCouleurs(JToolBar barre) {
		JPanel miniCouleurPanel = new JPanel(new GridLayout(1, 2, 2, 0));
		miniCouleurPanel.setMaximumSize(new Dimension(50, 30));
		miniCouleurPanel.setPreferredSize(new Dimension(50, 30));
		
		couleurPrincipale = new JPanel();
		couleurPrincipale.setBackground(Color.BLACK);
		couleurPrincipale.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		couleurPrincipale.setToolTipText("Couleur principale");
		
		// Ajouter un listener pour changer la couleur en cliquant
		couleurPrincipale.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				Color nouvelleCouleur = JColorChooser.showDialog(panneau, "Choisir une couleur", couleurPrincipale.getBackground());
				if (nouvelleCouleur != null) {
					couleurPrincipale.setBackground(nouvelleCouleur);
					panneau.definirCouleurDessin(nouvelleCouleur);
				}
			}
		});
		
		miniCouleurPanel.add(couleurPrincipale);
		barre.add(miniCouleurPanel);
		
		// Créer l'écouteur de changement de couleur depuis la pipette
		// Il sera enregistré plus tard quand le gestionnaire d'images sera initialisé
		ecouteurCouleurPipette = couleur -> {
			couleurPrincipale.setBackground(couleur);
		};
	}
	
	/**
	 * Connecte l'écouteur de couleur au contrôleur de dessin.
	 * Doit être appelé après l'initialisation du gestionnaire d'images.
	 */
	public void connecterEcouteurCouleur() {
		if (ecouteurCouleurPipette != null) {
			panneau.enregistrerEcouteurCouleur(ecouteurCouleurPipette);
		}
	}
	
	/**
	 * Crée un bouton toggle (sélectionnable) avec une icône.
	 *
	 * @param nomIcone Le nom de l'icône Lucide.
	 * @param infobulle Le texte de l'infobulle.
	 * @return Le bouton toggle configuré.
	 */
	private JToggleButton creerBoutonToggle(String nomIcone, String infobulle) {
		JToggleButton btn = new JToggleButton(LucideIconLoader.loadIcon(nomIcone, tailleIcone, couleurIcone));
		configurerBouton(btn, infobulle);
		return btn;
	}

	/**
	 * Crée un bouton standard avec une icône.
	 *
	 * @param nomIcone Le nom de l'icône Lucide.
	 * @param infobulle Le texte de l'infobulle.
	 * @return Le bouton configuré.
	 */
	private JButton creerBouton(String nomIcone, String infobulle) {
		JButton btn = new JButton(LucideIconLoader.loadIcon(nomIcone, tailleIcone, couleurIcone));
		configurerBouton(btn, infobulle);
		return btn;
	}
	
	/**
	 * Configure le style et le comportement d'un bouton.
	 * Applique les dimensions, couleurs et effets de survol.
	 *
	 * @param btn Le bouton à configurer.
	 * @param infobulle Le texte de l'infobulle.
	 */
	private void configurerBouton(AbstractButton btn, String infobulle) {
		btn.setToolTipText(infobulle);
		btn.setPreferredSize(new Dimension(40, 40));
		btn.setFocusPainted(false);
		btn.setBackground(new Color(240, 240, 240));
		btn.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
			BorderFactory.createEmptyBorder(5, 5, 5, 5)
		));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		btn.addMouseListener(new java.awt.event.MouseAdapter() {
			Color originalColor = btn.getBackground();
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btn.setBackground(new Color(220, 220, 220));
			}
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btn.setBackground(originalColor);
			}
		});
	}
}
