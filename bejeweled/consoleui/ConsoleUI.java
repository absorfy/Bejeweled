package bejeweled.consoleui;

import bejeweled.core.*;

import java.util.Scanner;
import java.util.regex.Pattern;

public class ConsoleUI {

    private final static Pattern INPUT_PATTERN = Pattern.compile("^(X)|(([0-9])([0-9])([WNSE]))$");
    private final Scanner scanner = new Scanner(System.in);
    private final static String RESET_TEXT_COLOR = "\u001B[0m";

    private final Field field;

    public ConsoleUI(Field field) {
        this.field = field;
    }

    public void play() {
        printField();
        while (true) {
            processInput();

            while(field.getState() == FieldState.BREAKING) {
                printField();
                field.fillEmpties();
                printField();
                field.checkMovedPointsAfterCombo();
            }

            if(FieldState.NO_POSSIBLE_MOVE == field.getState()) break;
        }
        System.out.println("No possible moves!");
    }

    private void processInput() {
        System.out.print("\nEnter command (X - exit, 25N - swap gem at 2 row and 5 col with north gem): ");
        var line = scanner.nextLine().toUpperCase();
        if (line.equals("X")) {
            System.exit(0);
        }

        var mather = INPUT_PATTERN.matcher(line);
        if(mather.matches()) {

            int row = Integer.parseInt(mather.group(3));
            int col = Integer.parseInt(mather.group(4));
            Point point1 = new Point(row, col);
            Point point2;
            switch(mather.group(5).charAt(0)) {
                case 'N': point2 = point1.toNorth(); break;
                case 'S': point2 = point1.toSouth(); break;
                case 'E': point2 = point1.toEast(); break;
                case 'W': point2 = point1.toWest(); break;
                default: point2 = point1; break;
            }

            field.swapGems(point1, point2);
        }
        else {
            System.out.println("Wrong input!");
        }
    }

    private void printField() {
        System.out.println("Score: " + field.getCurrentScore());
        System.out.println("Combo: " + field.getLastCombo() + "\n");

        System.out.print("   ");
        for(int col = 0; col < field.getColCount(); col++) {
            System.out.print(col + "  ");
        }
        for(Point p : Point.iterate(field.getColCount(), field.getRowCount())) {
            if(p.getCol() == 0) { System.out.print("\n" + RESET_TEXT_COLOR + p.getRow() + "  "); }
            printTile(field.getTile(p));
        }
        System.out.println(RESET_TEXT_COLOR + "\n\n");
    }

    private void printTile(Tile tile) {
        if(tile instanceof Gem) {
            String gemCode = ((Gem) tile).getImpact() == BreakImpact.NONE ? ((Gem) tile).getState().getCode() : "%";
            System.out.print(((Gem)tile).getColor().getColorCode() + gemCode + "  ");
        }
        else if(tile instanceof LockTile) {
            System.out.print(((LockTile)tile).getGem().getColor().getColorCode() + ((LockTile) tile).getNeedBreakCount() + "  ");
        }
        else if(tile instanceof EmptyTile) {
            System.out.print(RESET_TEXT_COLOR + "-  ");
        }
    }
}
