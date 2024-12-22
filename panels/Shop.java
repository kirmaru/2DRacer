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
    private static final String[] shopBackgrounds = {"textures/shop_ae86.png", "textures/shop_silvia.png"};

    public Shop(Player player) {
        this.player = player; 
        setLayout(new BorderLayout());
        updateShopUI();
    }

    private void updateShopUI() {
        removeAll(); 

        JLabel background = new JLabel(new ImageIcon(shopBackgrounds[currentPage]));
        add(background, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        JButton prevButton = createNavigationButton("Назад", e -> {
            currentPage = (currentPage - 1 + carTypes.length) % carTypes.length;
            updateShopUI();
        });

        JButton nextButton = createNavigationButton("Вперед", e -> {
            currentPage = (currentPage + 1) % carTypes.length;
            updateShopUI();
        });

        String carType = carTypes[currentPage];
        boolean owned = player.getOwnedCars().contains(carType);
        boolean isSelected = carType.equals(player.getSelectedCar());
        JButton actionButton = new JButton(owned ? (isSelected ? "Выбрано" : "Выбрать") : "Купить");

        actionButton.addActionListener(e -> handleCarAction(carType, owned));

        buttonPanel.add(prevButton);
        buttonPanel.add(actionButton);
        buttonPanel.add(nextButton);

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
                JOptionPane.showMessageDialog(this, "Машина выбрана: " + carType);
            } else {
                JOptionPane.showMessageDialog(this, "Эта машина уже выбрана.");
            }
        } else {
            int price = carPrices[currentPage];
            if (player.spendScore(price)) {
                player.addCar(carType);
                JOptionPane.showMessageDialog(this, "Вы купили: " + carType);
            } else {
                JOptionPane.showMessageDialog(this, "Недостаточно очков для покупки.");
            }
        }
        updateShopUI();
    }
}
