package panels;

import controls.CarController;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import model.Car;
import model.RaceTimer;
import model.Tile;
import model.Track;
import view.*;

public class GameFrame extends JLayeredPane {

    private static final double TIME_STEP = 0.02;
    private double deltaTime = 0;
    private Car car;
    private GameWindow gameWindow;
    private String generationType;
    private String trackName;
    private RaceTimer raceTimer;
    private Timer timer;
    private boolean isPaused = false;

    private PauseMenuPanel pauseMenuPanel; 

    public GameFrame(String generationType, String trackName, String carType, GameWindow gameWindow) {
        this.generationType = generationType;
        this.trackName = trackName;
        this.gameWindow = gameWindow;

        Track track = new Track(0, 0);

        if ("text".equals(generationType)) {
            track.loadTrack("src/main/resources/tracks/" + trackName);
            System.out.println("Загрузка трассы из текстового файла: " + trackName);
        }
        
        car = new Car(track.start.x - 1, track.start.y, 0);
        car.loadFromJson("src/main/resources/cars/" + carType + ".json");

        raceTimer = new RaceTimer(trackName, generationType);

        TrackView view = new TrackView(track, car, 2.8);
        HUDView hudView = new HUDView(car, raceTimer);
        CarController controller = new CarController(car);

        view.setBounds(0, 0, 1280, 720);
        hudView.setBounds(0, 0, 1280, 720);

        add(view, Integer.valueOf(0));
        add(hudView, Integer.valueOf(1));

        addKeyListener(controller);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    if (isPaused) {
                        resumeGame();
                    } else {
                        pauseGame();
                    }
                }
            }
        });

        pauseMenuPanel = new PauseMenuPanel(
            e -> resumeGame(),
            e -> {
                resumeGame();
                gameWindow.showMainMenu();
            }
        );
        
        pauseMenuPanel.setBounds(0, 0, 1280, 720);
        pauseMenuPanel.setVisible(false); 
        add(pauseMenuPanel, Integer.valueOf(2));

        timer = new Timer(20, e -> {
            if (!isPaused) {
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
                            showRaceResults();
                            break;
                        }
                    }
                } else {
                    car.move(TIME_STEP, track.getTrack());

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

    public void pauseGame() {
        if (!isPaused) {
            isPaused = true;
            raceTimer.pause();
            pauseMenuPanel.setVisible(true);
        }
    }

    public void resumeGame() {
        if (isPaused) {
            isPaused = false;
            raceTimer.resume();
            pauseMenuPanel.setVisible(false);  
        }
    }

    public Car getCar() {
       return car;
   }

   private void showRaceResults() {
       String resultsText =
           "Результаты заезда:\n" +
           "Время: " + String.format("%.2f", raceTimer.getTime()) + " секунд\n" +
           "Трасса: " + trackName + "\n" +
           "Тип генерации: " + generationType;

       pauseMenuPanel.displayResults(resultsText); 
       pauseGame();
   }
}
