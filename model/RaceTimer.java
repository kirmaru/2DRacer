package model;

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
    private long pausedTime;  
    private long totalPausedDuration;
    private long endTime;
    private boolean running;
    private boolean paused;
    private String trackName;
    private String generationType;
    private double seconds;
    private ScoreManager scoreManager;
    private Player player;

    public RaceTimer() {
        this.running = false;
        this.paused = false;
    }

    public RaceTimer(String trackName, String generationType) {
        this.running = false;
        this.paused = false;
        this.trackName = trackName;
        this.generationType = generationType;
        this.player = new Player();
        player.loadPlayerData();
        this.scoreManager = new ScoreManager(player);
    }

    public void start() {
        if (!running) {
            this.startTime = System.currentTimeMillis() - totalPausedDuration; 
            this.running = true;
            this.paused = false;
        }
    }

    public void pause() {
        if (running && !paused) {
            this.pausedTime = System.currentTimeMillis();
            this.paused = true;
        }
    }

    public void resume() {
        if (paused) {
            long pauseDuration = System.currentTimeMillis() - pausedTime;
            totalPausedDuration += pauseDuration; 
            this.startTime += pauseDuration; 
            this.paused = false;
        }
    }

    public void stop() {
        if (running) {
            long endTime = System.currentTimeMillis();
            this.totalPausedDuration += (paused ? (endTime - pausedTime) : 0); 
            this.seconds = (endTime - startTime - totalPausedDuration) / 1000.0;
            this.running = false;
            this.paused = false;
            printElapsedTime();
            saveResults(); 
            scoreManager.addPoints(seconds);
        }
    }

    public void printElapsedTime() {
        System.out.printf("Elapsed time: %.2f seconds%n", seconds);
    }

    private void saveResults() {
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

    public boolean isPaused() {
        return paused;
    }

    public double getTime() {
        return seconds;
    }
}
