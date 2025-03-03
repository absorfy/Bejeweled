package bejeweled.core;

public enum CombinationShape {
    COL_THREE(25, BreakImpact.NONE),
    COL_FOUR(50, BreakImpact.COLUMN),
    COL_FIVE(75, BreakImpact.COLUMN),

    ROW_THREE(25, BreakImpact.NONE),
    ROW_FOUR(50, BreakImpact.ROW),
    ROW_FIVE(75, BreakImpact.ROW),

    LETTER_L(75, BreakImpact.STAR),
    SQUARE(50, BreakImpact.EXPLODE),
    LETTER_T(75, BreakImpact.STAR),
    STAR(100, BreakImpact.STAR),
    NONE(0, BreakImpact.NONE),;

    private final int scoreCount;
    private BreakImpact breakImpact;

    CombinationShape(int scoreCount, BreakImpact breakImpact) {
        this.scoreCount = scoreCount;
        this.breakImpact = breakImpact;
    }

    public int getScoreCount() {
        return scoreCount;
    }

    public void setBreakImpact(BreakImpact breakImpact) {
        this.breakImpact = breakImpact;
    }

    public BreakImpact getBreakImpact() {
        return breakImpact;
    }
}
