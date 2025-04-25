package sk.tuke.kpi.kp.gamestudio.game.core;

public enum BreakImpact {
    EXPLODE(25, "⊗"),
    ROW(50, "~"),
    COLUMN(50, "⚡"),
    STAR(75, "🟄"),
    NONE(0, "◆");

    private final int scoreValue;
    private final String symbol;

    BreakImpact(int scoreValue, String symbol) {
        this.scoreValue = scoreValue;
        this.symbol = symbol;
    }

    public int getScoreValue() {
        return scoreValue;
    }

    public String getSymbol() {
        return symbol;
    }
}
