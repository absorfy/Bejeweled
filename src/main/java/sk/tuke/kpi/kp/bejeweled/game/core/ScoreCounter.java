package sk.tuke.kpi.kp.bejeweled.game.core;

import sk.tuke.kpi.kp.bejeweled.game.core.gem.Gem;
import sk.tuke.kpi.kp.bejeweled.game.core.tile.Tile;

public class ScoreCounter {
    private int currentScore;
    private int bonusScore;
    private int lastIncrementScore;

    public ScoreCounter() {
        reset();
    }

    public void addScore(int value) {
        lastIncrementScore += value;
    }

    public void saveScore() {
        currentScore += lastIncrementScore;
    }

    public void reset() {
        currentScore = 0;
        bonusScore = 0;
        lastIncrementScore = 0;
    }

    public void calculateBonusPoints(GameField field) {
        for(Point point : Point.iterate(field.getRowCount(), field.getColCount())) {
            Tile tile = field.getTile(point);
            if(tile instanceof Gem) {
                bonusScore += ((Gem) tile).getImpact().getScoreValue();
            }
        }
    }

    public void resetLastIncrementScore() {
        lastIncrementScore = 0;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getBonusScore() {
        return bonusScore;
    }

    public int getLastIncrementScore() {
        return lastIncrementScore;
    }
}
