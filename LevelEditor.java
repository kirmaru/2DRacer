import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.*;

public class LevelEditor extends JPanel {
    private static final int BLOCK_SIZE = 50;
    private int mapWidth = 10;
    private int mapHeight = 10;
    private BufferedImage[][] map;
    private HashMap<String, BufferedImage> blockImages;
    private String selectedBlock;
    private JTextField levelNameField;
    private JTextField widthField;
    private JTextField heightField;

    public LevelEditor() {
        setLayout(new BorderLayout());
        map = new BufferedImage[mapWidth][mapHeight];
        blockImages = loadBlockImages();

        BlockPanel blockPanel = new BlockPanel();
        MapPanel mapPanel = new MapPanel();

        JScrollPane scrollPane = new JScrollPane(mapPanel);
        scrollPane.setPreferredSize(new Dimension(600, 600));

        add(blockPanel, BorderLayout.EAST);
        add(scrollPane, BorderLayout.CENTER);
    }

    private HashMap<String, BufferedImage> loadBlockImages() {
        HashMap<String, BufferedImage> blocks = new HashMap<>();
        String[] blockTypes = {"down_left", "down_right", "down_straight", "up_straight",
                "up_left", "up_right", "left_straight", "left_up",
                "left_down", "right_straight", "right_down", "right_up",
                "grass", "finish", "start", "barrier"};

        for (String type : blockTypes) {
            try {
                BufferedImage img = loadImage("textures/tiles/" + type + ".png");
                if (img != null) {
                    blocks.put(type, img);
                }
            } catch (IOException e) {
                System.err.println("Failed to load image: " + type);
                e.printStackTrace();
            }
        }
        return blocks;
    }

    private BufferedImage loadImage(String path) throws IOException {
        File imgFile = new File(path);
        if (!imgFile.exists()) {
            throw new IOException("File not found: " + path);
        }
        BufferedImage img = ImageIO.read(imgFile);
        if (img == null) {
            throw new IOException("Failed to load image: " + path);
        }
        return img;
    }

    private class BlockPanel extends JPanel {
        public BlockPanel() {
            setLayout(new GridLayout(0, 1));

            levelNameField = new JTextField("Название трассы");
            add(levelNameField);

            widthField = new JTextField(String.valueOf(mapWidth), 5);
            heightField = new JTextField(String.valueOf(mapHeight), 5);
            JButton resizeButton = new JButton("Изменить размер");
            resizeButton.addActionListener(new ResizeAction());

            JPanel sizePanel = new JPanel();
            sizePanel.add(new JLabel("Ширина:"));
            sizePanel.add(widthField);
            sizePanel.add(new JLabel("Высота:"));
            sizePanel.add(heightField);
            sizePanel.add(resizeButton);

            add(sizePanel);

            for (String type : blockImages.keySet()) {
                JButton button = new JButton(type);
                button.setIcon(new ImageIcon(blockImages.get(type).getScaledInstance(BLOCK_SIZE, BLOCK_SIZE, Image.SCALE_SMOOTH)));
                button.addActionListener(e -> selectedBlock = type);
                add(button);
            }

            JButton saveButton = new JButton("Сохранить трассу");
            saveButton.addActionListener(new SaveAction());
            add(saveButton);

            JButton loadButton = new JButton("Загрузить трассу");
            loadButton.addActionListener(new LoadAction());
            add(loadButton);

            JButton loadFromListButton = new JButton("Загрузить трассу из списка");
            loadFromListButton.addActionListener(new LoadFromListAction());
            add(loadFromListButton);

            JButton newLevelButton = new JButton("Создать новую трассу");
            newLevelButton.addActionListener(new NewLevelAction());
            add(newLevelButton);

            JButton backToMenuButton = new JButton("Назад в меню");
            backToMenuButton.addActionListener(e -> {
                ((GameWindow) SwingUtilities.getWindowAncestor(LevelEditor.this)).showMainMenu();
            });
            add(backToMenuButton);
        }
    }

    private class ResizeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int newWidth = Integer.parseInt(widthField.getText().trim());
                int newHeight = Integer.parseInt(heightField.getText().trim());

