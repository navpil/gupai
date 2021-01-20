package io.github.navpil.gupai.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class Stats {
    private Map<String, Integer> points = new LinkedHashMap<>();


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
                '}';
    }
}
