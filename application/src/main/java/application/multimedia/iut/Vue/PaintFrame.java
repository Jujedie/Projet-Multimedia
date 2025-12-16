/**
 * Classe représentant la fenêtre principale de l'application.
 * Contient le menu, la barre d'outils et le panel de dessin.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Vue;

import javax.swing.*;

/**
 * Fenêtre principale de l'application de retouche d'images.
 * Contient le menu, la barre d'outils et le panneau de dessin.
 */
public class PaintFrame extends JFrame {
	private PaintPanel panneauPeinture;

	/**
	 * Constructeur de la fenêtre principale.
	 * Initialise l'interface graphique et configure les paramètres de la fenêtre.
	 */
	public PaintFrame() {
		setTitle("Projet Multimédia");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 700);

		panneauPeinture = new PaintPanel();
		add(panneauPeinture);
	}

	/**
	 * Obtient le panneau de peinture principal.
	 *
	 * @return Le panneau de peinture.
	 */
	public PaintPanel getPaintPanel() {
		return panneauPeinture;
	}
}
