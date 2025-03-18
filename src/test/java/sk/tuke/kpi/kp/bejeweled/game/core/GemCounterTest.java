package sk.tuke.kpi.kp.bejeweled.game.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GemCounterTest {
    private GemCounter gemCounter;

    @BeforeEach
    void setUp() {
        gemCounter = new GemCounter();
    }

    @Test
    void addGem() {
        int greenCount, redCount, orangeCount;
        for(greenCount = 0; greenCount < 2; greenCount++)
            gemCounter.addGem(GemColor.GREEN);
        for(redCount = 0; redCount < 1; redCount++)
            gemCounter.addGem(GemColor.RED);
        for(orangeCount = 0; orangeCount < 4; orangeCount++)
            gemCounter.addGem(GemColor.ORANGE);

        assertEquals(greenCount, gemCounter.getCount(GemColor.GREEN));
        assertEquals(redCount, gemCounter.getCount(GemColor.RED));
        assertEquals(orangeCount, gemCounter.getCount(GemColor.ORANGE));
        assertEquals(0, gemCounter.getCount(GemColor.PINK));
        assertEquals(0, gemCounter.getCount(GemColor.WHITE));
        assertEquals(0, gemCounter.getCount(GemColor.YELLOW));
        assertEquals(0, gemCounter.getCount(GemColor.BLUE));
    }

    @Test
    void removeGem() {
        int startCount = 0;
        for (GemColor color : GemColor.values()) {
            for(startCount = 0; startCount < 4; startCount++) {
                gemCounter.addGem(color);
            }
        }

        int greenCount, redCount, orangeCount;
        for(greenCount = 0; greenCount < 2; greenCount++)
            gemCounter.removeGem(GemColor.GREEN);
        for(redCount = 0; redCount < 1; redCount++)
            gemCounter.removeGem(GemColor.RED);
        for(orangeCount = 0; orangeCount < 4; orangeCount++)
            gemCounter.removeGem(GemColor.ORANGE);

        assertEquals(startCount - greenCount, gemCounter.getCount(GemColor.GREEN));
        assertEquals(startCount - redCount, gemCounter.getCount(GemColor.RED));
        assertEquals(startCount - orangeCount, gemCounter.getCount(GemColor.ORANGE));
        assertEquals(startCount, gemCounter.getCount(GemColor.PINK));
        assertEquals(startCount, gemCounter.getCount(GemColor.WHITE));
        assertEquals(startCount, gemCounter.getCount(GemColor.YELLOW));
        assertEquals(startCount, gemCounter.getCount(GemColor.BLUE));
    }

    @Test
    void reset() {
        gemCounter.addGem(GemColor.GREEN);
        gemCounter.addGem(GemColor.GREEN);
        gemCounter.addGem(GemColor.RED);

        gemCounter.reset();

        assertEquals(0, gemCounter.getCount(GemColor.GREEN));
        assertEquals(0, gemCounter.getCount(GemColor.RED));
        assertEquals(0, gemCounter.getCount(GemColor.ORANGE));
        assertEquals(0, gemCounter.getCount(GemColor.PINK));
        assertEquals(0, gemCounter.getCount(GemColor.WHITE));
        assertEquals(0, gemCounter.getCount(GemColor.YELLOW));
        assertEquals(0, gemCounter.getCount(GemColor.BLUE));
    }

    @Test
    void getEqualSpawnProbabilities() {
        gemCounter.addGem(GemColor.GREEN);
        gemCounter.addGem(GemColor.RED);
        gemCounter.addGem(GemColor.ORANGE);
        gemCounter.addGem(GemColor.PINK);
        gemCounter.addGem(GemColor.WHITE);
        gemCounter.addGem(GemColor.YELLOW);
        gemCounter.addGem(GemColor.BLUE);

        List<Double> probabilities = new ArrayList<>(gemCounter.getSpawnProbabilities().values());
        assertTrue(probabilities.stream().allMatch(p -> p.equals(probabilities.get(0))));
    }

    @Test
    void getNotEqualSpawnProbabilities() {
        gemCounter.addGem(GemColor.GREEN);
        gemCounter.addGem(GemColor.GREEN);
        gemCounter.addGem(GemColor.GREEN);
        gemCounter.addGem(GemColor.RED);
        gemCounter.addGem(GemColor.ORANGE);
        gemCounter.addGem(GemColor.PINK);
        gemCounter.addGem(GemColor.BLUE);

        Map<GemColor, Double> probabilities = gemCounter.getSpawnProbabilities();
        double greenProb = probabilities.get(GemColor.GREEN);

        probabilities.forEach((color, probability) -> {
            if(color !=  GemColor.GREEN)
                assertTrue(probability > greenProb);
        });
    }
}