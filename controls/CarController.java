package controls;

import common.Car;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class CarController extends KeyAdapter {
    private Car car;
    private Set<Integer> pressedKeys = new HashSet<>();

    public CarController(Car car) {
        this.car = car;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
        if(e.getKeyCode() == KeyEvent.VK_UP){
            car.isAccelerating = false;
        }
    }

    // Метод для обновления состояния автомобиля на основе нажатых клавиш
    public void update() {
        if (pressedKeys.contains(KeyEvent.VK_UP)) {
            car.accelerate();
            car.isAccelerating = true;
        }
        if (pressedKeys.contains(KeyEvent.VK_DOWN)) {
            car.decelerate();
        }
        if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
            car.turnLeft();
        }
        if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
            car.turnRight();
        }

        // Переключение передач с помощью клавиш W и D
        if (pressedKeys.contains(KeyEvent.VK_W)) {
            car.shiftUp(); // Увеличение передачи
        }
        if (pressedKeys.contains(KeyEvent.VK_D)) {
            car.shiftDown(); // Уменьшение передачи
        }
    }
}
