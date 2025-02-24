package bejeweled.core;

import java.util.*;

public class Field {
    private Tile[][] tiles;
    private final int winScore;
    private int currentScore;
    private FieldState state;

    public Field(int width, int height, int winScore) {
        if(width < 3 || height < 3 || winScore <= 0 ) {
            throw new IllegalArgumentException();
        }

        this.tiles = new Tile[height][width];
        this.winScore = winScore;
        this.currentScore = 0;
        this.state = FieldState.WAITING;

        generateField();
    }

    public void swapGems(Point point1, Point point2) {
        if(!point1.isValid(getWidth(), getHeight()) || !point2.isValid(getWidth(), getHeight())) return;
        if(!point1.isAdjacent(point2)) return;
        if(!(getTile(point1) instanceof Gem) || !(getTile(point2) instanceof Gem)) return;

        Gem gem1 = (Gem) getTile(point1);
        Gem gem2 = (Gem) getTile(point2);
        setTile(point1, gem2);
        setTile(point2, gem1);

        GemsCombination gemsComb1 = findGemsCombination(point1);
        GemsCombination gemsComb2 = findGemsCombination(point2);

        if(!gemsComb1.isValid() && !gemsComb2.isValid()) {
            setTile(point1, gem1);
            setTile(point2, gem2);
        }
        else {
            state = FieldState.BREAKING;
            processCombinations(gemsComb1, gemsComb2);
        }
    }

    private void processCombinations(GemsCombination... gemsCombination) {
        for(GemsCombination gComb : gemsCombination) {
            if(gComb.isValid()) {
                breakCombination(gComb);
            }
        }
    }

    private void breakCombination(GemsCombination gemsCombination) {
        for(Point p : gemsCombination.getPoints()) {
            breakTile(p);
            breakAdjacentLockTiles(p);
        }
    }

    private void breakAdjacentLockTiles(Point point) {
        Point from = new Point(point.getRow()-1, point.getCol()-1);
        Point to = new Point(point.getRow()+1, point.getCol()+1);

        for(Point p : Point.iterate(from, to)) {
            if(p.equals(point)) continue;

            if(point.isValid(getWidth(), getHeight()) && getTile(point) instanceof LockTile) {
                breakTile(point);
            }
        }
    }

    private void generateField() {
        for(Point p : Point.iterate(getWidth(), getHeight())) {
            do {
                setTile(p, new Gem());
            } while (findGemsCombination(p).isValid());
        }
    }

    private void setTile(Point point, Tile tileObject) {
        if(!point.isValid(getWidth(), getHeight()))
            return;

        tiles[point.getRow()][point.getCol()] = tileObject;
    }

    private GemsCombination findGemsCombination(Point point) {
        Set<Point> visited = new HashSet<>();
        Stack<Point> stack = new Stack<>();
        GemsCombination combination = new GemsCombination();

        if(!(getTile(point) instanceof Gem)) return combination;
        combination.setMovedPoint(point);

        Color combColor = ((Gem)getTile(point)).getColor();
        stack.push(point);

        while(!stack.isEmpty()) {
            Point current = stack.pop();
            int row = current.getRow();
            int col = current.getCol();

            if(row < 0 || row >= getHeight() || col < 0 || col >= getWidth()) continue;
            if(visited.contains(current)) continue;

            if(getTile(current) == null || !(getTile(current) instanceof Gem)) continue;
            if(((Gem) getTile(current)).getColor() != combColor) continue;

            visited.add(current);
            combination.addGemPoint(current);

            stack.push(new Point(row - 1, col));
            stack.push(new Point(row + 1, col));
            stack.push(new Point(row, col - 1));
            stack.push(new Point(row, col + 1));

        }
        return combination;
    }


    public boolean isWin() {
        return currentScore >= winScore;
    }


    private void breakTile(Point point) {
        Tile tile = getTile(point);
        if(tile instanceof Gem) {
            tiles[point.getRow()][point.getCol()] = new EmptyTile();
        }
        else if(tile instanceof LockTile) {
            LockTile lockTile = (LockTile) tile;
            if(lockTile.Break()) {
                tiles[point.getRow()][point.getCol()] = lockTile.getGem();
            }
        }
    }

    public int getFinishScore() {
        return winScore;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public FieldState getState() {
        return state;
    }

    public int getWidth() {
        return tiles[0].length;
    }

    public int getHeight() {
        return tiles.length;
    }

    public Tile getTile(Point point) {
        return tiles[point.getRow()][point.getCol()];
    }
}
