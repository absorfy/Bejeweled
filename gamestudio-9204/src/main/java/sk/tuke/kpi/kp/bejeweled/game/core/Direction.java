package sk.tuke.kpi.kp.bejeweled.game.core;

public enum Direction {
    NORTH, EAST, SOUTH, WEST;

    private static final Direction[] values = values();

    public Direction counterClockwise(int count) {
        count = (count % 4 + 4) % 4;
        return values[(this.ordinal() + 3 * count) % 4];
    }

    public Direction clockwise(int count) {
        count = (count % 4 + 4) % 4;
        return values[(this.ordinal() + count) % 4];
    }
}
