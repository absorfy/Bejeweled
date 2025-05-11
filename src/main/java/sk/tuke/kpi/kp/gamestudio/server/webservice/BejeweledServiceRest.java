package sk.tuke.kpi.kp.gamestudio.server.webservice;

import org.springframework.web.bind.annotation.*;
import sk.tuke.kpi.kp.gamestudio.game.core.FieldState;
import sk.tuke.kpi.kp.gamestudio.game.core.GameField;
import sk.tuke.kpi.kp.gamestudio.game.core.Point;
import sk.tuke.kpi.kp.gamestudio.server.dto.GameFieldDTO;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/bejeweled")
public class BejeweledServiceRest {


    @GetMapping("/start")
    public GameFieldDTO startNewGame(HttpSession session) {
        GameField gameField = new GameField();
        session.setAttribute("gameField", gameField);
        return gameField.toDTO();
    }

    @GetMapping("/field")
    public GameFieldDTO getField(HttpSession session) {
        return ((GameField)session.getAttribute("gameField")).toDTO();
    }

    @PostMapping("/swap")
    public List<GameFieldDTO> swapGems(@RequestBody SwapRequest request, HttpSession session) {
        GameField gameField = (GameField) session.getAttribute("gameField");
        if (gameField == null) {
            return List.of();
        }

        Point p1 = new Point(request.row1, request.col1);
        Point p2 = new Point(request.row2, request.col2);
        List<GameFieldDTO> fields = new ArrayList<>();
        gameField.swapGems(p1, p2);

        fields.add(gameField.toDTO());
        while(gameField.getState() == FieldState.BREAKING) {
            gameField.processGemCombinations();
            fields.add(gameField.toDTO());
            gameField.fillEmpties();
            fields.add(gameField.toDTO());
            gameField.checkNewPossibleCombinations();
            fields.add(gameField.toDTO());
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
