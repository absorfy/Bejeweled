package bejeweled.core;

import java.util.*;

public class Field {
    private final Tile[][] tiles;

    private final Queue<Point> brokenPoints;
    private final List<GemCombination> gemCombinations;

    private int currentScore;
    private FieldState state;

    public Field(int rowCount, int colCount) {
        if (colCount < 3 || rowCount < 3) {
            throw new IllegalArgumentException();
        }

        this.tiles = new Tile[rowCount][colCount];
        brokenPoints = new ArrayDeque<>();
        gemCombinations = new ArrayList<>();
        this.currentScore = 0;
        this.state = FieldState.WAITING;

        generateField();
    }

    public void reset() {
        currentScore = 0;
        GemCombination.resetComboCounter();
        brokenPoints.clear();
        gemCombinations.clear();
        state = FieldState.WAITING;
        generateField();
    }

    private void generateField() {
        for (Point point : Point.iterate(getColCount(), getRowCount())) {
            do {
                int blockedRowBorder = getRowCount() - getRowCount() / 2;
                if (getRowCount() >= 6 && point.getRow() >= blockedRowBorder)
                    setTile(point, new LockTile(3));
                else
                    setTile(point, new Gem());
            } while (checkCombinationIn(point));
        }

        if (!hasPossibleMoves()) {
            generateField();
        }
    }

    public Point[] findCombinationPoints() {
        Point[] points = getAllPoints();
        Collections.shuffle(Arrays.asList(points));
        for (Point point : points) {
            Tile tile = getTile(point);
            if (!(tile instanceof Gem)) continue;

            for (Direction direction : Direction.values()) {
                Point adjacentPoint = point.moveTo(direction);
                if (trySwapAt(point, adjacentPoint))
                    return new Point[]{point, adjacentPoint};
            }
        }
        return null;
    }

    public void swapGems(Point point1, Point point2) {
        if (state != FieldState.WAITING) return;
        if (point1.isNotValid(getRowCount(), getColCount()) || point2.isNotValid(getRowCount(), getColCount())) return;
        if (!point1.isAdjacent(point2)) return;
        if (!(getTile(point1) instanceof Gem) || !(getTile(point2) instanceof Gem)) return;

        GemCombination.resetComboCounter();
        swapTiles(point1, point2);

        GemCombination gemComb;
        gemComb = getCombinationFrom(point1);
        if(gemComb.isValid()) gemCombinations.add(gemComb);
        else gemComb.setGemsInCombo(false);
        gemComb = getCombinationFrom(point2);
        if(gemComb.isValid()) gemCombinations.add(gemComb);
        else gemComb.setGemsInCombo(false);

        if(gemCombinations.isEmpty())
            swapTiles(point1, point2);
        else
            state = FieldState.BREAKING;
    }

    public boolean hasPossibleMoves() {
        return findCombinationPoints() != null;
    }

    private boolean trySwapAt(Point point1, Point point2) {
        if (point1.isNotValid(getRowCount(), getColCount()) || point2.isNotValid(getRowCount(), getColCount()))
            return false;
        if (!(getTile(point1) instanceof Gem) || !(getTile(point2) instanceof Gem)) return false;

        swapTiles(point1, point2);
        boolean anyComb = checkCombinationIn(point1) || checkCombinationIn(point2);
        swapTiles(point1, point2);

        return anyComb;
    }


    private void swapTiles(Point point1, Point point2) {
        Tile tile1 = getTile(point1);
        setTile(point1, getTile(point2));
        setTile(point2, tile1);
    }


    public void processGemCombinations() {
        if(state !=  FieldState.BREAKING) return;
        GemCombination[] sortedCombinations = pullSortedGemCombinations();
        for (GemCombination gComb : sortedCombinations) {
            if (gComb.isValid()) {
                breakCombination(gComb);
                processCombinationShape(gComb);
            }
            else {
                gComb.setGemsInCombo(false);
            }
        }
    }

    private GemCombination[] pullSortedGemCombinations() {
        GemCombination[] sortedCombinations = gemCombinations.stream()
                .sorted(Comparator.reverseOrder())
                .toArray(GemCombination[]::new);
        gemCombinations.clear();
        return sortedCombinations;
    }

