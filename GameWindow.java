import common.Car;
import java.awt.*;
import javax.swing.*;

public class GameWindow extends JFrame {

    private MainMenu mainMenu;
    private GameFrame gameFrame;
    private LevelEditor levelEditor;

    public GameWindow() {
        setTitle("Racing Game");
        setPreferredSize(new Dimension(1280, 720));
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon img = new ImageIcon("textures/icon.png");
        setIconImage(img.getImage());
        mainMenu = new MainMenu(this);
        add(mainMenu, BorderLayout.CENTER);
    }

    public void startGame(String generationType, String trackName, String carType) {
        if (gameFrame != null) {
            remove(gameFrame);
        }
        gameFrame = new GameFrame(generationType, trackName, carType);
        add(gameFrame, BorderLayout.CENTER);
        mainMenu.setVisible(false);
        revalidate();
        repaint();
    }

    public void showMainMenu() {
        if (gameFrame != null) {
            remove(gameFrame);
            gameFrame = null;
        }
        if (levelEditor != null) {
            remove(levelEditor);
            levelEditor = null;
        }
        mainMenu.setVisible(true);
        revalidate();
        repaint();
    }

    public void showLevelEditor(LevelEditor levelEditor) {
        if (gameFrame != null) {
            remove(gameFrame);
            gameFrame = null;
        }
        if (this.levelEditor != null) {
            remove(this.levelEditor);
        }
        this.levelEditor = levelEditor;
        add(this.levelEditor, BorderLayout.CENTER);
        mainMenu.setVisible(false);
        revalidate();
        repaint();
    }

    public void showWindow() {
        pack();
        setVisible(true);
    }

    public Car getPlayerCar() {
        return gameFrame != null ? gameFrame.getCar() : null; // Assuming GameFrame has a getCar() method
    }
}