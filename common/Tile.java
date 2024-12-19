package common;

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

    // public static Tile generateTile(Tile tile, Tile[][] track) {
    //     Random rand = new Random();
    //     Tile newTile = new Tile(tile.x, tile.y, tile.type);
        
    //     switch (tile.type) { //сделать в каждом случае массив, который содержит вероятностьи генерации каждого типа дороги
    //         case "north" -> {
    //             String[] typeSelect = {"north", "east", "west"};
    //             int typeNumber = rand.nextInt(3);
    //             newTile.y++; 
    //             newTile.type = typeSelect[typeNumber];
    //         }
    //         case "south" -> {
    //             String[] typeSelect = {"south", "east", "west"};
    //             int typeNumber = rand.nextInt(3);
    //             newTile.y--;
    //             newTile.type = typeSelect[typeNumber];
    //         }
    //         case "east" -> {
    //             String[] typeSelect = {"north", "south", "east"};
    //             int typeNumber = rand.nextInt(3);
    //             newTile.x++; 
    //             newTile.type = typeSelect[typeNumber];
    //         }
    //         case "west" -> {
    //             String[] typeSelect = {"north", "south", "west"};
    //             int typeNumber = rand.nextInt(3);
    //             newTile.x--; 
    //             newTile.type = typeSelect[typeNumber];
    //         }
    //     }

    //     if (isPositionOccupied(newTile, track)) {
    //         return null; 
    //     }
    //     return newTile; 
    // }

    private static boolean isPositionOccupied(Tile tile, Tile[][] track) {
        if (tile.x < 0 || tile.x >= track.length || tile.y < 0 || tile.y >= track[0].length) {
            return true; 
        }
        return track[tile.x][tile.y] != null; 
    }

    // нужно добавить поддержку новых типов 
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
