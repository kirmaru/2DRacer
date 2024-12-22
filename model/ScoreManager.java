// ScoreManager.java
package model;

public class ScoreManager {
    private Player player;

    public ScoreManager(Player player) {
        this.player = player;
    }

    public int getScore() {
        return player.getScore();
    }

    public void addPoints(double timeInSeconds) {
        int pointsEarned = (int) Math.max(0, 10.0 * (300 - timeInSeconds));
        player.addScore(pointsEarned);
        System.out.println("Заработано очков: " + pointsEarned);
    }

    public boolean spendPoints(int amount) {
        if (player.spendScore(amount)) {
            System.out.println("Списано очков: " + amount);
            return true;
        } else {
            System.out.println("Недостаточно очков для списания!");
            return false;
        }
    }
}
