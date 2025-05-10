package sk.tuke.kpi.kp.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.tuke.kpi.kp.gamestudio.entity.Player;
import sk.tuke.kpi.kp.gamestudio.entity.Score;
import sk.tuke.kpi.kp.gamestudio.server.dto.ScoreDTO;
import sk.tuke.kpi.kp.gamestudio.service.PlayerService;
import sk.tuke.kpi.kp.gamestudio.service.ScoreService;

import java.util.List;

@RestController
@RequestMapping("/api/score")
public class ScoreServiceRest {

    @Autowired
    private ScoreService scoreService;
    @Autowired
    private PlayerService playerService;

    @GetMapping("/{game}")
    public List<Score> getTopScores(@PathVariable String game) {
        return scoreService.getTopScores(game);
    }

    @PostMapping
    public ResponseEntity<?> addScore(@RequestBody ScoreDTO scoreDTO) {
        Player player = playerService.getPlayer(scoreDTO.getPlayerLogin());

        if (player == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Player with login '" + scoreDTO.getPlayerLogin() + "' not found");
        }

        Score score = new Score(
                scoreDTO.getGame(),
                player,
                scoreDTO.getPoints(),
                scoreDTO.getPlayedOn()
        );

        scoreService.addScore(score);
        return ResponseEntity.ok("Score added");
    }
}
