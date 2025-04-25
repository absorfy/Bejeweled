package sk.tuke.kpi.kp.gamestudio.game;

import sk.tuke.kpi.kp.gamestudio.game.consoleui.ConsoleUI;
import sk.tuke.kpi.kp.gamestudio.game.core.GameField;


public class Main {

    public static void main(String[] args) {
        ConsoleUI consoleUI = new ConsoleUI(new GameField());
        consoleUI.play();
    }
}
