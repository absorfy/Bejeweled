package sk.tuke.kpi.kp.bejeweled.game.core.fieldshape;

import sk.tuke.kpi.kp.bejeweled.game.core.tile.Tile;

public interface FieldShapeStrategy {
    void applyShape(Tile[][] tiles, int rowCount, int colCount);
}
