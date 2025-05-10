package sk.tuke.kpi.kp.gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import sk.tuke.kpi.kp.gamestudio.game.consoleui.ConsoleUI;
import sk.tuke.kpi.kp.gamestudio.game.core.GameField;
import sk.tuke.kpi.kp.gamestudio.service.*;
import sk.tuke.kpi.kp.gamestudio.service.restclient.CommentServiceRestClient;
import sk.tuke.kpi.kp.gamestudio.service.restclient.RatingServiceRestClient;
import sk.tuke.kpi.kp.gamestudio.service.restclient.ScoreServiceRestClient;

@SpringBootApplication
@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
        pattern = "sk.tuke.kpi.kp.gamestudio.server.*"))
public class SpringClient {

    public static void main(String[] args) {
        //SpringApplication.run(SpringClient.class, args);
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
    }

    @Bean
    @Profile("!test")
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
    @Profile("!test")
    public ScoreService scoreService() {
        //return new ScoreServiceJPA();
        return new ScoreServiceRestClient();
    }

    @Bean
    @Profile("!test")
    public CommentService commentService() {
        //return new CommentServiceJPA();
        return new CommentServiceRestClient();
    }

    @Bean
    @Profile("!test")
    public RatingService ratingService() {
        //return new RatingServiceJPA();
        return new RatingServiceRestClient();
    }


    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
