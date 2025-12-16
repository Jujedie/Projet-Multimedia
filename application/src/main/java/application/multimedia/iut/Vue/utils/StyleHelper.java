/**
 * Classe utilitaire pour le style de l'interface.
 * Définit les couleurs, polices et composants standardisés.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Vue.utils;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 * Utilitaire de style pour l'interface utilisateur.
 * Définit les thèmes de couleurs, polices et composants standardisés.
 */
public class StyleHelper {
    
    public static final Color BACKGROUND_LIGHT = new Color(245, 245, 245);
    public static final Color BACKGROUND_DARK = new Color(45, 45, 45);
    public static final Color BORDER_COLOR = new Color(220, 220, 220);
    public static final Color BUTTON_PRIMARY = new Color(46, 125, 50);
    public static final Color BUTTON_SECONDARY = new Color(240, 240, 240);
    public static final Color BUTTON_INFO = new Color(70, 130, 180);
    public static final Color TEXT_DARK = new Color(80, 80, 80);
    public static final Color TEXT_LIGHT = new Color(70, 70, 70);
    
    /**
     * Crée un label avec le style standard.
     * Police Arial, gras, taille 13.
     *
     * @param text Le texte du label.
     * @return Le JLabel configuré.
     */
    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        return label;
    }
    
    /**
     * Crée un champ de texte avec le style standard.
     * Bordure arrondie et padding.
     *
     * @param defaultText Le texte par défaut.
     * @param columns Le nombre de colonnes.
     * @return Le JTextField configuré.
     */
    public static JTextField createTextField(String defaultText, int columns) {
        JTextField field = new JTextField(defaultText, columns);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }
    
    /**
     * Crée un bouton avec le style de base.
     * Curseur main et sans focus visible.
     *
     * @param text Le texte du bouton.
     * @return Le JButton configuré.
     */
    public static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    /**
     * Crée un bouton primaire avec le style vert.
     * Fond vert, texte blanc, dimensions personnalisées.
     *
     * @param text Le texte du bouton.
     * @param width La largeur en pixels.
     * @param height La hauteur en pixels.
     * @return Le JButton configuré.
     */
    public static JButton createPrimaryButton(String text, int width, int height) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(width, height));
        button.setBackground(BUTTON_PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    /**
     * Crée un bouton secondaire avec le style gris.
     * Fond gris clair, bordure, dimensions personnalisées.
     *
     * @param text Le texte du bouton.
     * @param width La largeur en pixels.
     * @param height La hauteur en pixels.
     * @return Le JButton configuré.
     */
    public static JButton createSecondaryButton(String text, int width, int height) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 13));
        button.setPreferredSize(new Dimension(width, height));
        button.setBackground(BUTTON_SECONDARY);
        button.setForeground(TEXT_DARK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    /**
     * Crée une bordure avec titre.
     * Bordure encadrée avec titre en haut à gauche.
     *
     * @param title Le titre de la bordure.
     * @param textColor La couleur du texte du titre.
     * @return La bordure configurée.
     */
    public static Border createTitledBorder(String title, Color textColor) {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                title,
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12),
                textColor
            )
        );
    }
}
