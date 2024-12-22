package tests;

import java.util.Scanner;
import model.*;


public class TestRaceTimer {
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