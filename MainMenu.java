import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.sound.sampled.*;
import javax.swing.*;
//ошибка
public class MainMenu extends JLayeredPane {

    private GameWindow gameWindow;

    public MainMenu(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        setLayout(new BorderLayout());
        JLayeredPane layeredPane = new JLayeredPane();
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setBounds(0, 0, 1280, 720); 
        
        layeredPane.add(backgroundPanel, Integer.valueOf(0));
        playBackgroundMusic("soundtrack/MYRONE_Exclusive_Coupe.wav");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20)); // 20 пикселей между кнопками

        buttonPanel.setBackground(new Color(0, 0, 0, 0));
        buttonPanel.setOpaque(false); 

        JButton newGameButton = createImageButton("textures/new_game.png", "Новая игра");
        JButton viewResultsButton = createImageButton("textures/records.png", "Просмотр результатов");
        JButton exitButton = createImageButton("textures/exit.png", "Выход");
        JButton editTrackButton = createImageButton("textures/level_edit.png", "Редактор трасс");
        JButton shop = createImageButton("textures/shop.png", "shop");

        buttonPanel.add(newGameButton);
        buttonPanel.add(editTrackButton);
        buttonPanel.add(viewResultsButton);
        buttonPanel.add(shop);
        buttonPanel.add(exitButton);

        buttonPanel.setBounds(0, 450, 1280, 720); 
        //buttonPanel.setBorder(BorderFactory.createEmptyBorder(00, 0, 00, 0));

        layeredPane.add(buttonPanel, Integer.valueOf(1));

        add(layeredPane);
    }

    private JButton createImageButton(String imagePath, String actionCommand) {
        ImageIcon icon = new ImageIcon(imagePath);
        JButton button = new JButton(icon) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(icon.getIconWidth(), icon.getIconHeight());
            }
        };

        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setActionCommand(actionCommand);
        button.setBackground(new Color(0, 0, 0, 0));
        button.setOpaque(false); 

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                playSound("soundtrack/button_pick.wav");
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                playSound("soundtrack/button_pick.wav");
            }
        });

        button.addActionListener(e -> {
            switch (actionCommand) {
                case "Новая игра":
                    showGameOptions();
                    break;
                case "Выход":
                    System.exit(0);
                    break;
                case "Просмотр результатов":
                    showRaceResults();
                    break;
                case "Редактор трасс":
                    openTrackEditor();
                    break;
                case "shop":
                    openShop();
            }
        });

        return button;
    }

    private void openTrackEditor() {
        LevelEditor levelEditor = new LevelEditor();
        gameWindow.showLevelEditor(levelEditor);
    }

    private void openShop() {
        Shop shop = new Shop(gameWindow.getPlayerCar()); // Assuming you have a method to get player's car
        JFrame shopFrame = new JFrame("Магазин");
        shopFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        shopFrame.setContentPane(shop);
        shopFrame.setSize(1280, 720);
        shopFrame.setVisible(true);
    }

    private void showGameOptions() {
        String[] options = {"Генерация с картинки", "Генерация с текстового файла"};

        int choice = JOptionPane.showOptionDialog(this,
                "Выберите способ генерации трассы:",
                "Опции генерации",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 0 || choice == 1) {
            showTrackSelection(choice == 0 ? "image" : "text");
        }
    }

    private void showTrackSelection(String type) {
        File trackDir = new File("tracks");
        String[] tracks = trackDir.list((dir, name) -> name.endsWith(type.equals("image") ? ".png" : ".txt"));

        if (tracks != null && tracks.length > 0) {
            String selectedTrack = (String) JOptionPane.showInputDialog(this,
                    "Выберите трассу:",
                    "Выбор трассы",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    tracks,
                    tracks[0]);

            if (selectedTrack != null) {
                gameWindow.startGame(type, selectedTrack, "silvia");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Нет доступных карт в папке 'tracks'.");
        }
    }

    private void showRaceResults() {
        RaceResultsDialog dialog = new RaceResultsDialog((JFrame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
    }

    private class BackgroundPanel extends JPanel {

        private Image backgroundImage;

        public BackgroundPanel() {
            backgroundImage = Toolkit.getDefaultToolkit().getImage("textures/background.png");
            setLayout(new BorderLayout());
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void playBackgroundMusic(String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playSound(String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}