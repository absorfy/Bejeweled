package bejeweled.core;

public enum Color {

    RED("\u001B[31m"),
    BLUE("\u001B[34m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[38;5;226m"),
    PINK("\u001B[38;5;206m"),
    ORANGE("\u001B[38;5;208m"),
    WHITE("\u001B[38;5;15m");


    private final String colorCode;

    private Color(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColorCode() {
        return colorCode;
    }
}

