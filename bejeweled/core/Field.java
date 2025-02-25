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
            if(!hasPossibleMoves()) {
                shuffle();
            }
        }
    }

    public void shuffle() {
        List<Gem> gems = new ArrayList<>();
        List<Point> gemPoints = new ArrayList<>();
        Random rand = new Random();

        for(Point point : Point.iterate(getWidth(), getHeight())) {
            Tile tile = getTile(point);
            if(tile instanceof Gem) {
                gems.add((Gem) tile);
                gemPoints.add(point);
            }
        }

        while(true) {
            Collections.shuffle(gems, rand);
            for(int i = 0; i < gems.size(); i++) {
                setTile(gemPoints.get(i), gems.get(i));
            }

            boolean hasCombination = false;
            for (Point p : gemPoints) {
                if (findGemsCombination(p).isValid()) {
                    hasCombination = true;
                    break;
                }
            }

            if(!hasCombination && hasPossibleMoves()) {
                break;
            }
        }
    }

    public boolean hasPossibleMoves() {
        int[] dx = {0, 1};
        int[] dy = {1, 0};

        for(Point point : Point.iterate(getWidth(), getHeight())) {
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
        if (!point1.isValid(getWidth(), getHeight()) || !point2.isValid(getWidth(), getHeight())) return false;

        Tile tile1 = getTile(point1);
        Tile tile2 = getTile(point2);
        if (!(tile1 instanceof Gem) || !(tile2 instanceof Gem)) return false;

        setTile(point1, tile2);
        setTile(point2, tile1);

        boolean foundCombination = findGemsCombination(point1).isValid() ||
                findGemsCombination(point2).isValid();

        setTile(point1, tile1);
        setTile(point2, tile2);

        return foundCombination;
    }


    private void processCombinations(GemsCombination... gemsCombination) {
        for(GemsCombination gComb : gemsCombination) {
            if(gComb.isValid()) {
                breakCombination(gComb);
            }
        }
        fillEmpties();
    }



    private void fillEmpties() {
        List<Point> points = new ArrayList<>();
        for (Point point : Point.iterate(getWidth(), getHeight())) {
            points.add(point);
        }
        Collections.reverse(points);

        for(Point point : points) {

            Tile tile = getTile(point);
            if(!(tile instanceof Gem)) continue;
            Gem gem = (Gem)tile;
            if(gem.getState() != GemState.FALLING) continue;

            Tile topTile = getTile(point.toNorth());
            if(topTile instanceof Gem) {
                ((Gem) topTile).setFalling();
            }

            Point bottomPoint = point;
            do {
                bottomPoint = bottomPoint.toSouth();
                if(!bottomPoint.isValid(getWidth(), getHeight())) break;
            } while(getTile(bottomPoint) instanceof AirTile || getTile(bottomPoint.toSouth()) instanceof EmptyTile);

            if(bottomPoint.isValid(getWidth(), getHeight()) && getTile(bottomPoint) instanceof EmptyTile) {
                setTile(bottomPoint, gem);
                setTile(point, new EmptyTile());
            }
            gem.setIdle();
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
        Stack<Point> stackAdjacent = new Stack<>();
        GemsCombination combination = new GemsCombination();

        if(!(getTile(point) instanceof Gem)) return combination;

        combination.setMovedPoint(point);
        Color combColor = ((Gem)getTile(point)).getColor();
        stackAdjacent.push(point);

        while(!stackAdjacent.isEmpty()) {
            Point current = stackAdjacent.pop();
            if(!current.isValid(getWidth(), getHeight()) || visited.contains(current)) continue;
            if(!(getTile(current) instanceof Gem) || ((Gem) getTile(current)).getColor() != combColor) continue;

            visited.add(current);
            combination.addGemPoint(current);

            stackAdjacent.push(current.toEast());
            stackAdjacent.push(current.toWest());
            stackAdjacent.push(current.toNorth());
            stackAdjacent.push(current.toSouth());
        }
        return combination;
    }


    public boolean isWin() {
        return currentScore >= winScore;
    }


    private void breakTile(Point point) {
        Tile tile = getTile(point);
        if(tile instanceof Gem) {
            setTile(point, new EmptyTile());
            setFallingGemFrom(point);
        }
        else if(tile instanceof LockTile) {
            LockTile lockTile = (LockTile) tile;
            if(lockTile.Break()) {
                Gem gem = lockTile.getGem();
                gem.setFalling();
                tiles[point.getRow()][point.getCol()] = gem;

            }
        }
    }

    private void setFallingGemFrom(Point point) {
        Tile tile = null;
        do {
            if(!point.isValid(getWidth(), getHeight())) break;
            point = point.toNorth();
        } while((tile = getTile(point)) instanceof AirTile);
        if(tile instanceof Gem) {
            ((Gem) tile).setFalling();
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
        if(!point.isValid(getWidth(), getHeight())) return null;
        return tiles[point.getRow()][point.getCol()];
    }
}
