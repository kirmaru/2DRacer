package tests;

import model.*;

public class TestTile {
    public static void main(String[] args) {
        System.out.println("Initializing Tile...");

        Tile tile = new Tile(5, 5, "grass");
        System.out.println("Tile type: " + tile.type);
        System.out.println("Tile friction: " + tile.friction);
    }
}