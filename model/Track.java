package model;
import java.io.BufferedReader; // чтение картинки буфер
import java.io.FileReader; // чтение файла буфер
import java.io.IOException; // тоже
import java.util.Arrays; //для чтения файла

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

    public void loadTrackFromImage(String filename) {
        try {
            BufferedImage image = ImageIO.read(new File(filename));
            int width = image.getWidth();
            int height = image.getHeight();

            this.borderX = width;
            this.borderY = height;
            this.track = new Tile[width][height];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);

                    int red = (rgb >> 16) & 0xFF;
                    int green = (rgb >> 8) & 0xFF;
                    int blue = rgb & 0xFF;

                    String type = "0"; 
    
                    if (red == 255 && green == 0 && blue == 0) {
                        type = "down_left"; // Красный
                    } else if (red == 255 && green == 255 && blue == 0) {
                        type = "down_right"; // Желтый
                    } else if (green == 255 && red == 0 && blue == 0) {
                        type = "up_straight"; // Зеленый
                    } else if (blue == 255 && red == 0 && green == 0) {
                        type = "down_straight"; // Синий
                    } else if (red == 255 && green == 255 && blue == 128) {
                        type = "left_straight"; // Светло-желтый
                    } else if (red == 128 && green == 0 && blue == 255) {
                        type = "right_straight"; // Фиолетовый
                    } else if (red == 139 && green == 69 && blue == 19) {
                        type = "up_left"; // Темно-красный
                    } else if (red == 0 && green == 128 && blue == 0) {
                        type = "up_right"; // Темно-зеленый
                    } else if (red == 0 && green == 0 && blue == 139) {
                        type = "left_up"; // Темно-синий
                    } else if (red == 135 && green == 206 && blue == 250) {
                        type = "left_down"; // Светло-синий
                    } else if (red == 255 && green == 20 && blue == 147) {
                        type = "right_up"; // Розовый
                    } else if (red == 75 && green == 0 && blue == 130) {
                        type = "right_down"; // Светло-фиолетовый
                    } else if (red == 255 && green == 105 && blue == 180) {
                        type = "start"; // Розовый пиксель - стартовая линия
                        start = new Tile(x, height - 1 - y, type);
                    } else if (red == 0 && green == 0 && blue == 0) {
                        type = "finish"; // Черный пиксель - финишная линия
                        finish = new Tile(x, height - 1 - y, type);
                    } else {
                        type = "grass"; // Остальные цвета - трава по умолчанию
                    }

                    if (!type.equals("0")) {
                        this.track[x][height - 1 - y] = new Tile(x, height - 1 - y, type); // Инвертируем Y
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка загрузки изображения: " + e.getMessage());
        }
    }
/////////////////////////
    // public void generateTrack(Tile startingPoint) {
    //     track[startingPoint.x][startingPoint.y] = startingPoint;
        
    //     Tile newTile = Tile.generateTile(startingPoint, track);
        
    //     while (newTile != null && (newTile.x < borderX && newTile.x >= 0) && (newTile.y < borderY && newTile.y >= 0)) {
    //         track[newTile.x][newTile.y] = newTile;

    //         newTile = Tile.generateTile(newTile, track);
    //     }
    // }

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