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

    public static void printGameDescription() {
        System.out.println("BEJEWELED\n" +
                "\n" +
                "• Crystals '◆':\n" +
                "\tThe crystals have only 7 colors: blue, green, yellow, white, red, pink, orange.\n" +
                "\tYou can move them to create combinations that will earn you points.\n" +
                "\tThere are also blocked crystals that cannot be used and are static, i.e., immovable,\n" +
                "\tand to release them you need to create 3 combinations near them.\n" +
                "\n" +
                "• There are also special crystals that have their own effects when combined: \n" +
                "\tBomb '⊗' - destroys a square 3 by 3 tiles away from itself.\n" +
                "\tHorizontal lightning '~' - destroys its entire row.\n" +
                "\tVertical lightning '⚡' - destroys its entire column.\n" +
                "\tStar '🟄' - destroys its entire row and column.\n" +
                "\tFor every special crystal used you get additional combos and points.\n" +
                "\n" +
                "• Combos (can be of any direction):\n" +
                "\n" +
                "◆  ◆  ◆\n" +
                "(does not create anything)\n" +
                "\n" +
                "◆  ◆  ◆  ◆\n" +
                "(creates directionally dependent lightning)\n" +
                "\n" +
                "◆  ◆  ◆  ◆  ◆\n" +
                "(creates directionally dependent lightning)\n" +
                "\n" +
                "◆  ◆\n" +
                "◆  ◆\n" +
                "(creates a bomb)\n" +
                "\n" +
                "◆  ◆  ◆\n" +
                "   ◆\n" +
                "   ◆\n" +
                "(creates a star)\n" +
                "\n" +
                "◆  ◆  ◆\n" +
                "◆\n" +
                "◆\n" +
                "(creates a star)\n" +
                "\n" +
                "◆  ◆  ◆  ◆  ◆\n" +
                "      ◆\n" +
                "      ◆\n" +
                "(creates a star)\n" +
                "\n" +
                "• Points, quantitative and speed combos:\n" +
                "\tYou get points for each combination you create according to its difficulty.\n" +
                "\tIf new combos are created after a successful one (due to new falling crystals),\n" +
                "\tthe points for each 3 combos are multiplied.\n" +
                "\tThe faster you move the crystals successfully, the more likely you are to get a bonus,\n" +
                "\twhich will also multiply the points for the combos you have made.\n" +
                "\n" +
                "Your goal is to collect as many points as possible\n" +
                "until there is no possible combination on the field.\n" +
                "Good luck!\n");
    }

//    BEJEWELED
//
//    crystals:
//    The crystals have only 7 colors: blue, green, yellow, white, red, pink, orange.
//    You can move them to create combinations that will earn you points.
//    There are also blocked crystals that cannot be used and are static, i.e., immovable, and to release them you need to create 3 combinations near them.
//
//    There are also special crystals that have their own effects when combined:
//    bomb - destroys a square 3 by 3 cells away from itself
//    horizontal lightning - destroys your entire row
//    vertical lightning - destroys its entire column
//    star - destroys its entire row and column
//for every special crystal used you get additional combos and points
//
//    combos (can be of any direction):
//    creates a bomb
//    creates a horizontal lightning bolt
//    creates a vertical lightning bolt
//    creates a star
//
//    points, quantity and speed combos
//    You get points for each combination you create according to its difficulty.
//    If new combos are created after a successful one (due to new falling crystals), the points for each 3 combos are multiplied.
//    the faster you move the crystals successfully, the more likely you are to receive a bonus, which will also multiply the points for the combos you have made.
//
//    Your goal is to collect as many points as possible until there is no possible combination on the board.
//    Good luck!


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
        System.out.print("Score: " + (field.getLastIncrementScore() > 0 ? field.getScore() - field.getLastIncrementScore() : field.getScore()));
        if(field.getLastIncrementScore() > 0) System.out.print(" + " + field.getLastIncrementScore());
        System.out.println();
        if(field.getChainCombo() > 1) System.out.println("Combo: " + field.getChainCombo());
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
            default: backgroundColor = RESET_BACKGROUND_COLOR; break;
        }
        System.out.print(backgroundColor + colorCode + symbol + RESET_TEXT_COLOR + RESET_TEXT_COLOR + "  ");
    }
}
