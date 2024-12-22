package panels;
import controls.CarController;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
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

    private String generationType;
    private String trackName;
    private RaceTimer raceTimer;
    private Timer timer;
    private boolean isPaused = false;

    private PauseMenuPanel pauseMenuPanel; 

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

        pauseMenuPanel = new PauseMenuPanel();
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

    private class PauseMenuPanel extends JPanel {
        private BufferedImage backgroundImage; 
        public PauseMenuPanel() {
            try {
                backgroundImage = ImageIO.read(new File("textures/background.png"));
            } catch (IOException e) {
                System.err.println("Ошибка загрузки изображения фона: " + e.getMessage());
            }

            setLayout(null);
            setBackground(new Color(0, 0, 0, 150)); 

            JButton resumeButton = new JButton("Продолжить");
            resumeButton.setBounds(540, 300, 200, 50);
            resumeButton.addActionListener(e -> resumeGame());
            add(resumeButton);

            JButton quitButton = new JButton("Выход");
            quitButton.setBounds(540, 400, 200, 50);
            quitButton.addActionListener(e -> System.exit(0));
            add(quitButton);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); 
                g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); 
            }
        }
    }
}
