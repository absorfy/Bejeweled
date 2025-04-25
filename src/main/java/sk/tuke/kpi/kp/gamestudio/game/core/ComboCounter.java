package sk.tuke.kpi.kp.gamestudio.game.core;

public class ComboCounter {
    private static final int SPEED_TIME_LIMIT = 7000;
    private int chainCombo;
    private int speedCombo;
    private long lastSwapTime;

    public ComboCounter() {
        chainCombo = 0;
        speedCombo = 0;
        lastSwapTime = System.currentTimeMillis();
    }

    public void saveCurrentSwapTime() {
        lastSwapTime = System.currentTimeMillis();
    }

    public void tryIncreaseSpeedCombo() {
        long currentSwapTime = System.currentTimeMillis();
        if (currentSwapTime - lastSwapTime <= SPEED_TIME_LIMIT)
            speedCombo++;
        else
            speedCombo = 0;
    }

    public void increaseComboChain() {
        chainCombo++;
    }

    public int getChainCombo() {
        return chainCombo;
    }

    public int getSpeedCombo() {
        return speedCombo;
    }

    public void resetChainCombo() {
        chainCombo = 0;
    }

    public void resetSpeedCombo() { speedCombo = 0; }

    public void reset() {
        resetChainCombo();
        resetSpeedCombo();
    }
}
