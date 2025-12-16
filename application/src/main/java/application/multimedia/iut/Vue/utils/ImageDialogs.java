package application.multimedia.iut.Vue.utils;

import javax.swing.*;
import java.awt.Component;
import java.io.File;

public final class ImageDialogs {
    private ImageDialogs() {}

    public enum LoadChoice { REPLACE, SUPERPOSE, CANCEL }

    public static File[] selectImages(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Ouvrir une image");
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                String name = f.getName().toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".jpeg") ||
                       name.endsWith(".png") || name.endsWith(".gif") ||
                       name.endsWith(".bmp");
            }
            public String getDescription() {
                return "Images (*.jpg, *.png, *.gif, *.bmp)";
            }
        });

        int result = fileChooser.showOpenDialog(parent);
        if (result != JFileChooser.APPROVE_OPTION) return null;

        File[] files = fileChooser.getSelectedFiles();
        if (files == null || files.length == 0) {
            File single = fileChooser.getSelectedFile();
            if (single != null) return new File[] { single };
        }
        return files;
    }

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
