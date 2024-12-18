package common;

import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Car {
    public double x;
    public double y;
    public double angle;
    private double speed;
    private double maxSpeed;
    private double acceleration;
    private double brake;
    private double friction;
    private String type;

    private int currentGear;
    private static final int MAX_GEARS = 3;

    private static final double TURN_LIMIT = 2000.0;
    private static final double TURN_RATE = 5.0;

    public Car(int startX, int startY, double startAngle) {
        this.x = startX;
        this.y = startY;
        this.angle = startAngle;
        this.speed = 0;
        this.maxSpeed = 0;
        this.acceleration = 0;
        this.friction = 5.0;
        this.brake = 0.8;
        this.currentGear = 1;
    }

    public void loadFromJson(String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONObject json = new JSONObject(content);

            this.maxSpeed = json.getDouble("maxSpeed");
            this.acceleration = json.getDouble("acceleration");
            this.brake = json.getDouble("brake");
            this.friction = json.getDouble("friction");
            this.type = json.getString("type"); 
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading car data from JSON: " + e.getMessage());
        }
    }

    public void move(double deltaTime) {
        x += speed * Math.cos(Math.toRadians(angle)) * deltaTime;
        y += speed * Math.sin(Math.toRadians(angle)) * deltaTime;

        if (speed > 0) {
            speed -= friction * deltaTime;
            if (speed < 0) speed = 0;
        }
    }

    public void accelerate() {
        if (speed < maxSpeed) {
            speed += acceleration;
            if (speed > maxSpeed) speed = maxSpeed;
        }
    }

    public void decelerate() {
        if (speed > 0) {
            speed -= brake * (speed / maxSpeed);
            if (speed < 0) speed = 0;
        }
    }

    public void turnLeft() {
        if (speed > 0.1 && angle > -TURN_LIMIT) {
            angle -= TURN_RATE;
            if (angle < -TURN_LIMIT) angle = -TURN_LIMIT;
        }
    }

    public void turnRight() {
        if (speed > 0.1 && angle < TURN_LIMIT) {
            angle += TURN_RATE;
            if (angle > TURN_LIMIT) angle = TURN_LIMIT;
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
        switch (currentGear) {
            case 1 -> {
                updateMaxSpeed(6);
                updateAccel(0.2);
            }
            case 2 -> {
                updateMaxSpeed(15);
                updateAccel(0.2);
            }
            case 3 -> {
                updateMaxSpeed(20);
                updateAccel(0.2);
            }
        }
    }

    public double getSpeed() {
        return speed;
    }

    public double getAngle() {
        return angle;
    }

    public int getCurrentGear() {
        return currentGear;
    }

    public void updateMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void updateAccel(double accel) {
        this.acceleration = accel;
    }

    public String getType(){
        return type;
    }
}
