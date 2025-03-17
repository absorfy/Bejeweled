package sk.tuke.kpi.kp.bejeweled.game.core;

public class CircleFieldShape implements FieldShapeStrategy {
    @Override
    public void applyShape(Field field) {
        double centerX = (field.getColCount() - 1) / 2.0;
        double centerY = (field.getRowCount() - 1) / 2.0;
        double radiusX = field.getColCount() / 2.0;
        double radiusY = field.getRowCount() / 2.0;

        for(Point point : Point.iterate(field.getRowCount(), field.getColCount())) {
            double equation = Math.pow(point.getCol() - centerX, 2) / Math.pow(radiusX, 2) +
                    Math.pow(point.getRow() - centerY, 2) / Math.pow(radiusY, 2);

            if (equation > 1)
                field.setTile(point, new AirTile());
        }
    }
}
