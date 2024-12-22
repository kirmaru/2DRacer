package tests;

import model.*;

public class TestScoreManager {
    public static void main(String[] args) {
        System.out.println("Initializing Player and ScoreManager...");

        Player player = new Player();
        ScoreManager scoreManager = new ScoreManager(player);

        System.out.println("Testing score addition...");
        scoreManager.addPoints(50);
        System.out.println("Player score: " + scoreManager.getScore());

        System.out.println("Testing score spending...");
        boolean success = scoreManager.spendPoints(30);
        System.out.println("Points spent: " + success);
        System.out.println("Remaining score: " + scoreManager.getScore());
    }
}