package sk.tuke.kpi.kp.gamestudio.game.core;

public class Gem extends Tile {
    private final GemColor gemColor;
    private final BreakImpact impact;
    private GemState state;

    public Gem() {
        this(BreakImpact.NONE);
    }

    public Gem(GemColor gemColor) {
        this(BreakImpact.NONE, gemColor);
    }

    public Gem(BreakImpact impact) {
        this(impact, GemColor.random());
    }

    public Gem(BreakImpact impact, GemColor gemColor) {
        this.gemColor = gemColor;
        this.impact = impact;
        this.state = GemState.IDLE;
    }

    public GemColor getColor() {
        return gemColor;
    }

    public BreakImpact getImpact() {
        return impact;
    }

    public GemState getState() {
        return state;
    }

    public void setState(GemState state) {
        this.state = state;
    }
}
