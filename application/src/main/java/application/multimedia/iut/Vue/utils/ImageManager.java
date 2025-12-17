/**
 * Classe utilitaire pour la gestion des images.
 * Gère le chargement, l'ajout et la manipulation des images.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Vue.utils;

import application.multimedia.iut.Main;
import application.multimedia.iut.Metier.image.CoucheImage;
import application.multimedia.iut.Metier.image.PileCouches;
import application.multimedia.iut.Metier.image.RenduToile;
import application.multimedia.iut.Metier.image.SessionPlacement;
import application.multimedia.iut.Metier.outils.OutilDessin;
import application.multimedia.iut.Vue.utils.ImageDialogs.LoadChoice;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Gestionnaire centralisé pour toutes les opérations sur images.
 * Coordonne le chargement, l'enregistrement, le zoom, et le placement d'images.
 */
public class ImageManager {
	private final Main.Controleur controleur;
	private final PileCouches pileCouches;
	private final SessionPlacement sessionPlacement;
	private final RenduToile renduToile;
	private final JLabel toile;
	private final JComponent parent;

	private Point dernierePositionSouris;
	private boolean glisserEnCours = false;
	private CoucheImage coucheGlissee;
	private boolean imageInitialePresente = true; // Flag pour la première ouverture d'image

	/**
	 * Constructeur du gestionnaire d'images.
	 * Initialise le gestionnaire avec une toile, un composant parent et le contrôleur.
	 *
	 * @param toile Le label servant de zone de dessin.
	 * @param parent Le composant parent pour les dialogues.
	 * @param controleur Le contrôleur central de l'application.
	 */
	public ImageManager(JLabel toile, JComponent parent, Main.Controleur controleur) {
		this.toile = toile;
		this.parent = parent;
		this.controleur = controleur;
		
		// Récupérer les références du modèle depuis le contrôleur
		this.pileCouches = controleur.getPileCouches();
		this.sessionPlacement = controleur.getSessionPlacement();
		this.renduToile = controleur.getRenduToile();
		
		// Créer une image blanche vide au démarrage pour permettre le dessin
		creerImageVide(800, 600);
		
		installerRaccourcisClavier();
	}

