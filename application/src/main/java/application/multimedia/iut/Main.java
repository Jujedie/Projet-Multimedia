/**
 * Classe principale pour lancer l'application de retouche d'images.
 * Point d'entrée de l'application.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */


package application.multimedia.iut;

import javax.swing.SwingUtilities;

import application.multimedia.iut.Vue.PaintFrame;

/**
 * Point d'entrée de l'application de retouche d'images.
 * Initialise et lance l'interface graphique Swing.
 */
public class Main {
    /**
     * Point d'entrée de l'application.
     * Lance l'interface graphique dans le thread de l'EDT.
     *
     * @param args Arguments de la ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PaintFrame view = new PaintFrame();
            view.setVisible(true);
        });
    }
}