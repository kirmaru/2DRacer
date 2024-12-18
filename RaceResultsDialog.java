import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RaceResultsDialog extends JDialog {

    public RaceResultsDialog(JFrame parent) {
        super(parent, "Результаты заездов", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane);

        loadRaceResults(textArea);
    }

    private void loadRaceResults(JTextArea textArea) {
        File resultsFile = new File("logs/race_results.json");
        if (resultsFile.exists()) {
            try {
                String content = new String(Files.readAllBytes(Paths.get(resultsFile.getPath())));
                JSONArray resultsArray = new JSONArray(content);

                StringBuilder resultsBuilder = new StringBuilder();
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject result = resultsArray.getJSONObject(i);
                    double elapsedTime = result.getDouble("elapsed_time");
                    String dateCompleted = result.getString("date_completed");
                    String trackName = result.getString("track_name");
                    String generationType = result.getString("generation_type");

                    resultsBuilder.append(String.format("Дата: %s\nТрасса: %s\nТип генерации: %s\nВремя: %.2f секунд\n\n",
                            dateCompleted, trackName, generationType, elapsedTime));
                }

                textArea.setText(resultsBuilder.toString());
            } catch (Exception e) {
                textArea.setText("Ошибка при загрузке результатов: " + e.getMessage());
            }
        } else {
            textArea.setText("Нет доступных результатов.");
        }
    }
}