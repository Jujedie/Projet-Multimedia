package application.multimedia.iut.Vue.panels;

import application.multimedia.iut.Vue.utils.StyleHelper;

import javax.swing.*;
import java.awt.*;

public class ActionButtonsPanel extends JPanel {
    
    private JButton btnValider;
    private JButton btnAnnuler;
    
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
    
    public JButton getBtnValider() {
        return btnValider;
    }
    
    public JButton getBtnAnnuler() {
        return btnAnnuler;
    }
}
