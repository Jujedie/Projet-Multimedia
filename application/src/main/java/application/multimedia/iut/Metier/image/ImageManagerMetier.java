package application.multimedia.iut.Metier.image;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import application.multimedia.iut.Vue.utils.ImageDialogs;
import application.multimedia.iut.Vue.utils.ImageDialogs.LoadChoice;

/**
 * Version métier du gestionnaire d'images : encapsule les opérations sur la PileCouches,
 * la SessionPlacement et le RenduToile sans aucune interaction UI.
 */
public class ImageManagerMetier {
	private final PileCouches pileCouches;
	private final SessionPlacement sessionPlacement;
	private final RenduToile renduToile;
	private boolean imageInitialePresente = true;

	/**
	 * Constructeur métier : reçoit les composants métier existants.
	 */
	public ImageManagerMetier(PileCouches pileCouches, SessionPlacement sessionPlacement, RenduToile renduToile) {
		this.pileCouches = pileCouches;
		this.sessionPlacement = sessionPlacement;
		this.renduToile = renduToile;
	}

	/**
	 * Crée une image vide blanche et l'ajoute comme couche de base.
	 */
	public void creerImageVide(int largeur, int hauteur, Dimension tailleToile) {
		BufferedImage imageVide = new BufferedImage(Math.max(1, largeur), Math.max(1, hauteur), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = imageVide.createGraphics();
		g2d.setColor(java.awt.Color.WHITE);
		g2d.fillRect(0, 0, imageVide.getWidth(), imageVide.getHeight());
		g2d.dispose();

		pileCouches.vider();
		pileCouches.ajouterCouche(imageVide, tailleToile, true);
		imageInitialePresente = true;
	}

	/**
	 * Définit l'image courante (remplace toutes les couches).
	 */
	public void definirImageCourante(BufferedImage image, Dimension tailleToile) {
		pileCouches.vider();
		sessionPlacement.annuler();
		imageInitialePresente = (image == null);
		if (image != null) {
			pileCouches.ajouterCouche(image, tailleToile, true);
			imageInitialePresente = false;
		}
	}

	/**
	 * Ajoute une image : si la pile est vide l'ajoute comme base, sinon démarre le placement.
	 * Retourne true si le placement a été démarré, false si l'image a été ajoutée directement.
	 */
	public boolean ajouterImageCommeNouvelleCouche(BufferedImage image, Dimension tailleToile) {
		if (image == null) return false;
		if (pileCouches.estVide()) {
			pileCouches.ajouterCouche(image, tailleToile, true);
			return false;
		} else {
			demarrerPlacement(image, tailleToile);
			return true;
		}
	}

	/**
	 * Ajoute une image selon le choix (REPLACE / SUPERPOSE / CANCEL).
	 * - REPLACE : vide la pile et ajoute l'image.
	 * - SUPERPOSE : démarre un placement si des couches existent.
	 * - CANCEL : ne fait rien.
	 * Retourne true si un placement a été démarré, false si l'image a été ajoutée ou annulée.
	 */
	public boolean ajouterImageAvecChoix(BufferedImage image, LoadChoice choix, Dimension tailleToile) {
		if (image == null || choix == LoadChoice.CANCEL) return false;

		boolean placementDemande = !pileCouches.estVide() && choix == LoadChoice.SUPERPOSE;

		if (choix == LoadChoice.REPLACE) {
			pileCouches.vider();
		}

		if (placementDemande) {
			demarrerPlacement(image, tailleToile);
			return true;
		} else {
			pileCouches.ajouterCouche(image, tailleToile, true);
			return false;
		}
	}

	/**
	 * Démarre la session de placement pour une image (ne fait pas d'IHM).
	 */
	public void demarrerPlacement(BufferedImage img, Dimension tailleToile) {
		sessionPlacement.demarrer(img, tailleToile, pileCouches.niveauZoom(), pileCouches.limitesBase());
	}

	/**
	 * Valide le placement et, si demandé, fusionne toutes les couches en une seule image composite.
	 * Renvoie true si le placement a été validé et fusion (si applicable) réussie.
	 * Si l'image placée est complètement hors-base, le placement est annulé et la méthode retourne false.
	 */
	public boolean validerPlacement() {
		if (!sessionPlacement.estActive()) return false;
		if (!sessionPlacement.intersecteBase(pileCouches.niveauZoom())) {
			sessionPlacement.annuler();
			return false;
		}

		CoucheImage placee = sessionPlacement.valider();
		if (placee != null) {
			pileCouches.ajouterCouche(placee);
			double zoomActuel = pileCouches.niveauZoom();
			pileCouches.reinitialiserZoom();
			BufferedImage imageComposite = renduToile.construireComposite(pileCouches);
			if (imageComposite != null) {
				pileCouches.vider();
				// tailleToile inconnue ici : la couche sera ajoutée en tant que base ; caller peut re-ajuster si besoin
				pileCouches.ajouterCouche(imageComposite, new Dimension(imageComposite.getWidth(), imageComposite.getHeight()), true);
				if (zoomActuel != 1.0) {
					pileCouches.zoomer(zoomActuel);
				}
			}
		}
		return true;
	}

	/**
	 * Annule la session de placement en cours.
	 */
	public void annulerPlacement() {
		sessionPlacement.annuler();
	}

	/**
	 * Construit et retourne le composite actuel (peut retourner null).
	 */
	public BufferedImage construireComposite() {
		return renduToile.construireComposite(pileCouches);
	}

	public boolean imageInitialePresente() {
		return imageInitialePresente;
	}

	/**
	 * Enregistre le composite courant au format PNG dans le fichier donné.
	 * Lance IOException en cas d'erreur d'écriture.
	 */
	public void enregistrerFichier(File fichier) throws IOException {
		try {
			BufferedImage composite = renduToile.construireComposite(pileCouches);
			ImageIO.write(composite, "png", fichier);
		} catch (IOException ex) {}
	}

	public BufferedImage ouvrirFichier(File fichier, LoadChoice choix, Dimension tailleToile) throws IOException {

		// Si c'est la première image chargée (image blanche initiale encore présente)
		// on remplace directement sans demander
		boolean possedeDejaImages = !pileCouches.estVide() && !imageInitialePresente;
		
		if (choix == LoadChoice.REPLACE || imageInitialePresente) {
			pileCouches.vider();
			sessionPlacement.annuler();
		}

		BufferedImage img = null;
		boolean placementDemande = possedeDejaImages && choix == LoadChoice.SUPERPOSE;
		try {
			img = ImageIO.read(fichier);
			if (img != null) {
				if (placementDemande) {
					demarrerPlacement(img, tailleToile);
				} else {
					pileCouches.ajouterCouche(img, tailleToile, true);
				}
			} 
		} catch (IOException ex) {}

		return img;
	}

	/**
	 * Zoom métier.
	 */
	public void zoomer(double facteur) {
		if (pileCouches.estVide()) return;
		pileCouches.zoomer(facteur);
	}

	/**
	 * Réinitialiser zoom métier.
	 */
	public void reinitialiserZoom() {
		if (pileCouches.estVide()) return;
		pileCouches.reinitialiserZoom();
	}

	/**
	 * Obtient l'image courante (de la couche active) ou null.
	 */
	public BufferedImage obtenirImageCourante() {
		CoucheImage active = pileCouches.coucheActive();
		return active != null ? active.image : null;
	}

	/**
	 * Vide la pile de couches.
	 */
	public void vider() {
		pileCouches.vider();
		sessionPlacement.annuler();
		imageInitialePresente = true;
	}

	/**
	 * Trouve la couche visible à une position donnée.
	 * Parcourt les couches du dessus vers le dessous.
	 * Ignore les pixels transparents pour permettre la sélection des couches inférieures.
	 *
	 * @param p Le point à tester.
	 * @return La couche trouvée, ou null si aucune.
	 */
	public CoucheImage coucheAuPoint(Point p) {
		List<CoucheImage> couches = pileCouches.couches();
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
	 * Indique si une session de placement est active.
	 */
	public boolean estPlacementActif() {
		return sessionPlacement != null && sessionPlacement.estActive();
	}

	/**
	 * Indicateur si l'image initiale blanche est encore présente.
	 */
	public boolean isImageInitialePresente() {
		return imageInitialePresente;
	}
}