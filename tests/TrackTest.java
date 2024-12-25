package tests;

import model.Track;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrackTest {
    private Track track;

    @BeforeEach
    void setUp() {
        track = new Track();
    }

    @Test
    void testLoadTrackFromFile() {
        String filename = "resources/tracks/first.txt"; 
        track.loadTrack(filename);

        assertNotNull(track.start, "Start point should not be null");
        System.out.println("Start point is at: (" + track.start.x + ", " + track.start.y + ")");

        assertNotNull(track.finish, "Finish point should not be null");
        System.out.println("Finish point is at: (" + track.finish.x + ", " + track.finish.y + ")");

        assertTrue(track.borderX > 0, "Track borderX should be greater than 0");
        assertTrue(track.borderY > 0, "Track borderY should be greater than 0");
        
        System.out.println("Track dimensions: " + track.borderX + " x " + track.borderY);
    }
}
