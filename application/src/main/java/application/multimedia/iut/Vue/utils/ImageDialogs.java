/**
 * Classe utilitaire pour les dialogues liés aux images.
 * Gère les sélecteurs de fichiers et les choix utilisateur.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Vue.utils;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Utilitaire pour les dialogues de sélection et gestion d'images.
 * Fournit des sélecteurs de fichiers et des dialogues de choix utilisateur.
 */
public final class ImageDialogs {
    private ImageDialogs() {}

    public enum LoadChoice { REPLACE, SUPERPOSE, CANCEL }

    /**
     * Ouvre un sélecteur de fichiers pour choisir une ou plusieurs images.
     * Supporte les formats JPG, PNG, GIF et BMP.
     *
     * @param parent Le composant parent pour le dialogue.
     * @return Un tableau de fichiers sélectionnés, ou null si annulé.
     */
    public static File selectImage(Component parent) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Ouvrir une image");
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				if (f.isDirectory()) return true;
				String name = f.getName().toLowerCase();
				return name.endsWith(".jpg") || name.endsWith(".jpeg") ||
					   name.endsWith(".png") || name.endsWith(".gif") ||
					   name.endsWith(".bmp");
			}
			public String getDescription() {
				return "Images (*.jpg, *.jpeg, *.png, *.gif, *.bmp)";
			}
		});

		int result = fileChooser.showOpenDialog(parent);
		if (result != JFileChooser.APPROVE_OPTION) return null;

		return fileChooser.getSelectedFile();
    }

    /**
     * Ouvre un sélecteur de fichier pour sauvegarder une image PNG.
     * Ajoute automatiquement l'extension .png si nécessaire.
     *
     * @param parent Le composant parent pour le dialogue.
     * @return Le fichier de destination, ou null si annulé.
     */
    public static File selectSavePng(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Enregistrer l'image (PNG)");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                String name = f.getName().toLowerCase();
                return name.endsWith(".png");
            }
            public String getDescription() {
                return "Images (*.png)";
            }
        });

        int result = fileChooser.showSaveDialog(parent);
        if (result != JFileChooser.APPROVE_OPTION) return null;

        File chosen = fileChooser.getSelectedFile();
        if (chosen == null) return null;
        String name = chosen.getName();
        if (!name.toLowerCase().endsWith(".png")) {
            chosen = new File(chosen.getAbsolutePath() + ".png");
        }
        return chosen;
    }

    /**
     * Demande à l'utilisateur comment charger une nouvelle image.
     * Propose de remplacer ou superposer l'image existante.
     *
     * @param parent Le composant parent pour le dialogue.
     * @return Le choix de l'utilisateur (REPLACE, SUPERPOSE ou CANCEL).
     */
    public static LoadChoice askLoadChoice(Component parent) {
        String[] options = {"Remplacer", "Superposer", "Annuler"};
        int choice = JOptionPane.showOptionDialog(parent,
            "Une image est déjà chargée. Que faire ?",
            "Chargement d'images",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[1]);
        if (choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION) return LoadChoice.CANCEL;
        if (choice == JOptionPane.NO_OPTION) return LoadChoice.SUPERPOSE;
        return LoadChoice.REPLACE;
    }
}
