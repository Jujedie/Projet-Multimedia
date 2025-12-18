/**
 * Classe utilitaire pour la gestion des images.
 * Gère le chargement, l'ajout et la manipulation des images.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Vue.utils;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import application.multimedia.iut.Controleur;
import application.multimedia.iut.Metier.image.CoucheImage;
import application.multimedia.iut.Metier.outils.OutilDessin;
import application.multimedia.iut.Vue.dialogs.SimpleTexteDialog;
import application.multimedia.iut.Vue.utils.ImageDialogs.LoadChoice;

/**
 * Gestionnaire centralisé pour toutes les opérations sur images.
 * Coordonne le chargement, l'enregistrement, le zoom, et le placement d'images.
 */
public class ImageManagerVue {
	private final Controleur controleur;
	private final JLabel toile;
	private final JComponent parent;

	private Point dernierePositionSouris;
	private boolean glisserEnCours = false;
	private CoucheImage coucheGlissee;

	/**
	 * Constructeur du gestionnaire d'images.
	 * Initialise le gestionnaire avec les composants nécessaires.
	 *
	 * @param toile Le label servant de zone de dessin.
	 * @param parent Le composant parent pour les dialogues.
	 * @param controleur Le contrôleur central de l'application.
	 */
	public ImageManagerVue(JLabel toile, JComponent parent, Controleur controleur) {
		this.toile = toile;
		this.parent = parent;
		this.controleur = controleur;
		
		// Créer une image blanche vide au démarrage pour permettre le dessin
		// Utiliser une taille plus grande (1920x1080 ou la taille préférée de la toile)
		int largeur = Math.max(1920, toile.getPreferredSize().width);
		int hauteur = Math.max(1080, toile.getPreferredSize().height);
		creerImageVide(largeur, hauteur);
		
		installerRaccourcisClavier();
	}

	/**
	 * Rafraîchit l'affichage de l'image sur la toile.
	 */
	private void afficherImage() {
		if (controleur.pileCouchesEstVide()) return;
		toile.setIcon(null);
		toile.repaint();
	}
	
	/**
	 * Rafraîchit l'affichage (version publique).
	 */
	public void rafraichirAffichage() {
		toile.setIcon(null);
		toile.repaint();
	}

	/**
	 * Obtient les dimensions actuelles de la zone de dessin.
	 *
	 * @return Les dimensions de la toile.
	 */
	private Dimension obtenirTailleToile() {
		return new Dimension(Math.max(1, toile.getWidth()), Math.max(1, toile.getHeight()));
	}
	
	/**
	 * Ouvre un fichier image et l'ajoute à la pile de couches.
	 * Gère le choix de remplacement ou de superposition si des images existent déjà.
	 */
	public void ouvrirFichier() {
		File fichierChoisi = ImageDialogs.selectImage(parent);
		if (fichierChoisi == null) return;

		boolean imageInitialePresente = controleur.isImageInitialeBlanchePresente();

		// Si c'est la première image chargée (image blanche initiale encore présente)
		// on remplace directement sans demander
		boolean possedeDejaImages = !controleur.pileCouchesEstVide() && !imageInitialePresente;
		LoadChoice choix = LoadChoice.REPLACE;
		if (possedeDejaImages) {
			choix = ImageDialogs.askLoadChoice(parent);
			if (choix == LoadChoice.CANCEL) return;
		}

		if (choix == LoadChoice.REPLACE || imageInitialePresente) {
			glisserEnCours = false;
			imageInitialePresente = false; // Marquer que l'image initiale a été remplacée
		}

		try{
			controleur.ouvrirFichier(fichierChoisi, choix, obtenirTailleToile());
		} catch (IOException ex) {
			messageErreur("Erreur", "Erreur lors du chargement: " + ex.getMessage());
			return;
		}
		
		if (!controleur.pileCouchesEstVide()) {
			afficherImage();
			BufferedImage active = obtenirImageCourante();
			messageInfo("Succès", "Image(s) chargée(s) !\nActive: " + (active != null ? active.getWidth() + "x" + active.getHeight() : "-"));
		}
	}

