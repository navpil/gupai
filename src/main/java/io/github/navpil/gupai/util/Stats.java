package io.github.navpil.gupai.util;

import io.github.navpil.gupai.jielong.player.MutableInteger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Stats {
    private Map<String, Integer> points = new LinkedHashMap<>();
    private int games;
    private int deuce;
    private List<Integer> rounds = new ArrayList<>();

    public Stats deuceAdded() {
        deuce++;
        return this;
    }

    public Stats gameAdded() {
        games++;
        return this;
    }

    public int getGames() {
        return games;
    }

    public boolean hasDeuces() {
        return deuce > 0;
    }

    public int getDeuce() {
        return deuce;
    }

    public void put(String name, int points) {
        this.points.put(name, points);
    }

    public void add(String name, int points) {
        Integer currentPoints = this.points.get(name);
        if (currentPoints == null) {
            currentPoints = 0;
        }
        this.points.put(name, currentPoints + points);
    }


    public Integer getPointsFor(String name) {
        return points.getOrDefault(name, 0);
    }

    public Map<String, Integer> getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return "Stats{" +
                "points=" + points +
                ", deuces=" + deuce +
                ", games=" + games +
                ", roundStats=" + roundStats() +
                '}';
    }

    private String roundStats() {
        long total = 0;
        Map<Integer, MutableInteger> roundsCount = new HashMap<>();
        for (Integer round : this.rounds) {
            total += round;
            if (!roundsCount.containsKey(round)) {
                roundsCount.put(round, new MutableInteger());
            }
            roundsCount.get(round).inc();
        }
        return "" + (total* 1.0 / games) + (roundsCount);
    }

    public Stats withRounds(int rounds) {
        this.rounds.add(rounds);
        return this;
    }

    public Stats withRounds(List<Integer> rounds) {
        this.rounds.addAll(rounds);
        return this;
    }

    public List<Integer> getRounds() {
        return rounds;
    }
}
