package panels;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PauseController extends KeyAdapter {

    private GameFrame gameFrame;

    public PauseController(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    public void togglePause() {
        if (gameFrame != null) {
            if (gameFrame.isPaused()) {
                gameFrame.resumeGame(); // Возобновляем игру
            } else {
                gameFrame.pauseGame(); // Пауза
            }
        }
    }
}
