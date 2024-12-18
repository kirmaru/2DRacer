package common;

import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class ScoreManager {
    private int score = 0; // Текущее количество очков
    private static final String SCORE_FILE = "logs/score.json";
    private static final int BASE_TIME = 300; // Базовое время для расчёта очков
    private static final double SCORE_MULTIPLIER = 10.0; // Множитель для расчёта очков

    // Конструктор: загружает очки из файла при создании объекта
    public ScoreManager() {
        loadScore();
    }

    // Возвращает текущее количество очков
    public int getScore() {
        return score;
    }

    // Добавляет очки за прохождение трассы
    public void addPointsForCompletion(double timeInSeconds) {
        // Чем меньше время прохождения, тем больше очков
        int pointsEarned = (int) Math.max(0, SCORE_MULTIPLIER * (BASE_TIME - timeInSeconds));
        score += pointsEarned;
        saveScore();
        System.out.println("Заработано очков: " + pointsEarned);
    }

    // Списывает очки при покупке
    public boolean spendPoints(int amount) {
        if (score >= amount) {
            score -= amount;
            saveScore();
            System.out.println("Списано очков: " + amount);
            return true;
        } else {
            System.out.println("Недостаточно очков для списания!");
            return false;
        }
    }

    // Сохраняет текущее количество очков в файл
    private void saveScore() {
        JSONObject json = new JSONObject();
        json.put("score", score);

        File file = new File(SCORE_FILE);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json.toString(4)); // Красивое форматирование JSON
            System.out.println("Очки сохранены в файл " + SCORE_FILE);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении очков: " + e.getMessage());
        }
    }

    // Загружает количество очков из файла
    private void loadScore() {
        File file = new File(SCORE_FILE);
        if (file.exists()) {
            try {
                String content = new String(Files.readAllBytes(file.toPath()));
                JSONObject json = new JSONObject(content);
                score = json.getInt("score");
                System.out.println("Очки загружены: " + score);
            } catch (IOException e) {
                System.err.println("Ошибка при загрузке очков: " + e.getMessage());
                score = 0;
            }
        } else {
            score = 0; // Если файла нет, начинаем с 0 очков
        }
    }

    
}