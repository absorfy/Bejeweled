package sk.tuke.kpi.kp.bejeweled.game.core.fieldshape;

import sk.tuke.kpi.kp.bejeweled.game.core.tile.AirTile;
import sk.tuke.kpi.kp.bejeweled.game.core.tile.Tile;


public class DonutFieldShape implements FieldShapeStrategy {
    @Override
    public void applyShape(Tile[][] tiles, int rowCount, int colCount) {
        if(rowCount * colCount != tiles.length) return;
        int width = (int)Math.ceil(rowCount / 3.0);
        int height = (int)Math.ceil(colCount / 3.0);

        for(int row = width; row < rowCount - width; row++) {
            for(int col = height; col < colCount - height; col++) {
                tiles[row][col] = new AirTile();
            }
        }
    }
}
