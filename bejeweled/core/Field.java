package bejeweled.core;

import java.util.*;

public class Field {
    private final Tile[][] tiles;
    private int currentScore;
    private FieldState state;
    private final Queue<Point> brokenPoints;

    public Field(int width, int height) {
        if(width < 3 || height < 3) {
            throw new IllegalArgumentException();
        }

        this.tiles = new Tile[height][width];
        brokenPoints = new ArrayDeque<>();
        this.currentScore = 0;
        this.state = FieldState.WAITING;

        generateField();
    }

    public void swapGems(Point point1, Point point2) {
        if(state !=  FieldState.WAITING) return;

        if(!point1.isValid(getColCount(), getRowCount()) || !point2.isValid(getColCount(), getRowCount())) return;
        if(!point1.isAdjacent(point2)) return;
        if(!(getTile(point1) instanceof Gem) || !(getTile(point2) instanceof Gem)) return;

        swapTiles(point1, point2);
        GemCombination gemsComb1 = findCombination(point1);
        GemCombination gemsComb2 = findCombination(point2);

        if(!gemsComb1.isValid() && !gemsComb2.isValid()) {
            swapTiles(point1, point2);
        }
        else {
            processGemCombinations(gemsComb1, gemsComb2);
        }
    }

    public boolean hasPossibleMoves() {
        int[] dx = {0, 1};
        int[] dy = {1, 0};

        for(Point point : Point.iterate(getColCount(), getRowCount())) {
            Tile tile = getTile(point);
            if (!(tile instanceof Gem)) continue;

            for (int d = 0; d < 2; d++) {
                int newRow = point.getRow() + dx[d];
                int newCol = point.getCol() + dy[d];

                if(trySwapAt(point, new Point(newRow, newCol)))
                    return true;
            }
        }
        return false;
    }

    private boolean trySwapAt(Point point1, Point point2) {
        if (!point1.isValid(getColCount(), getRowCount()) || !point2.isValid(getColCount(), getRowCount())) return false;
        if (!(getTile(point1) instanceof Gem) || !(getTile(point2) instanceof Gem)) return false;

        swapTiles(point1, point2);
        boolean anyComb = findCombination(point1).isValid() || findCombination(point2).isValid();
        swapTiles(point1, point2);

        return anyComb;
    }


    private void swapTiles(Point point1, Point point2) {
        Tile tile1 = getTile(point1);
        setTile(point1, getTile(point2));
        setTile(point2, tile1);
    }


    private void processGemCombinations(GemCombination... gemsCombination) {
        state = FieldState.BREAKING;
        for(GemCombination gComb : gemsCombination) {
            if(gComb.isValid()) {
                breakCombination(gComb);
            }
        }
    }

