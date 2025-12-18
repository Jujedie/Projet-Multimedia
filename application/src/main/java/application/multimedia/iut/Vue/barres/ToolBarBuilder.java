/**
 * Classe construisant la barre d'outils de l'application.
 * Contient les boutons d'accès rapide aux outils de dessin.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Vue.barres;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import application.multimedia.iut.Metier.GestionnaireOutils;
import application.multimedia.iut.Metier.outils.OutilDessin;
import application.multimedia.iut.Vue.PaintPanel;
import application.multimedia.iut.Vue.utils.LucideIconLoader;

/**
 * Composant personnalisé pour afficher une couleur.
 */
class PanneauCouleur extends JComponent {
	private Color couleur;
	
	public PanneauCouleur(Color couleurInitiale) {
		this.couleur = couleurInitiale;
		setOpaque(true);
		setPreferredSize(new Dimension(40, 40));
		setMinimumSize(new Dimension(40, 40));
		setMaximumSize(new Dimension(40, 40));
	}
	
	public void setCouleur(Color nouvelleCouleur) {
		System.out.println("PanneauCouleur.setCouleur appelé avec: " + nouvelleCouleur);
		System.out.println("Ancienne couleur: " + this.couleur);
		this.couleur = nouvelleCouleur;
		// Forcer le rafraîchissement complet
		invalidate();
		revalidate();
		repaint();
		// Forcer aussi le parent
		if (getParent() != null) {
			getParent().repaint();
		}
		System.out.println("PanneauCouleur rafraîchi, nouvelle couleur: " + this.couleur);
	}
	
	public Color getCouleur() {
		return couleur;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		// Ne pas appeler super pour avoir un contrôle total
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setColor(couleur);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setColor(Color.DARK_GRAY);
		g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		g2d.dispose();
	}
}

/**
 * Constructeur de la barre d'outils de l'application.
 * Fournit les boutons d'accès rapide aux outils de dessin et transformations.
 */
public class ToolBarBuilder {

	private PaintPanel panneau;
	private Color couleurIcone = new Color(60, 60, 60);
	private int tailleIcone = 20;
	private PanneauCouleur couleurPrincipale;
	private GestionnaireOutils.EcouteurCouleur ecouteurCouleurPipette;
	
	// Références aux boutons toggle pour la synchronisation
	private JToggleButton selectionBtn;
	private JToggleButton pinceauBtn;
	private JToggleButton gommeBtn;
	private JToggleButton pipetteBtn;
	private JToggleButton remplissageBtn;
	private JToggleButton texteBtn;

	
	// Références aux boutons toggle pour la synchronisation
	private JToggleButton selectionBtn;
	private JToggleButton pinceauBtn;
	private JToggleButton gommeBtn;
	private JToggleButton pipetteBtn;
	private JToggleButton remplissageBtn;
	private JToggleButton texteBtn;

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
		
		selectionBtn = creerBoutonToggle("square-dashed", "Sélection");
		pinceauBtn = creerBoutonToggle("pencil", "Pinceau");
		gommeBtn = creerBoutonToggle("eraser", "Gomme");
		pipetteBtn = creerBoutonToggle("pipette", "Pipette");
		remplissageBtn = creerBoutonToggle("paint-bucket", "Remplissage");
		texteBtn = creerBoutonToggle("type", "Texte");
		JButton texteImageBtn = creerBouton("image", "Texte avec image");
		
