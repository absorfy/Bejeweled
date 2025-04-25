package sk.tuke.kpi.kp.gamestudio.game.core;

public class CircleFieldShape implements FieldShapeStrategy {
    @Override
    public void applyShape(GameField field) {
        int rowCount = field.getRowCount();
        int colCount = field.getColCount();

        double centerX = (colCount - 1) / 2.0;
        double centerY = (rowCount - 1) / 2.0;
        double radiusX = colCount / 2.0;
        double radiusY = rowCount / 2.0;

        for(int row = 0; row < rowCount; row++) {
            for(int col = 0; col < colCount; col++) {
                double equation = Math.pow(col - centerX, 2) / Math.pow(radiusX, 2) +
                        Math.pow(row - centerY, 2) / Math.pow(radiusY, 2);

                if (equation > 1)
                    field.setTile(new Point(row, col), new AirTile());
            }
        }
    }
}
