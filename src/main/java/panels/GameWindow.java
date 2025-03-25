package panels;

import java.awt.*;
import javax.swing.*;
import model.Car;

public class GameWindow extends JFrame {

    private MainMenu mainMenu;
    private GameFrame gameFrame;
    private LevelEditor levelEditor;
    private Shop shop;

    public GameWindow() {
        setTitle("2DRacer");
        setPreferredSize(new Dimension(1280, 720));
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon img = new ImageIcon("src/main/resources/textures/icon.png");
        setIconImage(img.getImage());
        mainMenu = new MainMenu(this);
        add(mainMenu, BorderLayout.CENTER);
    }

    public void startGame(String generationType, String trackName, String carType) {
        if (gameFrame != null) {
            remove(gameFrame);
        }
        gameFrame = new GameFrame(generationType, trackName, carType, this);
       //pauseController = new PauseController(gameFrame);
        
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
        if (shop != null) {
            remove(shop);
            shop = null;
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

    public void showShop(Shop shop){
        if(gameFrame!=null){
            remove(gameFrame);
            gameFrame = null;
        }
        if(this.shop != null){
            remove(this.shop);
        }
        this.shop = shop;
        add(this.shop, BorderLayout.CENTER);
        mainMenu.setVisible(false);
        revalidate();
        repaint();
    }
    public void showWindow() {
        pack();
        setVisible(true);
    }

    public Car getPlayerCar() {
        return gameFrame != null ? gameFrame.getCar() : null;
    }
}