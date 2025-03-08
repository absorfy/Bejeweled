package bejeweled.consoleui;

import bejeweled.core.*;

import java.util.Scanner;
import java.util.regex.Pattern;

public class ConsoleUI {

    private static Pattern PLAYING_PATTERN;
    private final static String RESET_TEXT_COLOR = "\u001B[0m";
    private final Scanner scanner = new Scanner(System.in);

    private final int rowCount;
    private final int colCount;

    public ConsoleUI(int rowCount, int colCount) {
        this.rowCount = rowCount;
        this.colCount = colCount;

        PLAYING_PATTERN = Pattern.compile("^(X)|(([0-"
                + (rowCount - 1) + "])([0-"
                + (colCount - 1) + "])([WNSE]))$");
    }

    public void play() {
        Field field = new Field(rowCount, colCount);

        printField(field);
        printHint(field);
        while (field.getState() != FieldState.NO_POSSIBLE_MOVE) {
            processInput(field);

            while (field.getState() == FieldState.BREAKING) {
                printField(field);
                field.fillEmpties();
                printField(field);
                field.checkMovedPointsAfterCombo();
            }
        }
        System.out.println("No possible moves! Your score: " + field.getScore());
    }

    public boolean askContinue() {
        System.out.print("Play again? (Y/N): ");
        var line = scanner.nextLine().toUpperCase();
        return line.equals("Y") || line.equals("YES");
    }

    private void printHint(Field field) {
        Point[] combPoints = field.findCombinationPoints();
        if (combPoints == null) return;
        System.out.println("HINT: " + combPoints[0] + " <-> " + combPoints[1] + "\n");
    }

    private void processInput(Field field) {
        System.out.print("Enter command (X - exit, 25N - swap gem at 2 row and 5 col with north gem): ");
        var line = scanner.nextLine().toUpperCase();
        if (line.equals("X")) {
            System.exit(0);
        }

        var mather = PLAYING_PATTERN.matcher(line);
        if (mather.matches()) {

            int row = Integer.parseInt(mather.group(3));
            int col = Integer.parseInt(mather.group(4));
            Point point1 = new Point(row, col);
            Point point2;
            switch (mather.group(5).charAt(0)) {
                case 'N':
                    point2 = point1.toNorth();
                    break;
                case 'S':
                    point2 = point1.toSouth();
                    break;
                case 'E':
                    point2 = point1.toEast();
                    break;
                case 'W':
                    point2 = point1.toWest();
                    break;
                default:
                    point2 = point1;
                    break;
            }

            field.swapGems(point1, point2);
        } else {
            System.out.println("Wrong input!");
        }
    }

    private void printField(Field field) {
        System.out.println("Score: " + field.getScore());
        System.out.println("Combo: " + field.getComboCount() + "\n");

        System.out.print("   ");
        for (int col = 0; col < field.getColCount(); col++) {
            System.out.print(col + "  ");
        }
        for (Point p : Point.iterate(field.getColCount(), field.getRowCount())) {
            if (p.getCol() == 0) {
                System.out.print("\n" + RESET_TEXT_COLOR + p.getRow() + "  ");
            }
            printTile(field.getTile(p));
        }
        System.out.println(RESET_TEXT_COLOR + "\n");
    }

    private void printTile(Tile tile) {
        if (tile instanceof Gem) {
            String gemCode;
            if(((Gem) tile).getImpact() == BreakImpact.NONE) {
                switch (((Gem) tile).getState()) {
                    case IN_COMBINATION: gemCode = "$"; break;
                    case IDLE: gemCode = "@"; break;
                    case FALLING: gemCode = "!"; break;
                    default: gemCode = "?"; break;
                }
            }
            else {
                gemCode = "%";
            }
            System.out.print(((Gem) tile).getColor().getColorCode() + gemCode + "  ");
        } else if (tile instanceof LockTile) {
            System.out.print(((LockTile) tile).getGem().getColor().getColorCode() + ((LockTile) tile).getNeedBreakCount() + "  ");
        } else if (tile instanceof EmptyTile) {
            System.out.print(RESET_TEXT_COLOR + "-  ");
        }
    }
}
