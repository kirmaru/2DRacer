import model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestCar {
    private Car car;

    @BeforeEach
    void setUp() {
        car = new Car(0, 0, 0); 
        car.updateMaxSpeed(10);
        car.updateAccel(1);
        car.loadFromJson("src/main/resources/cars/silvia.json");
    }

    @Test
    void testInitialSpeed() {
        assertEquals(0, car.getSpeed(), "Initial speed should be 0");
    }

    @Test
    void testLoadedProperties() {
        validateLoadedProperties(car);
    }

    private void validateLoadedProperties(Car car) {
        String expectedType = "silvia";
        double expectedBrake = 3.5;
        double expectedFriction = 0.9;
        double expectedTurnRate = 3.0;

        double[] expectedGearSpeeds = {2.3, 2.8, 3.8};
        double[] expectedGearAccelerations = {0.3, 0.3, 0.1};

        assertEquals(expectedType, car.getType(), "Car type mismatch");
        assertEquals(expectedBrake, car.getBrake(), "Brake value mismatch");
        assertEquals(expectedFriction, car.getFriction(), "Friction value mismatch");
        assertEquals(expectedTurnRate, car.getTurnRate(), "Turn rate mismatch");

        for (int i = 0; i < expectedGearSpeeds.length; i++) {
            assertEquals(expectedGearSpeeds[i], car.gearMaxSpeeds[i], 
                         "Gear speed for gear " + (i + 1) + " mismatch");
            assertEquals(expectedGearAccelerations[i], car.gearAccelerations[i], 
                         "Gear acceleration for gear " + (i + 1) + " mismatch");
        }
    }
}
