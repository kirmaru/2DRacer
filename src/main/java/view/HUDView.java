package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import model.Car;
import model.RaceTimer;

public class HUDView extends JPanel {
    private Car car;
    private RaceTimer raceTimer;
    private BufferedImage backgroundImage;
    private BufferedImage steeringWheel;
    private Font customFont;

    public HUDView(Car car, RaceTimer raceTimer) {
        this.car = car;
        this.raceTimer = raceTimer;
        loadBackgroundImage();
        loadSteeringWheel();
        loadCustomFont(); 
        setBackground(new Color(0, 0, 0, 0));
        setOpaque(false); 
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("src/main/resources/textures/dash.png"));
        } catch (IOException e) {
            System.err.println("Error loading HUD background image: " + e.getMessage());
        }
    }
    
    private void loadSteeringWheel() {
        try {
            steeringWheel = ImageIO.read(new File("src/main/resources/textures/steering_wheel.png"));
        } catch (IOException e) {
            System.err.println("Error loading HUD wheel image: " + e.getMessage());
        }
    }

    private void loadCustomFont() {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/MOSCOW2024.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            customFont = font.deriveFont(Font.BOLD, 14);
        } catch (FontFormatException | IOException e) {
            System.err.println("Error loading custom font: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, -20, getWidth(), getHeight(), null);
        }

        drawCarInfo(g);
        wheelRotation(g);
    }

    private void wheelRotation(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        int width = steeringWheel.getWidth();
        int height = steeringWheel.getHeight();

        int centerX = 955;
        int centerY = 690;

        g2d.rotate(Math.toRadians(car.getAngle()), centerX, centerY);

        g2d.drawImage(steeringWheel, centerX - width / 2, centerY - height / 2, null);

        g2d.setTransform(g2d.getTransform());
    }

    private void drawCarInfo(Graphics g) {
        
        g.setFont(customFont);
        g.setColor(Color.WHITE);

        double speed = car.getSpeed();
        double angle = car.getAngle();
        double gear = car.getCurrentGear();

        double elapsedTime = raceTimer.isRunning() ? (System.currentTimeMillis() - raceTimer.startTime) / 1000.0 : 0;

        g.drawString("Speed: " + String.format("%.2f", speed) + " m/s", 870, 620);
        g.drawString("Elapsed Time: " + String.format("%.2f", elapsedTime) + " s", 870, 605);
        g.drawString("Angle: " + String.format("%.2f", angle) + " ", 870, 590);
        g.drawString("gear: " + String.format("%.2f", gear) + " ", 870, 575);
    }
}
