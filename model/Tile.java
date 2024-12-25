package model;

public class Tile {
    public int x;
    public int y;
    public String type;
    public double friction;
    public boolean isBarrier;

    public Tile() {
        x = 0;
        y = 0;
        type = "0";
        friction = 1.0;
        isBarrier = false;
    }

    public Tile(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.friction = setFriction(type);
        this.isBarrier = type.equals("barrier"); 
    }

    public static boolean isPositionOccupied(Tile tile, Tile[][] track) {
        if (tile.x < 0 || tile.x >= track.length || tile.y < 0 || tile.y >= track[0].length) {
            return true; 
        }
        return track[tile.x][tile.y] != null; 
    }

    private double setFriction(String type) {
        switch (type) {
            case "north":
            case "south":
            case "east":
            case "west":
            case "start":
            case "finish":
                return 1.0; 
            case "grass":
                return 0.3; 
        }
        return 1.0;
    }

}
