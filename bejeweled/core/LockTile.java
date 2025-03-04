package bejeweled.core;

import java.util.Random;

public class LockTile extends Tile {
    private int needBreakCount;
    private final Gem gemContainer;

    public LockTile(int needBreakCount) {
        if (needBreakCount < 1 || needBreakCount > 3) throw new IllegalArgumentException();

        this.needBreakCount = needBreakCount;
        this.gemContainer = new Gem();
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
