package sk.tuke.kpi.kp.gamestudio;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import sk.tuke.kpi.kp.gamestudio.service.*;
import sk.tuke.kpi.kp.gamestudio.service.jpa.*;

@TestConfiguration
public class TestConfig {

    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceJPA();
    }

    @Bean
    public CommentService commentService() {
        return new CommentServiceJPA();
    }

    @Bean
    public RatingService ratingService() {
        return new RatingServiceJPA();
    }

    @Bean
    public PlayerService playerService() {
        return new PlayerServiceJPA();
    }
}
