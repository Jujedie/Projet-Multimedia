/**
 * Classe gérant l'outil texte pour écrire sur une image.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Metier;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Outil pour ajouter du texte sur une image.
 * Permet de configurer la police, la taille et la couleur du texte.
 */
public class OutilTexte {
	private Font police;
	private Color couleur;

	/**
	 * Constructeur par défaut de l'outil texte.
	 * Initialise avec Arial 20, couleur noire.
	 */
	public OutilTexte() {
		this.police = new Font("Arial", Font.PLAIN, 20);
		this.couleur = Color.BLACK;
	}

	/**
	 * Constructeur avec paramètres.
	 *
	 * @param police La police de caractères.
	 * @param couleur La couleur du texte.
	 */
	public OutilTexte(Font police, Color couleur) {
		this.police = police;
		this.couleur = couleur;
	}

	/**
	 * Dessine du texte sur l'image à une position donnée.
	 *
	 * @param image L'image sur laquelle dessiner.
	 * @param texte Le texte à écrire.
	 * @param x Coordonnée X de départ.
	 * @param y Coordonnée Y de départ (baseline du texte).
	 */
	public void dessinerTexte(BufferedImage image, String texte, int x, int y) {
		if (image == null || texte == null || texte.isEmpty()) return;
		
		Graphics2D g2d = image.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
							RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setFont(police);
		g2d.setColor(couleur);
		g2d.drawString(texte, x, y);
		g2d.dispose();
	}

	/**
	 * Dessine du texte centré à une position.
	 *
	 * @param image L'image sur laquelle dessiner.
	 * @param texte Le texte à écrire.
	 * @param x Coordonnée X du centre.
	 * @param y Coordonnée Y du centre.
	 */
	public void dessinerTexteCentre(BufferedImage image, String texte, int x, int y) {
		if (image == null || texte == null || texte.isEmpty()) return;
		
		Graphics2D g2d = image.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
							RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setFont(police);
		
		FontMetrics fm = g2d.getFontMetrics();
		int largeurTexte = fm.stringWidth(texte);
		int hauteurTexte = fm.getHeight();
		
		int posX = x - largeurTexte / 2;
		int posY = y + (hauteurTexte / 2) - fm.getDescent();
		
		g2d.setColor(couleur);
		g2d.drawString(texte, posX, posY);
		g2d.dispose();
	}

	/**
	 * Définit la police de caractères.
	 *
	 * @param police La nouvelle police.
	 */
	public void setPolice(Font police) {
		this.police = police;
	}

	/**
	 * Obtient la police actuelle.
	 *
	 * @return La police de caractères.
	 */
	public Font getPolice() {
		return police;
	}

	/**
	 * Définit la couleur du texte.
	 *
	 * @param couleur La nouvelle couleur.
	 */
	public void setCouleur(Color couleur) {
		this.couleur = couleur;
	}

	/**
	 * Obtient la couleur actuelle du texte.
	 *
	 * @return La couleur du texte.
	 */
	public Color getCouleur() {
		return couleur;
	}
}
