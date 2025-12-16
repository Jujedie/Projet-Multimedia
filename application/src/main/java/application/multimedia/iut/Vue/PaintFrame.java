/**
 * Classe principale pour lancer l'application de retouche d'images.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Vue;

import javax.swing.*;

public class PaintFrame extends JFrame {
	private PaintPanel panneauPeinture;

	public PaintFrame() {
		setTitle("Projet Multimédia");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 700);

		panneauPeinture = new PaintPanel();
		add(panneauPeinture);
	}

	public PaintPanel getPaintPanel() {
		return panneauPeinture;
	}
}
