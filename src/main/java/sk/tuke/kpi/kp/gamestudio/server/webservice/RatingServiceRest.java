package sk.tuke.kpi.kp.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.tuke.kpi.kp.gamestudio.entity.Player;
import sk.tuke.kpi.kp.gamestudio.entity.Rating;
import sk.tuke.kpi.kp.gamestudio.server.dto.RatingDTO;
import sk.tuke.kpi.kp.gamestudio.service.PlayerService;
import sk.tuke.kpi.kp.gamestudio.service.RatingService;

@RestController
@RequestMapping("/api/rating")
public class RatingServiceRest {

    @Autowired
    private RatingService ratingService;
    @Autowired
    private PlayerService playerService;

    @GetMapping("/{game}")
    public int getAverageRating(@PathVariable String game) {
        return ratingService.getAverageRating(game);
    }

    @GetMapping("/{game}/{player}")
    public int getRating(@PathVariable String game, @PathVariable String player) {
        return ratingService.getRating(game, player);
    }

    @PostMapping
    public ResponseEntity<?> setRating(@RequestBody RatingDTO ratingDTO) {
        Player player = playerService.getPlayer(ratingDTO.getPlayerLogin());

        if (player == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Player with login '" + ratingDTO.getPlayerLogin() + "' not found");
        }

        Rating rating = new Rating(
                ratingDTO.getGame(),
                player,
                ratingDTO.getRating(),
                ratingDTO.getRatedOn()
        );

        ratingService.setRating(rating);
        return ResponseEntity.ok("Rating added");
    }
}
