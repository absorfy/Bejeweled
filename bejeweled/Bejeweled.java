package bejeweled;

import bejeweled.consoleui.ConsoleUI;
import bejeweled.core.Field;

public class Bejeweled {
    public static void main(String[] args) {
        ConsoleUI consoleUI = new ConsoleUI(new Field(5, 5));
        consoleUI.play();
    }
}
