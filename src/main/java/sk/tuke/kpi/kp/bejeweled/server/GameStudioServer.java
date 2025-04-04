package sk.tuke.kpi.kp.bejeweled.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.tuke.kpi.kp.bejeweled.service.CommentService;
import sk.tuke.kpi.kp.bejeweled.service.RatingService;
import sk.tuke.kpi.kp.bejeweled.service.ScoreService;
import sk.tuke.kpi.kp.bejeweled.service.jpa.CommentServiceJPA;
import sk.tuke.kpi.kp.bejeweled.service.jpa.RatingServiceJPA;
import sk.tuke.kpi.kp.bejeweled.service.jpa.ScoreServiceJPA;

@SpringBootApplication
@Configuration
@EntityScan("sk.tuke.kpi.kp.bejeweled.entity")
public class GameStudioServer {
    public static void main(String[] args) {
        SpringApplication.run(GameStudioServer.class, args);
    }

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
}
