package sk.tuke.kpi.kp.bejeweled.game.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.kpi.kp.bejeweled.game.core.tile.EmptyTile;
import sk.tuke.kpi.kp.bejeweled.game.core.tile.LockTile;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameFieldTest {

    private final Random randomGenerator = new Random();
    private GameField field;

    @BeforeEach
    public void generateField() {
        int size = randomGenerator.nextInt(10) + Math.max(GameField.minColCount, GameField.minRowCount);
        field = new GameField(size, size);
    }

    @Test
    public void testValidHint() {
        Point[] points = field.getCombinationDetector().findCombinationPoints(1).get(0);
        field.swapGems(points[0], points[1]);
        assertSame(FieldState.BREAKING, field.getState(),
                "The function of finding combination crystals should return " +
                        "crystals that can be used to make a guaranteed combination");
    }

    @Test
    public void testStartPossibleCombination() {
        assertNotNull(field.getCombinationDetector().findCombinationPoints(2),
                "The field must have a move to combine at the beginning of the game");
    }


    @Test
    public void testFillingAfterValidCombination() {
        Point[] points = field.getCombinationDetector().findCombinationPoints(1).get(0);
        field.swapGems(points[0], points[1]);
        while(field.getState() == FieldState.BREAKING) {
            field.processGemCombinations();
            field.fillEmpties();
            field.checkNewPossibleCombinations();
        }

        boolean isFilling = true;
        for(int row = 0; row < field.getRowCount(); row++) {
            for(int col = 0; col < field.getColCount(); col++) {
                if(field.getTile(row, col) instanceof EmptyTile) {
                    if(!isBlockTileOver(row, col)) {
                        isFilling = false;
                        break;
                    }

                }
            }
        }
        assertTrue(isFilling, "The field must be completely filled in after successful combinations");
    }

    private boolean isBlockTileOver(int row, int col) {
        for(; row >= 0; row--) {
            if(field.getTile(row, col) instanceof LockTile) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testSpeedComboIncrement() {
        int swapCount;
        for(swapCount = 0; swapCount < 3; swapCount++) {
            if(field.getState() == FieldState.NO_POSSIBLE_MOVE) break;
            Point[] points = field.getCombinationDetector().findCombinationPoints(1).get(0);
            field.swapGems(points[0], points[1]);
            while(field.getState() == FieldState.BREAKING) {
                field.processGemCombinations();
                field.fillEmpties();
                field.checkNewPossibleCombinations();
            }
        }

        assertEquals(swapCount, field.getSpeedCombo(), "Due to fast successful moves, it should increase the combo speed");
    }

    @Test
    public void testComboCountIncrement() {
        Point[] points = field.getCombinationDetector().findCombinationPoints(1).get(0);
        field.swapGems(points[0], points[1]);
        field.processGemCombinations();
        assertTrue(field.getChainCombo() > 0,
                "After a successful combination, the combo cannot be equal to 0");
    }

    @Test
    public void testScoreIncrement() {
        Point[] points = field.getCombinationDetector().findCombinationPoints(1).get(0);
        field.swapGems(points[0], points[1]);
        field.processGemCombinations();
        assertTrue(field.getScore() > 0,
                "After a successful combination, the score cannot be equal to 0");
    }

    @Test
    public void testGetFirstHint() {
        Point[] points = field.getHint();
        assertEquals(GameField.totalHintCount - 1, field.getHintCount());
        assertNotNull(points);
        field.swapGems(points[0], points[1]);
        assertEquals(FieldState.BREAKING, field.getState());
    }

    @Test
    public void testUsedAllHints() {
        for(int i = 0; i < GameField.totalHintCount; i++) {
            field.getHint();
        }
        Point[] points = field.getHint();
        assertEquals(0, field.getHintCount());
        assertNull(points);
    }
}