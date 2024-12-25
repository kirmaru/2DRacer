package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;
public class TileTest {

    private Tile tile;

    @BeforeEach
    public void setUp() {
        tile = new Tile();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(0, tile.x);
        assertEquals(0, tile.y);
        assertEquals("0", tile.type);
        assertEquals(1.0, tile.friction);
        assertFalse(tile.isBarrier);
    }

    @Test
    public void testParameterizedConstructor() {
        Tile customTile = new Tile(5, 10, "grass");
        assertEquals(5, customTile.x);
        assertEquals(10, customTile.y);
        assertEquals("grass", customTile.type);
        assertEquals(0.3, customTile.friction);
        assertFalse(customTile.isBarrier);

        Tile barrierTile = new Tile(3, 4, "barrier");
        assertTrue(barrierTile.isBarrier);
    }

    @Test
    public void testSetFriction() {
        assertEquals(1.0, new Tile(0, 0, "north").friction);
        assertEquals(1.0, new Tile(0, 0, "south").friction);
        assertEquals(1.0, new Tile(0, 0, "east").friction);
        assertEquals(1.0, new Tile(0, 0, "west").friction);
        assertEquals(1.0, new Tile(0, 0, "start").friction);
        assertEquals(1.0, new Tile(0, 0, "finish").friction);
        assertEquals(0.3, new Tile(0, 0, "grass").friction);
    }

    @Test
    public void testIsPositionOccupied() {
        Tile[][] track = new Tile[5][5];
        
        Tile testTile1 = new Tile(2, 2, "grass");
        assertFalse(Tile.isPositionOccupied(testTile1, track));

        track[2][2] = testTile1;
        assertTrue(Tile.isPositionOccupied(testTile1, track));

        Tile outOfBoundsTile = new Tile(-1, -1, "grass");
        assertTrue(Tile.isPositionOccupied(outOfBoundsTile, track));
        
        outOfBoundsTile = new Tile(5, 5, "grass");
        assertTrue(Tile.isPositionOccupied(outOfBoundsTile, track));
    }
}
