package application.multimedia.iut.Vue;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageManager {
	
	private BufferedImage currentImage;
	private double zoomLevel = 1.0;
	private JLabel canvas;
	private JComponent parent;
	
	public ImageManager(JLabel canvas, JComponent parent) {
		this.canvas = canvas;
		this.parent = parent;
	}
	
	public void ouvrirFichier() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Ouvrir une image");
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
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try {
				currentImage = ImageIO.read(selectedFile);
				if (currentImage != null) {
					zoomLevel = 1.0;
					afficherImage();
					JOptionPane.showMessageDialog(parent, 
						"Image chargée avec succès !\nTaille: " + currentImage.getWidth() + "x" + currentImage.getHeight(),
						"Succès", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(parent, 
						"Impossible de charger l'image.",
						"Erreur", JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(parent, 
					"Erreur lors du chargement: " + ex.getMessage(),
					"Erreur", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public void enregistrerFichier(boolean nouveauFichier) {
		if (currentImage == null) {
			JOptionPane.showMessageDialog(parent, 
				"Aucune image à enregistrer.",
				"Information", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Enregistrer l'image");
		fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				if (f.isDirectory()) return true;
				String name = f.getName().toLowerCase();
				return name.endsWith(".jpg") || name.endsWith(".jpeg") || 
					   name.endsWith(".png");
			}
			public String getDescription() {
				return "Images (*.jpg, *.png)";
			}
		});
		
		int result = fileChooser.showSaveDialog(parent);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			
			String fileName = selectedFile.getName();
			if (!fileName.toLowerCase().endsWith(".png") && !fileName.toLowerCase().endsWith(".jpg")) {
				selectedFile = new File(selectedFile.getAbsolutePath() + ".png");
			}
			
			try {
				String format = fileName.toLowerCase().endsWith(".jpg") ? "jpg" : "png";
				ImageIO.write(currentImage, format, selectedFile);
				JOptionPane.showMessageDialog(parent, 
					"Image enregistrée avec succès !",
					"Succès", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(parent, 
					"Erreur lors de l'enregistrement: " + ex.getMessage(),
					"Erreur", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void afficherImage() {
		if (currentImage == null) return;
		
		int newWidth = (int) (currentImage.getWidth() * zoomLevel);
		int newHeight = (int) (currentImage.getHeight() * zoomLevel);
		
		Image scaledImage = currentImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
		canvas.setIcon(new ImageIcon(scaledImage));
		
		// Forcer le rafraîchissement
		canvas.revalidate();
		canvas.repaint();
	}
	
	public void zoom(double factor) {
		if (currentImage == null) return;
		
		zoomLevel *= factor;
		// Limiter le zoom entre 10% et 500%
		if (zoomLevel < 0.1) zoomLevel = 0.1;
		if (zoomLevel > 5.0) zoomLevel = 5.0;
		
		afficherImage();
	}
	
	public void resetZoom() {
		if (currentImage == null) return;
		zoomLevel = 1.0;
		afficherImage();
	}
	
	public BufferedImage getCurrentImage() {
		return currentImage;
	}
	
	public void setCurrentImage(BufferedImage image) {
		this.currentImage = image;
		afficherImage();
	}
}
