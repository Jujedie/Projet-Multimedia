/**
 * Panneau de configuration du texte.
 * Permet de saisir le texte, choisir la police et la taille.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Vue.panels;

import application.multimedia.iut.Vue.utils.StyleHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

/**
 * Panneau de configuration des paramètres de texte.
 * Permet de saisir le texte, choisir police, taille et charger une image.
 */
public class TextConfigPanel extends JPanel {
    
    private JTextField champTexte;
    private JComboBox<String> comboPolice;
    private JSpinner spinnerTaille;
    private JButton btnChargerImage;
    
    /**
     * Constructeur du panneau de configuration de texte.
     * Crée l'interface pour saisir le texte et choisir sa mise en forme.
     *
     * @param onChange Callback appelé lors d'un changement de configuration.
     * @param onLoadImage Callback appelé lors du chargement d'une image.
     */
    public TextConfigPanel(Consumer<Void> onChange, Runnable onLoadImage) {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(StyleHelper.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        creerChampTexte(gbc, onChange);
        creerSelecteurPolice(gbc, onChange);
        creerConfigCouleurEtImage(gbc, onChange, onLoadImage);
    }
    
    /**
     * Crée le champ de saisie du texte.
     * Notifie les changements lors de la frappe.
     *
     * @param gbc Les contraintes de placement.
     * @param onChange Callback de changement.
     */
    private void creerChampTexte(GridBagConstraints gbc, Consumer<Void> onChange) {
        gbc.gridx = 0; gbc.gridy = 0;
        add(StyleHelper.createLabel("Texte :"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.gridwidth = 3;
        champTexte = StyleHelper.createTextField("MULTIMÉDIA", 30);
        champTexte.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                onChange.accept(null);
            }
        });
        add(champTexte, gbc);
    }
    
    /**
     * Crée les contrôles de sélection de police et de taille.
     * Liste toutes les polices système disponibles.
     *
     * @param gbc Les contraintes de placement.
     * @param onChange Callback de changement.
     */
    private void creerSelecteurPolice(GridBagConstraints gbc, Consumer<Void> onChange) {
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.gridwidth = 1;
        add(StyleHelper.createLabel("Police :"), gbc);
        
        gbc.gridx = 1;
        String[] polices = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        comboPolice = new JComboBox<>(polices);
        comboPolice.setSelectedItem("Arial");
        comboPolice.addActionListener(e -> onChange.accept(null));
        add(comboPolice, gbc);
        
        gbc.gridx = 2;
        add(StyleHelper.createLabel("Taille :"), gbc);
        
        gbc.gridx = 3;
        spinnerTaille = new JSpinner(new SpinnerNumberModel(48, 12, 200, 1));
        spinnerTaille.addChangeListener(e -> onChange.accept(null));
        add(spinnerTaille, gbc);
    }
    
    /**
     * Crée le bouton de chargement d'image de fond.
     *
     * @param gbc Les contraintes de placement.
     * @param onChange Callback de changement.
     * @param onLoadImage Callback de chargement d'image.
     */
    private void creerConfigCouleurEtImage(GridBagConstraints gbc, Consumer<Void> onChange, Runnable onLoadImage) {
        gbc.gridx = 0; gbc.gridy = 2;
        add(StyleHelper.createLabel("Image de fond :"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3;
        btnChargerImage = StyleHelper.createButton("Charger image");
        btnChargerImage.setBackground(StyleHelper.BUTTON_INFO);
        btnChargerImage.setForeground(Color.WHITE);
        btnChargerImage.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnChargerImage.addActionListener(e -> onLoadImage.run());
        add(btnChargerImage, gbc);
    }
    
    /**
     * Obtient le texte saisi par l'utilisateur.
     *
     * @return Le texte du champ.
     */
    public String getTexte() {
        return champTexte.getText();
    }
    
    /**
     * Obtient la police sélectionnée avec sa taille.
     * Retourne une police en gras.
     *
     * @return La police configurée.
     */
    public Font getPolice() {
        String nomPolice = (String) comboPolice.getSelectedItem();
        int taillePolice = (Integer) spinnerTaille.getValue();
        return new Font(nomPolice, Font.BOLD, taillePolice);
    }
    
    /**
     * Obtient la couleur du texte.
     * Actuellement fixée à blanc.
     *
     * @return La couleur du texte.
     */
    public Color getCouleurTexte() {
        return Color.WHITE;
    }
}
