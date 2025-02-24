package bejeweled.consoleui;

import bejeweled.core.*;
import com.sun.management.HotSpotDiagnosticMXBean;

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
        while (true) {
            printField();
            processInput();
        }
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
                case 'N': point2 = new Point(row-1, col); break;
                case 'S': point2 = new Point(row+1, col); break;
                case 'E': point2 = new Point(row, col+1); break;
                case 'W': point2 = new Point(row, col-1); break;
                default: point2 = new Point(row, col); break;
            }

            field.swapGems(point1, point2);
        }
        else {
            System.out.println("Wrong input!");
        }
    }

    private void printField() {
        System.out.print("   ");
        for(int col = 0; col < field.getWidth(); col++) {
            System.out.print(col + "  ");
        }
        for(Point p : Point.iterate(field.getWidth(), field.getHeight())) {
            if(p.getCol() == 0) { System.out.print("\n" + RESET_TEXT_COLOR + p.getRow() + "  "); }
            printTile(field.getTile(p));
        }
        System.out.println(RESET_TEXT_COLOR);
    }

    private void printTile(Tile tile) {
        if(tile instanceof Gem) {
            System.out.print(((Gem)tile).getColor().getColorCode() + "@  ");
        }
        else if(tile instanceof AirTile) {
            System.out.print("   ");
        }
        else if(tile instanceof LockTile) {
            System.out.print(((LockTile)tile).getGem().getColor().getColorCode() + "#  ");
        }
        else if(tile instanceof EmptyTile) {
            System.out.print(RESET_TEXT_COLOR + "-  ");
        }
    }
}
