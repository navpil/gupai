package io.navpil.github.zenzen.jielong;

import java.util.HashMap;
import java.util.Map;

public class Stats {
    Map<String, Integer> points = new HashMap<>();

    public void add(String name, int points) {
        this.points.put(name, points);
    }
}
