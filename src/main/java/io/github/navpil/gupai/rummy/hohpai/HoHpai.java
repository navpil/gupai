package io.github.navpil.gupai.rummy.hohpai;

import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.util.CircularInteger;
import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.util.RunManySimulations;
import io.github.navpil.gupai.util.Stats;

import java.util.Arrays;
import java.util.List;

public class HoHpai {

    /**
     * Total rounds played
     */
    private int totalRounds;
    /**
     * How many games came to a deadend with noone winning
     */
    private int deadends;
    /**
     * Maximum amount of rounds played
     */
    private int maxTotalRounds;
    /**
     * Distribution of rounds played
     */
    private final int [] maxTotalRoundsDistribution;

    /**
     * How many rounds can be played before the game is forcefully stopped (50 is a good default)
     */
    private final int cutoff;

    public static void main(String[] args) {

        //3-4 players according to Culin
        //up to 5 can play with DISCARD_FIRST
        //up to 6 can play with DRAW_FIRST rule
        //2 can also play, but same 24-tiles deck as for 3 players will apply
        final List<Player> players = List.of(
                new CleverPlayer("Comp-1"),
                new CleverPlayer("Comp-2"),
                new CleverPlayer("Comp-3"),
                new CleverPlayer("Comp-5")
//            new RealPlayer("Jim")
        );
        final List<Domino> deck = ChineseDominoSet.create();

        //This is a rule according to Culin. Can be played with a full deck though.
        //One source mentioned 64 tiles, which may mean that the game could be played with 2 sets
        if (players.size() <= 3) {
            deck.removeAll(Domino.ofList(33, 44, 55, 66));
        }

        final HoHpai simulation = new HoHpai(50);
        final int simCount = 10000;
        final Stats stats = new RunManySimulations().runManySimulations(
                deck,
                players,
                RuleSet.optimal(),
                simCount,
                simulation::runSimulation
        );

        System.out.println(stats);
        System.out.println(simulation.getTotalRounds() * 1.0 / (simCount - simulation.getDeadends()));
        System.out.println("Max total rounds: " + simulation.getMaxTotalRounds());
        System.out.println("Deadends frequency: " + simulation.getDeadends() * 1.0 / simCount);
        System.out.println("Deadends: " + simulation.getDeadends());
    }

    public HoHpai(int cutoff) {
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