    private void processCombinationShape(GemCombination gComb) {
        currentScore += gComb.getScoreCount();
        if (gComb.getBreakImpact() != BreakImpact.NONE) {
            Gem gem = new Gem(gComb.getBreakImpact(), gComb.getColor());
            gem.setState(GemState.FALLING);
            setTile(gComb.getAnchorPoint(), gem);
        }
    }


    public void checkNewPossibleCombinations() {
        if (state != FieldState.BREAKING) return;

        while (!brokenPoints.isEmpty()) {
            GemCombination gemCombination = getCombinationFrom(brokenPoints.poll());
            if (gemCombination.isValid() && !gemCombinations.contains(gemCombination))
                gemCombinations.add(gemCombination);
            else {
                gemCombination.setGemsInCombo(false);
            }
        }

        if (gemCombinations.isEmpty())
            state = hasPossibleMoves() ? FieldState.WAITING : FieldState.NO_POSSIBLE_MOVE;

    }


    private Point[] getAllPoints() {
        Point[] points = new Point[getColCount() * getRowCount()];
        for (Point point : Point.iterate(getColCount(), getRowCount())) {
            points[point.getRow() * getColCount() + point.getCol()] = point;
        }
        return points;
    }

    public void fillEmpties() {
        if (state != FieldState.BREAKING) return;
        processAllFallingGems();
        generateNewGems();
    }

    private void generateNewGems() {
        for (Point point : Point.iterate(0, 0, 0, getColCount() - 1)) {
            if (getTile(point) instanceof EmptyTile) {
                setTile(point, new Gem());
                processFallingGem(point);
            }
        }
        if (Arrays.stream(tiles[0]).anyMatch(tile -> tile instanceof EmptyTile)) {
            generateNewGems();
        }
    }

    private void processAllFallingGems() {
        Point[] fallingGemPoints = Arrays.stream(getAllPoints())
                .filter(p -> getTile(p) instanceof Gem)
                .filter(p -> ((Gem) getTile(p)).getState() == GemState.FALLING)
                .toArray(Point[]::new);

        for (int i = fallingGemPoints.length - 1; i >= 0; i--) {
            processFallingGem(fallingGemPoints[i]);
        }
    }

    private void processFallingGem(Point fromPoint) {
        Point bottomPoint = fromPoint;
        while (getTile(bottomPoint.toSouth()) instanceof EmptyTile) {
            bottomPoint = bottomPoint.toSouth();
        }

        Gem fallingGem = (Gem) getTile(fromPoint);

        if (getTile(bottomPoint) instanceof EmptyTile) {
            setTile(bottomPoint, fallingGem);
            setTile(fromPoint, new EmptyTile());
        }
        brokenPoints.add(bottomPoint);
        fallingGem.setState(GemState.IDLE);
    }

    private void breakCombination(GemCombination gemsCombination) {
        GemCombination.increaseComboCounter();
        for (Point p : gemsCombination.getGemPoints()) {
            breakTile(p);
        }
        breakAdjacentLockTiles(gemsCombination.getGemPoints());
    }

    private void breakAdjacentLockTiles(List<Point> points) {
        for (Point point : points) {
            for (Direction direction : Direction.values()) {
                Point adjacentPoint = point.moveTo(direction);
                if (!points.contains(adjacentPoint) && getTile(adjacentPoint) instanceof LockTile) {
                    breakLockTile(adjacentPoint, (LockTile) getTile(adjacentPoint));
                }
            }
        }
    }

    public int getComboCount() {
        return GemCombination.getComboCounter();
    }

    private void setTile(Point point, Tile tileObject) {
        if (point.isNotValid(getRowCount(), getColCount()))
            return;

        tiles[point.getRow()][point.getCol()] = tileObject;
    }

    private boolean checkCombinationIn(Point point) {
        GemCombination gemCombination = getCombinationFrom(point);
        if(gemCombination.isValid()) {
            gemCombination.setGemsInCombo(false);
            return true;
        }
        return false;
    }

