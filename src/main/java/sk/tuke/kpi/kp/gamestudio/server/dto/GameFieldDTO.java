package sk.tuke.kpi.kp.gamestudio.server.dto;

import sk.tuke.kpi.kp.gamestudio.game.core.FieldState;
import sk.tuke.kpi.kp.gamestudio.game.core.Tile;

public class GameFieldDTO {
    private int score;
    private int lastIncrementScore;
    private FieldState fieldState;
    private int colCount;
    private int rowCount;
    private Tile[][] tiles;
    private int hintCount;
    private int speedCombo;
    private int chainCombo;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLastIncrementScore() {
        return lastIncrementScore;
    }

    public void setLastIncrementScore(int lastIncrementScore) {
        this.lastIncrementScore = lastIncrementScore;
    }

    public FieldState getFieldState() {
        return fieldState;
    }

    public void setFieldState(FieldState fieldState) {
        this.fieldState = fieldState;
    }

    public int getColCount() {
        return colCount;
    }

    public void setColCount(int colCount) {
        this.colCount = colCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public int getHintCount() {
        return hintCount;
    }

    public void setHintCount(int hintCount) {
        this.hintCount = hintCount;
    }

    public int getSpeedCombo() {
        return speedCombo;
    }

    public void setSpeedCombo(int speedCombo) {
        this.speedCombo = speedCombo;
    }

    public int getChainCombo() {
        return chainCombo;
    }

    public void setChainCombo(int chainCombo) {
        this.chainCombo = chainCombo;
    }
}
