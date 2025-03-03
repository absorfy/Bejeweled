package bejeweled.core;

import java.util.*;
import java.util.stream.Collectors;

public class GemCombination {
    private List<Point> points;
    private Point movedPoint;
    private static int comboCounter = 0;
    private CombinationShape combinationShape = CombinationShape.NONE;
    private Color color;

    public GemCombination() {
        movedPoint = null;
        points = new ArrayList<>();
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public BreakImpact getBreakImpact() {
        return combinationShape.getBreakImpact();
    }

    public static void increaseComboCounter() {
        comboCounter++;
    }

    public int getScoreCount() {
        return combinationShape.getScoreCount() * (comboCounter / 3 + 1);
    }

    public void identifyCombination() {
        if(movedPoint == null || points.size() < 3) {
            return;
        }

        if(checkStar()) return;
        if(checkLShape()) return;
        if(checkSquare()) return;
        if(checkTShape()) return;
        checkLineShape();
    }


    private void checkLineShape() {
        List<Point> horizontalPoints = points.stream().filter(p -> p.getRow() == movedPoint.getRow()).collect(Collectors.toList());
        List<Point> verticalPoints = points.stream().filter(p -> p.getCol() == movedPoint.getCol()).collect(Collectors.toList());

        if(horizontalPoints.size() >= 3)
            setShapeByLinePoints(horizontalPoints);
        else if(verticalPoints.size() >= 3)
            setShapeByLinePoints(verticalPoints);
    }

    private void setShapeByLinePoints(List<Point> linePoints) {
        if(linePoints.size() < 3) return;
        boolean inRow = linePoints.stream().allMatch(p -> p.getCol() == movedPoint.getCol());
        switch (linePoints.size()) {
            case 3: combinationShape = inRow ? CombinationShape.ROW_THREE : CombinationShape.COL_THREE; break;
            case 4: combinationShape = inRow ? CombinationShape.ROW_FOUR : CombinationShape.COL_FOUR; break;
            case 5: combinationShape = inRow ? CombinationShape.ROW_FIVE : CombinationShape.COL_FIVE; break;
            default: break;
        }
        points = linePoints;
    }



    private boolean checkLShape() {
        return checkShape(new Point[]{
                movedPoint,
                movedPoint.moveTo(Direction.WEST),
                movedPoint.moveTo(Direction.WEST).moveTo(Direction.WEST),
                movedPoint.moveTo(Direction.NORTH),
                movedPoint.moveTo(Direction.NORTH).moveTo(Direction.NORTH)
        }, CombinationShape.LETTER_L);
    }


    private boolean checkSquare() {
        return checkShape(new Point[]{
                movedPoint,
                movedPoint.moveTo(Direction.WEST),
                movedPoint.moveTo(Direction.NORTH),
                movedPoint.moveTo(Direction.WEST).moveTo(Direction.NORTH)
        }, CombinationShape.SQUARE);
    }

    private boolean checkTShape() {
        return checkShape(new Point[]{
                movedPoint,
                movedPoint.moveTo(Direction.WEST),
                movedPoint.moveTo(Direction.NORTH),
                movedPoint.moveTo(Direction.NORTH).moveTo(Direction.NORTH),
                movedPoint.moveTo(Direction.EAST),
        }, CombinationShape.LETTER_T);
    }

    private boolean checkStar() {
        return checkShape(new Point[]{
                movedPoint,
                movedPoint.moveTo(Direction.WEST),
                movedPoint.moveTo(Direction.WEST).moveTo(Direction.WEST),
                movedPoint.moveTo(Direction.NORTH),
                movedPoint.moveTo(Direction.NORTH).moveTo(Direction.NORTH),
                movedPoint.moveTo(Direction.EAST),
                movedPoint.moveTo(Direction.EAST).moveTo(Direction.EAST),
        }, CombinationShape.STAR);
    }

    private Point[] rotatePointsClockwise(Point[] points) {
        return Arrays.stream(points)
                .map(p -> new Point(
                        movedPoint.getRow() - (p.getCol() - movedPoint.getCol()),
                        movedPoint.getCol() + (p.getRow() - movedPoint.getRow())
                ))
                .toArray(Point[]::new);
    }

    private boolean checkShape(Point[] checkPoints, CombinationShape checkShape) {
        for(int rotateCount = 0; rotateCount < 4; rotateCount++) {
            if (Arrays.stream(checkPoints).allMatch(p -> points.contains(p))) {
                combinationShape = checkShape;
                points = Arrays.stream(checkPoints).collect(Collectors.toList());
                return true;
            }
            checkPoints = rotatePointsClockwise(checkPoints);
        }
        return false;
    }

    public boolean isValid() {
        return combinationShape != CombinationShape.NONE;
    }

    public void addGemPoint(Point point) {
        points.add(point);
    }

    public void setMovedPoint(Point point) {
        movedPoint = point;
    }

    public static void resetComboCounter() {
        comboCounter = 0;
    }

    public List<Point> getPoints() {
        return points;
    }

    public Point getMovedPoint() {
        return movedPoint;
    }

    public static int getComboCounter() {
        return comboCounter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(points);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GemCombination gComb = (GemCombination) obj;
        return new HashSet<>(points).containsAll(gComb.getPoints());
    }
}
