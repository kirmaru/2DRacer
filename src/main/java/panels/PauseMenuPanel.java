package panels;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class PauseMenuPanel extends JPanel {
    private BufferedImage backgroundImage;
    private JLabel resultsLabel;

    public PauseMenuPanel(ActionListener resumeAction, ActionListener mainMenuAction) {
        try {
            backgroundImage = ImageIO.read(new File("src/main/resources/textures/pause.png"));
        } catch (IOException e) {
            System.err.println("Ошибка загрузки изображения фона: " + e.getMessage());
        }

        setLayout(new GridBagLayout());
        setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        JButton resumeButton = new JButton("Continue");
        gbc.gridy = 0;
        add(resumeButton, gbc);
        resumeButton.addActionListener(resumeAction);

        JButton mainMenuButton = new JButton("To Main Menu");
        gbc.gridy = 1;
        add(mainMenuButton, gbc);
        mainMenuButton.addActionListener(mainMenuAction);

        JButton quitButton = new JButton("Exit");
        gbc.gridy = 2;
        add(quitButton, gbc);
        quitButton.addActionListener(e -> System.exit(0));

        resultsLabel = new JLabel("", SwingConstants.CENTER);
        resultsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 3;
        add(resultsLabel, gbc);
    }

    public void displayResults(String results) {
        resultsLabel.setText("<html>" + results.replaceAll("\n", "<br>") + "</html>");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        }
    }
}
