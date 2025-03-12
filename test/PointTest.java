package test;

import bejeweled.core.Point;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
