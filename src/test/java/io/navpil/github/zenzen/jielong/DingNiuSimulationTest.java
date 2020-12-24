package io.navpil.github.zenzen.jielong;

import io.navpil.github.zenzen.DominoFactory;
import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.jielong.player.Player;
import io.navpil.github.zenzen.jielong.player.PlayerFactory;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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
        final Stats stats = new Stats();
        stats.points.put("Dima", 7);
        stats.points.put("MinMax", 2);
        stats.points.put("Rare", 11);
        stats.points.put("Combine", 14);
        stats.setSuanZhangPlayer(0);
        final DingNiuSimulation.WinningStats winningStats = DingNiuSimulation.resolvePoints(stats, List.of("Dima", "MinMax", "Rare", "Combine"), 0);
        Assertions.assertThat(winningStats.getPoints().get("Dima")).isEqualTo(-12);

    }

    @Test
    public void testWhyFails2() {
        final List<String> names = List.of("Dima", "MinMax", "Rare", "Combine");

        final ArrayList<Player> players = new ArrayList<>();
        players.add(PlayerFactory.createCombiningStrategyPlayer(names.get(0), DominoFactory.parseList("[6:5], [6:3], [3:1], [3:3], [6:1], [5:5]")));
        players.add(PlayerFactory.createMinMaxPlayer(names.get(1), DominoFactory.parseList("[6:4], [6:6], [6:1], [5:1], [4:4], [3:1]")));
        players.add(PlayerFactory.createRarenessPlayer(names.get(2), DominoFactory.parseList("[6:5], [6:4], [4:4], [5:1], [2:2], [3:3]")));
        players.add(PlayerFactory.createCombiningStrategyPlayer(names.get(3), DominoFactory.parseList("[6:2], [2:2], [5:5], [6:6], [1:1], [1:1]")));

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
                PlayerFactory.createMinMaxPlayer("Dima   ", DominoFactory.parseList("[6:6][6:5][1:1][5:5][2:2][5:5]".replace("][", "], ["))),
                PlayerFactory.createMinMaxPlayer("MinMax ", DominoFactory.parseList("[6:4][6:1][2:6][2:2][3:1][4:4]".replace("][", "], ["))),
                PlayerFactory.createRarenessPlayer("Rare   ", DominoFactory.parseList("[1:6][5:1][1:1][3:3][6:3][4:4]".replace("][", "], ["))),
                PlayerFactory.createCombiningStrategyPlayer("Combine", DominoFactory.parseList("[4:6][6:6][1:5][5:6][3:3][3:1]".replace("][", "], ["))));

        DingNiuSimulation.runSimulation(players, 6, 0);

    }

    @Test
    public void testWhyFails() {
        final List<String> names = List.of("Dima", "MinMax", "Rare", "Combine");

        List<Function<List<Domino>, Player>> playerFactories = new ArrayList<>();
//        playerFactories.add(list -> new RealPlayer(names.get(0), list));
//        playerFactories.add(list -> new RealPlayer(names.get(0), list, suanZhang));
        playerFactories.add(list -> PlayerFactory.createCombiningStrategyPlayer(names.get(0), list));
        playerFactories.add(list -> PlayerFactory.createMinMaxPlayer(names.get(1), list));
        playerFactories.add(list -> PlayerFactory.createRarenessPlayer(names.get(2), list));
        playerFactories.add(list -> PlayerFactory.createCombiningStrategyPlayer(names.get(3), list));

        final ArrayList<Player> players = new ArrayList<>();
        players.add(PlayerFactory.createCombiningStrategyPlayer(names.get(0), DominoFactory.parseList("[3:1], [5:1], [6:1], [3:3], [2:2], [6:3]")));
        players.add(PlayerFactory.createMinMaxPlayer(names.get(1), DominoFactory.parseList("[6:6], [6:5], [6:2], [6:4], [5:1], [3:3]")));
        players.add(PlayerFactory.createRarenessPlayer(names.get(2), DominoFactory.parseList("[3:1], [5:5], [1:1], [6:4], [4:4], [2:2]")));
        players.add(PlayerFactory.createCombiningStrategyPlayer(names.get(3), DominoFactory.parseList("[6:6], [1:1], [6:5], [6:1], [5:5], [4:4]")));

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

    public void testWhySuananang() {
        /*

            Next lead for 2
            Cumulative score: {Rare=-3, Combine=-3, Dima=13, MinMax=-7}
            io.navpil.github.zenzen.jielong.player.RealPlayer@5a2e4553
            AbstractPlayerImpl{name='MinMax', dominos=[[5:5], [2:2], [3:3], [4:4], [5:5], [5:1]], putDown=[]}
            AbstractPlayerImpl{name='Rare', dominos=[[2:2], [6:4], [5:1], [1:1], [6:4], [3:1]], putDown=[]}
            AbstractPlayerImpl{name='Combine', dominos=[[3:3], [6:3], [6:1], [1:1], [6:6], [6:6]], putDown=[]}
            Dragon [[1:1]]
            Player Combine played Move{side=1, inwards=1, outwards=6}
            Dragon [[1:1], [1:6]]
            You have these dominoes:
              (1) [6:5]
              (2) [4:4]
              (3) [6:2]
              (4) [6:1]
              (5) [3:1]
              (6) [6:5]
            Please choose which one to move
            4
            Put down [0] or move [1]?
            1
            Which side to put the [6:1]?
            1
            Player Dima played Move{side=1, inwards=6, outwards=1}
            Dragon [[1:1], [1:6], [6:1]]
            Player MinMax played Move{side=1, inwards=1, outwards=5}
            Dragon [[1:1], [1:6], [6:1], [1:5]]
            Player Rare played Move{side=1, inwards=5, outwards=1}
            Dragon [[1:1], [1:6], [6:1], [1:5], [5:1]]
            Player Combine played Move{side=1, inwards=1, outwards=1}
            Dragon [[1:1], [1:6], [6:1], [1:5], [5:1], [1:1]]
            You have these dominoes:
              (1) [6:5]
              (2) [4:4]
              (3) [6:2]
              (4) [3:1]
              (5) [6:5]
            Please choose which one to move
            4
            Put down [0] or move [1]?
            1
            Which side to put the [3:1]?
            1
            Player Dima played Move{side=1, inwards=1, outwards=3}
            Dragon [[1:1], [1:6], [6:1], [1:5], [5:1], [1:1], [1:3]]
            Player MinMax played Move{side=1, inwards=3, outwards=3}
            Dragon [[1:1], [1:6], [6:1], [1:5], [5:1], [1:1], [1:3], [3:3]]
            Player Rare played Move{side=2, inwards=1, outwards=3}
            Dragon [[3:1], [1:1], [1:6], [6:1], [1:5], [5:1], [1:1], [1:3], [3:3]]
            Player Combine played Move{side=1, inwards=3, outwards=6}
            Dragon [[3:1], [1:1], [1:6], [6:1], [1:5], [5:1], [1:1], [1:3], [3:3], [3:6]]
            You have these dominoes:
              (1) [6:5]
              (2) [4:4]
              (3) [6:2]
              (4) [6:5]
            Please choose which one to move
            1
            Put down [0] or move [1]?
            1
            Player Dima played Move{side=1, inwards=6, outwards=5}
            Dragon [[3:1], [1:1], [1:6], [6:1], [1:5], [5:1], [1:1], [1:3], [3:3], [3:6], [6:5]]
            Player MinMax played Move{side=1, inwards=5, outwards=5}
            Dragon [[3:1], [1:1], [1:6], [6:1], [1:5], [5:1], [1:1], [1:3], [3:3], [3:6], [6:5], [5:5]]
            Player Rare put a domino down: [2:2]
            Dragon [[3:1], [1:1], [1:6], [6:1], [1:5], [5:1], [1:1], [1:3], [3:3], [3:6], [6:5], [5:5]]
            Player Combine played Move{side=2, inwards=3, outwards=3}
            Dragon [[3:3], [3:1], [1:1], [1:6], [6:1], [1:5], [5:1], [1:1], [1:3], [3:3], [3:6], [6:5], [5:5]]
            You have these dominoes:
              (1) [4:4]
              (2) [6:2]
              (3) [6:5]
            Please choose which one to move
            3
            Put down [0] or move [1]?
            1
            Player Dima played Move{side=1, inwards=5, outwards=6}
            Dragon [[3:3], [3:1], [1:1], [1:6], [6:1], [1:5], [5:1], [1:1], [1:3], [3:3], [3:6], [6:5], [5:5], [5:6]]
            Player MinMax put a domino down: [2:2]
            Dragon [[3:3], [3:1], [1:1], [1:6], [6:1], [1:5], [5:1], [1:1], [1:3], [3:3], [3:6], [6:5], [5:5], [5:6]]
            Player Rare played Move{side=1, inwards=6, outwards=4}
            Dragon [[3:3], [3:1], [1:1], [1:6], [6:1], [1:5], [5:1], [1:1], [1:3], [3:3], [3:6], [6:5], [5:5], [5:6], [6:4]]
            Player Combine put a domino down: [6:6]
            Dragon [[3:3], [3:1], [1:1], [1:6], [6:1], [1:5], [5:1], [1:1], [1:3], [3:3], [3:6], [6:5], [5:5], [5:6], [6:4]]
            You have these dominoes:
              (1) [4:4]
              (2) [6:2]
            Please choose which one to move
            1
            Put down [0] or move [1]?
            1
            Player Dima played Move{side=1, inwards=4, outwards=4}
            Dragon [[3:3], [3:1], [1:1], [1:6], [6:1], [1:5], [5:1], [1:1], [1:3], [3:3], [3:6], [6:5], [5:5], [5:6], [6:4], [4:4]]
            Player MinMax played Move{side=1, inwards=4, outwards=4}
            Dragon [[3:3], [3:1], [1:1], [1:6], [6:1], [1:5], [5:1], [1:1], [1:3], [3:3], [3:6], [6:5], [5:5], [5:6], [6:4], [4:4], [4:4]]
            Player Rare played Move{side=1, inwards=4, outwards=6}
            Dragon [[3:3], [3:1], [1:1], [1:6], [6:1], [1:5], [5:1], [1:1], [1:3], [3:3], [3:6], [6:5], [5:5], [5:6], [6:4], [4:4], [4:4], [4:6]]
            Player Combine played Move{side=1, inwards=6, outwards=6}
            Dragon [[3:3], [3:1], [1:1], [1:6], [6:1], [1:5], [5:1], [1:1], [1:3], [3:3], [3:6], [6:5], [5:5], [5:6], [6:4], [4:4], [4:4], [4:6], [6:6]]
            You have these dominoes:
              (1) [6:2]
            Please choose which one to move
            1
            Put down [0] or move [1]?
            1
            Player Dima played Move{side=1, inwards=6, outwards=2}
            Dragon [[3:3], [3:1], [1:1], [1:6], [6:1], [1:5], [5:1], [1:1], [1:3], [3:3], [3:6], [6:5], [5:5], [5:6], [6:4], [4:4], [4:4], [4:6], [6:6], [6:2]]
            SuanZhang! CLASSIC
            Player MinMax put a domino down: [5:5]
            Dragon [[3:3], [3:1], [1:1], [1:6], [6:1], [1:5], [5:1], [1:1], [1:3], [3:3], [3:6], [6:5], [5:5], [5:6], [6:4], [4:4], [4:4], [4:6], [6:6], [6:2]]
            Dima has points: 0
            MinMax has points: 14
            Rare has points: 4
            Combine has points: 12
            Next lead for 0
            Cumulative score: {Rare=-5, Combine=-7, Dima=25, MinMax=-13}
            Player who SuanZhang-ed: 0

        * */
    }
}