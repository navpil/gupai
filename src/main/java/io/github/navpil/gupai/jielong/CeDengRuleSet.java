package io.github.navpil.gupai.jielong;

import io.github.navpil.gupai.DominoUtil;
import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.util.CircularInteger;
import io.github.navpil.gupai.jielong.player.Player;
import io.github.navpil.gupai.util.Stats;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class CeDengRuleSet implements RuleSet {

    public enum Penalty {
        SIX_HEAD_SEVEN_TAIL(6, 7),
        SEVEN_HEAD_EIGHT_TAIL(7, 8);

        private final int head;
        private final int tail;

        Penalty(int head, int tail) {
            this.head = head;
            this.tail = tail;
        }

        public int getHead() {
            return head;
        }

        public int getTail() {
            return tail;
        }
    }

    private final int redealDoubles;
    private final Penalty penalty;

    public CeDengRuleSet(int redealDoubles, Penalty penalty) {
        this.redealDoubles = redealDoubles;
        this.penalty = penalty;
    }

    @Override
    public boolean continueTheGame(List<? extends Player> players) {
        for (Player player : players) {
            final Collection<Domino> dominos = player.leftOvers();
            final long size = dominos.stream().filter(DominoUtil::isDouble).count();
            if (size >= redealDoubles) {
                return false;
            }
        }
        return true;
    }

    @Override
    public WinningStats resolvePoints(Stats __, List<? extends Player> players, int whoGoesFirst) {
        //Check 8 doubles in a hand special case (only one person can have it)
        final long count = players.stream().mapToLong(p -> p.leftOvers().size()).sum();
        final int[] pipSums = new int[players.size()];
        {
            int counter = 0;
            for (Player player : players) {
                final int sum = player.leftOvers().stream().mapToInt(DominoUtil::getPipPoints).sum();
                pipSums[counter++] = sum;
            }
        }
        if (count == 32) {
            //Game did not start yet, therefore one of the players should have too many doubles
            final int[] doubles = new int[players.size()];
            int counter = 0;
            int extraWinner = -1;
            for (Player player : players) {
                final int doubleCount = (int) player.leftOvers().stream().filter(DominoUtil::isDouble).count();
                if (doubleCount == 8) {
                    extraWinner = counter;
                }
                doubles[counter++] = doubleCount;
            }
            final HashMap<String, Integer> points = new HashMap<>();
            if (extraWinner >= 0) {
                //8 doubles in hand results in an extraordinary victory.
                int totalWin = 0;
                for (int i = 0; i < players.size(); i++) {
                    if (i == extraWinner) {
                        continue;
                    }
                    final Player player = players.get(i);
                    final int value = pipSums[i] * 2;
                    totalWin += value;
                    points.put(player.getName(), -value);
                }
                final String winnerName = players.get(extraWinner).getName();
                points.put(winnerName, totalWin);
                return new WinningStats(points, winnerName);
            } else {
                //Redeal
                for (Player player : players) {
                    points.put(player.getName(), 0);
                }
                return new WinningStats(points, players.get(whoGoesFirst).getName());
            }
        }
        //Usual case - game was played

        //First calculate bonuses - PASS (for having no dominoes), PENALTY (for head-tail), NONE for no bonuses
        final HashMap<String, Bonus> bonuses = new HashMap<>();
        for (int i = 0; i < players.size(); i++) {
            final Player player = players.get(i);
            final int leftoversSize = player.leftOvers().size();
            if (leftoversSize == 0) {
                bonuses.put(player.getName(), Bonus.PASS);
            } else if (i == whoGoesFirst && leftoversSize >= penalty.getHead()) {
                bonuses.put(player.getName(), Bonus.PENALTY);
            } else if (i != whoGoesFirst && leftoversSize >= penalty.getTail()) {
                bonuses.put(player.getName(), Bonus.PENALTY);
            } else {
                bonuses.put(player.getName(), Bonus.NONE);
            }
        }

        //Calculate points, and apply a bonus multiplier
        final HashMap<String, Integer> points = new HashMap<>();
        for (int i = 0; i < players.size(); i++) {
            final Player a = players.get(i);
            final Bonus aBonus = bonuses.get(a.getName());
            for (int j = i + 1; j < players.size(); j++) {
                final Player b = players.get(j);
                final Bonus bBonus = bonuses.get(b.getName());
                int aWins = (pipSums[j] - pipSums[i]) * (aBonus.multiplier(bBonus));

                final Integer aPoints = points.getOrDefault(a.getName(), 0);
                final Integer bPoints = points.getOrDefault(b.getName(), 0);

                points.put(a.getName(), aPoints + aWins);
                points.put(b.getName(), bPoints - aWins);
            }
        }

        //Find who is an actual winner
        final CircularInteger circularInteger = new CircularInteger(players.size(), whoGoesFirst);
        int minPoints = pipSums[circularInteger.current()];
        String winnersName = players.get(circularInteger.current()).getName();
        circularInteger.next();
        for (int i = 1; i < players.size(); i++) {
            final int currentPoints = pipSums[circularInteger.current()];
            if (currentPoints < minPoints) {
                minPoints = currentPoints;
                winnersName = players.get(circularInteger.current()).getName();
            }
            circularInteger.next();
        }

        return new WinningStats(points, winnersName);
    }

    enum Bonus {

        PASS, PENALTY, NONE;

        public int multiplier(Bonus bBonus) {
            if (this == bBonus) {
                //Pass-Pass, Penalty-Penalty, None-None result in no multiplier
                return 1;
            } if (this == NONE || bBonus == NONE) {
                //Pass-None or Penalty-None both result in x2, cannot be None-None as it is covered in previous clause
                return 2;
            } else {
                //Pass-Penalty is x2
                return 4;
            }
        }
    }
}