	/**
	 * Ouvre une boîte de dialogue pour sélectionner et charger des images.
	 * Gère le remplacement ou la superposition d'images existantes.
	 */
	public void ouvrirFichier() {
		File[] fichiersChoisis = ImageDialogs.selectImages(parent);
		if (fichiersChoisis == null || fichiersChoisis.length == 0) return;

		// Si c'est la première image chargée (image blanche initiale encore présente)
		// on remplace directement sans demander
		boolean possedeDejaImages = !pileCouches.estVide() && !imageInitialePresente;
		LoadChoice choix = LoadChoice.REPLACE;
		if (possedeDejaImages) {
			choix = ImageDialogs.askLoadChoice(parent);
			if (choix == LoadChoice.CANCEL) return;
		}

		if (choix == LoadChoice.REPLACE || imageInitialePresente) {
			pileCouches.vider();
			sessionPlacement.annuler();
			glisserEnCours = false;
			imageInitialePresente = false; // Marquer que l'image initiale a été remplacée
		}

		boolean placementDemande = possedeDejaImages && choix == LoadChoice.SUPERPOSE;
		boolean premierePlacee = false;
		for (File fichier : fichiersChoisis) {
			try {
				BufferedImage img = ImageIO.read(fichier);
				if (img != null) {
					if (placementDemande && !premierePlacee) {
						demarrerPlacement(img);
						premierePlacee = true;
					} else {
						pileCouches.ajouterCouche(img, obtenirTailleToile(), true);
					}
				} else {
					messageErreur("Erreur", "Impossible de charger " + fichier.getName());
				}
			} catch (Exception ex) {
				messageErreur("Erreur", "Erreur lors du chargement: " + ex.getMessage());
			}
		}
		if (!pileCouches.estVide()) {
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
		if (pileCouches.estVide()) {
			messageInfo("Information", "Aucune image à enregistrer.");
			return;
		}

		File fichierChoisi = ImageDialogs.selectSavePng(parent);
		if (fichierChoisi == null) return;

		try {
			BufferedImage composite = renduToile.construireComposite(pileCouches);
			ImageIO.write(composite, "png", fichierChoisi);
			messageInfo("Succès", "Image enregistrée avec succès !");
		} catch (Exception ex) {
			messageErreur("Erreur", "Erreur lors de l'enregistrement: " + ex.getMessage());
		}
	}

	/**
	 * Rafraîchit l'affichage de l'image sur la toile.
	 */
	private void afficherImage() {
		if (pileCouches.estVide()) return;
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
	 * Démarre le mode de placement interactif d'une nouvelle image.
	 * Affiche un message d'instruction à l'utilisateur.
	 *
	 * @param img L'image à placer.
	 */
	private void demarrerPlacement(BufferedImage img) {
		sessionPlacement.demarrer(img, obtenirTailleToile(), pileCouches.niveauZoom(), pileCouches.limitesBase());
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
		if (!sessionPlacement.estActive()) return;
		if (!sessionPlacement.intersecteBase(pileCouches.niveauZoom())) {
			JOptionPane.showMessageDialog(parent, "L'image est totalement hors de la première et sera ignorée.", "Placement refusé", JOptionPane.WARNING_MESSAGE);
			return;
		}
		CoucheImage placee = sessionPlacement.valider();
		if (placee != null) {
			pileCouches.ajouterCouche(placee);
			// Sauvegarder le niveau de zoom actuel
			double zoomActuel = pileCouches.niveauZoom();
			// Réinitialiser le zoom pour la fusion
			pileCouches.reinitialiserZoom();
			// Fusionner toutes les couches en une seule image composite
			BufferedImage imageComposite = renduToile.construireComposite(pileCouches);
			if (imageComposite != null) {
				// Remplacer toutes les couches par l'image fusionnée
				pileCouches.vider();
				pileCouches.ajouterCouche(imageComposite, obtenirTailleToile(), true);
				// Restaurer le zoom si nécessaire
				if (zoomActuel != 1.0) {
					pileCouches.zoomer(zoomActuel);
				}
			}
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
		renduToile.peindre(g, pileCouches, sessionPlacement);
	}

	/**
	 * Applique un facteur de zoom sur toutes les couches.
	 *
	 * @param facteur Le multiplicateur de zoom (&gt;1 pour agrandir, &lt;1 pour réduire).
	 */
	public void zoomer(double facteur) {
		if (pileCouches.estVide()) return;
		pileCouches.zoomer(facteur);
		afficherImage();
	}

	/**
	 * Réinitialise le zoom à 100% (facteur 1.0).
	 */
	public void reinitialiserZoom() {
		if (pileCouches.estVide()) return;
		pileCouches.reinitialiserZoom();
		afficherImage();
	}

	/**
	 * Récupère l'image de la couche actuellement active.
	 *
	 * @return L'image de la couche active, ou null si aucune.
	 */
	public BufferedImage obtenirImageCourante() {
		CoucheImage active = pileCouches.coucheActive();
		return active != null ? active.image : null;
	}

	/**
	 * Remplace toutes les couches par une nouvelle image unique.
	 * Réinitialise la pile de couches et les sessions en cours.
	 *
	 * @param image La nouvelle image à définir comme base.
	 */
	public void definirImageCourante(BufferedImage image) {
		pileCouches.vider();
		sessionPlacement.annuler();
		glisserEnCours = false;
		coucheGlissee = null;
		if (image != null) {
			pileCouches.ajouterCouche(image, obtenirTailleToile(), true);
		}
		afficherImage();
	}
	
	/**
	 * Ajoute une image comme nouvelle couche sur les couches existantes.
	 * Démarre le mode placement si des couches existent déjà.
	 *
	 * @param image L'image à ajouter.
	 */
	public void ajouterImageCommeNouvelleCouche(BufferedImage image) {
		if (image == null) return;
		
		if (pileCouches.estVide()) {
			pileCouches.ajouterCouche(image, obtenirTailleToile(), true);
		} else {
			demarrerPlacement(image);
		}
		afficherImage();
	}
	
	/**
	 * Ajoute une image en proposant à l'utilisateur le mode d'ajout.
	 * Demande si l'image doit remplacer ou se superposer.
	 *
	 * @param image L'image à ajouter.
	 */
	public void ajouterImageAvecChoix(BufferedImage image) {
		if (image == null) return;
		
		boolean possedeDejaImages = !pileCouches.estVide();
		LoadChoice choix = possedeDejaImages ? ImageDialogs.askLoadChoice(parent) : LoadChoice.REPLACE;
		
		if (choix == LoadChoice.CANCEL) return;
		
		boolean placementDemande = possedeDejaImages && choix == LoadChoice.SUPERPOSE;
		
		if (choix == LoadChoice.REPLACE) {
			pileCouches.vider();
		}
		
		if (placementDemande) {
			demarrerPlacement(image);
		} else {
			pileCouches.ajouterCouche(image, obtenirTailleToile(), true);
		}
		
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
		java.util.List<CoucheImage> couches = pileCouches.couches();
		double zoom = pileCouches.niveauZoom();
		for (int i = couches.size() - 1; i >= 0; i--) {
			CoucheImage couche = couches.get(i);
			int largeur = couche.largeurRedimensionnee(zoom);
			int hauteur = couche.hauteurRedimensionnee(zoom);
			if (p.x >= couche.x && p.x <= couche.x + largeur && p.y >= couche.y && p.y <= couche.y + hauteur) {
				// Vérifier si le pixel à cette position n'est pas transparent
				int pixelX = (int) ((p.x - couche.x) / zoom);
				int pixelY = (int) ((p.y - couche.y) / zoom);
				
				// S'assurer que les coordonnées sont dans les limites de l'image
				if (pixelX >= 0 && pixelX < couche.image.getWidth() && 
				    pixelY >= 0 && pixelY < couche.image.getHeight()) {
					int alpha = (couche.image.getRGB(pixelX, pixelY) >> 24) & 0xff;
					// Si le pixel n'est pas transparent (alpha > 0), cette couche est cliquable
					if (alpha > 0) {
						return couche;
					}
				}
			}
		}
		return null;
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
				if (outilActif != OutilDessin.SELECTION && !pileCouches.estVide()) {
					CoucheImage couche = pileCouches.coucheActive();
					if (couche != null) {
						controleur.commencerDessin(couche.image, e.getX() - couche.x, e.getY() - couche.y);
						toile.repaint();
						return;
					}
				}
				
				// Gestion du placement
				if (sessionPlacement.estActive()) {
					sessionPlacement.deplacerAu(e.getPoint(), pileCouches.niveauZoom());
					toile.repaint();
					dernierePositionSouris = e.getPoint();
					glisserEnCours = true;
					toile.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					return;
				}
				
				// Autoriser uniquement le déplacement de la première image (index 0)
				CoucheImage cible = coucheAuPoint(e.getPoint());
				if (cible != null) {
					java.util.List<CoucheImage> couches = pileCouches.couches();
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
				if (outilActif != OutilDessin.SELECTION && controleur.estEnDessin()) {
					CoucheImage couche = pileCouches.coucheActive();
					if (couche != null) {
						controleur.continuerDessin(couche.image, e.getX() - couche.x, e.getY() - couche.y);
						toile.repaint();
						return;
					}
				}
				
				if (!glisserEnCours) return;
				Point positionActuelle = e.getPoint();
				int dx = positionActuelle.x - dernierePositionSouris.x;
				int dy = positionActuelle.y - dernierePositionSouris.y;
				if (sessionPlacement.estActive()) {
					sessionPlacement.translater(dx, dy);
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
	public Main.Controleur getControleur() {
		return controleur;
	}
	
	/**
	 * Crée une nouvelle image vide blanche avec les dimensions spécifiées.
	 *
	 * @param largeur Largeur de l'image.
	 * @param hauteur Hauteur de l'image.
	 */
	private void creerImageVide(int largeur, int hauteur) {
		BufferedImage imageVide = new BufferedImage(
			Math.max(1, largeur), 
			Math.max(1, hauteur), 
			BufferedImage.TYPE_INT_ARGB
		);
		
		// Remplir en blanc
		Graphics2D g2d = imageVide.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, imageVide.getWidth(), imageVide.getHeight());
		g2d.dispose();
		
		pileCouches.ajouterCouche(imageVide, obtenirTailleToile(), true);
		afficherImage();
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
}
