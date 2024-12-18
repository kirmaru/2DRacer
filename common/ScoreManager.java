package common;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class ScoreManager {
    private int score;
    private static final String SCORE_FILE = "logs/score.json";
    private static final double SCORE_COEFFICIENT = 10.0; // Коэффициент расчета очков

    public ScoreManager() {
        loadScore();
    }

    public int getScore() {
        return score;
    }

    public void addPointsForTime(double timeInSeconds) {
        // Расчет очков: чем быстрее время, тем больше очков
        int points = (int) Math.max(0, SCORE_COEFFICIENT * (300 - timeInSeconds)); // 300 - максимальное время
        score += points;
        saveScore();
    }

    public void deductPoints(int points) {
        if (score >= points) {
            score -= points;
            saveScore();
        }
    }

    private void saveScore() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("score", score);

        File file = new File(SCORE_FILE);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(jsonObject.toString(4));
            System.out.println("Score saved to " + SCORE_FILE);
        } catch (IOException e) {
            System.err.println("Error saving score: " + e.getMessage());
        }
    }

    private void loadScore() {
        File file = new File(SCORE_FILE);
        if (file.exists()) {
            try {
                String content = new String(Files.readAllBytes(file.toPath()));
                JSONObject jsonObject = new JSONObject(content);
                score = jsonObject.getInt("score");
            } catch (IOException e) {
                System.err.println("Error loading score: " + e.getMessage());
            }
        } else {
            score = 0;
        }
    }
}
