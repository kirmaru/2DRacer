package panels;
import common.Car;
import common.RaceTimer;
import common.Tile;
import common.Track;
import controls.CarController;
import javax.swing.*;
import render.*;

public class GameFrame extends JLayeredPane {

    private static final double TIME_STEP = 0.02;
    private double deltaTime = 0;
    private Car car;

    private String generationType;
    private String trackName;
    private RaceTimer raceTimer;
    private Timer timer;             // Игровой таймер
    private boolean isPaused = false;

    public GameFrame(String generationType, String trackName, String carType) {
        this.generationType = generationType;
        this.trackName = trackName;

        Track track = new Track(0, 0);

        if ("random".equals(generationType)) {
            System.out.println("Случайная генерация трассы.");
        } else if ("image".equals(generationType)) {
            track.loadTrackFromImage("tracks/" + trackName);
            System.out.println("Загрузка трассы из изображения: " + trackName);
        } else if ("text".equals(generationType)) {
            track.loadTrack("tracks/" + trackName);
            System.out.println("Загрузка трассы из текстового файла: " + trackName);
        }

        car = new Car(track.start.x, track.start.y, 0);
        car.loadFromJson("cars/" + carType + ".json");

        raceTimer = new RaceTimer(trackName, generationType);

        TrackView view = new TrackView(track, car, 2.8);
        HUDView hudView = new HUDView(car, raceTimer);
        CarController controller = new CarController(car);

        view.setBounds(0, 0, 1280, 720);
        hudView.setBounds(0, 0, 1280, 720);

        add(view, Integer.valueOf(0));
        add(hudView, Integer.valueOf(1));

        addKeyListener(controller);

        timer = new Timer(20, e -> {
            if (!isPaused) { // Обновляем только если игра не на паузе
                controller.update();

                if (raceTimer.isRunning()) {
                    deltaTime += TIME_STEP;

                    while (deltaTime >= TIME_STEP) {
                        car.move(TIME_STEP, track.getTrack());
                        deltaTime -= TIME_STEP;

                        int tileX = (int) Math.round(car.x);
                        int tileY = (int) Math.round(car.y);

                        Tile currentTile = null;
                        if (tileX >= 0 && tileX < track.borderX && tileY >= 0 && tileY < track.borderY) {
                            currentTile = track.getTile(tileX, tileY);
                        }

                        if (currentTile != null && "finish".equals(currentTile.type)) {
                            raceTimer.stop();
                        }
                    }
                } else {
                    int tileX = (int) Math.round(car.x);
                    int tileY = (int) Math.round(car.y);

                    Tile currentTile = null;
                    if (tileX >= 0 && tileX < track.borderX && tileY >= 0 && tileY < track.borderY) {
                        currentTile = track.getTile(tileX, tileY);
                    }

                    if (currentTile != null && "start".equals(currentTile.type)) {
                        raceTimer.start();
                    }
                }
                view.repaint();
            }
        });

        timer.start();
        setFocusable(true);
    }
    

    // Метод для паузы игры
    public void pauseGame() {
        if (!isPaused) {
            raceTimer.stop(); // Останавливаем таймер гонки
            isPaused = true;  // Меняем флаг на паузу
        }
    }

    // Метод для возобновления игры
    public void resumeGame() {
        if (isPaused) {
            raceTimer.start(); // Возобновляем таймер гонки
            isPaused = false;  // Меняем флаг на возобновление
        }
    }

    public Car getCar() {
        return car;
    }
}
