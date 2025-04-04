package sk.tuke.kpi.kp.bejeweled;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import sk.tuke.kpi.kp.bejeweled.game.consoleui.ConsoleUI;
import sk.tuke.kpi.kp.bejeweled.game.core.GameField;
import sk.tuke.kpi.kp.bejeweled.service.CommentService;
import sk.tuke.kpi.kp.bejeweled.service.RatingService;
import sk.tuke.kpi.kp.bejeweled.service.ScoreService;
import sk.tuke.kpi.kp.bejeweled.service.restclient.CommentServiceRestClient;
import sk.tuke.kpi.kp.bejeweled.service.restclient.RatingServiceRestClient;
import sk.tuke.kpi.kp.bejeweled.service.restclient.ScoreServiceRestClient;

@SpringBootApplication
@Configuration
public class SpringClient {

    public static void main(String[] args) {
        //SpringApplication.run(SpringClient.class, args);
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
    }

    @Bean
    public CommandLineRunner runner(ConsoleUI ui) {
        return args -> ui.play();
    }

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
        //return new ScoreServiceJPA();
        return new ScoreServiceRestClient();
    }

    @Bean
    public CommentService commentService() {
        //return new CommentServiceJPA();
        return new CommentServiceRestClient();
    }

    @Bean
    public RatingService ratingService() {
        //return new RatingServiceJPA();
        return new RatingServiceRestClient();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
