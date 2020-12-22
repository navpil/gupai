package io.navpil.github.zenzen.jielong;

import java.util.LinkedHashMap;
import java.util.Map;

public class Stats {
    Map<String, Integer> points = new LinkedHashMap<>();
    private int suanZhangPlayer = -1;
    private boolean gameBlocked;

    public void add(String name, int points) {
        this.points.put(name, points);
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
}
