package sk.tuke.kpi.kp.bejeweled.game.core.fieldshape;

import sk.tuke.kpi.kp.bejeweled.game.core.tile.AirTile;
import sk.tuke.kpi.kp.bejeweled.game.core.tile.Tile;

public class SlopeFieldShape implements FieldShapeStrategy {
    @Override
    public void applyShape(Tile[][] tiles, int rowCount, int colCount) {
        if(rowCount * colCount != tiles.length) return;
        for (int row = 0; row < rowCount; row++) {
            int leftBorder = colCount / 2 - row;
            int rightBorder = colCount / 2 + row;

            for (int col = 0; col < colCount; col++) {
                if (col < leftBorder)
                    tiles[rowCount - row - 1][col] = new AirTile();
                else if(col > rightBorder)
                    tiles[row][col] = new AirTile();
            }
        }
    }
}
