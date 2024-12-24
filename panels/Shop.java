package panels;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import model.Player;

public class Shop extends JPanel {
    private Player player; 
    private int currentPage = 0; 
    private static final int[] carPrices = {1000, 2000};
    private static final String[] carTypes = {"ae86", "silvia"};
    private static final String[] shopBackgrounds = {"resources/textures/shop_ae86.png", "resources/textures/shop_silvia.png"};
    private GameWindow gameWindow;

    public Shop(Player player, GameWindow gameWindow) {
        this.player = player; 
        this.gameWindow = gameWindow;
        setLayout(new BorderLayout());
        updateShopUI();
    }

    private void updateShopUI() {
        removeAll(); 

        JLabel scoreLabel = new JLabel("Credit: " + player.getScore());
        scoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(scoreLabel, BorderLayout.NORTH);

        JLabel background = new JLabel(new ImageIcon(shopBackgrounds[currentPage]));
        add(background, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(true);
        buttonPanel.setBackground(new Color(0, 0, 0, 150));
        buttonPanel.setLayout(new FlowLayout());

        JButton prevButton = createNavigationButton("Prev", e -> {
            currentPage = (currentPage - 1 + carTypes.length) % carTypes.length;
            updateShopUI();
        });

        JButton nextButton = createNavigationButton("Next", e -> {
            currentPage = (currentPage + 1) % carTypes.length;
            updateShopUI();
        });

        JButton backToMenuButton = createNavigationButton("Main Menu", e -> {
            gameWindow.showMainMenu();
        });

        String carType = carTypes[currentPage];
        boolean owned = player.getOwnedCars().contains(carType);
        boolean isSelected = carType.equals(player.getSelectedCar());
        JButton actionButton = new JButton(owned ? (isSelected ? "Selected" : "Select") : "Purchase");

        actionButton.addActionListener(e -> handleCarAction(carType, owned));

        buttonPanel.add(prevButton);
        buttonPanel.add(actionButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(backToMenuButton);

        add(buttonPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private JButton createNavigationButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        return button;
    }

    private void handleCarAction(String carType, boolean owned) {
        if (owned) {
            if (!carType.equals(player.getSelectedCar())) {
                player.selectCar(carType);
                JOptionPane.showMessageDialog(this, "Car selected: " + carType);
            } else {
                JOptionPane.showMessageDialog(this, "Car is already been selected.");
            }
        } else {
            int price = carPrices[currentPage];
            if (player.spendScore(price)) {
                player.addCar(carType);
                JOptionPane.showMessageDialog(this, "You bought: " + carType);
            } else {
                JOptionPane.showMessageDialog(this, "Not enough credit.");
            }
        }
 
        updateShopUI();
    }
}
