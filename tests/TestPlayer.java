package tests;

import java.util.Scanner;
import common.*;

public class TestPlayer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Initializing Player...");

        Player player = new Player();

        System.out.println("Testing score addition...");
        player.addScore(100);
        System.out.println("Player score: " + player.getScore());

        System.out.println("Testing car management...");
        player.addCar("Sedan");
        player.selectCar("Sedan");
        System.out.println("Selected car: " + player.getSelectedCar());
    }
}
