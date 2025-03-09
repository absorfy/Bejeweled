package bejeweled.core;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class GemCombination implements Comparable<GemCombination> {

    private static int comboCounter = 0;

    private final Map<Point, Gem> gemPoints;
    private Point anchorPoint;
    private CombinationShape combinationShape = CombinationShape.NONE;
    private Color color;

    public GemCombination() {
        anchorPoint = null;
        gemPoints = new HashMap<Point, Gem>();
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

    public CombinationShape getShape() {
        return combinationShape;
    }

    public static void increaseComboCounter() {
        comboCounter++;
    }

    public int getScoreCount() {
        return combinationShape.getScoreCount() * (comboCounter / 3 + 1);
    }

    public void identifyCombination() {
        if (anchorPoint == null || gemPoints.size() < 3) {
            return;
        }
        if (checkStarShape() || checkLShape() || checkSquareShape() || checkTShape() || checkLineShape()) {
            setGemsInCombo(true);
        }
    }

    void setGemsInCombo(boolean inCombination) {
        gemPoints.values().forEach((gem) -> {
            if(inCombination)
                gem.setState(GemState.IN_COMBINATION);
            else if (gem.getState() != GemState.FALLING)
                gem.setState(GemState.IDLE);
        });
    }


    private boolean checkLineShape() {
        List<Point> horizontalPoints = gemPoints.keySet().stream()
                .filter(p -> p.getRow() == anchorPoint.getRow()).collect(Collectors.toList());
        List<Point> verticalPoints = gemPoints.keySet().stream()
                .filter(p -> p.getCol() == anchorPoint.getCol()).collect(Collectors.toList());

        if (horizontalPoints.size() >= 3) {
            setShapeByLinePoints(horizontalPoints, true);
            return true;
        }
        else if (verticalPoints.size() >= 3) {
            setShapeByLinePoints(verticalPoints, false);
            return true;
        }
        return false;
    }

    private void setShapeByLinePoints(List<Point> linePoints, boolean horizontal) {
        if (linePoints.size() < 3) return;
        switch (linePoints.size()) {
            case 3:
                combinationShape = horizontal ? CombinationShape.ROW_THREE : CombinationShape.COL_THREE;
                break;
            case 4:
                combinationShape = horizontal ? CombinationShape.ROW_FOUR : CombinationShape.COL_FOUR;
                break;
            case 5:
                combinationShape = horizontal ? CombinationShape.ROW_FIVE : CombinationShape.COL_FIVE;
                break;
            default:
                break;
        }

        gemPoints.keySet().retainAll(linePoints);
    }


    private boolean checkLShape() {
        return checkShape(new Point[]{
                anchorPoint,
                anchorPoint.moveTo(Direction.WEST),
                anchorPoint.moveTo(Direction.WEST).moveTo(Direction.WEST),
                anchorPoint.moveTo(Direction.NORTH),
                anchorPoint.moveTo(Direction.NORTH).moveTo(Direction.NORTH)
        }, CombinationShape.LETTER_L);
    }


    private boolean checkSquareShape() {
        return checkShape(new Point[]{
                anchorPoint,
                anchorPoint.moveTo(Direction.WEST),
                anchorPoint.moveTo(Direction.NORTH),
                anchorPoint.moveTo(Direction.WEST).moveTo(Direction.NORTH)
        }, CombinationShape.SQUARE);
    }

    private boolean checkTShape() {
        return checkShape(new Point[]{
                anchorPoint,
                anchorPoint.moveTo(Direction.WEST),
                anchorPoint.moveTo(Direction.NORTH),
                anchorPoint.moveTo(Direction.NORTH).moveTo(Direction.NORTH),
                anchorPoint.moveTo(Direction.EAST),
        }, CombinationShape.LETTER_T);
    }

    private boolean checkStarShape() {
        return checkShape(new Point[]{
                anchorPoint,
                anchorPoint.moveTo(Direction.WEST),
                anchorPoint.moveTo(Direction.WEST).moveTo(Direction.WEST),
                anchorPoint.moveTo(Direction.NORTH),
                anchorPoint.moveTo(Direction.NORTH).moveTo(Direction.NORTH),
                anchorPoint.moveTo(Direction.EAST),
                anchorPoint.moveTo(Direction.EAST).moveTo(Direction.EAST),
        }, CombinationShape.STAR);
    }

    private Point[] rotatePointsClockwise(Point[] points) {
        return Arrays.stream(points)
                .map(p -> new Point(
                        anchorPoint.getRow() - (p.getCol() - anchorPoint.getCol()),
                        anchorPoint.getCol() + (p.getRow() - anchorPoint.getRow())
                ))
                .toArray(Point[]::new);
    }

    private boolean checkShape(Point[] checkPoints, CombinationShape checkShape) {
        for (int rotateCount = 0; rotateCount < 4; rotateCount++) {
            if (Arrays.stream(checkPoints).allMatch(p -> gemPoints.containsKey(p))) {
                combinationShape = checkShape;
                gemPoints.keySet().retainAll(Arrays.stream(checkPoints).collect(Collectors.toList()));
                return true;
            }
            checkPoints = rotatePointsClockwise(checkPoints);
        }
        return false;
    }

    public boolean isValid() {
        return combinationShape != CombinationShape.NONE;
    }

    public void addGemPoint(Point point, Gem gem) {
        gemPoints.put(point, gem);
    }

    public void setAnchorPoint(Point point) {
        anchorPoint = point;
    }

    public static void resetComboCounter() {
        comboCounter = 0;
    }

    public List<Point> getGemPoints() {
        return new ArrayList<>(gemPoints.keySet());
    }

    public Point getAnchorPoint() {
        return anchorPoint;
    }

    static int getComboCounter() {
        return comboCounter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gemPoints);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GemCombination gComb = (GemCombination) obj;
        return new HashSet<>(gemPoints.keySet()).containsAll(gComb.getGemPoints());
    }

    @Override
    public int compareTo(GemCombination gemCombination) {
        return this.getShape().compareTo(gemCombination.getShape());
    }
}
