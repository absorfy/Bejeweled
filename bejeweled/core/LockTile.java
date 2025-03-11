package bejeweled.core;

import java.util.Random;
import java.util.concurrent.locks.Lock;

public class LockTile extends Tile {
    private int needBreakCount;
    private final Gem gemContainer;

    public LockTile() {
        this(3, new Gem());
    }

    public LockTile(int needBreakCount) {
        this(needBreakCount, new Gem());
    }

    public LockTile(Gem gem) {
        this(3, gem);
    }

    public LockTile(int needBreakCount, Gem gem) {
        if (needBreakCount < 1 || needBreakCount > 3) throw new IllegalArgumentException();

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
