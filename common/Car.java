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

    private static final double TURN_LIMIT = 2000.0; // Maximum angle for turning
    private static final double TURN_RATE = 3.0; // Rate of turning

    public Car(int startX, int startY, double startAngle) {
        this.x = startX;
        this.y = startY;
        this.angle = startAngle;
        this.speed = 0;
        this.maxSpeed = 0;
        this.acceleration = 0;
        this.friction = 5.0; // Friction factor
        this.brake = 0.8; // Brake factor
        this.currentGear = 1; // Initial gear
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

    public void move(double deltaTime, Tile[][] track) {
        // Calculate forward motion
        double forwardX = speed * Math.cos(Math.toRadians(angle));
        double forwardY = speed * Math.sin(Math.toRadians(angle));

        // Adjust lateral motion for less pronounced drifting
        double lateralSpeedFactor = 0.01; // Further reduced factor for subtle drift
        double lateralSpeed = (speed > 2) ? (speed * Math.sin(Math.toRadians(angle)) * lateralSpeedFactor) : 0; 
        double newX = x + forwardX * deltaTime + lateralSpeed * deltaTime; 
        double newY = y + forwardY * deltaTime;

        // Check for barriers and update position accordingly
        int newTileX = (int) Math.round(newX);
        int newTileY = (int) Math.round(newY);

        if (newTileX >= 0 && newTileX < track.length && newTileY >= 0 && newTileY < track[0].length) {
            Tile nextTile = track[newTileX][newTileY];

            if (nextTile != null && nextTile.isBarrier) {
                // Collision handling with barriers
                double distanceToBarrier = Math.sqrt(Math.pow(newX - x, 2) + Math.pow(newY - y, 2));
                double slowdownFactor = Math.max(0.1, distanceToBarrier / 10);

                speed -= slowdownFactor * deltaTime;
                if (speed < 0) speed = 0;

                System.out.println("Collision with barrier, slowing down: Speed = " + speed);
                return; // Return without updating position on collision
            }
        }

        // Update position if no barrier
        x = newX;
        y = newY;

        // Apply friction
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

            // Reduce lateral speed increase for subtle drifting
            double driftFactorLeft = 0.007; // Smaller factor for left turn drift effect
            if (speed > 2) { // Only apply drift when moving fast enough
                x += -driftFactorLeft * speed * Math.sin(Math.toRadians(angle)); 
                y += driftFactorLeft * speed * Math.cos(Math.toRadians(angle)); 
            }

            if (angle < -TURN_LIMIT) angle = -TURN_LIMIT; 
        }
    }
//ТУДУ
/*
 * driftFactorRight УВЕЛИЧИВАЕТСЯ ВМЕСТЕ СО СКОРОСТЬЮ
 */
    public void turnRight() {
        if (speed > 0.1 && angle < TURN_LIMIT) {
            angle += TURN_RATE;

            // Reduce lateral speed increase for subtle drifting
            double driftFactorRight = 0.007; // Smaller factor for right turn drift effect 
            if (speed > 2) { // Only apply drift when moving fast enough
                x += driftFactorRight * speed * Math.sin(Math.toRadians(angle)); 
                y += -driftFactorRight * speed * Math.cos(Math.toRadians(angle));
            }

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
