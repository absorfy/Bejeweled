package sk.tuke.kpi.kp.bejeweled.game.core;

import java.util.*;

public class GemCounter {
    private final Random random = new Random();
    private final Map<GemColor, Integer> gemCounts;
    private final Map<GemColor, Integer> comboPotentials;

    public GemCounter() {
        comboPotentials = new HashMap<>();
        gemCounts = new HashMap<>();
        initializeCountsWithGemColors(comboPotentials);
        initializeCountsWithGemColors(gemCounts);
    }

    public void initializeCountsWithGemColors(Map<GemColor, Integer> counts) {
        counts.clear();
        for (GemColor color : GemColor.values())
            counts.put(color, 0);
    }

    public void addGem(GemColor color) {
        gemCounts.put(color, gemCounts.getOrDefault(color, 0) + 1);
    }

    public void addComboPotential(GemColor color) {
        comboPotentials.put(color, comboPotentials.getOrDefault(color, 0) + 1);
    }

    public void removeGem(GemColor color) {
        gemCounts.put(color, Math.max(0, gemCounts.getOrDefault(color, 0) - 1));
    }

    public int getCount(GemColor color) {
        return gemCounts.getOrDefault(color, 0);
    }

    public void reset() {
        initializeCountsWithGemColors(comboPotentials);
        initializeCountsWithGemColors(gemCounts);
    }


    public void resetComboPotentials() {
        initializeCountsWithGemColors(comboPotentials);
    }


    Map<GemColor, Double> getSpawnProbabilities() {
        Map<GemColor, Double> probabilities = new HashMap<>();
        double totalInverse = 0.0;
        for (Map.Entry<GemColor, Integer> entry : gemCounts.entrySet()) {
            double inverseCount = 1.0 / (entry.getValue() + 1);
            double probability = inverseCount + comboPotentials.get(entry.getKey()) * 3;
            probabilities.put(entry.getKey(), probability);
            totalInverse += probability;
        }
        for (Map.Entry<GemColor, Double> entry : probabilities.entrySet()) {
            double dynamicProbability = (entry.getValue() / totalInverse) * 100;
            double uniformProbability = 100.0 / probabilities.size();
            double noiseFactor = 0.3;
            double mixedProbability = dynamicProbability * (1 - noiseFactor) + uniformProbability * noiseFactor;
            probabilities.put(entry.getKey(), mixedProbability);
        }
        return probabilities;
    }


    public GemColor getRandomGemColor() {
        Map<GemColor, Double> probabilities = getSpawnProbabilities();
        double randomValue = random.nextDouble() * 100;
        double cumulativeProbability = 0.0;
        for (Map.Entry<GemColor, Double> entry : probabilities.entrySet()) {
            cumulativeProbability += entry.getValue();
            if (randomValue <= cumulativeProbability) {
                return entry.getKey();
            }
        }
        return GemColor.random();
    }


}
