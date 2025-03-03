package bejeweled.core;

import java.util.Random;

public class Gem extends Tile {
    private Color color;
    private BreakImpact impact;
    private GemState state;

    public Gem(BreakImpact impact, Color color) {
        this.color = color;
        this.impact = impact;
        this.state = GemState.IDLE;
    }

    public Gem(BreakImpact impact) {
        this(impact, Color.values()[new Random().nextInt(Color.values().length)]);
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

    public void setFalling() {
        this.state = GemState.FALLING;
    }

    public void setIdle() {
        this.state = GemState.IDLE;
    }

    public void setInCombination() {
        this.state = GemState.IN_COMBINATION;
    }
}
