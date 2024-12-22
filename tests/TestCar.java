package tests;

import java.util.Scanner;
import common.*;

// Test Class for Car
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
        car.accelerate();
        System.out.println("Current speed: " + car.getSpeed());

        System.out.println("Testing movement...");
        car.move(1.0, new Tile[10][10]);
        System.out.println("Car position: (" + car.x + ", " + car.y + ")");

        System.out.println("Testing turning...");
        car.turnLeft();
        System.out.println("Car angle: " + car.getAngle());
    }
}
