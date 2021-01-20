package io.github.navpil.gupai.jielong;

import io.github.navpil.gupai.DominoParser;
import io.github.navpil.gupai.jielong.dingniu.DingNiuSimulation;
import io.github.navpil.gupai.jielong.dingniu.DingNiuStats;
import io.github.navpil.gupai.jielong.player.Player;
import io.github.navpil.gupai.jielong.player.PlayerFactory;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DingNiuSimulationTest {

    @Test
    public void resolveUnusualPoints() {
        final DingNiuStats stats = new DingNiuStats();
        stats.put("A", 0);
        stats.put("B", 0);
        stats.put("C", 0);
        stats.put("D", 0);

        final Map<String, Integer> points = DingNiuSimulation.resolvePoints(stats, new ArrayList<>(List.of("A", "B", "C", "D")), 2).getPoints();

        Assertions.assertThat(points.get("C")).isEqualTo(6);
        Assertions.assertThat(points.get("D")).isEqualTo(-3);
        Assertions.assertThat(points.get("A")).isEqualTo(-2);
        Assertions.assertThat(points.get("B")).isEqualTo(-1);
    }

    @Test
    public void resolveNormalPoints() {
        final DingNiuStats stats = new DingNiuStats();
        stats.put("A", 5);
        stats.put("B", 10);
        stats.put("C", 2);
        stats.put("D", 15);

        final Map<String, Integer> points = DingNiuSimulation.resolvePoints(stats, new ArrayList<>(List.of("A", "B", "C", "D")), 2).getPoints();

        Assertions.assertThat(points.get("C")).isEqualTo(6);
        Assertions.assertThat(points.get("A")).isEqualTo(-1);
        Assertions.assertThat(points.get("B")).isEqualTo(-2);
        Assertions.assertThat(points.get("D")).isEqualTo(-3);
    }

    @Test
    public void resolveWinningMatchPoints() {
        final DingNiuStats stats = new DingNiuStats();
        stats.put("A", 10);
        stats.put("B", 2);
        stats.put("C", 2);
        stats.put("D", 15);

        final Map<String, Integer> points = DingNiuSimulation.resolvePoints(stats, new ArrayList<>(List.of("A", "B", "C", "D")), 2).getPoints();

        Assertions.assertThat(points.get("C")).isEqualTo(6);
        Assertions.assertThat(points.get("B")).isEqualTo(-1);
        Assertions.assertThat(points.get("A")).isEqualTo(-2);
        Assertions.assertThat(points.get("D")).isEqualTo(-3);
    }

    @Test
    public void resolveLosingMatchPoints() {
        final DingNiuStats stats = new DingNiuStats();
        stats.put("A", 10);
        stats.put("B", 10);
        stats.put("C", 2);
        stats.put("D", 15);

        final Map<String, Integer> points = DingNiuSimulation.resolvePoints(stats, new ArrayList<>(List.of("A", "B", "C", "D")), 2).getPoints();

        Assertions.assertThat(points.get("C")).isEqualTo(6);
        Assertions.assertThat(points.get("B")).isEqualTo(-1);
        Assertions.assertThat(points.get("A")).isEqualTo(-2);
        Assertions.assertThat(points.get("D")).isEqualTo(-3);
    }

    @Test
    public void resolveSuanZhangPoints() {
/*
        Dima has points: 7
        MinMax has points: 2
        Rare has points: 11
        Combine has points: 14
        Next lead for 1
        Cumulative score: {Rare=-4, Combine=-6, Dima=-2, MinMax=12}
        Player who SuanZhang-ed: 0
        Player Rare got -4 points
        Player Combine got -6 points
        Player Dima got -2 points
        Player MinMax got 12 points
*/
        final DingNiuStats stats = new DingNiuStats();
        stats.put("Dima", 7);
        stats.put("MinMax", 2);
        stats.put("Rare", 11);
        stats.put("Combine", 14);
        stats.setSuanZhangPlayer(0);
        final DingNiuSimulation.WinningStats winningDingNiuStats = DingNiuSimulation.resolvePoints(stats, List.of("Dima", "MinMax", "Rare", "Combine"), 0);
        Assertions.assertThat(winningDingNiuStats.getPoints().get("Dima")).isEqualTo(-12);

    }

    @Test
    public void testWhyFails2() {
        final List<String> names = List.of("Dima", "MinMax", "Rare", "Combine");

        final ArrayList<Player> players = new ArrayList<>();
        players.add(PlayerFactory.createCombiningStrategyPlayer(names.get(0)).deal(DominoParser.parseList("[6:5], [6:3], [3:1], [3:3], [6:1], [5:5]")));
        players.add(PlayerFactory.createMinMaxPlayer(names.get(1)).deal( DominoParser.parseList("[6:4], [6:6], [6:1], [5:1], [4:4], [3:1]")));
        players.add(PlayerFactory.createRarenessPlayer(names.get(2)).deal( DominoParser.parseList("[6:5], [6:4], [4:4], [5:1], [2:2], [3:3]")));
        players.add(PlayerFactory.createCombiningStrategyPlayer(names.get(3)).deal( DominoParser.parseList("[6:2], [2:2], [5:5], [6:6], [1:1], [1:1]")));

        DingNiuSimulation.runSimulation(players, 6, 0);

        /*
        AbstractPlayerImpl{name='Dima', dominos=[[6:5], [6:3], [3:1], [3:3], [6:1], [5:5]], putDown=[]}
        AbstractPlayerImpl{name='MinMax', dominos=[[6:4], [6:6], [6:1], [5:1], [4:4], [3:1]], putDown=[]}
        AbstractPlayerImpl{name='Rare', dominos=[[6:5], [6:4], [4:4], [5:1], [2:2], [3:3]], putDown=[]}
        AbstractPlayerImpl{name='Combine', dominos=[[6:2], [2:2], [5:5], [6:6], [1:1], [1:1]], putDown=[]}
        Dragon [[6:5]]
        Player MinMax played Move{side=2, inwards=6, outwards=6}
        Dragon [[6:6], [6:5]]
        Player Rare played Move{side=2, inwards=6, outwards=4}
        Dragon [[4:6], [6:6], [6:5]]
        Player Combine played Move{side=1, inwards=5, outwards=5}
        Dragon [[4:6], [6:6], [6:5], [5:5]]
        Player Dima played Move{side=1, inwards=5, outwards=5}
        Dragon [[4:6], [6:6], [6:5], [5:5], [5:5]]
        Player MinMax played Move{side=2, inwards=4, outwards=6}
        Dragon [[6:4], [4:6], [6:6], [6:5], [5:5], [5:5]]
        Player Rare played Move{side=1, inwards=5, outwards=6}

         */
    }

    @Test
    public void testPlayersWillThinkAboutDoublesTwice() {
        final List<Player> players = List.of(
                PlayerFactory.createMinMaxPlayer("Dima   ").deal( DominoParser.parseList("[6:6][6:5][1:1][5:5][2:2][5:5]".replace("][", "], ["))),
                PlayerFactory.createMinMaxPlayer("MinMax ").deal( DominoParser.parseList("[6:4][6:1][2:6][2:2][3:1][4:4]".replace("][", "], ["))),
                PlayerFactory.createRarenessPlayer("Rare   ").deal( DominoParser.parseList("[1:6][5:1][1:1][3:3][6:3][4:4]".replace("][", "], ["))),
                PlayerFactory.createCombiningStrategyPlayer("Combine").deal( DominoParser.parseList("[4:6][6:6][1:5][5:6][3:3][3:1]".replace("][", "], ["))));

        DingNiuSimulation.runSimulation(players, 6, 0);

    }

    @Test
    public void testWhyFails() {
        final List<String> names = List.of("Dima", "MinMax", "Rare", "Combine");

        final ArrayList<Player> players = new ArrayList<>();
        players.add(PlayerFactory.createCombiningStrategyPlayer(names.get(0)).deal( DominoParser.parseList("[3:1], [5:1], [6:1], [3:3], [2:2], [6:3]")));
        players.add(PlayerFactory.createMinMaxPlayer(names.get(1)).deal( DominoParser.parseList("[6:6], [6:5], [6:2], [6:4], [5:1], [3:3]")));
        players.add(PlayerFactory.createRarenessPlayer(names.get(2)).deal( DominoParser.parseList("[3:1], [5:5], [1:1], [6:4], [4:4], [2:2]")));
        players.add(PlayerFactory.createCombiningStrategyPlayer(names.get(3)).deal( DominoParser.parseList("[6:6], [1:1], [6:5], [6:1], [5:5], [4:4]")));

        DingNiuSimulation.runSimulation(players, 6, 0);

        /*
        AbstractPlayerImpl{name='Dima', dominos=[[3:1], [5:1], [6:1], [3:3], [2:2], [6:3]], putDown=[]}
        AbstractPlayerImpl{name='MinMax', dominos=[[6:6], [6:5], [6:2], [6:4], [5:1], [3:3]], putDown=[]}
        AbstractPlayerImpl{name='Rare', dominos=[[3:1], [5:5], [1:1], [6:4], [4:4], [2:2]], putDown=[]}
        AbstractPlayerImpl{name='Combine', dominos=[[6:6], [1:1], [6:5], [6:1], [5:5], [4:4]], putDown=[]}
        Dragon [[3:3]]
        Player MinMax played Move{side=1, inwards=3, outwards=3}
        Dragon [[3:3], [3:3]]
        Player Rare played Move{side=1, inwards=3, outwards=1}
        Dragon [[3:3], [3:3], [3:1]]
        Player Combine played Move{side=1, inwards=1, outwards=6}
        Dragon [[3:3], [3:3], [3:1], [1:6]]
        Player Dima played Move{side=1, inwards=6, outwards=1}
        Dragon [[3:3], [3:3], [3:1], [1:6], [6:1]]
        Player MinMax played Move{side=1, inwards=1, outwards=5}
        Dragon [[3:3], [3:3], [3:1], [1:6], [6:1], [1:5]]
        Player Rare played Move{side=1, inwards=5, outwards=5}
        Dragon [[3:3], [3:3], [3:1], [1:6], [6:1], [1:5], [5:5]]
        Player Combine played Move{side=1, inwards=5, outwards=6}
        Dragon [[3:3], [3:3], [3:1], [1:6], [6:1], [1:5], [5:5], [5:6]]
        Player Dima played Move{side=1, inwards=6, outwards=3}
        Dragon [[3:3], [3:3], [3:1], [1:6], [6:1], [1:5], [5:5], [5:6], [6:3]]
        Player MinMax put a domino down: [6:2]
        Exception in thread "main" java.lang.IllegalStateException: Cannot connect 6 to 3
        at io.navpil.github.zenzen.jielong.GraphSuanZhang.executeMove(GraphSuanZhang.java:94)
        at io.navpil.github.zenzen.jielong.SuanZhang.executeMove(SuanZhang.java:73)
        at io.navpil.github.zenzen.jielong.DingNiuSimulation.runSimulation(DingNiuSimulation.java:183)
        at io.navpil.github.zenzen.jielong.DingNiuSimulation.main(DingNiuSimulation.java:65)

         */
    }

}
