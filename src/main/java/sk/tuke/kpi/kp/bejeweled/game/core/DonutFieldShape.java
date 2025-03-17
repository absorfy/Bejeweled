package sk.tuke.kpi.kp.bejeweled.game.core;

public class DonutFieldShape implements FieldShapeStrategy {
    @Override
    public void applyShape(Field field) {
        int width = (int)Math.ceil(field.getRowCount() / 3.0);
        int height = (int)Math.ceil(field.getColCount() / 3.0);
        for (Point point : Point.iterate(width, height, field.getRowCount() - width - 1,  field.getColCount() - height - 1)) {
            field.setTile(point, new AirTile());
        }
    }
}
