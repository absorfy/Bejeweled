package bejeweled.core;

import java.util.ArrayList;
import java.util.List;

public class GemsCombination {
    private final List<Point> points;
    private Point movedPoint;
    private static int comboCounter = 0;

    public GemsCombination() {
        movedPoint = null;
        points = new ArrayList<>();
        comboCounter++;
    }

    public boolean isValid() {
        return points.size() >= 3;
    }

    public void addGemPoint(Point point) {
        points.add(point);
    }

    public void setMovedPoint(Point point) {
        movedPoint = point;
    }

    public void resetComboCounter() {
        comboCounter = 0;
    }

    public List<Point> getPoints() {
        return points;
    }

    public Point getMovedPoint() {
        return movedPoint;
    }

    public static int getComboCounter() {
        return comboCounter;
    }
}
