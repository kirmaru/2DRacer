package common;

import java.util.Scanner;

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

// Test Class for Player
class TestPlayer {
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

// Test Class for RaceTimer
class TestRaceTimer {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Initializing RaceTimer...");

        System.out.print("Enter track name: ");
        String trackName = scanner.nextLine();

        System.out.print("Enter generation type: ");
        String generationType = scanner.nextLine();

        RaceTimer timer = new RaceTimer(trackName, generationType);

        System.out.println("Starting timer...");
        timer.start();
        Thread.sleep(2000); // Simulate elapsed time
        timer.stop();
        System.out.println("Elapsed time: " + timer.getTime() + " seconds");
    }
}

// Test Class for ScoreManager
class TestScoreManager {
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

// Test Class for Tile
class TestTile {
    public static void main(String[] args) {
        System.out.println("Initializing Tile...");

        Tile tile = new Tile(5, 5, "grass");
        System.out.println("Tile type: " + tile.type);
        System.out.println("Tile friction: " + tile.friction);
    }
}

// Test Class for Track
class TestTrack {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Initializing Track...");

        System.out.print("Enter track width: ");
        int width = scanner.nextInt();

        System.out.print("Enter track height: ");
        int height = scanner.nextInt();

        Track track = new Track(width, height);

        System.out.println("Testing track loading...");
        track.loadTrack("track.txt"); // Provide a valid file path
        Tile startTile = track.start;
        Tile finishTile = track.finish;
        System.out.println("Start tile: (" + startTile.x + ", " + startTile.y + ")");
        System.out.println("Finish tile: (" + finishTile.x + ", " + finishTile.y + ")");
    }
}
