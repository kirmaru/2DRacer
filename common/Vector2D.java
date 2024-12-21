package common;

public class Vector2D {
    public double x;
    public double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Добавление вектора
    public void add(Vector2D other) {
        this.x += other.x;
        this.y += other.y;
    }

    // Умножение вектора на скаляр
    public void scale(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
    }

    // Длина вектора
    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    // Нормализация (преобразование вектора в единичный)
    public void normalize() {
        double mag = magnitude();
        if (mag > 0) {
            this.x /= mag;
            this.y /= mag;
        }
    }

    // Поворот вектора на заданный угол
    public void rotate(double angleDegrees) {
        double radians = Math.toRadians(angleDegrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        double newX = x * cos - y * sin;
        double newY = x * sin + y * cos;

        this.x = newX;
        this.y = newY;
    }
}
