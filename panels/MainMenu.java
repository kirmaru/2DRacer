package panels;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.sound.sampled.*;
import javax.swing.*;
import model.Player;

public class MainMenu extends JLayeredPane {

    private GameWindow gameWindow;
    private Player player;

    public MainMenu(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        this.player = new Player();
        setLayout(new BorderLayout());
        
        JLayeredPane layeredPane = new JLayeredPane();
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setBounds(0, 0, 1280, 720); 
        
        layeredPane.add(backgroundPanel, Integer.valueOf(0));
        playBackgroundMusic("resources/sounds/soundtrack/MYRONE_Exclusive_Coupe.wav");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(0, 0, 0, 0));
        buttonPanel.setOpaque(false); 

        JButton newGameButton = createImageButton("resources/textures/new_game.png", "Новая игра");
        JButton viewResultsButton = createImageButton("resources/textures/records.png", "Просмотр результатов");
        JButton exitButton = createImageButton("resources/textures/exit.png", "Выход");
        JButton editTrackButton = createImageButton("resources/textures/level_edit.png", "Редактор трасс");
        JButton shopButton = createImageButton("resources/textures/shop.png", "shop");

        buttonPanel.add(newGameButton);
        buttonPanel.add(editTrackButton);
        buttonPanel.add(viewResultsButton);
        buttonPanel.add(shopButton);
        buttonPanel.add(exitButton);

        buttonPanel.setBounds(0, 450, 1280, 720); 
       
        layeredPane.add(buttonPanel, Integer.valueOf(1));

        // Создание кнопки FAQ
        JButton faqButton = createImageButton("resources/textures/faq_icon.png", "faq");
        faqButton.setBounds(10,10,60,80);
        layeredPane.add(faqButton, Integer.valueOf(2));

        add(layeredPane);
    }

    private JButton createFAQButton() {
        JButton faqButton = new JButton();
        faqButton.setIcon(new ImageIcon("resources/textures/faq_icon.png"));
        faqButton.setPreferredSize(new Dimension(50, 50));
        faqButton.setContentAreaFilled(false);
        faqButton.setBorderPainted(false);
        
        faqButton.addActionListener(e -> showFAQ());

        faqButton.setBounds(10, 10, 50, 50);

        return faqButton;
    }

    private void showFAQ() {
        JFrame faqFrame = new JFrame("FAQ");
        JLabel label = new JLabel(new ImageIcon("resources/textures/faq.png"));
        faqFrame.add(label);
        
        faqFrame.setSize(600, 400);
        faqFrame.setLocationRelativeTo(this);
        faqFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        faqFrame.setVisible(true);
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
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                playSound("resources/sounds/soundtrack/button_pick.wav");
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                playSound("resources/sounds/soundtrack/button_pick.wav");
            }
        });

        button.addActionListener(e -> {
            switch (actionCommand) {
                case "Новая игра":
                    showTrackSelection();
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
                    break;
                case "faq":
                    showFAQ();
                    break;
            }
        });

        return button;
    }

    private void openTrackEditor() {
        LevelEditor levelEditor = new LevelEditor();
        gameWindow.showLevelEditor(levelEditor);
    }

    private void openShop() {
        Shop shop = new Shop(player, gameWindow);
        gameWindow.showShop(shop);
    }

    private void showTrackSelection() {
        File trackDir = new File("resources/tracks");
        String[] tracks = trackDir.list((dir, name) -> name.endsWith(".png") || name.endsWith(".txt"));

        if (tracks != null && tracks.length > 0) {
            String selectedTrack = (String) JOptionPane.showInputDialog(this,
                    "Выберите трассу:",
                    "Выбор трассы",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    tracks,
                    tracks[0]);

            if (selectedTrack != null && !player.getSelectedCar().equals("null")) {
                String type = selectedTrack.endsWith(".png") ? "image" : "text";
                gameWindow.startGame(type, selectedTrack, player.getSelectedCar());
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
           backgroundImage = Toolkit.getDefaultToolkit().getImage("resources/textures/background.png");
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
