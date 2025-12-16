package application.multimedia.iut.Vue.panels;

import application.multimedia.iut.Vue.utils.StyleHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Supplier;

public class PreviewPanel extends JPanel {
    
    private Supplier<BufferedImage> imageSupplier;
    
    public PreviewPanel(Supplier<BufferedImage> imageSupplier) {
        this.imageSupplier = imageSupplier;
        setBackground(StyleHelper.BACKGROUND_DARK);
        setBorder(StyleHelper.createTitledBorder("Aperçu", Color.WHITE));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (imageSupplier == null) return;
        
        BufferedImage image = imageSupplier.get();
        if (image != null) {
            int x = (getWidth() - image.getWidth()) / 2;
            int y = (getHeight() - image.getHeight()) / 2;
            g.drawImage(image, x, y, null);
        }
    }
}
