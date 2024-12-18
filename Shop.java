//package shop;

import common.Car;
import common.ScoreManager;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class Shop extends JPanel {
    private Car playerCar; // Reference to the player's car
    private int currentPage = 0; // 0 for AE86, 1 for Silvia
    private static final String[] carTextures = {"textures/ae86.png", "textures/silvia.png"};
    private static final double[][] carAttributes = {
        {6.0, 0.5, 5.0}, // AE86: maxSpeed, acceleration, friction
        {15.0, 0.4, 3.5} // Silvia: maxSpeed, acceleration, friction
    };
    private static final int[] carPrices = {1000, 5000}; // Prices for AE86 and Silvia
    private static final String PLAYER_INFO_FILE = "playerInfo.json";

    private ScoreManager scoreManager;

    public Shop(Car playerCar) {
        this.playerCar = playerCar;
        this.scoreManager = new ScoreManager();
        setLayout(new BorderLayout());
        updateShopUI();
    }

    private void updateShopUI() {
        removeAll(); // Clear existing components

        // Background image for the current page
        JLabel background = new JLabel(new ImageIcon("textures/shop_" + (currentPage == 0 ? "ae86" : "silvia") + ".png"));
        add(background, BorderLayout.CENTER);

        // Button to select and buy the car
        JButton selectButton = new JButton("Выбрать");
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                purchaseCar();
            }
        });

        // Navigation buttons
        JButton prevButton = new JButton("Назад");
        JButton nextButton = new JButton("Вперед");

        prevButton.addActionListener(e -> {
            currentPage = (currentPage - 1 + carTextures.length) % carTextures.length;
            updateShopUI();
        });

        nextButton.addActionListener(e -> {
            currentPage = (currentPage + 1) % carTextures.length;
            updateShopUI();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(prevButton);
        buttonPanel.add(selectButton);
        buttonPanel.add(nextButton);

        add(buttonPanel, BorderLayout.SOUTH);

        revalidate(); // Refresh the panel
        repaint(); // Repaint the panel
    }

    private void purchaseCar() {
        int carPrice = carPrices[currentPage];
        if (scoreManager.getScore() >= carPrice) {
            // Player can afford the car
            scoreManager.spendPoints(carPrice);
            addCarToPlayer();
            JOptionPane.showMessageDialog(this, "Вы купили " + (currentPage == 0 ? "AE86" : "Silvia") + "!");
        } else {
            // Not enough points
            JOptionPane.showMessageDialog(this, "У вас недостаточно очков для покупки этого автомобиля.");
        }
    }

    private void addCarToPlayer() {
        JSONObject playerInfo = loadPlayerInfo();
        String carKey = currentPage == 0 ? "ae86" : "silvia";

        // Add the car to the player's list of purchased cars if not already there
        if (!playerInfo.has(carKey)) {
            playerInfo.put(carKey, true);
            savePlayerInfo(playerInfo);
        }
        
        // Update car attributes for the player
        double[] attributes = carAttributes[currentPage];
        playerCar.updateMaxSpeed(attributes[0]);
        playerCar.updateAccel(attributes[1]);
    }

    private JSONObject loadPlayerInfo() {
        File file = new File(PLAYER_INFO_FILE);
        JSONObject playerInfo = new JSONObject();

        if (file.exists()) {
            try {
                String content = new String(Files.readAllBytes(file.toPath()));
                playerInfo = new JSONObject(content);
            } catch (IOException e) {
                System.err.println("Error loading player info: " + e.getMessage());
            }
        }

        return playerInfo;
    }

    private void savePlayerInfo(JSONObject playerInfo) {
        try (FileWriter writer = new FileWriter(PLAYER_INFO_FILE)) {
            writer.write(playerInfo.toString(4));
            System.out.println("Player info saved to " + PLAYER_INFO_FILE);
        } catch (IOException e) {
            System.err.println("Error saving player info: " + e.getMessage());
        }
    }
}
