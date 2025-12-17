package application.multimedia.iut.Metier.image;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
	public BufferedImage creerImageVide(int largeur, int hauteur, Dimension tailleToile) {
		BufferedImage imageVide = new BufferedImage(Math.max(1, largeur), Math.max(1, hauteur), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = imageVide.createGraphics();
		g2d.setColor(java.awt.Color.WHITE);
		g2d.fillRect(0, 0, imageVide.getWidth(), imageVide.getHeight());
		g2d.dispose();

		pileCouches.vider();
		pileCouches.ajouterCouche(imageVide, tailleToile, true);
		imageInitialePresente = true;
		return imageVide;
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

	/**
	 * Enregistre le composite courant au format PNG dans le fichier donné.
	 * Lance IOException en cas d'erreur d'écriture.
	 */
	public void enregistrerFichier(File fichier) throws IOException {
		if (fichier == null) throw new IllegalArgumentException("fichier null");
		BufferedImage composite = construireComposite();

		if (composite == null) throw new IOException("Aucun composite à enregistrer");
		ImageIO.write(composite, "png", fichier);
	}

	public void ouvrirFichier(File fichier) throws IOException {
		if (fichier == null) throw new IllegalArgumentException("fichier null");
		BufferedImage image = ImageIO.read(fichier);

		if (image == null) throw new IOException("Impossible de lire l'image depuis le fichier : " + fichier.getAbsolutePath());
		definirImageCourante(image, new Dimension(image.getWidth(), image.getHeight()));
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