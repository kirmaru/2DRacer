package common;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RaceTimer {
    public long startTime;
    private long endTime;
    private boolean running;
    private String trackName;
    private String generationType;
    private double seconds;
    private ScoreManager scoreManager;
    private Player player;

    public RaceTimer() {
        this.running = false;
    }
    
    public RaceTimer(String trackName, String generationType) {
        this.running = false;
        this.trackName = trackName;
        this.generationType = generationType;
        this.player = new Player();
        player.loadPlayerData();
        this.scoreManager = new ScoreManager(player);
    }

    public void start() {
        if (!running) {
            this.startTime = System.currentTimeMillis();
            this.running = true;
        }
    }

    public void stop() {
        if (running) {
            this.endTime = System.currentTimeMillis();
            this.running = false;
            printElapsedTime();
            saveResults(); 
            scoreManager.addPoints(seconds);
        }
    }

    public void reset() {
        this.startTime = 0;
        this.endTime = 0;
        this.running = false;
    }

    public void printElapsedTime() {
        long elapsedTime = endTime - startTime;
        this.seconds = elapsedTime / 1000.0;
        System.out.printf("Elapsed time: %.2f seconds%n", seconds);
    }

    private void saveResults() {
        long elapsedTime = endTime - startTime; 
        this.seconds = elapsedTime / 1000.0;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("elapsed_time", seconds);

        String dateCompleted = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        jsonObject.put("date_completed", dateCompleted);

        jsonObject.put("track_name", trackName);
        jsonObject.put("generation_type", generationType);

        File logsDir = new File("logs");
        if (!logsDir.exists()) {
            logsDir.mkdirs();
        }

        File resultsFile = new File(logsDir, "race_results.json");

        try {
            JSONArray resultsArray;

            if (resultsFile.exists()) {
                String content = new String(Files.readAllBytes(resultsFile.toPath()));
     
                if (content.trim().startsWith("[")) {
                    resultsArray = new JSONArray(content);
                } else {
                    resultsArray = new JSONArray();
                    resultsArray.put(new JSONObject(content));
                }
            } else {
                resultsArray = new JSONArray();
            }

            resultsArray.put(jsonObject);

            try (FileWriter fileWriter = new FileWriter(resultsFile)) {
                fileWriter.write(resultsArray.toString(4));
                System.out.println("Results saved to " + resultsFile.getAbsolutePath());
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isRunning() {
        return running;
    }

    public double getTime() {
        return seconds;
    }
}