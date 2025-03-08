package bejeweled.core;

import java.util.Random;

public class Gem extends Tile {
    private final Color color;
    private final BreakImpact impact;
    private GemState state;

    public Gem() {
        this(BreakImpact.NONE);
    }

    public Gem(BreakImpact impact) {
        this(impact, Color.values()[new Random().nextInt(Color.values().length)]);
    }

    public Gem(BreakImpact impact, Color color) {
        this.color = color;
        this.impact = impact;
        this.state = GemState.IDLE;
    }

    public Color getColor() {
        return color;
    }

    public BreakImpact getImpact() {
        return impact;
    }

    public GemState getState() {
        return state;
    }

    void setState(GemState state) {
        this.state = state;
    }
}
