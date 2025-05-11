package sk.tuke.kpi.kp.gamestudio.game.core;

import java.util.UUID;

public abstract class Tile {

    private final UUID id;

    protected Tile(UUID id) {
        this.id = id;
    }

    protected Tile() {
        this.id = UUID.randomUUID();
    }

    public String getTileName() {
        return this.toString();
    }

    public UUID getId() {
        return id;
    }
}
