package test;

import bejeweled.core.Direction;
import bejeweled.core.Field;
import bejeweled.core.FieldState;
import bejeweled.core.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertSame;

public class FieldTest {

    private final Random randomGenerator = new Random();
    private Field field;


    @BeforeEach
    public void generateField() {
        int size = randomGenerator.nextInt(8) + 3;
        field = new Field(size, size);
    }

    @Test
    public void checkStartPossibleCombination() {
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
}
