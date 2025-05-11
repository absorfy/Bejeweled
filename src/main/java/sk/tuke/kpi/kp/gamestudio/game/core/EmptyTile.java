package sk.tuke.kpi.kp.gamestudio.game.core;

import java.util.UUID;

public class EmptyTile extends Tile {

    public EmptyTile() {
        super();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
