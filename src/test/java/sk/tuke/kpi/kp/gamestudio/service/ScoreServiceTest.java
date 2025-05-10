package sk.tuke.kpi.kp.gamestudio.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import sk.tuke.kpi.kp.gamestudio.TestConfig;
import sk.tuke.kpi.kp.gamestudio.entity.Player;
import sk.tuke.kpi.kp.gamestudio.entity.Score;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
class ScoreServiceTest {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private PlayerService playerService;

    @Test
    void reset() {
        scoreService.reset();
        assertEquals(0, scoreService.getTopScores("mines").size());
    }

    @Test
    void addScore() {
        playerService.reset();
        scoreService.reset();

        Player player = new Player("jaro");
        playerService.addPlayer(player);
        scoreService.addScore(new Score("bejeweled", player, 150, new Date()));
        var scores =  scoreService.getTopScores("bejeweled");
        assertEquals(1, scores.size());
        var score = scores.get(0);
        assertEquals("jaro", score.getPlayer().getLogin());
        assertEquals("bejeweled", score.getGame());
        assertEquals(150, score.getPoints());
    }

    @Test
    void getTopScores() {
        playerService.reset();
        scoreService.reset();

        Player player = new Player("jaro");
        playerService.addPlayer(player);
        scoreService.addScore(new Score("bejeweled", player, 150, new Date()));
        player = new Player("fero");
        playerService.addPlayer(player);
        scoreService.addScore(new Score("mines", player, 150, new Date()));
        player = new Player("mara");
        playerService.addPlayer(player);
        scoreService.addScore(new Score("bejeweled", player, 200, new Date()));
        player = new Player("jano");
        playerService.addPlayer(player);
        scoreService.addScore(new Score("bejeweled", player, 200, new Date()));
        player = new Player("zuza");
        playerService.addPlayer(player);
        scoreService.addScore(new Score("bejeweled", player, 50, new Date()));
        var scores =  scoreService.getTopScores("bejeweled");
        assertEquals(4, scores.size());

        var score = scores.get(0);
        assertEquals("bejeweled", score.getGame());
        assertEquals(200, score.getPoints());

        score = scores.get(1);
        assertEquals("bejeweled", score.getGame());
        assertEquals(200, score.getPoints());

        score = scores.get(2);
        assertEquals("jaro", score.getPlayer().getLogin());
        assertEquals("bejeweled", score.getGame());
        assertEquals(150, score.getPoints());

        score = scores.get(3);
        assertEquals("zuza", score.getPlayer().getLogin());
        assertEquals("bejeweled", score.getGame());
        assertEquals(50, score.getPoints());
    }

}