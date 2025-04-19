package sk.tuke.kpi.kp.bejeweled.game.core;

import sk.tuke.kpi.kp.bejeweled.game.core.tile.AirTile;

public class SlopeFieldShape implements FieldShapeStrategy {
    @Override
    public void applyShape(GameField field) {
        int rowCount = field.getRowCount();
        int colCount = field.getColCount();

        for (int row = 0; row < rowCount; row++) {
            int leftBorder = colCount / 2 - row;
            int rightBorder = colCount / 2 + row;

            for (int col = 0; col < colCount; col++) {
                if (col < leftBorder)
                    field.setTile(new Point(rowCount - row - 1, col), new AirTile());
                else if(col > rightBorder)
                    field.setTile(new Point(row, col), new AirTile());
            }
        }
    }
}
