package sk.tuke.kpi.kp.bejeweled.service;

import org.junit.jupiter.api.Test;
import sk.tuke.kpi.kp.bejeweled.entity.Score;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ScoreServiceTest {
    private final ScoreService scoreService = new ScoreServiceJDBC();

    @Test
    void reset() {
        scoreService.reset();
        assertEquals(0, scoreService.getTopScores("mines").size());
    }

    @Test
    void addScore() {
        scoreService.reset();
        scoreService.addScore(new Score("bejeweled", "jaro", 150, new Date()));
        var scores =  scoreService.getTopScores("bejeweled");
        assertEquals(1, scores.size());
        var score = scores.get(0);
        assertEquals("jaro", score.getPlayer());
        assertEquals("bejeweled", score.getGame());
        assertEquals(150, score.getPoints());
    }

    @Test
    void getTopScores() {
        scoreService.reset();
        scoreService.addScore(new Score("bejeweled", "jaro", 150, new Date()));
        scoreService.addScore(new Score("mines", "fero", 150, new Date()));
        scoreService.addScore(new Score("bejeweled", "mara", 200, new Date()));
        scoreService.addScore(new Score("bejeweled", "jano", 200, new Date()));
        scoreService.addScore(new Score("bejeweled", "zuza", 50, new Date()));
        var scores =  scoreService.getTopScores("bejeweled");
        assertEquals(4, scores.size());

        var score = scores.get(0);
        assertEquals("bejeweled", score.getGame());
        assertEquals(200, score.getPoints());

        score = scores.get(1);
        assertEquals("bejeweled", score.getGame());
        assertEquals(200, score.getPoints());

        score = scores.get(2);
        assertEquals("jaro", score.getPlayer());
        assertEquals("bejeweled", score.getGame());
        assertEquals(150, score.getPoints());

        score = scores.get(3);
        assertEquals("zuza", score.getPlayer());
        assertEquals("bejeweled", score.getGame());
        assertEquals(50, score.getPoints());
    }

}