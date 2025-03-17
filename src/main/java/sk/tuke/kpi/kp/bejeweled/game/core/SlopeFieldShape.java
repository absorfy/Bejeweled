package sk.tuke.kpi.kp.bejeweled.game.core;

public class SlopeFieldShape implements FieldShapeStrategy {
    @Override
    public void applyShape(Field field) {
        for (int row = 0; row < field.getRowCount(); row++) {
            int leftBorder = field.getColCount() / 2 - row;
            int rightBorder = field.getColCount() / 2 + row;

            for (int col = 0; col < field.getColCount(); col++) {
                if (col < leftBorder) {
                    field.setTile(new Point(field.getRowCount() - row - 1, col), new AirTile());
                }
                else if(col > rightBorder) {
                    field.setTile(new Point(row, col), new AirTile());
                }
            }
        }
    }
}
