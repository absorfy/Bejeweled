package sk.tuke.kpi.kp.bejeweled.server.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.kpi.kp.bejeweled.game.core.FieldState;
import sk.tuke.kpi.kp.bejeweled.game.core.GameField;
import sk.tuke.kpi.kp.bejeweled.game.core.Point;
import sk.tuke.kpi.kp.bejeweled.game.core.gem.Gem;
import sk.tuke.kpi.kp.bejeweled.game.core.tile.AirTile;
import sk.tuke.kpi.kp.bejeweled.game.core.tile.LockTile;

@Controller
@RequestMapping("/bejeweled")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class BejeweledController {
    private GameField field = new GameField();

    private Point firstSelect = null;


    @RequestMapping
    public String bejeweled(@RequestParam(value = "row", required = false) String row,
                            @RequestParam(value = "column", required = false) String column)
    {
        if(row != null && column != null) {
            selectTile(Integer.parseInt(row), Integer.parseInt(column));
        }
        return "bejeweled";
    }

    @RequestMapping("/new")
    public String newGame() {
        field = new GameField();
        firstSelect = null;
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
                sb.append(String.format("<td class='tile%s%s'>\n",
                        (!(tile instanceof AirTile) ? ((field.getColCount() * row + col - (row % 2)) % 2 + 1) : 0),
                        (firstSelect != null && row == firstSelect.getRow() && col == firstSelect.getCol()) ? " selected" : ""));

                if (tile instanceof Gem)
                    sb.append(String.format("<a href='/bejeweled?row=%d&column=%d'>\n<img src='images/bejeweled/%s_gem.png'>\n</a>\n", row, col, ((Gem) tile).getColor().toString().toLowerCase()));
                else if (tile instanceof LockTile)
                    sb.append(String.format("<span>%d</span>\n", ((LockTile) tile).getNeedBreakCount()));
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");
        return sb.toString();
    }

}