		// Connecter les boutons aux outils
		selectionBtn.addActionListener(e -> panneau.activerOutilDessin(application.multimedia.iut.Metier.outils.OutilDessin.SELECTION));
		pinceauBtn.addActionListener(e -> panneau.activerOutilDessin(application.multimedia.iut.Metier.outils.OutilDessin.PINCEAU));
		gommeBtn.addActionListener(e -> panneau.activerOutilDessin(application.multimedia.iut.Metier.outils.OutilDessin.GOMME));
		pipetteBtn.addActionListener(e -> panneau.activerOutilDessin(application.multimedia.iut.Metier.outils.OutilDessin.PIPETTE));
		remplissageBtn.addActionListener(e -> panneau.activerOutilDessin(application.multimedia.iut.Metier.outils.OutilDessin.REMPLISSAGE));
		texteBtn.addActionListener(e -> panneau.activerOutilDessin(application.multimedia.iut.Metier.outils.OutilDessin.TEXTE));
		texteImageBtn.addActionListener(e -> panneau.ouvrirEditeurTexteImage());
		
		selectionBtn.setSelected(true);
		
		groupeOutils.add(selectionBtn);
		groupeOutils.add(pinceauBtn);
		groupeOutils.add(gommeBtn);
		groupeOutils.add(pipetteBtn);
		groupeOutils.add(remplissageBtn);
		groupeOutils.add(texteBtn);
		groupeOutils.add(texteBtn);
		
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
		
		flipHBtn.addActionListener(e -> panneau.flipH());
		flipVBtn.addActionListener(e -> panneau.flipV());
		rotationBtn.addActionListener(e -> panneau.rotation());
		rediBtn.addActionListener(e -> panneau.redimensionner());
		mergerBtn.addActionListener(e -> panneau.fusionHorizontale());
		
