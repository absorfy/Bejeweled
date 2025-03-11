package test;

import bejeweled.core.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class FieldTest {

    private final Random randomGenerator = new Random();
    private Field field;

    @BeforeEach
    public void generateField() {
        int size = randomGenerator.nextInt(8) + 3;
        field = new Field(size, size);
    }

    @Test
    public void testStartPossibleCombination() {
        for (Point point : Point.iterate(field.getRowCount(), field.getColCount())) {
            for (Direction direction : Direction.values()) {
                field.swapGems(point, point.moveTo((direction)));
                if (field.getState() == FieldState.BREAKING)
                    break;
            }
            if (field.getState() == FieldState.BREAKING)
                break;
        }

        assertSame(FieldState.BREAKING, field.getState(),
                "The field must have a move to combine at the beginning of the game");
    }

    @Test
    public void testValidHint() {
        Point[] points = field.findCombinationPoints();
        field.swapGems(points[0], points[1]);
        assertSame(FieldState.BREAKING, field.getState(),
                "The function of finding combination crystals should return " +
                        "crystals that can be used to make a guaranteed combination");
    }

    @Test
    public void testFillingAfterValidCombination() {
        Point[] points = field.findCombinationPoints();
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
            Point[] points = field.findCombinationPoints();
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
        Point[] points = field.findCombinationPoints();
        field.swapGems(points[0], points[1]);
        field.processGemCombinations();
        assertTrue(field.getComboCount() > 0,
                "After a successful combination, the combo cannot be equal to 0");
    }

    @Test
    public void testScoreIncrement() {
        Point[] points = field.findCombinationPoints();
        field.swapGems(points[0], points[1]);
        field.processGemCombinations();
        assertTrue(field.getScore() > 0,
                "After a successful combination, the score cannot be equal to 0");
    }


}