    public void checkMovedPoints() {
        if(state != FieldState.BREAKING) return;

        List<GemCombination> gemCombinations = new ArrayList<>();
        while(!brokenPoints.isEmpty()) {
            GemCombination gemCombination = findCombination(brokenPoints.poll());
            if(gemCombination.isValid())
                gemCombinations.add(gemCombination);
        }

        if(!gemCombinations.isEmpty()) {
            processGemCombinations(gemCombinations.toArray(new GemCombination[0]));
        }
        else {
            if(hasPossibleMoves()) {
                state = FieldState.WAITING;
            }
            else {
                state = FieldState.NO_POSSIBLE_MOVE;
            }
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
        if(state != FieldState.BREAKING) return;
        processAllFallingGems();
        generateNewGems();
    }

    private void generateNewGems() {
        for(Point point : Point.iterate(0, 0, 0, getColCount() - 1)) {
            if(getTile(point) instanceof EmptyTile) {
                setTile(point, new Gem());
                processFallingGem(point);
            }
        }
        if(Arrays.stream(tiles[0]).anyMatch(tile -> tile instanceof EmptyTile)) {
            generateNewGems();
        }
    }

    private void processAllFallingGems() {
        Point[] fallingGemPoints = Arrays.stream(getAllPoints())
                .filter(p -> getTile(p) instanceof Gem)
                .filter(p -> ((Gem) getTile(p)).getState() == GemState.FALLING)
                .toArray(Point[]::new);

        for(int i = fallingGemPoints.length - 1; i >= 0; i--) {
            processFallingGem(fallingGemPoints[i]);
        }
    }

    private void processFallingGem(Point fromPoint) {
        Point bottomPoint = fromPoint;
        do {
            bottomPoint = bottomPoint.toSouth();
        } while(getTile(bottomPoint.toSouth()) instanceof EmptyTile);

        Gem fallingGem = (Gem) getTile(fromPoint);

        if(getTile(bottomPoint) instanceof EmptyTile) {
            setTile(bottomPoint, fallingGem);
            setTile(fromPoint, new EmptyTile());
        }
        brokenPoints.add(bottomPoint);
        fallingGem.setIdle();
    }

    private void breakCombination(GemCombination gemsCombination) {
        for(Point p : gemsCombination.getPoints()) {
            breakTile(p);
            breakAdjacentLockTiles(p);
        }
    }

    private void breakAdjacentLockTiles(Point centerPoint) {
        Point from = new Point(centerPoint.getRow()-1, centerPoint.getCol()-1);
        Point to = new Point(centerPoint.getRow()+1, centerPoint.getCol()+1);

        for(Point point : Point.iterate(from, to)) {
            if(point.equals(centerPoint)) continue;

            if(centerPoint.isValid(getColCount(), getRowCount()) && getTile(centerPoint) instanceof LockTile) {
                breakTile(centerPoint);
            }
        }
    }

    private void generateField() {
        for(Point point : Point.iterate(getColCount(), getRowCount())) {
            do {
                setTile(point, new Gem());
            } while (findCombination(point).isValid());
        }

        if(!hasPossibleMoves()) {
            generateField();
        }
    }

    private void setTile(Point point, Tile tileObject) {
        if(!point.isValid(getColCount(), getRowCount()))
            return;

        tiles[point.getRow()][point.getCol()] = tileObject;
    }

    private GemCombination findCombination(Point point) {
        Set<Point> visited = new HashSet<>();
        Stack<Point> stackAdjacent = new Stack<>();
        GemCombination combination = new GemCombination();

        if(!(getTile(point) instanceof Gem)) return combination;

        combination.setMovedPoint(point);
        Color combColor = ((Gem)getTile(point)).getColor();
        stackAdjacent.push(point);

        while(!stackAdjacent.isEmpty()) {
            Point current = stackAdjacent.pop();
            if(!current.isValid(getColCount(), getRowCount()) || visited.contains(current)) continue;

            Tile checkTile;
            if(!((checkTile = getTile(current)) instanceof Gem)
                    //|| ((Gem) checkTile).getState() != GemState.IDLE
                    || ((Gem) checkTile).getColor() != combColor) {
                continue;
            }


            visited.add(current);
            //((Gem)checkTile).setInCombination();
            combination.addGemPoint(current);

            stackAdjacent.push(current.toEast());
            stackAdjacent.push(current.toWest());
            stackAdjacent.push(current.toNorth());
            stackAdjacent.push(current.toSouth());
        }
        return combination;
    }

    private void breakTile(Point point) {
        Tile tile = getTile(point);
        if(tile instanceof Gem)
            breakGem(point);
        else if(tile instanceof LockTile)
            breakLockTile(point, (LockTile) tile);
    }

    private void breakGem(Point point) {
        setTile(point, new EmptyTile());
        setTopGemFalling(point);
    }

    private void breakLockTile(Point lockPoint, LockTile lockTile) {
        if(lockTile.Break()) {
            Gem gem = lockTile.getGem();
            gem.setFalling();
            setTile(lockPoint, gem);
        }
    }

    private void setTopGemFalling(Point point) {
        Tile tile = null;
        do {
            point = point.toNorth();
            if(!point.isValid(getColCount(), getRowCount())) return;
        } while((tile = getTile(point)) instanceof EmptyTile);
        if(tile instanceof Gem) {
            ((Gem) tile).setFalling();
        }
        setTopGemFalling(point);
    }

    public int getCurrentScore() {
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

    public Tile getTile(Point point) {
        if(!point.isValid(getColCount(), getRowCount())) return null;
        return tiles[point.getRow()][point.getCol()];
    }
}
