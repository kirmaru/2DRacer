package tests;

import model.*;

public class TrackTest {
    public static void main(String[] args) {
        testLoadTrackFromFile("resources/tracks/first.txt");
    }

    private static void testLoadTrackFromFile(String filename) {
        Track track = new Track();
        track.loadTrack(filename);

        if (track.start != null) {
            System.out.println("Start point is at: (" + track.start.x + ", " + track.start.y + ")");
        } else {
            System.err.println("Start point not found.");
        }

        if (track.finish != null) {
            System.out.println("Finish point is at: (" + track.finish.x + ", " + track.finish.y + ")");
        } else {
            System.err.println("Finish point not found.");
        }

        System.out.println("Track dimensions: " + track.borderX + " x " + track.borderY);
    }
}
