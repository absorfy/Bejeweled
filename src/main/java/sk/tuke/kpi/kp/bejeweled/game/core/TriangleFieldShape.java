package sk.tuke.kpi.kp.bejeweled.game.core;

public class TriangleFieldShape implements FieldShapeStrategy {

    @Override
    public void applyShape(Field field) {
        int isEvenWidth = (field.getColCount() % 2) == 0 ? 1 : 0;
        for (int row = 0; row < field.getRowCount(); row++) {
            int leftBorder = field.getColCount() / 2 - row / 2 - isEvenWidth;
            int rightBorder = field.getColCount() / 2 + row / 2;

            for (int col = 0; col < field.getColCount(); col++) {
                if (col < leftBorder || col > rightBorder) {
                    field.setTile(new Point(row, col), new AirTile());
                }
            }
        }
    }
}
