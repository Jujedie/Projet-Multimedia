/**
 * Classe principale pour lancer l'application de retouche d'images.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */


package application.multimedia.iut;

	import application.multimedia.iut.Vue.PaintFrame;
	import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PaintFrame view = new PaintFrame();
            view.setVisible(true);
        });
    }
}