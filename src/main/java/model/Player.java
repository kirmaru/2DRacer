// Player.java
package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

public class Player {
    private static final String PLAYER_INFO_FILE = "src/main/resources/logs/playerInfo.json";

    private Set<String> ownedCars;
    private int score;
    private String selectedCar;

    public Player() {
        this.ownedCars = new HashSet<>();
        this.score = 0;
        this.selectedCar = null;
        createPlayerInfoFileIfNotExists();
        loadPlayerData();
    }

    public Set<String> getOwnedCars() {
        return ownedCars;
    }

    public int getScore() {
        return score;
    }

    public String getSelectedCar() {
        return selectedCar;
    }

    public void addScore(int points) {
        this.score += points;
        savePlayerData();
    }

    public boolean spendScore(int points) {
        if (score >= points) {
            this.score -= points;
            savePlayerData();
            return true;
        }
        return false;
    }

    public void addCar(String carType) {
        this.ownedCars.add(carType);
        savePlayerData();
    }

    public void selectCar(String carType) {
        if (ownedCars.contains(carType)) {
            this.selectedCar = carType;
            savePlayerData();
        } else {
            throw new IllegalArgumentException("Car type not owned: " + carType);
        }
    }

    public void removeCar(String carType) {
        if (ownedCars.contains(carType)) {
            ownedCars.remove(carType);
            if (carType.equals(selectedCar)) {
                selectedCar = null;
            }
            savePlayerData();
        } else {
            throw new IllegalArgumentException("Car type not owned: " + carType);
        }
    }


    private void createPlayerInfoFileIfNotExists() {
        File file = new File(PLAYER_INFO_FILE);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                JSONObject initialData = new JSONObject();
                initialData.put("score", 1500);
                initialData.put("ownedCars", new JSONArray());
                initialData.put("selectedCar", JSONObject.NULL);

                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(initialData.toString(4));
                    System.out.println("Player info file created: " + PLAYER_INFO_FILE);
                }
            } catch (IOException e) {
                System.err.println("Error creating player info file: " + e.getMessage());
            }
        }
    }

    public void loadPlayerData() {
        File file = new File(PLAYER_INFO_FILE);
        if (file.exists()) {
            try {
                String content = new String(Files.readAllBytes(file.toPath()));
                JSONObject json = new JSONObject(content);

                this.score = json.getInt("score");

                JSONArray carsArray = json.getJSONArray("ownedCars");
                for (int i = 0; i < carsArray.length(); i++) {
                    this.ownedCars.add(carsArray.getString(i));
                }

                this.selectedCar = json.optString("selectedCar", null);

            } catch (IOException e) {
                System.err.println("Error loading player data: " + e.getMessage());
            }
        }
    }

    private void savePlayerData() {
        JSONObject json = new JSONObject();
        json.put("score", this.score);

        JSONArray carsArray = new JSONArray(this.ownedCars);
        json.put("ownedCars", carsArray);
        json.put("selectedCar", this.selectedCar);

        try (FileWriter writer = new FileWriter(PLAYER_INFO_FILE)) {
            writer.write(json.toString(4));
        } catch (IOException e) {
            System.err.println("Error saving player data: " + e.getMessage());
        }
    }
}
