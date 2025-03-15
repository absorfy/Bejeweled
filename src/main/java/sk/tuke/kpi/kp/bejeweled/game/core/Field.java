package sk.tuke.kpi.kp.bejeweled.game.core;

import java.util.*;

public class Field {

    private final Tile[][] tiles;
    private final Queue<Point> brokenPoints;
    private final List<GemCombination> savedGemCombinations;

    private int currentScore;
    private int lastIncrementScore;
    private FieldState state;
    private final FieldShape shape;


    public Field(int rowCount, int colCount) {
        this(rowCount, colCount, null);
    }

    public Field(int rowCount, int colCount, FieldShape shape) {
        if (colCount < 5 || rowCount < 5) {
            throw new IllegalArgumentException();
        }
        this.tiles = new Tile[rowCount][colCount];
        brokenPoints = new ArrayDeque<>();
        savedGemCombinations = new ArrayList<>();
        this.currentScore = 0;
        this.state = FieldState.WAITING;
        this.shape = shape;

        generateField();
        GemCombination.resetChainCombo();
        GemCombination.resetSpeedCombo();
        GemCombination.saveCurrentSwapTime();
    }

    private void setAirTileForShape(FieldShape shape) {
        switch(shape) {
            case DONUT: setDonutFieldShape(); break;
            case TRIANGLE: setTriangleFieldShape(); break;
            case SLOPE: setSlopeFieldShape(); break;
            case CIRCLE: setCircleFieldShape(); break;
            default: break;
        }
    }

    private void setCircleFieldShape() {
        double centerX = (getColCount() - 1) / 2.0;
        double centerY = (getRowCount() - 1) / 2.0;
        double radiusX = getColCount() / 2.0;
        double radiusY = getRowCount() / 2.0;

        for(Point point : Point.iterate(getRowCount(), getColCount())) {
            double equation = Math.pow(point.getCol() - centerX, 2) / Math.pow(radiusX, 2) +
                    Math.pow(point.getRow() - centerY, 2) / Math.pow(radiusY, 2);

            if (equation > 1)
                setTile(point, new AirTile());
        }
    }

    private void setSlopeFieldShape() {
        for (int row = 0; row < getRowCount(); row++) {
            int leftBorder = getColCount() / 2 - row;
            int rightBorder = getColCount() / 2 + row;

            for (int col = 0; col < getColCount(); col++) {
                if (col < leftBorder) {
                    setTile(new Point(getRowCount() - row - 1, col), new AirTile());
                }
                else if(col > rightBorder) {
                    setTile(new Point(row, col), new AirTile());
                }
            }
        }
    }

    private void setTriangleFieldShape() {
        int isEvenWidth = (getColCount() % 2) == 0 ? 1 : 0;
        for (int row = 0; row < getRowCount(); row++) {
            int leftBorder = getColCount() / 2 - row / 2 - isEvenWidth;
            int rightBorder = getColCount() / 2 + row / 2;

            for (int col = 0; col < getColCount(); col++) {
                if (col < leftBorder || col > rightBorder) {
                    setTile(new Point(row, col), new AirTile());
                }
            }
        }
    }

    private void setDonutFieldShape() {
        int width = (int)Math.ceil(getRowCount() / 3.0);
        int height = (int)Math.ceil(getColCount() / 3.0);
        for (Point point : Point.iterate(width, height, getRowCount() - width - 1,  getColCount() - height - 1)) {
            setTile(point, new AirTile());
        }
    }


    public int getSpeedCombo() {
        return GemCombination.getSpeedCombo();
    }

    public void reset() {
        currentScore = 0;
        GemCombination.resetSpeedCombo();
        GemCombination.resetChainCombo();
        brokenPoints.clear();
        savedGemCombinations.clear();
        clearTiles();
        generateField();
        GemCombination.saveCurrentSwapTime();
        state = FieldState.WAITING;
    }

    private void clearTiles() {
        for(Point point : Point.iterate(getRowCount(), getColCount())) {
            setTile(point, null);
        }
    }

    private void generateField() {
        setAirTileForShape(shape != null ? shape : FieldShape.random());
        generateBlockedTiles();
        generateStartGems();
    }

    private void generateBlockedTiles() {
        boolean bottomBlocked = new Random().nextInt(2) == 1;
        if(bottomBlocked)
            blockBottomTiles();
        else
            blockSideTiles();
    }

    private void blockSideTiles() {
        int widthBlocked = (int)Math.ceil(getColCount() / 5.0);
        for(Point point : Point.iterate(0, 0, getRowCount(), widthBlocked - 1)) {
            if(getTile(point) == null)
                setTile(point, new LockTile());
        }
        for(Point point : Point.iterate(0, getColCount() - widthBlocked, getRowCount(), getColCount() - 1)) {
            if(getTile(point) == null)
                setTile(point, new LockTile());
        }
    }

