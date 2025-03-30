package sk.tuke.kpi.kp.bejeweled.game.core;

public class LockTile extends Tile {
    private int needBreakCount;
    private final Gem gemContainer;

    public LockTile(GemColor color) {
        this(3, new Gem(color));
    }

    public LockTile(int needBreakCount) {
        this(needBreakCount, new Gem());
    }

    public LockTile(int needBreakCount, Gem gem) {
        if (needBreakCount < 1)
            throw new IllegalArgumentException();

        this.needBreakCount = needBreakCount;
        this.gemContainer = gem;
    }

    public boolean Break() {
        if (needBreakCount > 0) {
            needBreakCount--;
            return needBreakCount == 0;
        }
        return false;
    }

    public Gem getGem() {
        return gemContainer;
    }

    public int getNeedBreakCount() {
        return needBreakCount;
    }
}
