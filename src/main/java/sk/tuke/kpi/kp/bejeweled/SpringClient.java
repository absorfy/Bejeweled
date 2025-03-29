package sk.tuke.kpi.kp.bejeweled;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.tuke.kpi.kp.bejeweled.game.consoleui.ConsoleUI;
import sk.tuke.kpi.kp.bejeweled.game.core.GameField;
import sk.tuke.kpi.kp.bejeweled.service.CommentService;
import sk.tuke.kpi.kp.bejeweled.service.RatingService;
import sk.tuke.kpi.kp.bejeweled.service.ScoreService;
import sk.tuke.kpi.kp.bejeweled.service.jpa.CommentServiceJPA;
import sk.tuke.kpi.kp.bejeweled.service.jpa.RatingServiceJPA;
import sk.tuke.kpi.kp.bejeweled.service.jpa.ScoreServiceJPA;

@SpringBootApplication
@Configuration
public class SpringClient {

    public static void main(String[] args) {
        SpringApplication.run(SpringClient.class, args);
    }

//    @Bean
//    public CommandLineRunner runner(ConsoleUI ui) {
//        return args -> ui.play();
//    }

    @Bean
    public ConsoleUI consoleUI(GameField field) {
        return new ConsoleUI(field);
    }

    @Bean
    public GameField field() {
        return new GameField();
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
