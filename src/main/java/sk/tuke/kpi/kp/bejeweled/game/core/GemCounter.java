package sk.tuke.kpi.kp.bejeweled.game.core;

import java.util.*;

public class GemCounter {
    private final Random random = new Random();
    private final Map<GemColor, Integer> gemCounts;
    private final Map<GemColor, Double> bonusProbabilities;

    public GemCounter() {
        bonusProbabilities = new HashMap<>();
        gemCounts = new HashMap<>();
        for (GemColor color : GemColor.values()) {
            gemCounts.put(color, 0);
        }
    }

    public void addGem(GemColor color) {
        gemCounts.put(color, gemCounts.getOrDefault(color, 0) + 1);
    }

    public void removeGem(GemColor color) {
        gemCounts.put(color, Math.max(0, gemCounts.getOrDefault(color, 0) - 1));
    }

    public int getCount(GemColor color) {
        return gemCounts.getOrDefault(color, 0);
    }

    public void reset() {
        bonusProbabilities.clear();
        gemCounts.clear();
        for (GemColor color : GemColor.values()) {
            gemCounts.put(color, 0);
        }
    }

    public void modifyProbability(GemColor color, double bonus) {
        bonusProbabilities.put(color, bonusProbabilities.getOrDefault(color, 0.0) + bonus);
    }

    Map<GemColor, Double> getSpawnProbabilities() {
        Map<GemColor, Double> probabilities = new HashMap<>();
        double totalInverse = 0.0;

        for (Map.Entry<GemColor, Integer> entry : gemCounts.entrySet()) {
            double inverseCount = 1.0 / (entry.getValue() + 1);
            double modifiedProbability = inverseCount + bonusProbabilities.getOrDefault(entry.getKey(), 0.0);
            modifiedProbability = Math.max(modifiedProbability, 0);
            probabilities.put(entry.getKey(), modifiedProbability);
            totalInverse += modifiedProbability;
        }

        for (Map.Entry<GemColor, Double> entry : probabilities.entrySet()) {
            probabilities.put(entry.getKey(), (entry.getValue() / totalInverse) * 100);
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
                bonusProbabilities.clear();
                return entry.getKey();
            }
        }

        List<GemColor> gemTypes = new ArrayList<>(probabilities.keySet());
        bonusProbabilities.clear();
        return gemTypes.get(random.nextInt(gemTypes.size()));
    }
}
