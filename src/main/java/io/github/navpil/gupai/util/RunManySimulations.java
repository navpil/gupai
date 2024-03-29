package io.github.navpil.gupai.util;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.fishing.tsungshap.NamedPlayer;

import java.util.Collections;
import java.util.List;

public class RunManySimulations {

    public enum PointsCalculationType {
        WITH_EACH_OTHER, KEEP_AS_IS
    }

    public interface SimulationFunctionWithRuleSet<X, Y> {
        Stats runSimulation(List<Domino> dominos, List<X> players, Y ruleSet, int whoGoesFirst);
    }

    public interface SimulationFunction<X> {
        Stats runSimulation(List<Domino> dominos, List<X> players, int whoGoesFirst);
    }

    public  <Player extends NamedPlayer> Stats runManySimulations(List<Domino> deck, List<Player> players, int maxSimCount, SimulationFunction<Player> simFunction, PointsCalculationType pointsCalculationType) {
        return runManySimulations(deck, players, null, maxSimCount, (d, p, rs, pc) -> simFunction.runSimulation(d, p, pc), pointsCalculationType);
    }

    public <T extends NamedPlayer, RuleSet> Stats runManySimulations(List<Domino> deck, List<T> players, RuleSet rules, int simCount, SimulationFunctionWithRuleSet<T, RuleSet> f) {
        return runManySimulations(deck, players, rules, simCount, f, PointsCalculationType.WITH_EACH_OTHER);
    }

    public <T extends NamedPlayer, RuleSet> Stats runManySimulations(List<Domino> deck, List<T> players, RuleSet rules, int simCount, SimulationFunctionWithRuleSet<T, RuleSet> f, PointsCalculationType pointsCalculationType) {
        Stats overallStats = new Stats();
        for (NamedPlayer player : players) {
            overallStats.put(player.getName(), 0);
        }
        int firstPlayer = 0;
        for (int sim = 0; sim < simCount; sim++) {
            Collections.shuffle(deck);

            final Stats stats = f.runSimulation(
                    deck,
                    players,
                    rules, firstPlayer
            );

            if (pointsCalculationType == PointsCalculationType.WITH_EACH_OTHER) {
                for (int i = 0; i < players.size(); i++) {
                    final NamedPlayer p1 = players.get(i);
                    for (int j = i + 1; j < players.size(); j++) {
                        final NamedPlayer p2 = players.get(j);
                        final Integer p1Points = stats.getPointsFor(p1.getName());
                        final Integer p2Points = stats.getPointsFor(p2.getName());

                        final int p1WinningPoints = p1Points - p2Points;

                        overallStats.add(p1.getName(), p1WinningPoints);
                        overallStats.add(p2.getName(), -p1WinningPoints);
                    }
                }
            } else {
                for (final NamedPlayer p1 : players) {
                    overallStats.add(p1.getName(), stats.getPointsFor(p1.getName()));
                }
            }

            //Rules do not specify who goes first next, but let's assume it's the same as for JieLong
            //the one who gets the most points, hand closer to dealer wins when a tie occurs.
            firstPlayer = nextPlayerCalculation(stats, firstPlayer, players);
            if (stats.hasDeuces()) {
                overallStats.deuceAdded();
            }
            overallStats.gameAdded().withRounds(stats.getRounds());
            System.out.println("Running stats: " + overallStats);
        }
        return overallStats;
    }

    public static int nextPlayerCalculation(Stats stats, int previousFirstPlayer, List<? extends NamedPlayer> players) {
        int maxPlayerIndex = -1;
        int maxPoints = -1;
        for (int i = 0; i < players.size(); i++) {
            final int currentPlayerIndex = (i + previousFirstPlayer) % players.size();
            final NamedPlayer player = players.get(currentPlayerIndex);
            final Integer pointsFor = stats.getPointsFor(player.getName());
            if (pointsFor > maxPoints) {
                maxPlayerIndex = currentPlayerIndex;
                maxPoints = pointsFor;
            }
        }
        return maxPlayerIndex;
    }


}
