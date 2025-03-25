package model;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Track {
    public Tile[][] track;
    public int borderX;
    public int borderY;
    public Tile start;
    public Tile finish;

    public Track() {
        this.borderX = 0;
        this.borderY = 0;
        this.track = new Tile[0][0];
    }

    public Track(int width, int height) {
        this.borderX = width;
        this.borderY = height;
        this.track = new Tile[width][height];
    }

        public void loadTrack(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String sizeLine = br.readLine();
            if (sizeLine == null) {
                throw new IOException("File is empty or missing dimensions line.");
            }

            String[] sizeParts = sizeLine.split(",");
            int cols = Integer.parseInt(sizeParts[0]);
            int rows = Integer.parseInt(sizeParts[1]);

            this.borderX = cols;
            this.borderY = rows;
            this.track = new Tile[cols][rows];

            for (int y = 0; y < rows; y++) {
                String line = br.readLine();
                if (line == null) {
                    throw new IOException("Missing or incomplete line in the file.");
                }

                line = line.trim();
                String[] parts = line.split(",");

                parts = Arrays.stream(parts).filter(part -> !part.isEmpty()).toArray(String[]::new);

                if (parts.length != cols) {
                    throw new IOException("Line " + y + " has incorrect column count. Expected: " + cols + ", Found: " + parts.length);
                }

                for (int x = 0; x < cols; x++) {
                    String type = parts[x].trim();

                    if (!type.equals("empty")) {
                        this.track[x][y] = new Tile(x, y, type);
                        if (type.equals("start")) {
                            start = new Tile(x, y, type);
                            System.out.println("Start point found at: (" + x + ", " + y + ")");
                        } else if (type.equals("finish")) {
                            finish = new Tile(x, y, type);
                            System.out.println("Finish point found at: (" + x + ", " + y + ")");
                        }
                    }
                }
            }

            if (start == null) {
                System.err.println("Start point not found in the track.");
                throw new IllegalStateException("Track must have a start point.");
            }
            if (finish == null) {
                System.err.println("Finish point not found in the track.");
                throw new IllegalStateException("Track must have a finish point.");
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number: " + e.getMessage());
        }
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || x >= borderX || y < 0 || y >= borderY) {
            return null;
        }
        return track[x][y];
    }

    public Tile[][] getTrack() {
        return this.track;
    }
}