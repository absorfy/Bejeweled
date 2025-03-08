package bejeweled;

import bejeweled.consoleui.ConsoleUI;

public class Bejeweled {

    public static void main(String[] args) {

        ConsoleUI consoleUI = new ConsoleUI(3, 3);
        do {
            consoleUI.play();
        } while (consoleUI.askContinue());
    }
}
