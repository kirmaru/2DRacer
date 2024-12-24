package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;
import model.Car;
import model.Tile;
import model.Track;

public class TrackView extends JPanel {  
    private Track track;  
    private Car car;  
    private CarView carView;  
    private Map<String, BufferedImage> tileImages;  
    private double scaleFactor;

    public TrackView(Track track, Car car, double scaleFactor) {  
        this.track = track;  
        this.car = car;  
        this.carView = new CarView(car);  
        this.scaleFactor = scaleFactor;  
        //setPreferredSize(new Dimension(1280, 720));  
        loadTileImages();  
        setBackground(new Color(0, 134, 89));
    }  

    private void loadTileImages() {  
        tileImages = new HashMap<>();  
        String[] tileTypes = {"down_left", "down_right", "down_straight", "up_straight", "up_left", "up_right", "left_straight", "left_up", "left_down", "right_straight", "right_down", "right_up", "grass", "finish", "start", "barrier", "tarmac"};

        for (String type : tileTypes) {  
            try {  
                BufferedImage image = ImageIO.read(new File("resources/textures/tiles/" + type + ".png"));  
                tileImages.put(type, image);  
            } catch (IOException e) {  
                System.err.println("Error loading texture for " + type + ": " + e.getMessage());  
            }  
        }  
    }  

    @Override   
    public void paintComponent(Graphics g) {   
        super.paintComponent(g);   
        Graphics2D g2d = (Graphics2D) g;

        double cameraX = car.x * 100 * scaleFactor + 50 * scaleFactor;   
        double cameraY = car.y * 100 * scaleFactor + 50 * scaleFactor;

        g2d.translate(getWidth() / 2 - cameraX, getHeight() / 2 - cameraY);

        drawTrack(g2d);
        
        carView.draw(g2d, scaleFactor);    
    }  

    private void drawTrack(Graphics2D g2d) {
        for (int i = 0; i < track.borderY; i++) {   
            for (int j = 0; j < track.borderX; j++) {   
                Tile tile = track.track[j][i];

                int xPos = (int) (j * 100 * scaleFactor);   
                int yPos = (int) (i * 100 * scaleFactor);   
                int size = (int) (100 * scaleFactor);

                if (tile == null) { 
                    BufferedImage tileImage = tileImages.get("grass");  
                    g2d.drawImage(tileImage, xPos, yPos, size, size, null); 
                } else {   
                    BufferedImage tileImage = tileImages.get(tile.type);   
                    if (tileImage != null) {
                        // Draw current tile
                        g2d.drawImage(tileImage, xPos, yPos, size, size, null);   
                    } else {   
                        g2d.setColor(Color.green);   
                        g2d.fillRect(xPos, yPos, size, size);   
                    }   
                }   
            }   
        }
    }

    private BufferedImage getRotatedTileImage(String tileType, int angle) {
        BufferedImage originalImage = tileImages.get(tileType);
        if (originalImage == null) return null;

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        BufferedImage rotatedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g2d = rotatedImage.createGraphics();
        g2d.rotate(Math.toRadians(angle), width / 2.0, height / 2.0);
        g2d.drawImage(originalImage, 0, 0, null);
        g2d.dispose();

        return rotatedImage;
    }
}
