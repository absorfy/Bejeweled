package bejeweled.core;

public enum BreakImpact {
    EXPLODE(25),
    ROW(50),
    COLUMN(50),
    STAR(75),
    NONE(0);

    private final int scoreValue;

    BreakImpact(int scoreValue) {
        this.scoreValue = scoreValue;
    }

    public int getScoreValue() {
        return scoreValue;
    }
}
