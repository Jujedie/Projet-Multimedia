/**
 * Panneau contenant les boutons d'action (Créer, Annuler).
 * Gère la validation ou l'annulation des dialogues.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Vue.panels;

import application.multimedia.iut.Vue.utils.StyleHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Panneau de boutons Valider/Annuler pour les dialogues.
 * Fournit une interface standardisée pour confirmer ou annuler des actions.
 */
public class ActionButtonsPanel extends JPanel {
    
    private JButton btnValider;
    private JButton btnAnnuler;
    
    /**
     * Constructeur du panneau de boutons d'action.
     * Crée les boutons Valider et Annuler avec leurs actions.
     *
     * @param onValidate L'action à exécuter lors de la validation.
     * @param onCancel L'action à exécuter lors de l'annulation.
     */
    public ActionButtonsPanel(Runnable onValidate, Runnable onCancel) {
        setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        setBackground(StyleHelper.BACKGROUND_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
        
        btnAnnuler = StyleHelper.createSecondaryButton("Annuler", 100, 35);
        btnAnnuler.addActionListener(e -> onCancel.run());
        
        btnValider = StyleHelper.createPrimaryButton("Créer", 100, 35);
        btnValider.addActionListener(e -> onValidate.run());
        
        add(btnAnnuler);
        add(btnValider);
    }
    
    /**
     * Obtient le bouton de validation.
     *
     * @return Le bouton "Créer".
     */
    public JButton getBtnValider() {
        return btnValider;
    }
    
    /**
     * Obtient le bouton d'annulation.
     *
     * @return Le bouton "Annuler".
     */
    public JButton getBtnAnnuler() {
        return btnAnnuler;
    }
}