	/**
	 * Enregistre l'image composite dans un fichier PNG.
	 *
	 * @param nouveauFichier True pour "Enregistrer sous", false pour "Enregistrer".
	 */
	public void enregistrerFichier(boolean nouveauFichier) {
		if (controleur.pileCouchesEstVide()) {
			messageInfo("Information", "Aucune image à enregistrer.");
			return;
		}

		File fichierChoisi = ImageDialogs.selectSavePng(parent);
		try{
			controleur.enregistrerFichier(fichierChoisi);
			messageInfo("Succès", "Image enregistrée avec succès !");
		} catch (IOException ex) {
			messageErreur("Erreur", "Erreur lors de l'enregistrement: " + ex.getMessage());
		}
		
	}

	/**
	 * Démarre le mode de placement interactif d'une nouvelle image.
	 * Affiche un message d'instruction à l'utilisateur.
	 *
	 * @param img L'image à placer.
	 */
	private void demarrerPlacement(BufferedImage img) {
		controleur.demarrerPlacement(img, obtenirTailleToile());
		toile.requestFocusInWindow();
		JOptionPane.showMessageDialog(parent,
			"Clique sur la zone où placer l'image, puis appuie sur Entrée pour valider.",
			"Placement de l'image", JOptionPane.INFORMATION_MESSAGE);
		toile.repaint();
	}

