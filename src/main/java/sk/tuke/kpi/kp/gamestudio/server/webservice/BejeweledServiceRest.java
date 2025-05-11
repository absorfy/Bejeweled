package sk.tuke.kpi.kp.gamestudio.server.webservice;

import org.springframework.web.bind.annotation.*;
import sk.tuke.kpi.kp.gamestudio.game.core.FieldState;
import sk.tuke.kpi.kp.gamestudio.game.core.GameField;
import sk.tuke.kpi.kp.gamestudio.game.core.Point;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/bejeweled")
public class BejeweledServiceRest {


    @GetMapping("/start")
    public GameField startNewGame(HttpSession session) {
        GameField gameField = new GameField();
        session.setAttribute("gameField", gameField);
        return gameField;
    }

    @GetMapping("/field")
    public GameField getField(HttpSession session) {
        return (GameField) session.getAttribute("gameField");
    }

    @PostMapping("/swap")
    public List<GameField> swapGems(@RequestBody SwapRequest request, HttpSession session) {
        GameField gameField = (GameField) session.getAttribute("gameField");
        if (gameField == null) {
            return List.of();
        }

        Point p1 = new Point(request.row1, request.col1);
        Point p2 = new Point(request.row2, request.col2);
        List<GameField> fields = new ArrayList<>();
        gameField.swapGems(p1, p2);
        try {
            fields.add(gameField.clone());
            while(gameField.getState() == FieldState.BREAKING) {
                gameField.processGemCombinations();
                fields.add(gameField.clone());
                gameField.fillEmpties();
                fields.add(gameField.clone());
                gameField.checkNewPossibleCombinations();
                fields.add(gameField.clone());
            }
        }
        catch (CloneNotSupportedException e) {
            System.out.println(e.getMessage());
        }

        return fields;
    }


    public static class SwapRequest {
        public int row1;
        public int col1;
        public int row2;
        public int col2;
    }
}
