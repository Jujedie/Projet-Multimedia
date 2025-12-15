package application.multimedia.iut.Vue;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.TranscoderException;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.StringReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class LucideIconLoader {
	
	private static final String LUCIDE_CDN_BASE = "https://unpkg.com/lucide-static@latest/icons/";
	private static final Map<String, Icon> iconCache = new HashMap<>();

	public static Icon loadIcon(String iconName, int size, Color color) {
		String cacheKey = iconName + "_" + size + "_" + color.getRGB();
		
		if (iconCache.containsKey(cacheKey)) {
			return iconCache.get(cacheKey);
		}
		
		try {
			String svgContent = downloadSVG(iconName);
			if (svgContent != null) {
				// Modifier la couleur du SVG
				svgContent = svgContent.replaceAll("stroke=\"[^\"]*\"", 
					String.format("stroke=\"rgb(%d,%d,%d)\"", color.getRed(), color.getGreen(), color.getBlue()));
				
				BufferedImage image = convertSVGToImage(svgContent, size);
				if (image != null) {
					Icon icon = new ImageIcon(image);
					iconCache.put(cacheKey, icon);
					return icon;
				}
			}
		} catch (Exception e) {
			System.err.println("Erreur lors du chargement de l'icône " + iconName + ": " + e.getMessage());
		}
		
		return createFallbackIcon(size, color);
	}
	
	private static String downloadSVG(String iconName) {
		try {
			URL url = new URL(LUCIDE_CDN_BASE + iconName + ".svg");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			
			if (conn.getResponseCode() == 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuilder content = new StringBuilder();
				String line;
				
				while ((line = reader.readLine()) != null) {
					content.append(line);
				}
				reader.close();
				return content.toString();
			}
		} catch (Exception e) {
			System.err.println("Impossible de télécharger l'icône " + iconName + ": " + e.getMessage());
		}
		return null;
	}
	
	private static BufferedImage convertSVGToImage(String svgContent, int size) {
		try {
			final BufferedImage[] imagePointer = new BufferedImage[1];
			
			ImageTranscoder transcoder = new ImageTranscoder() {
				@Override
				public BufferedImage createImage(int width, int height) {
					return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
				}

				@Override
				public void writeImage(BufferedImage img, TranscoderOutput output) {
					imagePointer[0] = img;
				}
			};
			
			transcoder.addTranscodingHint(ImageTranscoder.KEY_WIDTH, (float) size);
			transcoder.addTranscodingHint(ImageTranscoder.KEY_HEIGHT, (float) size);
			
			TranscoderInput input = new TranscoderInput(new StringReader(svgContent));
			transcoder.transcode(input, null);
			
			return imagePointer[0];
		} catch (TranscoderException e) {
			System.err.println("Erreur lors de la conversion SVG: " + e.getMessage());
			return null;
		}
	}
	
	private static Icon createFallbackIcon(int size, Color color) {
		BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(color);
		g2d.setStroke(new BasicStroke(2f));
		g2d.drawRect(4, 4, size - 8, size - 8);
		g2d.dispose();
		return new ImageIcon(image);
	}
	
	public static void preloadCommonIcons() {
		String[] commonIcons = {
			"square-dashed", "pencil", "eraser", "pipette", "paint-bucket", "type",
			"save", "folder-open", "trash-2", "flip-horizontal", "flip-vertical",
			"rotate-cw", "maximize", "layers", "contrast", "undo", "redo"
		};
		
		Color defaultColor = new Color(60, 60, 60);
		for (String iconName : commonIcons) {
			loadIcon(iconName, 20, defaultColor);
		}
	}
}