	/**
	 * Valide et finalise le placement de l'image en cours.
	 * Vérifie que l'image intersecte bien la zone de base.
	 * Fusionne toutes les couches en une seule image composite.
	 */
	private void validerPlacement() {
		if (!controleur.sessionPlacementValide()) {
			JOptionPane.showMessageDialog(parent, "L'image est totalement hors de la première et sera ignorée. ou n'est pas active", "Placement refusé", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		glisserEnCours = false;
		toile.repaint();
	}

	/**
	 * Installe les raccourcis clavier pour le mode de placement.
	 * Configure la touche Entrée pour valider le placement.
	 */
	private void installerRaccourcisClavier() {
		InputMap im = toile.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = toile.getActionMap();
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "validerPlacement");
		am.put("validerPlacement", new AbstractAction() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				validerPlacement();
			}
		});
	}

	/**
	 * Dessine l'image composée de toutes les couches sur le contexte graphique.
	 *
	 * @param g Le contexte graphique pour le rendu.
	 */
	public void dessinerImage(Graphics g) {
		controleur.peindre(g);
	}

	/**
	 * Applique un facteur de zoom sur toutes les couches.
	 *
	 * @param facteur Le multiplicateur de zoom (&gt;1 pour agrandir, &lt;1 pour réduire).
	 */
	public void zoomer(double facteur) {
		controleur.zoomer(facteur);
		afficherImage();
	}

	/**
	 * Réinitialise le zoom à 100% (facteur 1.0).
	 */
	public void reinitialiserZoom() {
		controleur.reinitialiserZoom();
		afficherImage();
	}

	/**
	 * Récupère l'image de la couche actuellement active.
	 *
	 * @return L'image de la couche active, ou null si aucune.
	 */
	public BufferedImage obtenirImageCourante() {
		return controleur.obtenirImageCourante();
	}

	/**
	 * Remplace toutes les couches par une nouvelle image unique.
	 * Réinitialise la pile de couches et les sessions en cours.
	 *
	 * @param image La nouvelle image à définir comme base.
	 */
	public void definirImageCourante(BufferedImage image) {
		controleur.definirImageCourante(image, obtenirTailleToile());
		glisserEnCours = false;
		coucheGlissee = null;
		afficherImage();
	}
	
	/**
	 * Ajoute une image comme nouvelle couche sur les couches existantes.
	 * Démarre le mode placement si des couches existent déjà.
	 *
	 * @param image L'image à ajouter.
	 */
	public void ajouterImageCommeNouvelleCouche(BufferedImage image) {
		controleur.ajouterImageCommeNouvelleCouche(image, obtenirTailleToile());
		afficherImage();
	}
	
	/**
	 * Ajoute une image en proposant à l'utilisateur le mode d'ajout.
	 * Demande si l'image doit remplacer ou se superposer.
	 *
	 * @param image L'image à ajouter.
	 */
	public void ajouterImageAvecChoix(BufferedImage image) {
		boolean possedeDejaImages = !controleur.pileCouchesEstVide();
		LoadChoice choix = possedeDejaImages ? ImageDialogs.askLoadChoice(parent) : LoadChoice.REPLACE;
		
		controleur.ajouterImageAvecChoix(image, choix, obtenirTailleToile());
		
		afficherImage();
	}

	/**
	 * Trouve la couche visible à une position donnée.
	 * Parcourt les couches du dessus vers le dessous.
	 * Ignore les pixels transparents pour permettre la sélection des couches inférieures.
	 *
	 * @param p Le point à tester.
	 * @return La couche trouvée, ou null si aucune.
	 */
	private CoucheImage coucheAuPoint(Point p) {
		return controleur.coucheAuPoint(p);
	}

	/**
	 * Active les gestionnaires de souris pour le déplacement des images.
	 * Permet de placer de nouvelles images en mode placement et de déplacer uniquement la première image (image de base).
	 */
	public void activerDeplacementImage() {
		MouseAdapter adaptationSouris = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (!SwingUtilities.isLeftMouseButton(e)) return;
				
				// Gestion des outils de dessin
				OutilDessin outilActif = controleur.getOutilActif();
				
				// Gestion de l'outil texte
				if (outilActif == OutilDessin.TEXTE && !controleur.pileCouchesEstVide()) {
					CoucheImage couche = controleur.getPileCouches().coucheActive();
					if (couche != null) {
						int x = e.getX() - couche.x;
						int y = e.getY() - couche.y;
						// Vérifier que les coordonnées sont dans l'image
						if (x >= 0 && x < couche.image.getWidth() && y >= 0 && y < couche.image.getHeight()) {
							// Ouvrir une boîte de dialogue pour saisir le texte
							String texte = JOptionPane.showInputDialog(parent, 
								"Entrez le texte à écrire :", 
								"Saisie de texte", 
								JOptionPane.PLAIN_MESSAGE);
							if (texte != null && !texte.trim().isEmpty()) {
								controleur.dessinerTexte(couche.image, texte, x, y);
								toile.repaint();
							}
						}
						return;
					}
				}
				
				// Gestion du pot de peinture
				if (outilActif == OutilDessin.REMPLISSAGE && !controleur.pileCouchesEstVide()) {
					CoucheImage couche = controleur.getPileCouches().coucheActive();
					if (couche != null) {
						int x = e.getX() - couche.x;
						int y = e.getY() - couche.y;
						// Vérifier que les coordonnées sont dans l'image
						if (x >= 0 && x < couche.image.getWidth() && y >= 0 && y < couche.image.getHeight()) {
							Color couleur = controleur.getCouleurActive();
							int couleurRGB = couleur.getRGB();
							controleur.appliquerPotDePeinture(couleurRGB, 50, true, x, y);
							toile.repaint();
						}
						return;
					}
				}
				
				// Gestion des autres outils de dessin (pinceau, gomme)
				if (outilActif != OutilDessin.SELECTION && outilActif != OutilDessin.REMPLISSAGE && outilActif != OutilDessin.TEXTE) {
					// Si aucune image n'existe, créer une image vide pour permettre le dessin
					if (controleur.pileCouchesEstVide()) {
						creerImageVide(1920, 1080);
					}
					
					if (!controleur.pileCouchesEstVide()) {
						CoucheImage couche = controleur.getPileCouches().coucheActive();
						if (couche != null) {
							int xImage = e.getX() - couche.x;
							int yImage = e.getY() - couche.y;
							
							// Vérifier que les coordonnées sont dans l'image
							if (xImage >= 0 && xImage < couche.image.getWidth() && yImage >= 0 && yImage < couche.image.getHeight()) {
								controleur.commencerDessin(couche.image, xImage, yImage);
								toile.repaint();
							}
							return;
						}
					}
				}
				
				// Gestion du placement
				if (controleur.getSessionPlacement().estActive()) {
					controleur.getSessionPlacement().deplacerAu(e.getPoint(), controleur.getPileCouches().niveauZoom());
					toile.repaint();
					dernierePositionSouris = e.getPoint();
					glisserEnCours = true;
					toile.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					return;
				}
				
				// Autoriser uniquement le déplacement de la première image (index 0)
				CoucheImage cible = coucheAuPoint(e.getPoint());
				if (cible != null) {
					java.util.List<CoucheImage> couches = controleur.getPileCouches().couches();
					// Vérifier si c'est la première couche
					if (!couches.isEmpty() && cible == couches.get(0)) {
						coucheGlissee = cible;
						dernierePositionSouris = e.getPoint();
						glisserEnCours = true;
						toile.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					controleur.terminerDessin();
					glisserEnCours = false;
					coucheGlissee = null;
					toile.setCursor(Cursor.getDefaultCursor());
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				// Gestion des outils de dessin
				OutilDessin outilActif = controleur.getOutilActif();
				if (outilActif != OutilDessin.SELECTION && outilActif != OutilDessin.REMPLISSAGE && controleur.estEnDessin()) {
					CoucheImage couche = controleur.getPileCouches().coucheActive();
					if (couche != null) {
						int xImage = e.getX() - couche.x;
						int yImage = e.getY() - couche.y;
						
						// Vérifier que les coordonnées sont dans l'image
						if (xImage >= 0 && xImage < couche.image.getWidth() && yImage >= 0 && yImage < couche.image.getHeight()) {
							controleur.continuerDessin(couche.image, xImage, yImage);
							toile.repaint();
						}
					}
					return;
				}
				
				if (!glisserEnCours) return;
				Point positionActuelle = e.getPoint();
				int dx = positionActuelle.x - dernierePositionSouris.x;
				int dy = positionActuelle.y - dernierePositionSouris.y;
				if (controleur.getSessionPlacement().estActive()) {
					controleur.getSessionPlacement().translater(dx, dy);
				} else if (coucheGlissee != null) {
					// Déplacer uniquement la première couche
					coucheGlissee.x += dx;
					coucheGlissee.y += dy;
				}
				dernierePositionSouris = positionActuelle;
				toile.repaint();
			}
		};

		toile.addMouseListener(adaptationSouris);
		toile.addMouseMotionListener(adaptationSouris);
	}

	/**
	 * Affiche un message d'erreur dans une boîte de dialogue.
	 *
	 * @param titre Le titre de la fenêtre.
	 * @param message Le message d'erreur.
	 */
	private void messageErreur(String titre, String message) {
		JOptionPane.showMessageDialog(parent, message, titre, JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Affiche un message d'information dans une boîte de dialogue.
	 *
	 * @param titre Le titre de la fenêtre.
	 * @param message Le message informatif.
	 */
	private void messageInfo(String titre, String message) {
		JOptionPane.showMessageDialog(parent, message, titre, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Obtient le contrôleur principal.
	 *
	 * @return Le contrôleur principal.
	 */
	public Controleur getControleur() {
		return controleur;
	}
	
	/**
	 * Crée une nouvelle image vide blanche avec les dimensions spécifiées.
	 *
	 * @param largeur Largeur de l'image.
	 * @param hauteur Hauteur de l'image.
	 */
	private void creerImageVide(int largeur, int hauteur) {
		controleur.creerImageVide(largeur, hauteur, obtenirTailleToile());
		afficherImage();
		toile.repaint();
	}

	/**
	 * Active un outil de dessin.
	 *
	 * @param outil L'outil à activer.
	 */
	public void activerOutil(OutilDessin outil) {
		controleur.setOutilActif(outil);
	}

	/**
	 * Définit la couleur de dessin active.
	 *
	 * @param couleur La nouvelle couleur.
	 */
	public void definirCouleur(Color couleur) {
		controleur.definirCouleurActive(couleur);
	}
	
	/**
	 * Ouvre le dialogue de saisie de texte et dessine le texte sur l'image.
	 * Utilise le pattern MVC : Vue -> Contrôleur -> Métier.
	 *
	 * @param image L'image sur laquelle dessiner le texte.
	 * @param x La coordonnée X où dessiner le texte.
	 * @param y La coordonnée Y où dessiner le texte.
	 */
	private void ouvrirDialogueTexte(BufferedImage image, int x, int y) {
		Frame frame = (Frame) SwingUtilities.getWindowAncestor(parent);
		Color couleurActuelle = controleur.getCouleurActive();
		
		SimpleTexteDialog dialogue = new SimpleTexteDialog(frame, couleurActuelle);
		dialogue.setVisible(true);
		
		if (dialogue.estValide()) {
			String texte = dialogue.getTexte();
			Font police = dialogue.getPolice();
			
			// Mettre à jour la police dans le contrôleur via le gestionnaire d'outils
			controleur.setPoliceTexte(police);
			
			// Dessiner le texte via le contrôleur (pattern MVC)
			controleur.dessinerTexte(image, texte, x, y);
		}
	}
}
