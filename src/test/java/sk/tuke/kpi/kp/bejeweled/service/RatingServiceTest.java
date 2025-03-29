package sk.tuke.kpi.kp.bejeweled.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sk.tuke.kpi.kp.bejeweled.entity.Rating;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RatingServiceTest {

    @Autowired
    private RatingService ratingService;

    @Test
    void reset() {
        ratingService.reset();
        assertEquals(0, ratingService.getAverageRating("bejeweled"));
    }

    @Test
    void setAndGetRating() {
        ratingService.reset();
        ratingService.setRating(new Rating("bejeweled", "jaro", 4, new Date()));
        var rating =  ratingService.getRating("bejeweled", "jaro");
        assertEquals(4, rating);
        ratingService.setRating(new Rating("bejeweled", "jaro", 2, new Date()));
        rating =  ratingService.getRating("bejeweled", "jaro");
        assertEquals(2, rating);
        assertThrows(RatingException.class, () -> ratingService.setRating(new Rating("bejeweled", "jaro", 6, new Date())));
    }

    @Test
    void getAverageRating() {
        ratingService.reset();
        ratingService.setRating(new Rating("bejeweled", "jaro", 4, new Date()));
        ratingService.setRating(new Rating("mines", "fero", 5, new Date()));
        ratingService.setRating(new Rating("bejeweled", "mara", 4, new Date()));
        ratingService.setRating(new Rating("bejeweled", "jano", 1, new Date()));
        ratingService.setRating(new Rating("bejeweled", "zuza", 2, new Date()));
        var averageRating = ratingService.getAverageRating("bejeweled");
        assertEquals(3, averageRating);
    }
}