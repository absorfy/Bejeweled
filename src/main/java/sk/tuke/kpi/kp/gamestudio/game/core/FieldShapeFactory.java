package sk.tuke.kpi.kp.gamestudio.game.core;

public class FieldShapeFactory {
    public static FieldShapeStrategy getShapeStrategy(FieldShape shape) {
        switch (shape) {
            case DONUT:
                return new DonutFieldShape();
            case TRIANGLE:
                return new TriangleFieldShape();
            case SLOPE:
                return new SlopeFieldShape();
            case CIRCLE:
                return new CircleFieldShape();
            case SQUARE:
                return new SquareFieldShape();
            default:
                throw new IllegalArgumentException("Unknown shape");
        }
    }
}
