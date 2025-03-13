package sk.tuke.kpi.kp.bejeweled.game.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LockTileTest {

    @Test
    public void testUnlockTile() {
        LockTile lockTile = new LockTile(1);
        assertTrue(lockTile.Break(), "LockTile should be unlocked");
    }

    @Test
    public void testLockedTile() {
        LockTile lockTile = new LockTile(2);
        assertFalse(lockTile.Break(), "LockTile should be locked");
    }
}
