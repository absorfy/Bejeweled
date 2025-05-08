package sk.tuke.kpi.kp.gamestudio.game.core;

import java.util.UUID;

public class LockTile extends Tile {
    private int needBreakCount;
    private final Gem gemContainer;

    private LockTile(int needBreakCount, Gem gem, UUID id) {
        super(id);
        if (needBreakCount < 1)
            throw new IllegalArgumentException();

        this.needBreakCount = needBreakCount;
        this.gemContainer = gem;
    }

    public LockTile(GemColor color) {
        this(3, new Gem(color));
    }

    public LockTile(int needBreakCount) {
        this(needBreakCount, new Gem());
    }

    public LockTile(int needBreakCount, Gem gem) {
        super();

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

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    @Override
    public LockTile clone() {
        return new LockTile(needBreakCount, gemContainer, super.getId());
    }
}
