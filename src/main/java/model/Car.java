package model;

import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Car {
    public double x;
    public double y;
    public double angle;
    private double speed;
    private double maxSpeed;
    private double brake;
    private double friction;
    private String type;
    private double driftFactor;
    private double acceleration;
    private double TURN_RATE; 
    private int currentGear;

    private static final int MAX_GEARS = 3;

    public double[] gearMaxSpeeds = new double[MAX_GEARS];
    public double[] gearAccelerations = new double[MAX_GEARS];

    private static final double TURN_LIMIT = 2000.0; 
    private static final double DRIFT_MAX = 0.01;

    public boolean isAccelerating; 
    public boolean handbrake;

    public Car(int startX, int startY, double startAngle) {
        this.x = startX;
        this.y = startY;
        this.angle = startAngle;
        this.speed = 0;
        this.friction = 0;
        this.maxSpeed = 0;
        this.brake = 0; 
        this.currentGear = 1; 
        this.isAccelerating = false; 
        this.handbrake = false;
        this.driftFactor = 0.008;
        this.acceleration = 0;
    }

    public void loadFromJson(String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONObject json = new JSONObject(content);

            this.brake = json.getDouble("brake");
            this.friction = json.getDouble("friction");
            this.type = json.getString("type");
            this.TURN_RATE = json.getDouble("turnRate");

            JSONArray gearSpeedsJson = json.getJSONArray("gearSpeeds");
            for (int i = 0; i < MAX_GEARS; i++) {
                if (i < gearSpeedsJson.length()) {
                    gearMaxSpeeds[i] = gearSpeedsJson.getDouble(i);
                }
            }

            JSONArray gearAccelerationsJson = json.getJSONArray("gearAccelerations");
            for (int i = 0; i < MAX_GEARS; i++) {
                if (i < gearAccelerationsJson.length()) {
                    gearAccelerations[i] = gearAccelerationsJson.getDouble(i);
                }
            }

            updateGearParameters(); 

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка загрузки данных автомобиля из JSON: " + e.getMessage());
        }
    }

    public void move(double deltaTime, Tile[][] track) {
        double forwardX = speed * Math.cos(Math.toRadians(angle));
        double forwardY = speed * Math.sin(Math.toRadians(angle));

        double lateralSpeedFactor = 0.01;
        double lateralSpeed = (speed > 2) ? (speed * Math.sin(Math.toRadians(angle)) * lateralSpeedFactor) : 0; 
        double newX = x + forwardX * deltaTime + lateralSpeed * deltaTime; 
        double newY = y + forwardY * deltaTime;

        int newTileX = (int) Math.round(newX);
        int newTileY = (int) Math.round(newY);

        if (newTileX >= 0 && newTileX < track.length && newTileY >= 0 && newTileY < track[0].length) {
            Tile nextTile = track[newTileX][newTileY];

            if (nextTile != null && nextTile.isBarrier) {
                double distanceToBarrier = Math.sqrt(Math.pow(newX - x, 2) + Math.pow(newY - y, 2));
                double slowdownFactor = Math.max(0.1, distanceToBarrier / 10);

                speed *= slowdownFactor;
                if (speed < 0) speed = 0;

                return; 
            }
            if(nextTile != null && nextTile.type.equals("grass")){
                if(speed > 0) speed *=0.6;
            }
        }

        x = newX;
        y = newY;

        if (speed > 0) {
            speed -= friction * deltaTime;
            if (speed < 0) speed = 0;
        }
    }

    public void accelerate() {
        if (speed < maxSpeed) {
            speed += gearAccelerations[currentGear - 1];
            if (speed > maxSpeed) speed = maxSpeed;  
        } else {
            isAccelerating = false; 
        }
        
    }

    public void decelerate() {
        if (speed > 0) {
            speed -= brake * (speed / (maxSpeed * 5));
            if (speed < 0) speed = 0;
        }
        isAccelerating = false;
    }

    private void normalizeAngle() {
        if (angle > 180) {
            angle -= 360;
        } else if (angle < -180) {
            angle += 360;
        }
    }

    public void turnLeft() {
        if (speed > 0.1) {
            angle -= TURN_RATE;
            normalizeAngle(); 

            if (speed > 1 && handbrake == true) {
                driftFactor += 0.002;
                if(driftFactor > DRIFT_MAX) driftFactor = DRIFT_MAX;
                x += -driftFactor * speed * Math.sin(Math.toRadians(angle));
                y += driftFactor * speed * Math.cos(Math.toRadians(angle));
            }
        }
    }

    public void turnRight() {
        if (speed > 0.1) {
            angle += TURN_RATE;
            normalizeAngle(); 

            if (speed > 1 && handbrake == true) {
                driftFactor += 0.002;
                if(driftFactor > DRIFT_MAX) driftFactor = DRIFT_MAX;
                x += driftFactor * speed * Math.sin(Math.toRadians(angle));
                y += -driftFactor * speed * Math.cos(Math.toRadians(angle));
            }
        }
    }

    public void shiftUp() {
        if (currentGear < MAX_GEARS) {
            currentGear++;
            updateGearParameters();
        }
    }

    public void shiftDown() {
        if (currentGear > 1) {
            currentGear--;
            updateGearParameters();
        }
    }

    private void updateGearParameters() {
        if (currentGear >= 1 && currentGear <= MAX_GEARS) {
            updateMaxSpeed(gearMaxSpeeds[currentGear - 1]);
            updateAccel(gearAccelerations[currentGear - 1]); 
        }
    }

    // Getter methods
    public double getSpeed() { return speed; }
    
    public double getAngle() { return angle; }

    public double getBrake() { return brake; }

    public int getCurrentGear() { return currentGear; }

    public String getType() { return type; }

    public boolean isAccelerating() { return isAccelerating; } 

    public double getAccel() { return acceleration; }

    public double getFriction() { return friction; }
    
    public double getTurnRate() { return TURN_RATE; }

    public void updateMaxSpeed(double maxSpeed) { this.maxSpeed = maxSpeed; }

    public void updateAccel(double accel) { this.acceleration = accel; }
}

