package tests;

import model.Player;
import model.ScoreManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestScoreManager {
    private Player player;
    private ScoreManager scoreManager;

    @BeforeEach
    void setUp() {
        // Инициализация Player и ScoreManager перед каждым тестом
        player = new Player();
        scoreManager = new ScoreManager(player);
    }
    
}
