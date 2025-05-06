package sk.tuke.kpi.kp.gamestudio.server.webservice;

import org.springframework.web.bind.annotation.*;
import sk.tuke.kpi.kp.gamestudio.game.core.GameField;
import sk.tuke.kpi.kp.gamestudio.game.core.Point;

@RestController
@RequestMapping("/api/bejeweled")
public class BejeweledServiceRest {

    private final GameField gameField = new GameField();

    @GetMapping("/start")
    public GameField startNewGame() {
        gameField.reset();
        return gameField;
    }

    @GetMapping("/field")
    public GameField getField() {
        return gameField;
    }

    @PostMapping("/swap")
    public GameField swapGems(@RequestBody SwapRequest request) {
        Point p1 = new Point(request.row1, request.col1);
        Point p2 = new Point(request.row2, request.col2);
        gameField.swapGems(p1, p2);
        return gameField;
    }

    @PostMapping("/process")
    public GameField process() {
        gameField.processGemCombinations();
        return gameField;
    }

    @PostMapping("/fill")
    public GameField fill() {
        gameField.fillEmpties();
        return gameField;
    }

    @PostMapping("/check")
    public GameField check() {
        gameField.checkNewPossibleCombinations();
        return gameField;
    }

    public static class SwapRequest {
        public int row1;
        public int col1;
        public int row2;
        public int col2;
    }
}
