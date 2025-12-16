/**
 * Classe principale pour lancer l'application de retouche d'images.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Vue.utils;

import application.multimedia.iut.Metier.image.CoucheImage;
import application.multimedia.iut.Metier.image.PileCouches;
import application.multimedia.iut.Metier.image.RenduToile;
import application.multimedia.iut.Metier.image.SessionPlacement;
import application.multimedia.iut.Vue.utils.ImageDialogs.LoadChoice;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageManager {
	private final PileCouches pileCouches = new PileCouches();
	private final SessionPlacement sessionPlacement = new SessionPlacement();
	private final RenduToile renduToile = new RenduToile();
	private final JLabel toile;
	private final JComponent parent;

	private Point dernierePositionSouris;
	private boolean glisserEnCours = false;
	private CoucheImage coucheGlissee;

	public ImageManager(JLabel toile, JComponent parent) {
		this.toile = toile;
		this.parent = parent;
		installerRaccourcisClavier();
	}

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

	private void afficherImage() {
		if (pileCouches.estVide()) return;
		toile.setIcon(null);
		toile.repaint();
	}

	private Dimension obtenirTailleToile() {
		return new Dimension(Math.max(1, toile.getWidth()), Math.max(1, toile.getHeight()));
	}

	private void demarrerPlacement(BufferedImage img) {
		sessionPlacement.demarrer(img, obtenirTailleToile(), pileCouches.niveauZoom(), pileCouches.limitesBase());
		toile.requestFocusInWindow();
		JOptionPane.showMessageDialog(parent,
			"Clique sur la zone où placer l'image, puis appuie sur Entrée pour valider.",
			"Placement de l'image", JOptionPane.INFORMATION_MESSAGE);
		toile.repaint();
	}

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

	public void dessinerImage(Graphics g) {
		renduToile.peindre(g, pileCouches, sessionPlacement);
	}

	public void zoomer(double facteur) {
		if (pileCouches.estVide()) return;
		pileCouches.zoomer(facteur);
		afficherImage();
	}

	public void reinitialiserZoom() {
		if (pileCouches.estVide()) return;
		pileCouches.reinitialiserZoom();
		afficherImage();
	}

	public BufferedImage obtenirImageCourante() {
		CoucheImage active = pileCouches.coucheActive();
		return active != null ? active.image : null;
	}

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
	
	public void ajouterImageCommeNouvelleCouche(BufferedImage image) {
		if (image == null) return;
		
		if (pileCouches.estVide()) {
			pileCouches.ajouterCouche(image, obtenirTailleToile(), true);
		} else {
			demarrerPlacement(image);
		}
		afficherImage();
	}
	
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

	private void messageErreur(String titre, String message) {
		JOptionPane.showMessageDialog(parent, message, titre, JOptionPane.ERROR_MESSAGE);
	}

	private void messageInfo(String titre, String message) {
		JOptionPane.showMessageDialog(parent, message, titre, JOptionPane.INFORMATION_MESSAGE);
	}
}
