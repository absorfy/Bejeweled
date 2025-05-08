package sk.tuke.kpi.kp.gamestudio.game.core;

import java.util.UUID;

public class AirTile extends Tile {

    public AirTile() {
        super();
    }



    private AirTile(UUID id) {
        super(id);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    @Override
    public AirTile clone() {
        return new AirTile(super.getId());
    }
}
