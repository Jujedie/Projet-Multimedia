/**
 * Dialogue simple pour saisir du texte avec configuration de police.
 * Utilisé pour l'outil texte de base (différent de TexteAvecImage).
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Vue.dialogs;

import application.multimedia.iut.Vue.utils.StyleHelper;
import javax.swing.*;
import java.awt.*;

/**
 * Dialogue permettant à l'utilisateur de saisir du texte et de choisir sa mise en forme.
 */
public class SimpleTexteDialog extends JDialog {
    private JTextField champTexte;
    private JComboBox<String> comboPolice;
    private JSpinner spinnerTaille;
    private JComboBox<String> comboStyle;
    private boolean valide = false;
    
    /**
     * Constructeur du dialogue de saisie de texte.
     *
     * @param parent La fenêtre parente.
     * @param couleurActuelle La couleur actuellement active (pour info).
     */
    public SimpleTexteDialog(Frame parent, Color couleurActuelle) {
        super(parent, "Saisir du texte", true);
        initComponents(couleurActuelle);
        setSize(450, 250);
        setLocationRelativeTo(parent);
    }
    
    /**
     * Initialise les composants de l'interface.
     *
     * @param couleurActuelle La couleur actuellement active.
     */
    private void initComponents(Color couleurActuelle) {
        JPanel panneauPrincipal = new JPanel(new BorderLayout(10, 10));
        panneauPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panneauPrincipal.setBackground(StyleHelper.BACKGROUND_LIGHT);
        
        // Panneau de configuration
        JPanel panneauConfig = creerPanneauConfiguration(couleurActuelle);
        panneauPrincipal.add(panneauConfig, BorderLayout.CENTER);
        
        // Panneau de boutons
        JPanel panneauBoutons = creerPanneauBoutons();
        panneauPrincipal.add(panneauBoutons, BorderLayout.SOUTH);
        
        add(panneauPrincipal);
    }
    
    /**
     * Crée le panneau de configuration du texte.
     *
     * @param couleurActuelle La couleur actuellement active.
     * @return Le panneau de configuration.
     */
    private JPanel creerPanneauConfiguration(Color couleurActuelle) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(StyleHelper.BACKGROUND_LIGHT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Ligne 1: Texte
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(StyleHelper.createLabel("Texte :"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        champTexte = StyleHelper.createTextField("Votre texte", 20);
        champTexte.selectAll();
        panel.add(champTexte, gbc);
        
        // Ligne 2: Police
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(StyleHelper.createLabel("Police :"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        String[] polices = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        comboPolice = new JComboBox<>(polices);
        comboPolice.setSelectedItem("Arial");
        panel.add(comboPolice, gbc);
        
        // Ligne 3: Taille
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(StyleHelper.createLabel("Taille :"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        spinnerTaille = new JSpinner(new SpinnerNumberModel(20, 8, 200, 1));
        panel.add(spinnerTaille, gbc);
        
        // Ligne 4: Style
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(StyleHelper.createLabel("Style :"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        String[] styles = {"Normal", "Gras", "Italique", "Gras Italique"};
        comboStyle = new JComboBox<>(styles);
        panel.add(comboStyle, gbc);
        
        // Ligne 5: Info couleur
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JLabel labelCouleur = StyleHelper.createLabel(
            String.format("Couleur actuelle : RGB(%d, %d, %d)", 
            couleurActuelle.getRed(), couleurActuelle.getGreen(), couleurActuelle.getBlue())
        );
        labelCouleur.setForeground(couleurActuelle);
        panel.add(labelCouleur, gbc);
        
        return panel;
    }
    
    /**
     * Crée le panneau contenant les boutons OK et Annuler.
     *
     * @return Le panneau de boutons.
     */
    private JPanel creerPanneauBoutons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(StyleHelper.BACKGROUND_LIGHT);
        
        JButton btnOk = StyleHelper.createButton("OK");
        btnOk.addActionListener(e -> valider());
        
        JButton btnAnnuler = StyleHelper.createButton("Annuler");
        btnAnnuler.addActionListener(e -> annuler());
        
        panel.add(btnOk);
        panel.add(btnAnnuler);
        
        return panel;
    }
    
    /**
     * Valide la saisie et ferme le dialogue.
     */
    private void valider() {
        String texte = champTexte.getText().trim();
        if (texte.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Veuillez saisir un texte.",
                "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }
        valide = true;
        dispose();
    }
    
    /**
     * Annule la saisie et ferme le dialogue.
     */
    private void annuler() {
        valide = false;
        dispose();
    }
    
    /**
     * Indique si l'utilisateur a validé la saisie.
     *
     * @return true si validé, false sinon.
     */
    public boolean estValide() {
        return valide;
    }
    
    /**
     * Obtient le texte saisi.
     *
     * @return Le texte saisi.
     */
    public String getTexte() {
        return champTexte.getText();
    }
    
    /**
     * Obtient la police configurée.
     *
     * @return La police avec le nom, le style et la taille choisis.
     */
    public Font getPolice() {
        String nomPolice = (String) comboPolice.getSelectedItem();
        int taille = (Integer) spinnerTaille.getValue();
        int style = getStyleFromCombo();
        return new Font(nomPolice, style, taille);
    }
    
    /**
     * Convertit la sélection du combo style en constante Font.
     *
     * @return Le style de police (Font.PLAIN, Font.BOLD, etc.).
     */
    private int getStyleFromCombo() {
        int index = comboStyle.getSelectedIndex();
        switch (index) {
            case 1: return Font.BOLD;
            case 2: return Font.ITALIC;
            case 3: return Font.BOLD | Font.ITALIC;
            default: return Font.PLAIN;
        }
    }
}
