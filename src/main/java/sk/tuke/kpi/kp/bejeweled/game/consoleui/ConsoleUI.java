package sk.tuke.kpi.kp.bejeweled.game.consoleui;

import sk.tuke.kpi.kp.bejeweled.entity.Comment;
import sk.tuke.kpi.kp.bejeweled.entity.Rating;
import sk.tuke.kpi.kp.bejeweled.entity.Score;
import sk.tuke.kpi.kp.bejeweled.game.core.*;
import sk.tuke.kpi.kp.bejeweled.service.*;

import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ConsoleUI {
    private static Pattern PLAYING_PATTERN;
    private final static Pattern SERVICES_PATTERN = Pattern.compile("(SCORES)|((COMMENT)\\s*'([^']*)')|(COMMENTS)|((RESET)\\s*(COMMENTS|SCORES|RATING))|(RATING)|((RATING)\\s*([1-5]))", Pattern.CASE_INSENSITIVE);
    private final static String RESET_TEXT_COLOR = "\u001B[0m";
    private final static String RESET_BACKGROUND_COLOR = "\u001B[0m";

    private final Scanner scanner = new Scanner(System.in);
    private final ScoreService scoreService = new ScoreServiceJDBC();
    private final CommentService commentService = new CommentServiceJDBC();
    private final RatingService ratingService = new RatingServiceJDBC();

    private final Field field;

    public ConsoleUI(Field field) {
        this.field = field;

        PLAYING_PATTERN = Pattern.compile("^(X)|" +
                "(([A-" + (char) (field.getRowCount() + 'A') + "])([1-" + field.getColCount() + "])([WNSE]))|(FIELD)$", Pattern.CASE_INSENSITIVE);
    }

    public void play() {
        field.reset();
        printScores();
        printField();
        printHint();
        while (field.getState() != FieldState.NO_POSSIBLE_MOVE) {
            processInput();
            while (field.getState() == FieldState.BREAKING) {
                printField();
                field.processGemCombinations();
                printField();
                field.fillEmpties();
                printField();
                field.checkNewPossibleCombinations();
                if(field.getState() == FieldState.WAITING)
                    printHint();
            }
        }
        System.out.println("No possible moves! Your score: " + field.getScore() + "\n");
        saveScore();
    }

    public boolean askContinue() {
        System.out.print("Play again? (Y/N): ");
        var line = scanner.nextLine().toUpperCase();
        return line.equals("Y") || line.equals("YES");
    }

    private void printHint() {
        Point[] combPoints = field.findCombinationPoints(1).get(0);
        if (combPoints == null) return;
        System.out.println("HINT: " +
                (char) (combPoints[0].getRow() + 'A') + (combPoints[0].getCol() + 1) +
                " <-> " +
                (char) (combPoints[1].getRow() + 'A') + (combPoints[1].getCol() + 1) +
                "\n");
    }

    private void processInput() {
        System.out.print("Enter command (X - exit, A5N - swap gem at A-row and 5-column with north gem): ");
        var line = scanner.nextLine();
        if ("X".equalsIgnoreCase(line)) {
            System.exit(0);
        }
        if(!checkPlayingMather(line) && !checkServicesMather(line))
            System.out.println("Wrong input!");
    }

    private boolean checkServicesMather(String line) {
        var serviceMather = SERVICES_PATTERN.matcher(line);
        if (serviceMather.matches()) {
            if ("SCORES".equalsIgnoreCase(serviceMather.group(1))) {
                printScores();
            } else if ("COMMENTS".equalsIgnoreCase(serviceMather.group(5))) {
                printComments();
            } else if ("COMMENT".equalsIgnoreCase(serviceMather.group(3))) {
                saveComment(serviceMather.group(4));
            }
            else if("RESET".equalsIgnoreCase(serviceMather.group(7))) {
                resetService(serviceMather.group(8));
            }
            else if("RATING".equalsIgnoreCase(serviceMather.group(9))) {
                printRating();
            }
            else if("RATING".equalsIgnoreCase(serviceMather.group(11))) {
                saveRating(Integer.parseInt(serviceMather.group(12)));
            }
            return true;
        }
        return false;
    }

    private void printRating() {
        int myRating = ratingService.getRating("bejeweled", System.getProperty("user.name"));
        int averageRating = ratingService.getAverageRating("bejeweled");
        System.out.println("------------------------------\n" +
                "MY RATING.....: " + myRating + "\n" +
                "AVERAGE RATING: " + averageRating + "\n" +
                "------------------------------\n");
    }

    private void resetService(String line) {
        System.out.println("------------------------------");
        if("COMMENTS".equalsIgnoreCase(line)) {
            commentService.reset();
            System.out.println("Comments are reset");
        }
        else if("SCORES".equalsIgnoreCase(line)) {
            scoreService.reset();
            System.out.println("Scores are reset");
        }
        else if("RATING".equalsIgnoreCase(line)) {
            ratingService.reset();
            System.out.println("Ratings are reset");
        }
        System.out.println("------------------------------\n");
    }

    private boolean checkPlayingMather(String line) {
        var playingMather = PLAYING_PATTERN.matcher(line);
        if (playingMather.matches()) {
            if("FIELD".equalsIgnoreCase(playingMather.group(6))) {
                printField();
                printHint();
                return true;
            }

            int row = playingMather.group(3).toUpperCase().charAt(0) - 'A';
            int col = Integer.parseInt(playingMather.group(4)) - 1;
            Point point1 = new Point(row, col);
            Point point2 = getPointBySymbol(playingMather.group(5).charAt(0), point1);
            field.swapGems(point1, point2);
            return true;
        }
        return false;
    }

    private static Point getPointBySymbol(char symbol, Point point) {
        switch (Character.toUpperCase(symbol)) {
            case 'N': return point.toNorth();
            case 'S': return point.toSouth();
            case 'E': return point.toEast();
            case 'W': return point.toWest();
            default: return point;
        }
    }

    private void printField() {
        System.out.println("------------------------------\n");
        System.out.println("Gems:");
        field.gemCounter.gemCounts.forEach((key, value) -> System.out.println(key + ": " + value));
        System.out.println("Potentials:");
        field.gemCounter.comboPotentials.forEach((key, value) -> System.out.println(key + ": " + value));

        printGameStats();
        printHeader();
        printBody();
        System.out.println("------------------------------\n");
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

    public void printGameDescription() {
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
                "Good luck!\n" +
                "------------------------------\n");
    }

    private void printScores() {
        List<Score> scores = scoreService.getTopScores("bejeweled");
        System.out.println("------------------------------");
        if(scores.isEmpty()) {
            System.out.println("The scoreboard is empty..");
        }
        else {
            for (int i = 0; i < scores.size(); i++) {
                System.out.printf("%d. %s %d\n", i + 1, scores.get(i).getPlayer(), scores.get(i).getPoints());
            }
        }
        System.out.println("------------------------------\n");
    }

    private void printComments() {
        List<Comment> comments = commentService.getComments("bejeweled");
        System.out.println("------------------------------");
        if(comments.isEmpty()) {
            System.out.println("No comments..");
        }
        else {
            comments.forEach(comm ->
                System.out.printf("%s[%s] %s%s%s: %s\n",
                        GemColor.BLUE.getColorCode(),
                        comm.getCommentedOn().toString(),
                        GemColor.ORANGE.getColorCode(),
                        comm.getPlayer(),
                        RESET_TEXT_COLOR,
                        comm.getComment())
            );
        }
        System.out.println("------------------------------\n");
    }

    private void saveScore() {
        scoreService.addScore(
                new Score("bejeweled", System.getProperty("user.name"), field.getScore(), new Date())
        );
    }

    private void saveComment(String text) {
        commentService.addComment(
                new Comment("bejeweled", System.getProperty("user.name"), text, new Date())
        );
        System.out.println("------------------------------\n" +
                "Saved comment: " + text + "\n" +
                "------------------------------\n"
                );
    }

    private void saveRating(int rating) {
        ratingService.setRating(new Rating("bejeweled", System.getProperty("user.name"), rating, new Date()));
        System.out.println("------------------------------\n" +
                "Saved rating: " + rating + "\n" +
                "------------------------------\n"
        );
    }

}
