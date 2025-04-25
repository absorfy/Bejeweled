package sk.tuke.kpi.kp.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.kpi.kp.gamestudio.entity.Score;
import sk.tuke.kpi.kp.gamestudio.game.core.FieldState;
import sk.tuke.kpi.kp.gamestudio.game.core.GameField;
import sk.tuke.kpi.kp.gamestudio.game.core.Point;
import sk.tuke.kpi.kp.gamestudio.game.core.Gem;
import sk.tuke.kpi.kp.gamestudio.game.core.AirTile;
import sk.tuke.kpi.kp.gamestudio.game.core.LockTile;
import sk.tuke.kpi.kp.gamestudio.game.core.Tile;
import sk.tuke.kpi.kp.gamestudio.service.ScoreService;

import java.util.Date;
import java.util.Objects;

@Controller
@RequestMapping("/bejeweled")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class BejeweledController {
    @Autowired
    private UserController userController;

    @Autowired
    private ScoreService scoreService;
    private GameField field = new GameField();

    private Point firstSelect = null;
    private Point[] hintSelects = null;

    @RequestMapping
    public String bejeweled(@RequestParam(value = "row", required = false) String row,
                            @RequestParam(value = "column", required = false) String column,
                            Model model)
    {
        if(row != null && column != null && field.getState() == FieldState.WAITING) {
            selectTile(Integer.parseInt(row), Integer.parseInt(column));
            if(field.getState() == FieldState.NO_POSSIBLE_MOVE && userController.isLogged())
                scoreService.addScore(new Score("bejeweled", userController.getLoggedUser().getLogin(), field.getScore(), new Date()));
        }

        model.addAttribute("scores", scoreService.getTopScores("bejeweled"));
        return "bejeweled";
    }

    public int getHintCount() {
        return field.getHintCount();
    }

    public String getState() {
        if (Objects.requireNonNull(field.getState()) == FieldState.NO_POSSIBLE_MOVE)
            return "no possible move";
        return "playing";
    }

    @RequestMapping("/new")
    public String newGame() {
        field = new GameField();
        firstSelect = null;
        return "redirect:/bejeweled";
    }

    @RequestMapping("/hint")
    public String getHint() {
        hintSelects = field.getHint();
        return "redirect:/bejeweled";
    }

    private void selectTile(int row, int col) {
        if(firstSelect == null) {
            firstSelect = new Point(row, col);
            return;
        }
        field.swapGems(firstSelect, new Point(row, col));
        while (field.getState() == FieldState.BREAKING) {
            field.processGemCombinations();
            field.fillEmpties();
            field.checkNewPossibleCombinations();
        }
        firstSelect = null;
    }

    public String getHtmlField() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='minefield'>\n");
        for (var row = 0; row < field.getRowCount(); row++) {
            sb.append("<tr>\n");
            for (var col = 0; col < field.getColCount(); col++) {
                var tile = field.getTile(row, col);
                sb.append(String.format("<td class='%s'>\n", getTdClass(tile.getClass(), new Point(row, col))));
                if (tile instanceof Gem)
                    sb.append(String.format("<a href='/bejeweled?row=%d&column=%d'>\n<img src='images/bejeweled/%s_gem.png'>\n</a>\n", row, col, ((Gem) tile).getColor().toString().toLowerCase()));
                else if (tile instanceof LockTile)
                    sb.append(String.format("<span>%d</span>\n", ((LockTile) tile).getNeedBreakCount()));
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");

        hintSelects = null;
        return sb.toString();
    }

    public String getTdClass(Class<? extends Tile> tileClass, Point point) {
        StringBuilder sb = new StringBuilder("tile");
        if(!tileClass.equals(AirTile.class))
            sb.append((field.getColCount() * point.getRow() + point.getCol() - (point.getRow() % 2)) % 2 + 1);
        else {
            sb.append("0");
        }

        if(firstSelect != null && firstSelect.equals(point)) {
            sb.append(" selected");
        }

        if(hintSelects != null && (hintSelects[0].equals(point) || hintSelects[1].equals(point))) {
            sb.append(" hint");
        }

        return sb.toString();
    }

}
