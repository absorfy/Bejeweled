package sk.tuke.kpi.kp.bejeweled.game.core.fieldshape;

import sk.tuke.kpi.kp.bejeweled.game.core.tile.AirTile;
import sk.tuke.kpi.kp.bejeweled.game.core.tile.Tile;

public class TriangleFieldShape implements FieldShapeStrategy {

    @Override
    public void applyShape(Tile[][] tiles, int rowCount, int colCount) {
        if(rowCount * colCount != tiles.length) return;
        int isEvenWidth = (colCount % 2) == 0 ? 1 : 0;
        for (int row = 0; row < rowCount; row++) {
            int leftBorder = colCount / 2 - row / 2 - isEvenWidth;
            int rightBorder = colCount / 2 + row / 2;

            for (int col = 0; col < colCount; col++) {
                if (col < leftBorder || col > rightBorder) {
                    tiles[row][col] = new AirTile();
                }
            }
        }
    }
}
