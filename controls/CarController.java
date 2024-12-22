package controls;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import model.Car;

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
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            car.handbrake = false;
        }

    }

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
        if (pressedKeys.contains(KeyEvent.VK_W)) {
            car.shiftUp(); 
        }
        if (pressedKeys.contains(KeyEvent.VK_D)) {
            car.shiftDown();
        }
        if (pressedKeys.contains(KeyEvent.VK_SPACE)){
            car.handbrake = true;
        }
        if(pressedKeys.contains(KeyEvent.VK_ESCAPE)){
        }
    }
}
