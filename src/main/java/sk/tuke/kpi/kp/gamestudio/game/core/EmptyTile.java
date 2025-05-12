package sk.tuke.kpi.kp.gamestudio.game.core;

import java.util.UUID;

public class EmptyTile extends Tile {

    public EmptyTile() {
        super();
    }

    private EmptyTile(UUID id) {
        super(id);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    @Override
    public EmptyTile clone() {
        return new EmptyTile(super.getId());
    }
}
