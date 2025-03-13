package sk.tuke.kpi.kp.bejeweled.game.core;

import java.util.Random;

public enum FieldShape {
    TRIANGLE,
    CIRCLE,
    SQUARE,
    SLOPE,
    DONUT;

    public static FieldShape random() {
        return FieldShape.values()[new Random().nextInt(FieldShape.values().length)];
    }
}
