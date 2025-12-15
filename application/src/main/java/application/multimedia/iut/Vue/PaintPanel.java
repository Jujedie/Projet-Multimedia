package application.multimedia.iut.Vue;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;

public class PaintPanel extends JPanel {
	private JLabel canvas;
	private JButton sauvegarderBtn, chargerBtn, effacerBtn, mergerBtn, flipHBBtn, flipVBBtn, rotationBtn;
	private JButton rediBtn, contrasteBtn, texteBtn, remplirBtn;
	private JButton undoBtn, redoBtn;
	private JToggleButton selectionBtn, pinceauBtn, gommeBtn, pipetteBtn, remplissageBtn;
	private Boolean remplirMode = false;
	private int remplirTolerence;
	private Color remplirCouleur;
	private JPanel panneauCouleur;
	//private PaintController controlleur;

	public PaintPanel() {
		setLayout(new BorderLayout());
		
		// Pré-charger les icônes Lucide en arrière-plan
		new Thread(() -> LucideIconLoader.preloadCommonIcons()).start();

		// Panneau en haut avec menu et barre d'outils
		JPanel panneauHaut = new JPanel(new BorderLayout());
		
		// Barre de menu
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fichierMenu = new JMenu("Fichier");
		JMenuItem nouveauItem = new JMenuItem("Nouveau");
		JMenuItem ouvrirItem = new JMenuItem("Ouvrir");
		JMenuItem enregistrerItem = new JMenuItem("Enregistrer");
		JMenuItem enregistrerSousItem = new JMenuItem("Enregistrer sous...");
		JMenuItem quitterItem = new JMenuItem("Quitter");
		fichierMenu.add(nouveauItem);
		fichierMenu.add(ouvrirItem);
		fichierMenu.add(enregistrerItem);
		fichierMenu.add(enregistrerSousItem);
		fichierMenu.addSeparator();
		fichierMenu.add(quitterItem);
		
		JMenu editionMenu = new JMenu("Édition");
		JMenuItem annulerItem = new JMenuItem("Annuler");
		JMenuItem refaireItem = new JMenuItem("Refaire");
		JMenuItem effacerItem = new JMenuItem("Effacer tout");
		editionMenu.add(annulerItem);
		editionMenu.add(refaireItem);
		editionMenu.addSeparator();
		editionMenu.add(effacerItem);
		
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
		
		JMenu filtresMenu = new JMenu("Filtres");
		JMenuItem contrasteItem = new JMenuItem("Contraste");
		filtresMenu.add(contrasteItem);
		
		menuBar.add(fichierMenu);
		menuBar.add(editionMenu);
		menuBar.add(imageMenu);
		menuBar.add(filtresMenu);
		
		// Barre d'outils avec tous les outils
		JToolBar barreOutils = new JToolBar();
		barreOutils.setFloatable(false);
		barreOutils.setBackground(new Color(240, 240, 240));
		barreOutils.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		
		// Groupe de boutons outils
		ButtonGroup groupeOutils = new ButtonGroup();
		
		Color iconColor = new Color(60, 60, 60);
		int iconSize = 20;
		
		// Outils de sélection et dessin
		selectionBtn = creerBoutonAvecIconeLucide("square-dashed", "Sélection", iconSize, iconColor);
		pinceauBtn = creerBoutonAvecIconeLucide("pencil", "Pinceau", iconSize, iconColor);
		gommeBtn = creerBoutonAvecIconeLucide("eraser", "Gomme", iconSize, iconColor);
		pipetteBtn = creerBoutonAvecIconeLucide("pipette", "Pipette", iconSize, iconColor);
		remplissageBtn = creerBoutonAvecIconeLucide("paint-bucket", "Remplissage", iconSize, iconColor);
		texteBtn = new JButton(LucideIconLoader.loadIcon("type", iconSize, iconColor));
		configBoutonBarreOutils(texteBtn, "Texte");
		
		groupeOutils.add(selectionBtn);
		groupeOutils.add(pinceauBtn);
		groupeOutils.add(gommeBtn);
		groupeOutils.add(pipetteBtn);
		groupeOutils.add(remplissageBtn);
		
		barreOutils.add(selectionBtn);
		barreOutils.add(pinceauBtn);
		barreOutils.add(gommeBtn);
		barreOutils.add(pipetteBtn);
		barreOutils.add(remplissageBtn);
		barreOutils.add(texteBtn);
		barreOutils.addSeparator();
		
		// Outils fichier
		sauvegarderBtn = creerBoutonActionLucide("save", "Sauvegarder", iconSize, iconColor);
		chargerBtn = creerBoutonActionLucide("folder-open", "Ouvrir", iconSize, iconColor);
		effacerBtn = creerBoutonActionLucide("trash-2", "Effacer tout", iconSize, iconColor);
		
		barreOutils.add(sauvegarderBtn);
		barreOutils.add(chargerBtn);
		barreOutils.add(effacerBtn);
		barreOutils.addSeparator();
		
		// Outils transformation
		flipHBBtn = creerBoutonActionLucide("flip-horizontal", "Flip Horizontal", iconSize, iconColor);
		flipVBBtn = creerBoutonActionLucide("flip-vertical", "Flip Vertical", iconSize, iconColor);
		rotationBtn = creerBoutonActionLucide("rotate-cw", "Rotation", iconSize, iconColor);
		rediBtn = creerBoutonActionLucide("maximize", "Redimensionner", iconSize, iconColor);
		mergerBtn = creerBoutonActionLucide("layers", "Fusionner", iconSize, iconColor);
		
		barreOutils.add(flipHBBtn);
		barreOutils.add(flipVBBtn);
		barreOutils.add(rotationBtn);
		barreOutils.add(rediBtn);
		barreOutils.add(mergerBtn);
		barreOutils.addSeparator();
		
		// Outils filtres
		contrasteBtn = creerBoutonActionLucide("contrast", "Contraste", iconSize, iconColor);
		
		barreOutils.add(contrasteBtn);
		barreOutils.addSeparator();
		
		// Historique
		undoBtn = creerBoutonActionLucide("undo", "Annuler", iconSize, iconColor);
		redoBtn = creerBoutonActionLucide("redo", "Refaire", iconSize, iconColor);
		
		barreOutils.add(undoBtn);
		barreOutils.add(redoBtn);
		barreOutils.addSeparator();
		
		// Sélecteur de couleurs dans la barre d'outils
		JPanel miniCouleurPanel = new JPanel(new GridLayout(1, 2, 2, 0));
		miniCouleurPanel.setMaximumSize(new Dimension(50, 30));
		miniCouleurPanel.setPreferredSize(new Dimension(50, 30));
		
		JPanel couleurPrincipale = new JPanel();
		couleurPrincipale.setBackground(Color.BLACK);
		couleurPrincipale.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		couleurPrincipale.setToolTipText("Couleur principale");
		
		JPanel couleurSecondaire = new JPanel();
		couleurSecondaire.setBackground(Color.WHITE);
		couleurSecondaire.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		couleurSecondaire.setToolTipText("Couleur secondaire");
		
		miniCouleurPanel.add(couleurPrincipale);
		miniCouleurPanel.add(couleurSecondaire);
		barreOutils.add(miniCouleurPanel);
		
		panneauHaut.add(menuBar, BorderLayout.NORTH);
		panneauHaut.add(barreOutils, BorderLayout.SOUTH);

		
		canvas = new JLabel();
		canvas.setHorizontalAlignment(JLabel.CENTER);
		canvas.setBackground(Color.WHITE);
		canvas.setOpaque(true);
		canvas.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		JScrollPane scrollPane = new JScrollPane(canvas);
		scrollPane.setBackground(Color.DARK_GRAY);
		
		add(panneauHaut, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
	}

	// Méthode pour créer un bouton toggle avec icône Lucide
	private JToggleButton creerBoutonAvecIconeLucide(String iconName, String tooltip, int size, Color color) {
		JToggleButton btn = new JToggleButton(LucideIconLoader.loadIcon(iconName, size, color));
		configBoutonBarreOutils(btn, tooltip);
		return btn;
	}
	
	// Méthode pour créer un bouton d'action avec icône Lucide
	private JButton creerBoutonActionLucide(String iconName, String tooltip, int size, Color color) {
		JButton btn = new JButton(LucideIconLoader.loadIcon(iconName, size, color));
		configBoutonBarreOutils(btn, tooltip);
		return btn;
	}

	// Méthode pour créer un bouton outil toggle dans la barre
	private JToggleButton creerBoutonBarreOutils(String icone, String tooltip) {
		JToggleButton btn = new JToggleButton(icone);
		configBoutonBarreOutils(btn, tooltip);
		return btn;
	}
	
	// Méthode pour créer un bouton d'action dans la barre
	private JButton creerBoutonAction(String icone, String tooltip) {
		JButton btn = new JButton(icone);
		configBoutonBarreOutils(btn, tooltip);
		return btn;
	}
	
	// Configuration commune des boutons dans la barre d'outils
	private void configBoutonBarreOutils(AbstractButton btn, String tooltip) {
		btn.setToolTipText(tooltip);
		btn.setPreferredSize(new Dimension(40, 40));
		btn.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 18));
		btn.setFocusPainted(false);
		btn.setBackground(new Color(240, 240, 240));
		btn.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
			BorderFactory.createEmptyBorder(5, 5, 5, 5)
		));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		// Effet hover
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

	// attendre le controlleur pour test
	/*
	 * // Sceau Couleur public void mouseClicked(java.awt.event.MouseEvent e) {
	 * if (remplirMode && controlleur != null) { Icon icon = canvas.getIcon();
	 * if (icon instanceof ImageIcon) { controlleur.floodFill(e.getX(),
	 * e.getY(), remplirCouleur, remplirTolerence); remplirMode = false; } } }
	 * 
	 * public void setController(PaintController controller) { this.controlleur
	 * = controller; }
	 * 
	 */

	public void setRemplirMode(Boolean mode, Color couleur, int tolerence) {
		this.remplirMode = mode;
		this.remplirCouleur = couleur;
		this.remplirTolerence = tolerence;
	}

	//Listener setters
	public void setSauvegarderListener() 
	{
		//to be implemented
	}
}
