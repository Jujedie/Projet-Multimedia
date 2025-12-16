/**
 * Classe principale pour lancer l'application de retouche d'images.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Vue.utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class StyleHelper {
    
    public static final Color BACKGROUND_LIGHT = new Color(245, 245, 245);
    public static final Color BACKGROUND_DARK = new Color(45, 45, 45);
    public static final Color BORDER_COLOR = new Color(220, 220, 220);
    public static final Color BUTTON_PRIMARY = new Color(46, 125, 50);
    public static final Color BUTTON_SECONDARY = new Color(240, 240, 240);
    public static final Color BUTTON_INFO = new Color(70, 130, 180);
    public static final Color TEXT_DARK = new Color(80, 80, 80);
    public static final Color TEXT_LIGHT = new Color(70, 70, 70);
    
    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        return label;
    }
    
    public static JTextField createTextField(String defaultText, int columns) {
        JTextField field = new JTextField(defaultText, columns);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }
    
    public static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
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
