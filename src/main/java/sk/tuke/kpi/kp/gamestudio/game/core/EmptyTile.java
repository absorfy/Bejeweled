package sk.tuke.kpi.kp.gamestudio.game.core;

import java.util.UUID;

public class EmptyTile extends Tile {

    private EmptyTile(UUID id) {
        super(id);
    }

    public EmptyTile() {
        super();
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
