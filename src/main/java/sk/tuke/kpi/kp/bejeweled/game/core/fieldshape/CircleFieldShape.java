package sk.tuke.kpi.kp.bejeweled.game.core.fieldshape;

import sk.tuke.kpi.kp.bejeweled.game.core.AirTile;
import sk.tuke.kpi.kp.bejeweled.game.core.Tile;

public class CircleFieldShape implements FieldShapeStrategy {
    @Override
    public void applyShape(Tile[][] tiles, int rowCount, int colCount) {
        if(rowCount * colCount != tiles.length) return;
        double centerX = (colCount - 1) / 2.0;
        double centerY = (rowCount - 1) / 2.0;
        double radiusX = colCount / 2.0;
        double radiusY = rowCount / 2.0;

        for(int row = 0; row < rowCount; row++) {
            for(int col = 0; col < colCount; col++) {
                double equation = Math.pow(col - centerX, 2) / Math.pow(radiusX, 2) +
                        Math.pow(row - centerY, 2) / Math.pow(radiusY, 2);

                if (equation > 1)
                    tiles[row][col] = new AirTile();
            }
        }
    }
}
