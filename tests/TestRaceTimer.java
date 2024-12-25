package tests;

import model.RaceTimer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestRaceTimer {
    private RaceTimer timer;

    @BeforeEach
    void setUp() {
        // Инициализация RaceTimer с заданными параметрами
        timer = new RaceTimer("Test Track", "Standard");
    }

}
