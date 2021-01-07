package io.github.navpil.gupai.jielong;

import io.github.navpil.gupai.jielong.player.MutableInteger;
import io.github.navpil.gupai.jielong.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KkoriputIgiRuleSet implements RuleSet {

    private final CalculationType calculationType;

    public enum CalculationType {
        POINTS, TOKENS
    }

    public KkoriputIgiRuleSet(CalculationType type) {
        calculationType = type;
    }

    @Override
    public WinningStats resolvePoints(Stats stats, List<? extends Player> players, int whoGoesFirst) {
        final List<String> originalNameList = players.stream().map(Player::getName).collect(Collectors.toList());

        Map<String, Integer> order = new HashMap<>();
        List<String> names = new ArrayList<>(originalNameList);
        for (int i = 0, size = names.size(); i < size; i++) {
            int playerIndex = (whoGoesFirst + i) % size;
            order.put(names.get(playerIndex), i);
        }

        names.sort((n1, n2) -> {
            final Integer p1 = stats.points.get(n1);
            final Integer p2 = stats.points.get(n2);
            if (!p1.equals(p2)) {
                return p1.compareTo(p2);
            }
            return order.get(n1).compareTo(order.get(n2));
        });
        final String winner = names.get(0);

        final Integer minPoints = stats.points.values().stream().min(Integer::compareTo).get();
        final Integer maxPoints = stats.points.values().stream().max(Integer::compareTo).get();
        final HashMap<String, MutableInteger> points = new HashMap<>();
        for (String s : originalNameList) {
            points.put(s, new MutableInteger());
        }
        if (!minPoints.equals(maxPoints)) {
            List<String> winners = new ArrayList<>();
            List<String> losers = new ArrayList<>();
            boolean thirtyPointsAreLosers = minPoints < 30;

            for (Map.Entry<String, Integer> e : stats.points.entrySet()) {
                if (e.getValue().equals(minPoints)) {
                    winners.add(e.getKey());
                } else if (e.getValue().equals(maxPoints)) {
                    losers.add(e.getKey());
                } else if (thirtyPointsAreLosers && e.getValue() >= 30) {
                    losers.add(e.getKey());
                }
            }

            for (String win : winners) {
                for (String loser : losers) {
                    if (calculationType == CalculationType.TOKENS) {
                        points.get(win).add(1);
                        points.get(loser).add(-1);
                    } else {
                        final Integer winnerPoints = stats.points.get(win);
                        final Integer loserPoints = stats.points.get(loser);

                        final int wonPoints = loserPoints - winnerPoints;
                        points.get(win).add(wonPoints);
                        points.get(loser).add(-wonPoints);
                    }
                }
            }
        }

        final HashMap<String, Integer> result = new HashMap<>();
        for (Map.Entry<String, MutableInteger> stringCounterEntry : points.entrySet()) {
            result.put(stringCounterEntry.getKey(), stringCounterEntry.getValue().getCount());
        }

        return new WinningStats(result, winner);
    }
}
