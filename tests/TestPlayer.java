package tests;

import model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestPlayer {
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player();
    }

    @Test
    void testScoreAddition() {
        System.out.println("Testing score addition...");
        player.addScore(100);
        assertEquals(100, player.getScore(), "Player score should be 100 after adding 100");
    }

    @Test
    void testCarManagement() {
        System.out.println("Testing car management...");
        player.addCar("Sedan");
        player.selectCar("Sedan");
        assertEquals("Sedan", player.getSelectedCar(), "Selected car should be 'Sedan'");
    }
}
