package tests;

import java.util.Scanner;
import common.*;

public class TestTrack {
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
