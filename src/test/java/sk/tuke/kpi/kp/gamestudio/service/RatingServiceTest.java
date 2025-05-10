package sk.tuke.kpi.kp.gamestudio.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import sk.tuke.kpi.kp.gamestudio.TestConfig;
import sk.tuke.kpi.kp.gamestudio.entity.Player;
import sk.tuke.kpi.kp.gamestudio.entity.Rating;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
class RatingServiceTest {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private PlayerService playerService;

    @Test
    void reset() {
        ratingService.reset();
        assertEquals(0, ratingService.getAverageRating("bejeweled"));
    }

    @Test
    void setAndGetRating() {
        playerService.reset();
        ratingService.reset();

        Player player = new Player("jaro");
        playerService.addPlayer(player);
        ratingService.setRating(new Rating("bejeweled", player, 4, new Date()));
        var rating =  ratingService.getRating("bejeweled", "jaro");
        assertEquals(4, rating);
        ratingService.setRating(new Rating("bejeweled", player, 2, new Date()));
        rating =  ratingService.getRating("bejeweled", "jaro");
        assertEquals(2, rating);
        assertThrows(RatingException.class, () -> ratingService.setRating(new Rating("bejeweled", player, 6, new Date())));
    }

    @Test
    void getAverageRating() {
        playerService.reset();
        ratingService.reset();
        Player player = new Player("jaro");
        playerService.addPlayer(player);
        ratingService.setRating(new Rating("bejeweled", player, 4, new Date()));
        player = new Player("fero");
        playerService.addPlayer(player);
        ratingService.setRating(new Rating("mines", player, 5, new Date()));
        player = new Player("mara");
        playerService.addPlayer(player);
        ratingService.setRating(new Rating("bejeweled", player, 4, new Date()));
        player = new Player("jano");
        playerService.addPlayer(player);
        ratingService.setRating(new Rating("bejeweled", player, 1, new Date()));
        player = new Player("zuza");
        playerService.addPlayer(player);
        ratingService.setRating(new Rating("bejeweled", player, 2, new Date()));
        var averageRating = ratingService.getAverageRating("bejeweled");
        assertEquals(3, averageRating);
    }
}