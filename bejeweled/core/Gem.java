package bejeweled.core;

import java.util.Random;

public class Gem extends Tile {
    private Color color;
    private BreakImpact impact;
    private GemState state;

    public Gem(BreakImpact impact) {
        this.color = Color.values()[new Random().nextInt(Color.values().length)];
        this.impact = impact;
        this.state = GemState.FALLING;
    }

    public Gem() {
        this(BreakImpact.NONE);
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
}
