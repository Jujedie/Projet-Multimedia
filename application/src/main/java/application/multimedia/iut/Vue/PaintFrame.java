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
