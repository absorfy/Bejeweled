package sk.tuke.kpi.kp.gamestudio.game.core;

import java.util.*;

public class CombinationDetector {
    private final GameField field;

    public CombinationDetector(GameField field) {
        this.field = field;
    }

    public List<Point[]> findCombinationPoints(int count) {
        if (count <= 0) return null;
        Point[] points = field.getAllPoints();
        List<Point[]> combPoints = new ArrayList<>();
        Collections.shuffle(Arrays.asList(points));
        for (Point point : points) {
            Tile tile = field.getTile(point);
            if (!(tile instanceof Gem)) continue;
            for (Direction direction : Direction.values()) {
                Point adjacentPoint = point.moveTo(direction);
                if (trySwapAt(point, adjacentPoint)) {
                    if (combPoints.stream().noneMatch(x -> x[0].equals(adjacentPoint) && x[1].equals(point))) {
                        combPoints.add(new Point[]{point, adjacentPoint});
                        if (combPoints.size() == count) return combPoints;
                    }
                }
            }
        }
        return null;
    }

    public boolean trySwapAt(Point point1, Point point2) {
        if (!(field.getTile(point1) instanceof Gem) || !(field.getTile(point2) instanceof Gem)) return false;
        field.swapTiles(point1, point2);
        boolean anyComb = isCombinationAt(point1) || isCombinationAt(point2);
        field.swapTiles(point1, point2);
        return anyComb;
    }

    public boolean isCombinationAt(Point point) {
        GemCombination gemCombination = getCombinationAt(point);
        if (gemCombination.isValid()) {
            gemCombination.setGemsInCombo(false);
            return true;
        }
        return false;
    }

    public GemCombination getCombinationAt(Point point) {
        GemCombination combination = new GemCombination();
        if (!(field.getTile(point) instanceof Gem)) return combination;

        Set<Point> visited = new HashSet<>();
        Stack<Point> stackAdjacent = new Stack<>();
        combination.setAnchorPoint(point);
        combination.setColor(((Gem) field.getTile(point)).getColor());
        stackAdjacent.push(point);

        while (!stackAdjacent.isEmpty()) {
            Point currentPoint = stackAdjacent.pop();
            if (visited.contains(currentPoint) || !(field.getTile(currentPoint) instanceof Gem)) continue;
            Gem gem = (Gem) field.getTile(currentPoint);
            if (gem.getState() != GemState.IDLE || gem.getColor() != combination.getColor()) continue;

            visited.add(currentPoint);
            combination.addGemPoint(currentPoint, gem);
            stackAdjacent.push(currentPoint.toEast());
            stackAdjacent.push(currentPoint.toWest());
            stackAdjacent.push(currentPoint.toNorth());
            stackAdjacent.push(currentPoint.toSouth());
        }
        combination.identifyCombination();
        return combination;
    }
}
