package bejeweled;

import bejeweled.consoleui.ConsoleUI;
import bejeweled.core.Field;

public class Bejeweled {

    public static void main(String[] args) {
        ConsoleUI.printGameDescription();
        ConsoleUI consoleUI = new ConsoleUI(new Field(8, 8));
        do {
            consoleUI.play();
        } while (consoleUI.askContinue());
    }
}
