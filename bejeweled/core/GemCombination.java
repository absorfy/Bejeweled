package bejeweled.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GemCombination {
    private List<Point> points;
    private Point movedPoint;
    private static int comboCounter = 0;
    private CombinationShape combinationForm = CombinationShape.NONE;

    public GemCombination() {
        movedPoint = null;
        points = new ArrayList<>();
        comboCounter++;
    }

    public void identifyCombination() {
        if(movedPoint == null || points.size() < 3) {
            return;
        }

        if(checkStar(movedPoint)) return;
        if(checkLShape(movedPoint)) return;
        if(checkSquare(movedPoint)) return;
        if(checkTShape(movedPoint)) return;
        checkLineShape(movedPoint);
    }




    private void checkLineShape(Point fromPoint) {
        List<Point> horizontalPoints = points.stream().filter(p -> p.getRow() == fromPoint.getRow()).collect(Collectors.toList());
        List<Point> verticalPoints = points.stream().filter(p -> p.getCol() == fromPoint.getCol()).collect(Collectors.toList());

        List<Point> validPoints = horizontalPoints.size() >= 3 ? horizontalPoints : (verticalPoints.size() >= 3 ? verticalPoints : null);
        if(validPoints == null) return;

        if(validPoints.size() >= 3) {
            switch (validPoints.size()) {
                case 3: combinationForm = CombinationShape.THREE; break;
                case 4: combinationForm = CombinationShape.FOUR; break;
                case 5: combinationForm = CombinationShape.FIVE; break;
                default: break;
            }
            points = validPoints;
        }
    }



    private boolean checkLShape(Point fromPoint) {
        Direction directionWest = Direction.WEST;
        Direction directionNorth = Direction.NORTH;
        for(int i = 0; i < 4; i++) {
            Point[] checkPoints = new Point[]{
                    fromPoint,
                    fromPoint.moveTo(directionWest),
                    fromPoint.moveTo(directionWest).moveTo(directionWest),
                    fromPoint.moveTo(directionNorth),
                    fromPoint.moveTo(directionNorth).moveTo(directionNorth)
            };
            directionWest = directionWest.clockwise(1);
            directionNorth = directionNorth.clockwise(1);

            if (Arrays.stream(checkPoints).allMatch(p -> points.contains(p))) {
                combinationForm = CombinationShape.LETTER_L;
                points = Arrays.stream(checkPoints).collect(Collectors.toList());
                return true;
            }
        }
        return false;
    }


    private boolean checkSquare(Point fromPoint) {
        Direction directionWest = Direction.WEST;
        Direction directionNorth = Direction.NORTH;
        for(int i = 0; i < 4; i++) {
            Point[] checkPoints = new Point[]{
                    fromPoint,
                    fromPoint.moveTo(directionWest),
                    fromPoint.moveTo(directionNorth),
                    fromPoint.moveTo(directionWest).moveTo(directionNorth)
            };
            directionWest = directionWest.clockwise(1);
            directionNorth = directionNorth.clockwise(1);

            if (Arrays.stream(checkPoints).allMatch(p -> points.contains(p))) {
                combinationForm = CombinationShape.SQUARE;
                points = Arrays.stream(checkPoints).collect(Collectors.toList());
                return true;
            }
        }
        return false;
    }

    private boolean checkTShape(Point fromPoint) {
        Direction directionWest = Direction.WEST;
        Direction directionNorth = Direction.NORTH;
        Direction directionEast = Direction.EAST;
        for(int i = 0; i < 4; i++) {
            Point[] checkPoints = new Point[]{
                    fromPoint,
                    fromPoint.moveTo(directionWest),
                    fromPoint.moveTo(directionNorth),
                    fromPoint.moveTo(directionNorth).moveTo(directionNorth),
                    fromPoint.moveTo(directionEast),
            };
            directionWest = directionWest.clockwise(1);
            directionNorth = directionNorth.clockwise(1);
            directionEast = directionEast.clockwise(1);

            if (Arrays.stream(checkPoints).allMatch(p -> points.contains(p))) {
                combinationForm = CombinationShape.LETTER_T;
                points = Arrays.stream(checkPoints).collect(Collectors.toList());
                return true;
            }
        }
        return false;
    }

//    private Point[] rotatePointsClockwise(Point[] points, Point fromPoint) {
//        return Arrays.stream(points)
//                .map(p -> new Point(
//                        fromPoint.getRow() - (p.getCol() - fromPoint.getCol()), // newRow
//                        fromPoint.getCol() + (p.getRow() - fromPoint.getRow())  // newCol
//                ))
//                .toArray(Point[]::new);
//    }
//
//    private boolean checkShape(Point[] checkPoints, Point fromPoint, CombinationShape checkShape) {
//        for(int rotateCount = 0; rotateCount < 4; rotateCount++) {
//            if (Arrays.stream(checkPoints).allMatch(p -> points.contains(p))) {
//                combinationForm = checkShape;
//                points = Arrays.stream(checkPoints).collect(Collectors.toList());
//                return true;
//            }
//            checkPoints = rotatePointsClockwise(checkPoints, fromPoint);
//        }
//        return false;
//    }

    private boolean checkStar(Point fromPoint) {
        Direction directionWest = Direction.WEST;
        Direction directionNorth = Direction.NORTH;
        Direction directionEast = Direction.EAST;

        for(int i = 0; i < 4; i++) {
            Point[] checkPoints = new Point[]{
                    fromPoint,
                    fromPoint.moveTo(directionWest),
                    fromPoint.moveTo(directionWest).moveTo(directionWest),
                    fromPoint.moveTo(directionNorth),
                    fromPoint.moveTo(directionNorth).moveTo(directionNorth),
                    fromPoint.moveTo(directionEast),
                    fromPoint.moveTo(directionEast).moveTo(directionEast),
            };

            directionWest = directionWest.clockwise(1);
            directionNorth = directionNorth.clockwise(1);
            directionEast = directionEast.clockwise(1);

            if (Arrays.stream(checkPoints).allMatch(p -> points.contains(p))) {
                combinationForm = CombinationShape.STAR;
                points = Arrays.stream(checkPoints).collect(Collectors.toList());
                return true;
            }
        }
        return false;
    }

    public boolean isValid() {
        return combinationForm != CombinationShape.NONE;
    }

    public void addGemPoint(Point point) {
        points.add(point);
    }

    public void setMovedPoint(Point point) {
        movedPoint = point;
    }

    public void resetComboCounter() {
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
}
