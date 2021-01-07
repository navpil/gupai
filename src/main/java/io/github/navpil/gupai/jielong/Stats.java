package io.github.navpil.gupai.jielong;

import java.util.LinkedHashMap;
import java.util.Map;

public class Stats {
    Map<String, Integer> points = new LinkedHashMap<>();
    private int suanZhangPlayer = -1;
    private boolean gameBlocked;

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

    public void setSuanZhangPlayer(int suanZhangPlayer) {
        this.suanZhangPlayer = suanZhangPlayer;
    }

    public int getSuanZhangPlayer() {
        return suanZhangPlayer;
    }

    public void setGameBlocked(boolean gameBlocked) {
        this.gameBlocked = gameBlocked;
    }

    public boolean getGameBlocked() {
        return gameBlocked;
    }

    public Integer getPointsFor(String name) {
        return points.getOrDefault(name, 0);
    }

    @Override
    public String toString() {
        return "Stats{" +
                "points=" + points +
                '}';
    }
}
