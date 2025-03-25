import model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.lang.Math.abs;
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
        int OLDscore = player.getScore();
        player.addScore(100);
        assertEquals(100, abs(OLDscore - player.getScore()), "Player score should be 100 after adding 100");
    }

    @Test
    void testCarManagement() {
        System.out.println("Testing car management...");
        player.addCar("RX-7");
        player.selectCar("RX-7");
        assertEquals("RX-7", player.getSelectedCar(), "Selected car should be 'RX-7'");
        player.removeCar("RX-7");
    }
}
