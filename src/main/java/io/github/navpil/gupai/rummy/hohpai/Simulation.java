package io.github.navpil.gupai.rummy.hohpai;

import io.github.navpil.gupai.fishing.CircularInteger;
import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.jielong.Stats;

import java.util.Arrays;
import java.util.List;

public class Simulation {

    private int totalRounds;
    private int deadends;
    private int maxTotalRounds;
    private int [] maxTotalRoundsDistribution;

    private final int cutoff;

    public Simulation(int cutoff) {
        this.cutoff = cutoff;
        maxTotalRoundsDistribution = new int[cutoff];
    }

    public Stats runSimulation(List<Domino> dominos, List<Player> players, RuleSet ruleSet, int whoGoesFirst) {
        if (ruleSet.getGiveDiscardType() == RuleSet.GiveDiscardType.DISCARD_FIRST && players.size() > 5) {
            throw new IllegalStateException("Max players for DISCARD_FIRST type is 5");
        } else if (ruleSet.getGiveDiscardType() == RuleSet.GiveDiscardType.DRAW_FIRST && players.size() > 6) {
            throw new IllegalStateException("Max players for DRAW_FIRST type is 6");
        }

        final CircularInteger currentPlayer = new CircularInteger(players.size(), whoGoesFirst);
        int cardsPerPlayer = ruleSet.getGiveDiscardType() == RuleSet.GiveDiscardType.DISCARD_FIRST ? 6 : 5;

        //If table has an EYE, then table will take care of it itself
        final HoHpaiTable table = new HoHpaiTable(dominos.subList(players.size() * cardsPerPlayer, dominos.size()), ruleSet);

        for (int i = 0; i < players.size(); i++) {
            final Player player = players.get(i);
            final List<Domino> deal = dominos.subList(i * cardsPerPlayer, (i + 1) * cardsPerPlayer);
            System.out.println("Player " + player.getName() + " was dealt " + deal);
            player.deal(deal);
            player.showTable(table);
        }

        final CircularInteger round = new CircularInteger(players.size(), 0);
        int roundNumber = 0;

        while (true) {

            if (round.current() == 0) {
                roundNumber++;
                System.out.println("---- Round " + roundNumber + " ----");
                if (roundNumber >= cutoff) {
                    deadends++;
                    System.out.println(players);
                    return new Stats();
                }
            }
            Player player = players.get(currentPlayer.current());

            if (ruleSet.getGiveDiscardType() == RuleSet.GiveDiscardType.DRAW_FIRST) {
                giveDomino(table, player);
                Stats points = winCheck(table, player);
                if (points != null) {
                    totalRounds += roundNumber;
                    maxTotalRounds = Math.max(maxTotalRounds, roundNumber);
                    maxTotalRoundsDistribution[roundNumber]++;
                    return points;
                }
            } else if (roundNumber == 1) {
                //Special case for DISCARD_FIRST on a first round - check whether a player has a winning hand from start
                Stats points = winCheck(table, player);
                if (points != null) return points;
            }

            final Domino discard = player.discard();
            System.out.println(player.getName() + " discarded " + discard);
            table.discard(discard);

            if (ruleSet.getGiveDiscardType() == RuleSet.GiveDiscardType.DISCARD_FIRST) {
                giveDomino(table, player);
                Stats points = winCheck(table, player);
                if (points != null){
                    totalRounds += roundNumber;
                    maxTotalRounds = Math.max(maxTotalRounds, roundNumber);
                    maxTotalRoundsDistribution[roundNumber]++;
                    return points;
                }
            }

            currentPlayer.next();
            round.next();
        }
    }

    public int getDeadends() {
        return deadends;
    }

    private static void giveDomino(HoHpaiTable table, Player player) {
        final Domino give = table.remove();
        System.out.println(player.getName() + " was given " + give);
        player.give(give);
    }

    private static Stats winCheck(Table table, Player player) {
        if (player.won()) {
            System.out.println(player.getName() + " won with combinations " + player.getWinningHand());
            int points = new HandCalculator(table.getRuleSet()).calculatePoints(player.getWinningHand());
            return statsFor(player.getName(), points);
        }
        return null;
    }

    private static Stats statsFor(String name, int points) {
        final Stats stats = new Stats();
        stats.put(name, points);
        return stats;
    }

    public int getTotalRounds() {
        return totalRounds;
    }

    public String getMaxTotalRounds() {
        return maxTotalRounds + " ( " + Arrays.toString(maxTotalRoundsDistribution) + " )";
    }
}
