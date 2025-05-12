package sk.tuke.kpi.kp.gamestudio.game.core;

import java.util.UUID;

public class Gem extends Tile {
    private final GemColor gemColor;
    private final BreakImpact impact;
    private GemState state;

    public Gem() {
        this(BreakImpact.NONE);
    }

    private Gem(UUID id, GemColor gemColor, BreakImpact impact, GemState state) {
        super(id);
        this.gemColor = gemColor;
        this.impact = impact;
        this.state = state;
    }

    public Gem(GemColor gemColor) {
        this(BreakImpact.NONE, gemColor);
    }

    public Gem(BreakImpact impact) {
        this(impact, GemColor.random());
    }

    public Gem(BreakImpact impact, GemColor gemColor) {
        super();
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

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Gem clone() {
        return new Gem(super.getId(), gemColor, impact, state);
    }
}
