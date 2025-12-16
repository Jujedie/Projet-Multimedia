/**
 * Classe principale pour lancer l'application de retouche d'images.
 * 
 * @author Lechasles Antoine , Martin Ravenel , Julien Oyer
 * @version 1.0
 */
package application.multimedia.iut.Vue.panels;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

import javax.swing.JPanel;

public class SelectionPanel extends JPanel {
    private BufferedImage image;
    private Rectangle selection;
    private Point start;
    private final Consumer<Rectangle> selectionListener;

    public SelectionPanel(Consumer<Rectangle> selectionListener) {
        this.selectionListener = selectionListener;
        setBackground(Color.WHITE);
        installerGestionSouris();
    }

    private void installerGestionSouris() {
        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (image == null) return;
                start = e.getPoint();
                selection = new Rectangle(start.x, start.y, 0, 0);
                setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (image == null || start == null) return;
                int x = Math.min(start.x, e.getX());
                int y = Math.min(start.y, e.getY());
                int w = Math.abs(e.getX() - start.x);
                int h = Math.abs(e.getY() - start.y);

                x = Math.max(0, Math.min(x, image.getWidth() - 1));
                y = Math.max(0, Math.min(y, image.getHeight() - 1));
                w = Math.min(w, image.getWidth() - x);
                h = Math.min(h, image.getHeight() - y);

                selection = new Rectangle(x, y, w, h);
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
                if (image == null) return;
                if (selection != null && selection.width > 5 && selection.height > 5) {
                    selectionListener.accept(selection);
                } else {
                    selection = new Rectangle(0, 0, image.getWidth(), image.getHeight());
                    selectionListener.accept(selection);
                    repaint();
                }
            }
        };
        addMouseListener(adapter);
        addMouseMotionListener(adapter);
    }

    public void setImage(BufferedImage img) {
        this.image = img;
        if (img != null) {
            selection = new Rectangle(0, 0, img.getWidth(), img.getHeight());
            selectionListener.accept(selection);
        }
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image == null) {
            dessinerMessageVide(g);
            return;
        }

        g.drawImage(image, 0, 0, this);
        if (selection == null || selection.width <= 0 || selection.height <= 0) return;

        dessinerOverlaySelection((Graphics2D) g);
    }

    private void dessinerMessageVide(Graphics g) {
        g.setColor(Color.GRAY);
        String msg = "Cliquez sur 'Charger image' pour sélectionner une image";
        FontMetrics fm = g.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(msg)) / 2;
        int y = getHeight() / 2;
        g.drawString(msg, x, y);
    }

    private void dessinerOverlaySelection(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 120));
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());

        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(selection.x, selection.y, selection.width, selection.height);
        g2d.setComposite(AlphaComposite.SrcOver);
        g2d.drawImage(image.getSubimage(selection.x, selection.y, selection.width, selection.height),
            selection.x, selection.y, null);

        g2d.setColor(new Color(0, 120, 215));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(selection.x, selection.y, selection.width, selection.height);

        String dims = selection.width + " x " + selection.height;
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics fm = g2d.getFontMetrics();
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(selection.x, selection.y - 20, fm.stringWidth(dims) + 10, 20);
        g2d.setColor(Color.WHITE);
        g2d.drawString(dims, selection.x + 5, selection.y - 5);
    }

    @Override
    public Dimension getPreferredSize() {
        if (image != null) {
            return new Dimension(image.getWidth(), image.getHeight());
        }
        return new Dimension(400, 400);
    }
}
