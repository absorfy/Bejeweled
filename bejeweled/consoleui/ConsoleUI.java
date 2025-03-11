package bejeweled.consoleui;

import bejeweled.core.*;

import java.util.Scanner;
import java.util.regex.Pattern;

public class ConsoleUI {

    private static Pattern PLAYING_PATTERN;
    private final static String RESET_TEXT_COLOR = "\u001B[0m";
    private final static String RESET_BACKGROUND_COLOR = "\u001B[0m";
    private final Scanner scanner = new Scanner(System.in);

    private final Field field;

    public ConsoleUI(Field field) {
        this.field = field;

        PLAYING_PATTERN = Pattern.compile("^(X)|(" +
                "([A-" + (char) (field.getRowCount() + 'A') + "])" +
                "([1-" + field.getColCount() + "])([WNSE]))$");
    }

    public void play() {
        field.reset();

        printField();
        while (field.getState() != FieldState.NO_POSSIBLE_MOVE) {
            printHint();
            processInput();
            while (field.getState() == FieldState.BREAKING) {
                printField();
                field.processGemCombinations();
                printField();
                field.fillEmpties();
                printField();
                field.checkNewPossibleCombinations();
            }
        }
        System.out.println("No possible moves! Your score: " + field.getScore());
    }

    public boolean askContinue() {
        System.out.print("Play again? (Y/N): ");
        var line = scanner.nextLine().toUpperCase();
        return line.equals("Y") || line.equals("YES");
    }

    private void printHint() {
        Point[] combPoints = field.findCombinationPoints();
        if (combPoints == null) return;
        System.out.println("HINT: " +
                (char) (combPoints[0].getRow() + 'A') + (combPoints[0].getCol() + 1) +
                " <-> " +
                (char) (combPoints[1].getRow() + 'A') + (combPoints[1].getCol() + 1) +
                "\n");
    }

    private void processInput() {
        System.out.print("Enter command (X - exit, A5N - swap gem at A-row and 5-column with north gem): ");
        var line = scanner.nextLine().toUpperCase();
        if (line.equals("X")) {
            System.exit(0);
        }

        var mather = PLAYING_PATTERN.matcher(line);
        if (mather.matches()) {
            int row = mather.group(3).toUpperCase().charAt(0) - 'A';
            int col = Integer.parseInt(mather.group(4)) - 1;
            Point point1 = new Point(row, col);
            Point point2 = getPointBySymbol(mather.group(5).charAt(0), point1);
            field.swapGems(point1, point2);
        } else {
            System.out.println("Wrong input!");
        }
    }

    private static Point getPointBySymbol(char symbol, Point point) {
        switch (symbol) {
            case 'N': return point.toNorth();
            case 'S': return point.toSouth();
            case 'E': return point.toEast();
            case 'W': return point.toWest();
            default: return point;
        }
    }

    private void printField() {
        printGameStats();
        printHeader();
        printBody();
    }

    private void printBody() {
        for (Point p : Point.iterate(field.getColCount(), field.getRowCount())) {
            if (p.getCol() == 0)
                System.out.print("\n" + RESET_TEXT_COLOR + (char) (p.getRow() + 'A') + "  ");
            printTile(field.getTile(p));
        }
        System.out.println(RESET_TEXT_COLOR + "\n");
    }

    private void printHeader() {
        System.out.print("\n   ");
        for (int col = 1; col <= field.getColCount(); col++) {
            System.out.print(col + "  ");
        }
    }

    private void printGameStats() {
        System.out.println("Score: " + field.getScore());
        if(field.getComboCount() > 1) System.out.println("Combo: " + field.getComboCount());
        if(field.getSpeedCombo() > 0) System.out.println("Speed combo: " + field.getSpeedCombo());
    }

    private void printTile(Tile tile) {
        if (tile instanceof Gem) {
            printGem((Gem) tile);
        } else if (tile instanceof LockTile) {
            printLockTile((LockTile) tile);
        } else if (tile instanceof EmptyTile) {
            System.out.print("◌  ");
        } else if (tile instanceof AirTile) {
            System.out.print("   ");
        }

    }

    private static void printLockTile(LockTile lockTile) {
        String colorCode;
        String symbol;
        colorCode = lockTile.getGem().getColor().getColorCode();
        switch (lockTile.getNeedBreakCount()) {
            case 3: symbol = "■"; break;
            case 2: symbol = "◩"; break;
            case 1: symbol = "□"; break;
            default: symbol = "?"; break;
        }
        System.out.print(colorCode + symbol + RESET_TEXT_COLOR + RESET_TEXT_COLOR + "  ");
    }

    private static void printGem(Gem gem) {
        String colorCode;
        String symbol;
        colorCode = gem.getColor().getColorCode();
        symbol = gem.getImpact().getSymbol();
        String backgroundColor;
        switch (gem.getState()) {
            case FALLING: backgroundColor = "\u001B[48;5;52m"; break;
            case IN_COMBINATION: backgroundColor = "\u001B[48;5;22m"; break;
            default: backgroundColor = RESET_TEXT_COLOR; break;
        }
        System.out.print(backgroundColor + colorCode + symbol + RESET_TEXT_COLOR + RESET_TEXT_COLOR + "  ");
    }
}
