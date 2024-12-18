package render;

import common.Car;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class CarView {
    private Car car;
    private BufferedImage carImage;

    public CarView(Car car) { 
        this.car = car;
        loadCarImage(car.getType()); 
    }

    private void loadCarImage(String type) { 
        try { 
            carImage = ImageIO.read(new File("textures/" + type + ".png")); 
        } catch(IOException e){ 
            e.printStackTrace(); 
            System.err.println("Error loading car image."); 
        } 
    }

    public void draw(Graphics2D g2d, double scaleFactor) { 
        g2d.translate(car.x * 100 * scaleFactor + 50 * scaleFactor,
                      car.y * 100 * scaleFactor + 50 * scaleFactor);

        g2d.rotate(Math.toRadians(car.angle)); 

        if(carImage != null) {  
            // Increase the size of the car by adjusting the scaling factor
            double enlargedScaleFactor = scaleFactor * 1.2; // Increase size by 20%
            int width = (int)(carImage.getWidth() * enlargedScaleFactor);  
            int height = (int)(carImage.getHeight() * enlargedScaleFactor);  
            g2d.drawImage(carImage, -width / 2, -height / 2, width, height, null);  
        } 

        g2d.rotate(-Math.toRadians(car.angle));  
        g2d.translate(-car.x * 100 * scaleFactor - 50 * scaleFactor,
                      -car.y * 100 * scaleFactor - 50 * scaleFactor);  
    }  
}