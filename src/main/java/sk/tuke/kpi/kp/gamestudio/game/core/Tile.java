package sk.tuke.kpi.kp.gamestudio.game.core;

public abstract class Tile implements Cloneable {
    public String getTileName() {
        return this.toString();
    }

    abstract public Tile clone();
}
