package bejeweled.core;

import java.util.Iterator;
import java.util.Objects;

public class Point {
    private final int row;
    private final int col;

    public Point(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }


    public boolean isValid(int colCount, int rowCount) {
        return row >= 0 && col >= 0 && row < rowCount && col < colCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point p = (Point) obj;
        return this.row == p.row && this.col == p.col;
    }


    public boolean isAdjacent(Point point) {
        return Math.abs(this.getCol() - point.getCol()) == 1 && this.getRow() == point.getRow() ||
                Math.abs(this.getRow() - point.getRow()) == 1 && this.getCol() == point.getCol();
    }

    public static Iterable<Point> iterate(int width, int height) {
        return iterate(new Point(0, 0), new Point(height - 1, width - 1));
    }

    public Point moveTo(Direction direction) {
        switch (direction) {
            case NORTH:
                return toNorth();
            case EAST:
                return toEast();
            case SOUTH:
                return toSouth();
            case WEST:
                return toWest();
            default:
                return this;
        }
    }

    public Point toNorth() {
        return new Point(row - 1, col);
    }

    public Point toEast() {
        return new Point(row, col + 1);
    }

    public Point toSouth() {
        return new Point(row + 1, col);
    }

    public Point toWest() {
        return new Point(row, col - 1);
    }


    public static Iterable<Point> iterate(int row1, int col1, int row2, int col2) {
        return iterate(new Point(row1, col1), new Point(row2, col2));
    }

    public static Iterable<Point> iterate(Point fromPoint, Point toPoint) {
        return () -> new Iterator<>() {
            private int row = fromPoint.getRow();
            private int col = fromPoint.getCol();
            private int height = toPoint.getRow() + 1;
            private int width = toPoint.getCol() + 1;

            public boolean hasNext() {
                return row < height && col < width;
            }

            @Override
            public Point next() {
                if (!hasNext()) {
                    throw new IllegalStateException("No more points to iterate.");
                }

                Point p = new Point(row, col);
                if (++col >= width) {
                    col = fromPoint.getCol();
                    row++;
                }
                return p;
            }
        };
    }

    @Override
    public String toString() {
        return "[row: " + row + ", col: " + col + "]";
    }
}
