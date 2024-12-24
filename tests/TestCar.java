package tests;

import java.util.Scanner;
import model.*;

public class TestCar {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Initializing Car...");

        System.out.print("Enter start X: ");
        int startX = scanner.nextInt();

        System.out.print("Enter start Y: ");
        int startY = scanner.nextInt();

        System.out.print("Enter start angle: ");
        double startAngle = scanner.nextDouble();

        Car car = new Car(startX, startY, startAngle);

        System.out.println("Testing acceleration...");
        car.updateMaxSpeed(10);
        car.updateAccel(1);
        System.out.println("Current speed after setting max speed 10: " + car.getSpeed());

        System.out.println("Testing movement...");
        System.out.println("Car position: (" + car.x + ", " + car.y + ")");

        System.out.println("Testing turning...");
        System.out.println("Car angle: " + car.getAngle());

        System.out.println("Loading car Silvia");
        car.loadFromJson("resources/cars/silvia.json");

        validateLoadedProperties(car);

        System.out.println("Car angle: " + car.getAngle());

        scanner.close();
    }

    private static void validateLoadedProperties(Car car) {
        String expectedType = "silvia";
        double expectedBrake = 3.5;
        double expectedFriction = 0.9;
        double expectedTurnRate = 3.0;
        
        double[] expectedGearSpeeds = {2.3, 2.9, 3.8};
        double[] expectedGearAccelerations = {0.3, 0.3, 0.1};

        if (!car.getType().equals(expectedType)) {
            System.err.println("Error: Expected type '" + expectedType + "', but got '" + car.getType() + "'");
        } else {
            System.out.println("Type is correct: " + car.getType());
        }

        if (car.getBrake() != expectedBrake) {
            System.err.println("Error: Expected brake '" + expectedBrake + "', but got '" + car.getBrake() + "'");
        } else {
            System.out.println("Brake is correct: " + car.getBrake());
        }

        if (car.getFriction() != expectedFriction) {
            System.err.println("Error: Expected friction '" + expectedFriction + "', but got '" + car.getFriction() + "'");
        } else {
            System.out.println("Friction is correct: " + car.getFriction());
        }

        if (car.getTurnRate() != expectedTurnRate) {
            System.err.println("Error: Expected turn rate '" + expectedTurnRate + "', but got '" + car.getTurnRate() + "'");
        } else {
            System.out.println("Turn rate is correct: " + car.getTurnRate());
        }

       for (int i = 0; i < expectedGearSpeeds.length; i++) {
           if (car.gearMaxSpeeds[i] != expectedGearSpeeds[i]) {
               System.err.println("Error: Expected gear speed for gear " + (i+1) 
                                  + " to be '" + expectedGearSpeeds[i] 
                                  + "', but got '" + car.gearMaxSpeeds[i] + "'");
           } else {
               System.out.println("Gear speed for gear " + (i+1) 
                                  + " is correct: " + car.gearMaxSpeeds[i]);
           }
           
           if (car.gearAccelerations[i] != expectedGearAccelerations[i]) {
               System.err.println("Error: Expected gear acceleration for gear " 
                                  + (i+1) 
                                  + " to be '" 
                                  + expectedGearAccelerations[i] 
                                  + "', but got '" 
                                  + car.gearAccelerations[i] + "'");
           } else {
               System.out.println("Gear acceleration for gear " 
                                  + (i+1) 
                                  + " is correct: " 
                                  + car.gearAccelerations[i]);
           }
       }
    }
}