    private void blockBottomTiles() {
        int borderBlocked = getRowCount() - (int)Math.ceil(getRowCount() / 3.0);
        for(Point point : Point.iterate(borderBlocked, 0, getRowCount() - 1, getColCount() - 1)) {
            if(getTile(point) == null) {
                setTile(point, new LockTile());
            }
        }
    }

    private void generateStartGems() {
        do {
            for (Point point : Point.iterate(getColCount(), getRowCount())) {
                do {
                    if (getTile(point) == null ||  getTile(point) instanceof Gem)
                        setTile(point, new Gem());
                } while (isCombinationAt(point));
            }
        } while (!hasPossibleMoves());
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
        if (!(getTile(point1) instanceof Gem) || !(getTile(point2) instanceof Gem)) return;
        if (!point1.isAdjacent(point2)) return;

        GemCombination.resetChainCombo();
        swapTiles(point1, point2);
        trySaveCombinationAt(point1);
        trySaveCombinationAt(point2);
        if (savedGemCombinations.isEmpty()) {
            swapTiles(point1, point2);
        }
        else {
            state = FieldState.BREAKING;
            savedGemCombinations.forEach(GemCombination::setMadeMyself);
            GemCombination.tryIncreaseSpeedCombo();
        }
    }


    public boolean hasPossibleMoves() {
        return findCombinationPoints() != null;
    }

    private boolean trySwapAt(Point point1, Point point2) {
        if (!(getTile(point1) instanceof Gem) || !(getTile(point2) instanceof Gem)) return false;
        swapTiles(point1, point2);
        boolean anyComb = isCombinationAt(point1) || isCombinationAt(point2);
        swapTiles(point1, point2);
        return anyComb;
    }


    private void swapTiles(Point point1, Point point2) {
        Tile tile1 = getTile(point1);
        setTile(point1, getTile(point2));
        setTile(point2, tile1);
    }


    public void processGemCombinations() {
        if (state != FieldState.BREAKING) return;
        GemCombination[] sortedCombinations = pullSortedGemCombinations();

        for (GemCombination gComb : sortedCombinations) {
            if (gComb.isValid()) {
                breakCombination(gComb);
                processCombinationShape(gComb);
                lastIncrementScore = gComb.getScoreCount();
                currentScore += lastIncrementScore;
            } else gComb.setGemsInCombo(false);
        }
    }

    private GemCombination[] pullSortedGemCombinations() {
        GemCombination[] sortedCombinations = savedGemCombinations.stream()
                .sorted(Comparator.reverseOrder())
                .toArray(GemCombination[]::new);
        savedGemCombinations.clear();
        return sortedCombinations;
    }

    private void processCombinationShape(GemCombination gComb) {
        if (gComb.getBreakImpact() != BreakImpact.NONE) {
            Gem gem = new Gem(gComb.getBreakImpact(), gComb.getColor());
            gem.setState(GemState.FALLING);
            setTile(gComb.getAnchorPoint(), gem);
        }
    }


    public void checkNewPossibleCombinations() {
        if (state != FieldState.BREAKING) return;

        while (!brokenPoints.isEmpty())
            trySaveCombinationAt(brokenPoints.poll());

        if (savedGemCombinations.isEmpty()) {
            GemCombination.saveCurrentSwapTime();
            state = hasPossibleMoves() ? FieldState.WAITING : FieldState.NO_POSSIBLE_MOVE;
        }
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
        lastIncrementScore = 0;
        processAllFallingGems();
        generateNewGems();
    }

    private void generateNewGems() {
        for (Point point : getTopEmptyPointsInField()) {
            setTile(point, new Gem());
            processFallingGemFrom(point);
        }
        if (isAnyTopPointEmpty()) {
            generateNewGems();
        }
    }

    private boolean isAnyTopPointEmpty() {
        for (Point point : Point.iterate(0, 0, 0, getColCount() - 1)) {
            if (getTile(skipTilesFrom(point, Direction.SOUTH, AirTile.class)) instanceof EmptyTile)
                return true;
        }
        return false;
    }

    private List<Point> getTopEmptyPointsInField() {
        List<Point> emptyTopPoints = new ArrayList<>();
        for (Point point : Point.iterate(0, 0, 0, getColCount() - 1)) {
            Point topPoint = point;
            topPoint = skipTilesFrom(topPoint, Direction.SOUTH, AirTile.class);
            if (getTile(topPoint) instanceof EmptyTile) {
                emptyTopPoints.add(topPoint);
            }
        }
        return emptyTopPoints;
    }

