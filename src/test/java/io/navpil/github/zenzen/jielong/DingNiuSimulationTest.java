package io.navpil.github.zenzen.jielong;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DingNiuSimulationTest {

    @Test
    public void resolveUnusualPoints() {
        final Stats stats = new Stats();
        stats.add("A", 0);
        stats.add("B", 0);
        stats.add("C", 0);
        stats.add("D", 0);

        final Map<String, Integer> points = DingNiuSimulation.resolvePoints(stats, new ArrayList<>(List.of("A", "B", "C", "D")), 2).getPoints();

        Assertions.assertThat(points.get("C")).isEqualTo(6);
        Assertions.assertThat(points.get("D")).isEqualTo(-3);
        Assertions.assertThat(points.get("A")).isEqualTo(-2);
        Assertions.assertThat(points.get("B")).isEqualTo(-1);
    }

    @Test
    public void resolveNormalPoints() {
        final Stats stats = new Stats();
        stats.add("A", 5);
        stats.add("B", 10);
        stats.add("C", 2);
        stats.add("D", 15);

        final Map<String, Integer> points = DingNiuSimulation.resolvePoints(stats, new ArrayList<>(List.of("A", "B", "C", "D")), 2).getPoints();

        Assertions.assertThat(points.get("C")).isEqualTo(6);
        Assertions.assertThat(points.get("A")).isEqualTo(-1);
        Assertions.assertThat(points.get("B")).isEqualTo(-2);
        Assertions.assertThat(points.get("D")).isEqualTo(-3);
    }

    @Test
    public void resolveWinningMatchPoints() {
        final Stats stats = new Stats();
        stats.add("A", 10);
        stats.add("B", 2);
        stats.add("C", 2);
        stats.add("D", 15);

        final Map<String, Integer> points = DingNiuSimulation.resolvePoints(stats, new ArrayList<>(List.of("A", "B", "C", "D")), 2).getPoints();

        Assertions.assertThat(points.get("C")).isEqualTo(6);
        Assertions.assertThat(points.get("B")).isEqualTo(-1);
        Assertions.assertThat(points.get("A")).isEqualTo(-2);
        Assertions.assertThat(points.get("D")).isEqualTo(-3);
    }

    @Test
    public void resolveLosingMatchPoints() {
        final Stats stats = new Stats();
        stats.add("A", 10);
        stats.add("B", 10);
        stats.add("C", 2);
        stats.add("D", 15);

        final Map<String, Integer> points = DingNiuSimulation.resolvePoints(stats, new ArrayList<>(List.of("A", "B", "C", "D")), 2).getPoints();

        Assertions.assertThat(points.get("C")).isEqualTo(6);
        Assertions.assertThat(points.get("B")).isEqualTo(-1);
        Assertions.assertThat(points.get("A")).isEqualTo(-2);
        Assertions.assertThat(points.get("D")).isEqualTo(-3);
    }
}