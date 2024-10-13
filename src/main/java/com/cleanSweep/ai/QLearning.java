package com.cleanSweep.ai;

import java.util.HashMap;
import java.util.Map;

public class QLearning {
    private Map<String, Double> qTable = new HashMap<>();
    private double learningRate;
    private double discountFactor;
    private double explorationRate;

    public QLearning(double learningRate, double discountFactor, double explorationRate) {
        this.learningRate = learningRate;
        this.discountFactor = discountFactor;
        this.explorationRate = explorationRate;
    }

    public void updateQValues(String state, String action, double reward, String nextState) {
        String key = state + ":" + action;
        double currentQ = qTable.getOrDefault(key, 0.0);
        double maxNextQ = qTable.entrySet().stream()
                .filter(e -> e.getKey().startsWith(nextState + ":"))
                .mapToDouble(Map.Entry::getValue)
                .max()
                .orElse(0.0);

        double newQ = currentQ + learningRate * (reward + discountFactor * maxNextQ - currentQ);
        qTable.put(key, newQ);
    }
}