    private boolean isTileOfClass(Point point, Class<? extends Tile>[] tileClasses) {
        return Arrays.stream(tileClasses).anyMatch(c -> c.isInstance(getTile(point)));
    }

    @SafeVarargs
    private Point skipTilesFrom(Point fromPoint, Direction direction, Class<? extends Tile>... tileClasses) {
        Point skippedPoint = fromPoint;
        if (isTileOfClass(skippedPoint, tileClasses)) {
            do {
                skippedPoint = skippedPoint.moveTo(direction);
            } while (isTileOfClass(skippedPoint, tileClasses));
        }
        return skippedPoint;
    }


    private void processAllFallingGems() {
        Point[] fallingGemPoints = Arrays.stream(getAllPoints())
                .filter(p -> getTile(p) instanceof Gem)
                .filter(p -> ((Gem) getTile(p)).getState() == GemState.FALLING)
                .toArray(Point[]::new);

        for (int i = fallingGemPoints.length - 1; i >= 0; i--) {
            processFallingGemFrom(fallingGemPoints[i]);
        }
    }

    private void processFallingGemFrom(Point fromPoint) {
        Point bottomPoint = getFallingPoint(fromPoint);
        dropGemFromTo(fromPoint, bottomPoint);
    }

    private void dropGemFromTo(Point fromPoint, Point bottomPoint) {
        if (!(getTile(fromPoint) instanceof Gem)) return;

        Gem fallingGem = (Gem) getTile(fromPoint);
        if (getTile(bottomPoint) instanceof EmptyTile) {
            setTile(bottomPoint, fallingGem);
            setTile(fromPoint, new EmptyTile());
        }
        brokenPoints.add(bottomPoint);
        fallingGem.setState(GemState.IDLE);
    }

    private Point getFallingPoint(Point fromPoint) {
        Point bottomPoint = fromPoint.toSouth();
        boolean canFall = true;
        while (canFall) {
            bottomPoint = skipTilesFrom(bottomPoint, Direction.SOUTH, EmptyTile.class);
            canFall = getTile(bottomPoint) instanceof AirTile;
            if (canFall) {
                Point nextBottomPoint = skipTilesFrom(bottomPoint, Direction.SOUTH, AirTile.class);
                canFall = getTile(nextBottomPoint) instanceof EmptyTile;
                if (canFall) bottomPoint = nextBottomPoint;
            }
        }
        return bottomPoint.toNorth();
    }

    private void breakCombination(GemCombination gemsCombination) {
        GemCombination.increaseComboChain();
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

    public int getChainCombo() {
        return GemCombination.getChainCombo();
    }

    private void setTile(Point point, Tile tileObject) {
        if (point.isNotValid(getRowCount(), getColCount()))
            return;

        tiles[point.getRow()][point.getCol()] = tileObject;
    }

    private void trySaveCombinationAt(Point point) {
        GemCombination gemCombination = getCombinationAt(point);
        if (gemCombination.isValid() && !savedGemCombinations.contains(gemCombination))
            savedGemCombinations.add(gemCombination);
        else
            gemCombination.setGemsInCombo(false);
    }

    private boolean isCombinationAt(Point point) {
        GemCombination gemCombination = getCombinationAt(point);
        if (gemCombination.isValid()) {
            gemCombination.setGemsInCombo(false);
            return true;
        }
        return false;
    }


    private GemCombination getCombinationAt(Point point) {
        GemCombination combination = new GemCombination();
        if (!(getTile(point) instanceof Gem)) return combination;

        Set<Point> visited = new HashSet<>();
        Stack<Point> stackAdjacent = new Stack<>();
        combination.setAnchorPoint(point);
        combination.setColor(((Gem) getTile(point)).getColor());
        stackAdjacent.push(point);

        while (!stackAdjacent.isEmpty()) {
            Point currentPoint = stackAdjacent.pop();
            if (visited.contains(currentPoint) || !(getTile(currentPoint) instanceof Gem)) continue;
            Gem gem = (Gem) getTile(currentPoint);
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
            GemCombination.increaseComboChain();

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
        setNextTopGemFalling(point);
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

    private void setNextTopGemFalling(Point point) {
        point = skipTilesFrom(point, Direction.NORTH, EmptyTile.class, AirTile.class);
        if (point.isNotValid(getRowCount(), getColCount())) return;
        Tile tile = getTile(point);
        if (tile instanceof Gem && ((Gem) tile).getState() == GemState.IDLE) {
            ((Gem) tile).setState(GemState.FALLING);
        }
        if (!(tile instanceof LockTile))
            setNextTopGemFalling(point.toNorth());
    }

    public int getScore() {
        return currentScore;
    }

    public int getLastIncrementScore() {
        return lastIncrementScore;
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
