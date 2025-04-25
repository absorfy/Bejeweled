package sk.tuke.kpi.kp.gamestudio.game.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PointTest {

    @Test
    public void testInvalidPoint() {
        Point point = new Point(-1, 10);
        assertTrue(point.isNotValid(5, 5), "The point with coordinates outside the field must be invalid");
    }

    @Test
    public void testValidPoint() {
        Point point = new Point(0, 4);
        assertFalse(point.isNotValid(5, 5), "The point with coordinates within  the field must be valid");
    }
}