    private GemCombination getCombinationFrom(Point point) {
        Set<Point> visited = new HashSet<>();
        Stack<Point> stackAdjacent = new Stack<>();
        GemCombination combination = new GemCombination();

        if (!(getTile(point) instanceof Gem)) return combination;

        combination.setAnchorPoint(point);
        combination.setColor(((Gem) getTile(point)).getColor());
        Color combColor = ((Gem) getTile(point)).getColor();
        stackAdjacent.push(point);

        while (!stackAdjacent.isEmpty()) {
            Point current = stackAdjacent.pop();
            if (current.isNotValid(getRowCount(), getColCount()) || visited.contains(current)) continue;

            Tile checkTile;
            if (!((checkTile = getTile(current)) instanceof Gem)
                    || ((Gem) checkTile).getState() != GemState.IDLE
                    || ((Gem) checkTile).getColor() != combColor) {
                continue;
            }
            visited.add(current);

            combination.addGemPoint(current, (Gem)checkTile);

            stackAdjacent.push(current.toEast());
            stackAdjacent.push(current.toWest());
            stackAdjacent.push(current.toNorth());
            stackAdjacent.push(current.toSouth());
        }

        combination.identifyCombination();

        return combination;
    }


    private void breakTile(Point point) {
        Tile tile = getTile(point);
        if (tile instanceof Gem) {
            breakGem(point);
            processBreakImpact(point, (Gem) tile);
        } else if (tile instanceof LockTile) {
            breakLockTile(point, (LockTile) tile);
        }
    }

    private void processBreakImpact(Point point, Gem gem) {
        currentScore += gem.getImpact().getScoreValue();
        if (gem.getImpact() != BreakImpact.NONE)
            GemCombination.increaseComboCounter();

        switch (gem.getImpact()) {
            case ROW:
                breakRow(point.getRow());
                break;
            case COLUMN:
                breakColumn(point.getCol());
                break;
            case STAR:
                breakRow(point.getRow());
                breakColumn(point.getCol());
                break;
            case EXPLODE:
                breakSquare(point);
            default:
                break;
        }
    }

    private void breakSquare(Point point) {
        for (int row = point.getRow() - 1; row <= point.getRow() + 1; row++) {
            for (int col = point.getCol() - 1; col <= point.getCol() + 1; col++) {
                Point breakPoint = new Point(row, col);
                if (breakPoint.equals(point)) continue;
                breakTile(breakPoint);
            }
        }
    }

    private void breakRow(int row) {
        if (row < 0 || row >= getRowCount()) return;
        for (int col = 0; col < getColCount(); col++) {
            breakTile(new Point(row, col));
        }
    }

    private void breakGem(Point point) {
        setTile(point, new EmptyTile());
        setTopGemFalling(point);
    }

    private void breakColumn(int col) {
        if (col < 0 || col >= getColCount()) return;
        for (int row = 0; row < getRowCount(); row++) {
            breakTile(new Point(row, col));
        }
    }

    private void breakLockTile(Point lockPoint, LockTile lockTile) {
        if (lockTile.Break()) {
            Gem gem = lockTile.getGem();
            gem.setState(GemState.FALLING);
            setTile(lockPoint, gem);
        }
    }

    private void setTopGemFalling(Point point) {
        Tile tile;
        do {
            point = point.toNorth();
            if (point.isNotValid(getRowCount(), getColCount())) return;
        } while ((tile = getTile(point)) instanceof EmptyTile);

        if (tile instanceof Gem) {
            ((Gem) tile).setState(GemState.FALLING);
        }

        if (!(getTile(point.toNorth()) instanceof LockTile))
            setTopGemFalling(point);
    }

    public int getScore() {
        return currentScore;
    }

    public FieldState getState() {
        return state;
    }

    public int getColCount() {
        return tiles[0].length;
    }

    public int getRowCount() {
        return tiles.length;
    }

    public Tile getTile(int row, int col) {
        return getTile(new Point(row, col));
    }

    public Tile getTile(Point point) {
        if (point.isNotValid(getRowCount(), getColCount())) return null;
        return tiles[point.getRow()][point.getCol()];
    }
}
