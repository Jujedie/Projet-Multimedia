/**
 * Classe utilitaire pour la gestion des images.
 * Gère le chargement, l'ajout et la manipulation des images.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Vue.utils;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import application.multimedia.iut.Metier.image.CoucheImage;
import application.multimedia.iut.Metier.image.PileCouches;
import application.multimedia.iut.Metier.image.RenduToile;
import application.multimedia.iut.Metier.image.SessionPlacement;
import application.multimedia.iut.Vue.utils.ImageDialogs.LoadChoice;

/**
 * Gestionnaire centralisé pour toutes les opérations sur images.
 * Coordonne le chargement, l'enregistrement, le zoom, et le placement d'images.
 */
public class ImageManager {
	private final PileCouches pileCouches = new PileCouches();
	private final SessionPlacement sessionPlacement = new SessionPlacement();
	private final RenduToile renduToile = new RenduToile();
	private final JLabel toile;
	private final JComponent parent;

	private Point dernierePositionSouris;
	private boolean glisserEnCours = false;
	private CoucheImage coucheGlissee;

	/**
	 * Constructeur du gestionnaire d'images.
	 * Initialise le gestionnaire avec une toile et un composant parent.
	 *
	 * @param toile Le label servant de zone de dessin.
	 * @param parent Le composant parent pour les dialogues.
	 */
	public ImageManager(JLabel toile, JComponent parent) {
		this.toile = toile;
		this.parent = parent;
		installerRaccourcisClavier();
	}

	/**
	 * Ouvre une boîte de dialogue pour sélectionner et charger des images.
	 * Gère le remplacement ou la superposition d'images existantes.
	 */
	public void ouvrirFichier() {
		File[] fichiersChoisis = ImageDialogs.selectImages(parent);
		if (fichiersChoisis == null || fichiersChoisis.length == 0) return;

		boolean possedeDejaImages = !pileCouches.estVide();
		LoadChoice choix = LoadChoice.REPLACE;
		if (possedeDejaImages) {
			choix = ImageDialogs.askLoadChoice(parent);
			if (choix == LoadChoice.CANCEL) return;
		}

		if (choix == LoadChoice.REPLACE) {
			pileCouches.vider();
			sessionPlacement.annuler();
			glisserEnCours = false;
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
				return couche;
			}
		}
		return null;
	}

	/**
	 * Active les gestionnaires de souris pour le déplacement des images.
	 * Permet de glisser-déposer les couches et de placer de nouvelles images.
	 */
	public void activerDeplacementImage() {
		MouseAdapter adaptationSouris = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (!SwingUtilities.isLeftMouseButton(e)) return;
				if (sessionPlacement.estActive()) {
					sessionPlacement.deplacerAu(e.getPoint(), pileCouches.niveauZoom());
					toile.repaint();
					dernierePositionSouris = e.getPoint();
					glisserEnCours = true;
					toile.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					return;
				}
				CoucheImage cible = coucheAuPoint(e.getPoint());
				if (cible != null) {
					coucheGlissee = cible;
					dernierePositionSouris = e.getPoint();
					glisserEnCours = true;
					toile.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					glisserEnCours = false;
					coucheGlissee = null;
					toile.setCursor(Cursor.getDefaultCursor());
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				if (!glisserEnCours) return;
				Point positionActuelle = e.getPoint();
				int dx = positionActuelle.x - dernierePositionSouris.x;
				int dy = positionActuelle.y - dernierePositionSouris.y;
				if (sessionPlacement.estActive()) {
					sessionPlacement.translater(dx, dy);
				} else if (coucheGlissee != null) {
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
}
