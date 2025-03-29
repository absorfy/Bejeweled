package sk.tuke.kpi.kp.bejeweled.game;

import sk.tuke.kpi.kp.bejeweled.game.consoleui.ConsoleUI;
import sk.tuke.kpi.kp.bejeweled.game.core.GameField;


public class Main {

    public static void main(String[] args) {
        ConsoleUI consoleUI = new ConsoleUI(new GameField());
        consoleUI.play();
    }
}
