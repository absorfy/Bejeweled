package sk.tuke.kpi.kp.gamestudio.game.core;

public class TriangleFieldShape implements FieldShapeStrategy {

    @Override
    public void applyShape(GameField field) {
        int rowCount = field.getRowCount();
        int colCount = field.getColCount();

        int isEvenWidth = (colCount % 2) == 0 ? 1 : 0;
        for (int row = 0; row < rowCount; row++) {
            int leftBorder = colCount / 2 - row / 2 - isEvenWidth;
            int rightBorder = colCount / 2 + row / 2;

            for (int col = 0; col < colCount; col++) {
                if (col < leftBorder || col > rightBorder) {
                    field.setTile(new Point(row, col), new AirTile());
                }
            }
        }
    }
}
