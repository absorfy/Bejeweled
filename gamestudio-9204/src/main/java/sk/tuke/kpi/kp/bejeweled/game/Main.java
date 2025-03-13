package sk.tuke.kpi.kp.bejeweled.game;

import sk.tuke.kpi.kp.bejeweled.game.consoleui.ConsoleUI;
import sk.tuke.kpi.kp.bejeweled.game.core.Field;

public class Main {

    public static void main(String[] args) {
        ConsoleUI.printGameDescription();
        ConsoleUI consoleUI = new ConsoleUI(new Field(8, 8));
        do {
            consoleUI.play();
        } while (consoleUI.askContinue());
    }
}