                if (newWidth > 0 && newHeight > 0) {
                    mapWidth = newWidth;
                    mapHeight = newHeight;
                    map = new BufferedImage[mapWidth][mapHeight];
                    repaint();
                    JOptionPane.showMessageDialog(LevelEditor.this, "Размер трассы изменен!", "Изменение размера", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(LevelEditor.this, "Ширина и высота должны быть больше нуля.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(LevelEditor.this, "Введите корректные числовые значения.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class SaveAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String levelName = levelNameField.getText().trim();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("tracks/" + levelName + ".txt"))) {
                writer.write(mapWidth + "," + mapHeight);
                writer.newLine();

                for (int y = 0; y < mapHeight; y++) {
                    for (int x = 0; x < mapWidth; x++) {
                        if (map[x][y] != null) {
                            writer.write(getBlockType(map[x][y]) + ",");
                        } else {
                            writer.write("empty,");
                        }
                    }
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(LevelEditor.this, "Трасса успешно сохранена!", "Сохранение", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(LevelEditor.this, "Ошибка при сохранении трассы: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }

        private String getBlockType(BufferedImage image) {
            for (String key : blockImages.keySet()) {
                if (blockImages.get(key).equals(image)) {
                    return key;
                }
            }
            return "unknown";
        }
    }

    private class LoadAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String levelName = levelNameField.getText().trim();

            try (BufferedReader reader = new BufferedReader(new FileReader(levelName + ".txt"))) {
                String line;

                line = reader.readLine();
                String[] sizes = line.split(",");
                if (sizes.length == 2) {
                    mapWidth = Integer.parseInt(sizes[0].trim());
                    mapHeight = Integer.parseInt(sizes[1].trim());
                    map = new BufferedImage[mapWidth][mapHeight];
                }

                int y = 0;
                while ((line = reader.readLine()) != null && y < mapHeight) {
                    String[] tokens = line.split(",");
                    for (int x = 0; x < mapWidth && x < tokens.length; x++) {
                        if (!tokens[x].trim().equals("empty")) {
                            map[x][y] = blockImages.get(tokens[x].trim());
                        } else {
                            map[x][y] = null;
                        }
                    }
                    y++;
                }
                repaint();
                JOptionPane.showMessageDialog(LevelEditor.this, "Трасса успешно загружена!", "Загрузка", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(LevelEditor.this, "Ошибка при загрузке трассы: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(LevelEditor.this, "Ошибка в формате файла: размеры не корректны.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private class LoadFromListAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));

            int result = fileChooser.showOpenDialog(LevelEditor.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                    String line;

                    line = reader.readLine();
                    String[] sizes = line.split(",");
                    if (sizes.length == 2) {
                        mapWidth = Integer.parseInt(sizes[0].trim());
                        mapHeight = Integer.parseInt(sizes[1].trim());
                        map = new BufferedImage[mapWidth][mapHeight];
                    }

                    int y = 0;
                    while ((line = reader.readLine()) != null && y < mapHeight) {
                        String[] tokens = line.split(",");
                        for (int x = 0; x < mapWidth && x < tokens.length; x++) {
                            if (!tokens[x].trim().equals("empty")) {
                                map[x][y] = blockImages.get(tokens[x].trim());
                            } else {
                                map[x][y] = null;
                            }
                        }
                        y++;
                    }
                    repaint();
                    JOptionPane.showMessageDialog(LevelEditor.this, "Трасса успешно загружена из файла!", "Загрузка", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(LevelEditor.this, "Ошибка при загрузке трассы: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(LevelEditor.this, "Ошибка в формате файла: размеры не корректны.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        }
    }

    private class NewLevelAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int response = JOptionPane.showConfirmDialog(LevelEditor.this,
                    "Вы уверены, что хотите создать новую трассу? Все изменения будут потеряны.",
                    "Создание новой трассы",
                    JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                map = new BufferedImage[mapWidth][mapHeight];
                repaint();
                levelNameField.setText("Название трассы");
                selectedBlock = null;
                JOptionPane.showMessageDialog(LevelEditor.this, "Новая трасса создана!", "Создание новой трассы", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private class MapPanel extends JPanel {
        public MapPanel() {
            setPreferredSize(new Dimension(mapWidth * BLOCK_SIZE, mapHeight * BLOCK_SIZE));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int x = e.getX() / BLOCK_SIZE;
                    int y = e.getY() / BLOCK_SIZE;

                    if (selectedBlock != null && x >= 0 && x < mapWidth && y >= 0 && y < mapHeight) {
                        map[x][y] = blockImages.get(selectedBlock);
                        repaint();
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            for (int x = 0; x < mapWidth; x++) {
                for (int y = 0; y < mapHeight; y++) {
                    if (map[x][y] != null) {
                        g.drawImage(map[x][y], x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, null);
                        g.setColor(Color.RED);
                        g.drawRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE - 1, BLOCK_SIZE - 1);
                    } else {
                        g.setColor(Color.GRAY);
                        g.drawRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE - 1, BLOCK_SIZE - 1);
                    }
                }
            }
        }
    }
}
