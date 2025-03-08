package test;

import bejeweled.core.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void testComboIncrement() {
        Point[] points = field.findCombinationPoints();
        field.swapGems(points[0], points[1]);
        assertTrue(field.getComboCount() > 0,
                "After a successful combination, the combo cannot be equal to 0");
    }

    @Test
    public void testScoreIncrement() {
        Point[] points = field.findCombinationPoints();
        field.swapGems(points[0], points[1]);
        assertTrue(field.getScore() > 0,
                "After a successful combination, the score cannot be equal to 0");
    }


}