		barre.add(flipHBtn);
		barre.add(flipVBtn);
		barre.add(rotationBtn);
		barre.add(rediBtn);
		barre.add(mergerBtn);
	}
	
	/**
	 * Ajoute les outils de filtres d'images à la barre d'outils.
	 * Contient : Contraste, Luminosité, Teinture.
	 *
	 * @param barre La barre d'outils à modifier.
	 */
	private void ajouterOutilsFiltres(JToolBar barre) {
		JButton contrasteBtn = creerBouton("contrast", "Contraste");
		JButton luminositeBtn = creerBouton("sun", "Luminosité");
		JButton teintureBtn = creerBouton("palette", "Teinture");
		
		contrasteBtn.addActionListener(e -> ouvrirDialogueContraste());
		luminositeBtn.addActionListener(e -> ouvrirDialogueLuminosite());
		teintureBtn.addActionListener(e -> ouvrirDialogueTeinture());
		
		barre.add(contrasteBtn);
		barre.add(luminositeBtn);
		barre.add(teintureBtn);
	}
	
	/**
	 * Ouvre une boîte de dialogue pour ajuster le contraste.
	 */
	private void ouvrirDialogueContraste() {
		JSlider slider = new JSlider(-100, 100, 0);
		slider.setMajorTickSpacing(50);
		slider.setMinorTickSpacing(10);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		
		int resultat = JOptionPane.showConfirmDialog(panneau, slider, "Ajuster le contraste", 
			JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		if (resultat == JOptionPane.OK_OPTION) {
			panneau.appliquerContraste(slider.getValue());
		}
	}
	
	/**
	 * Ouvre une boîte de dialogue pour ajuster la luminosité.
	 */
	private void ouvrirDialogueLuminosite() {
		JSlider slider = new JSlider(-255, 255, 0);
		slider.setMajorTickSpacing(100);
		slider.setMinorTickSpacing(25);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		
		int resultat = JOptionPane.showConfirmDialog(panneau, slider, "Ajuster la luminosité", 
			JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		if (resultat == JOptionPane.OK_OPTION) {
			panneau.appliquerLuminosite(slider.getValue());
		}
	}
	
	/**
	 * Ouvre une boîte de dialogue pour appliquer une teinture.
	 */
	private void ouvrirDialogueTeinture() {
		JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
		
		JSlider redSlider = new JSlider(0, 255, 255);
		JSlider greenSlider = new JSlider(0, 255, 0);
		JSlider blueSlider = new JSlider(0, 255, 0);
		JSlider alphaSlider = new JSlider(0, 255, 128);
		
		panel.add(new JLabel("Rouge:"));
		panel.add(redSlider);
		panel.add(new JLabel("Vert:"));
		panel.add(greenSlider);
		panel.add(new JLabel("Bleu:"));
		panel.add(blueSlider);
		panel.add(new JLabel("Intensité:"));
		panel.add(alphaSlider);
		
		int resultat = JOptionPane.showConfirmDialog(panneau, panel, "Appliquer une teinture", 
			JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		if (resultat == JOptionPane.OK_OPTION) {
			panneau.appliquerTeinte(redSlider.getValue(), greenSlider.getValue(), 
				blueSlider.getValue(), alphaSlider.getValue());
		}
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
		
		undoBtn.addActionListener(e -> panneau.annulerAction());
		redoBtn.addActionListener(e -> panneau.refaireAction());
		
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
		// Créer le panneau personnalisé pour la couleur
		couleurPrincipale = new PanneauCouleur(Color.BLACK);
		couleurPrincipale.setToolTipText("Couleur principale - Cliquez pour changer");
		
		// Ajouter un listener pour changer la couleur en cliquant
		couleurPrincipale.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				Color nouvelleCouleur = JColorChooser.showDialog(panneau, "Choisir une couleur", couleurPrincipale.getCouleur());
				if (nouvelleCouleur != null) {
					couleurPrincipale.setCouleur(nouvelleCouleur);
					panneau.definirCouleurDessin(nouvelleCouleur);
				}
			}
		});
		
		barre.add(couleurPrincipale);
		
		// Créer l'écouteur de changement de couleur depuis la pipette
		ecouteurCouleurPipette = couleur -> {
			System.out.println("=== PIPETTE: Changement de couleur détecté ===");
			System.out.println("Nouvelle couleur: " + couleur);
			System.out.println("RGB: " + couleur.getRed() + ", " + couleur.getGreen() + ", " + couleur.getBlue());
			
			// Mettre à jour la couleur
			SwingUtilities.invokeLater(() -> {
				couleurPrincipale.setCouleur(couleur);
				panneau.definirCouleurDessin(couleur);
				System.out.println("Couleur mise à jour: " + couleurPrincipale.getCouleur());
			});
			
			System.out.println("=========================================");
		};
	}
	
	/**
	 * Connecte l'écouteur de couleur au contrôleur de dessin.
	 * Doit être appelé après l'initialisation du gestionnaire d'images.
	 */
	public void connecterEcouteurCouleur() {
		System.out.println("Connexion de l'écouteur de couleur...");
		if (ecouteurCouleurPipette != null) {
			panneau.enregistrerEcouteurCouleur(ecouteurCouleurPipette);
		}
	}
	
	/**
	 * Synchronise la sélection d'outil dans la barre d'outils.
	 * Appelé depuis le menu pour refléter la sélection dans la barre d'outils.
	 * 
	 * @param outil L'outil à sélectionner visuellement.
	 */
	public void synchroniserSelectionOutil(application.multimedia.iut.Metier.outils.OutilDessin outil) {
		if (outil == null) return;
		
		switch (outil) {
			case SELECTION:
				if (selectionBtn != null) selectionBtn.setSelected(true);
				break;
			case PINCEAU:
				if (pinceauBtn != null) pinceauBtn.setSelected(true);
				break;
			case GOMME:
				if (gommeBtn != null) gommeBtn.setSelected(true);
				break;
			case PIPETTE:
				if (pipetteBtn != null) pipetteBtn.setSelected(true);
				break;
			case REMPLISSAGE:
				if (remplissageBtn != null) remplissageBtn.setSelected(true);
				break;
			case TEXTE:
				if (texteBtn != null) texteBtn.setSelected(true);
				break;
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
            
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btn.setBackground(new Color(220, 220, 220));
			}
            
			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btn.setBackground(originalColor);
			}
		});
	}
}
