package sk.tuke.kpi.kp.bejeweled.game.core;

import sk.tuke.kpi.kp.bejeweled.game.core.tile.AirTile;


public class DonutFieldShape implements FieldShapeStrategy {
    @Override
    public void applyShape(GameField field) {
        int rowCount = field.getRowCount();
        int colCount = field.getColCount();

        int width = (int)Math.ceil(rowCount / 3.0);
        int height = (int)Math.ceil(colCount / 3.0);

        for(int row = width; row < rowCount - width; row++) {
            for(int col = height; col < colCount - height; col++) {
                field.setTile(new Point(row, col), new AirTile());
            }
        }
    }
}
