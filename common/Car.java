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
    private double driftFactor;

    private int currentGear;
    private static final int MAX_GEARS = 3;

    private static final double TURN_LIMIT = 2000.0; // Максимальный угол для поворота
    private static final double TURN_RATE = 3.0; // Скорость поворота
    private static final double DRIFT_MAX = 0.01;

    public boolean isAccelerating; // Флаг для ускорения
    public boolean handbrake;

    public Car(int startX, int startY, double startAngle) {
        this.x = startX;
        this.y = startY;
        this.angle = startAngle;
        this.speed = 0;
        this.maxSpeed = 0;
        this.acceleration = 0;
        this.friction = 5.0; // Коэффициент трения
        this.brake = 0.8; // Коэффициент торможения
        this.currentGear = 1; // Начальная передача
        this.isAccelerating = false; // Инициализация флага
        this.handbrake = false;
        this.driftFactor = 0.008;
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
            System.err.println("Ошибка загрузки данных автомобиля из JSON: " + e.getMessage());
        }
    }

    public void move(double deltaTime, Tile[][] track) {
        // Вычисление движения вперед
        double forwardX = speed * Math.cos(Math.toRadians(angle));
        double forwardY = speed * Math.sin(Math.toRadians(angle));

        // Корректировка бокового движения для менее выраженного дрифта
        double lateralSpeedFactor = 0.01; // Уменьшенный коэффициент для тонкого дрифта
        double lateralSpeed = (speed > 2) ? (speed * Math.sin(Math.toRadians(angle)) * lateralSpeedFactor) : 0; 
        double newX = x + forwardX * deltaTime + lateralSpeed * deltaTime; 
        double newY = y + forwardY * deltaTime;

        // Проверка на препятствия и обновление позиции соответственно
        int newTileX = (int) Math.round(newX);
        int newTileY = (int) Math.round(newY);

        if (newTileX >= 0 && newTileX < track.length && newTileY >= 0 && newTileY < track[0].length) {
            Tile nextTile = track[newTileX][newTileY];

            if (nextTile != null && nextTile.isBarrier) {
                // Обработка столкновения с препятствием
                double distanceToBarrier = Math.sqrt(Math.pow(newX - x, 2) + Math.pow(newY - y, 2));
                double slowdownFactor = Math.max(0.1, distanceToBarrier / 10);

                speed -= slowdownFactor * deltaTime;
                if (speed < 0) speed = 0;

                System.out.println("Столкновение с препятствием, замедляемся: Скорость = " + speed);
                return; // Возврат без обновления позиции при столкновении
            }
            if(nextTile != null && nextTile.type.equals("grass")){
                if(speed > 0) speed *=0.6;
                System.out.println("grass");
            }

        }

        // Обновление позиции, если нет препятствий
        x = newX;
        y = newY;

        // Применение трения
        if (speed > 0) {
            speed -= friction * deltaTime;
            if (speed < 0) speed = 0;
        }
    }

    public void accelerate() {
        if (speed < maxSpeed) {
            speed += acceleration;
            if (speed > maxSpeed) speed = maxSpeed;
            //isAccelerating = true; // Установить флаг в true при ускорении
        } else {
            isAccelerating = false; // Установить флаг в false, если достигнута максимальная скорость
        }
        
    }

    public void decelerate() {
        if (speed > 0) {
            speed -= brake * (speed / (maxSpeed*5));
            if (speed < 0) speed = 0;
        }
        isAccelerating = false; // Установить флаг в false при замедлении
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
            normalizeAngle(); // Нормализация угла
            //System.out.println("isAccel " + isAccelerating);

            if (speed > 1 && handbrake == true) {
                // if (isAccelerating) {
                //     driftFactor += 0.002;
                    
                //     x += -driftFactor * speed * Math.sin(Math.toRadians(angle));
                //     y += driftFactor * speed * Math.cos(Math.toRadians(angle));
                // }
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
            normalizeAngle(); // Нормализация угла
            //System.out.println("isAccel " + isAccelerating);

            if (speed > 1 && handbrake == true) {
                // if (isAccelerating) {
                //     driftFactor += 0.002;
                //     x += driftFactor * speed * Math.sin(Math.toRadians(angle));
                //     y += -driftFactor * speed * Math.cos(Math.toRadians(angle));
                // }
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
        switch (currentGear) {
            case 1 -> { updateMaxSpeed(6); updateAccel(0.2); }
            case 2 -> { updateMaxSpeed(15); updateAccel(0.2); }
            case 3 -> { updateMaxSpeed(20); updateAccel(0.2); }
        }
    }

    public double getSpeed() { return speed; }
    
    public double getAngle() { return angle; }

    public int getCurrentGear() { return currentGear; }

    public void updateMaxSpeed(double maxSpeed) { this.maxSpeed = maxSpeed; }

    public void updateAccel(double accel) { this.acceleration = accel; }

    public String getType(){ return type; }

    public boolean isAccelerating() { return isAccelerating; } // Геттер для флага ускорения
}
