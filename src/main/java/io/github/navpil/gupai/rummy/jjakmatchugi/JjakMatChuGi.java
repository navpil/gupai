package io.github.navpil.gupai.rummy.jjakmatchugi;

import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.util.CircularInteger;
import io.github.navpil.gupai.util.RunManySimulations;
import io.github.navpil.gupai.util.Stats;
import io.github.navpil.gupai.util.CombineCollection;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class JjakMatChuGi {

    public static void main(String[] args) {

        boolean humanPlayerIncluded = false;
        final List<Player> players = List.of(
                new CleverPlayer("Comp1"),
                new CleverPlayer("Comp2"),
                new CleverPlayer("Clever-Comp3"),
                humanPlayerIncluded ? new HumanPlayer("Jim") : new CleverPlayer("Clever-Comp4")
        );

        final JjakMatChuGi jjakMatChuGi = new JjakMatChuGi();
        final int simCount = 1000;
        new RunManySimulations().runManySimulations(
                ChineseDominoSet.create(),
                players,
                RuleSet.mahJongLike(),
                simCount,
                jjakMatChuGi::runSimulation,
                RunManySimulations.PointsCalculationType.KEEP_AS_IS
        );

        System.out.println("Out of " + simCount + " " + jjakMatChuGi.wonGames + " were won (" + (100.0 * jjakMatChuGi.wonGames / simCount) + "%)");
    }

    private int wonGames;

    private Stats runSimulation(List<Domino> dominos, List<? extends Player> players, RuleSet ruleSet, int whoGoesFirst) {
        final int cardsPerPlayer = 5;
        final LinkedList<Domino> woodpile = new LinkedList<>(dominos.subList(players.size() * cardsPerPlayer, dominos.size()));
        final Table table = new Table(ruleSet);
        for (int i = 0; i < players.size(); i++) {
            final Player player = players.get(i);
            final List<Domino> deal = dominos.subList(i * cardsPerPlayer, (i + 1) * cardsPerPlayer);
            player.deal(deal);
            System.out.println("Player " + player.getName() + " was dealt " + deal);
            player.showTable(table);
        }
        final CircularInteger playerIndex = new CircularInteger(players.size(), whoGoesFirst);
        boolean wonWithDiscard = false;
        boolean playerWon = false;
        if (ruleSet.isForceShowPairs()) {
            int i = 0;
            for (Player player : players) {
                Collection<Domino> extractedPairs = player.extractPairs();
                if (!extractedPairs.isEmpty()) {
                    System.out.println(player.getName() + " shown pairs: " + extractedPairs);
                    table.addCombination(i, extractedPairs);
                }
                i++;
            }
        }
        int lastDiscarded = 0;
        game_loop:
        while(!woodpile.isEmpty()) {
            final int current = playerIndex.current();
            Player player = players.get(current);
            boolean discardTaken = false;
            if (table.lastDiscard() != null) {
                if (ruleSet.isOfferToAll()) {
                    int playerCounter = 0;
                    while (playerCounter < players.size() && !discardTaken) {
                        Collection<Domino> combination = player.offer(table.lastDiscard());
                        if (!combination.isEmpty()) {
                            System.out.println(player.getName() + " took the " + table.lastDiscard() + " and created a combination " + combination);
                            discardTaken = true;
                            if (!isValid(combination, ruleSet)) {
                                throw new IllegalStateException("Combination " + combination + " is invalid");
                            }
                            table.addCombination(playerIndex.current(), combination);
                        }
                        if (!discardTaken) {
                            playerCounter++;
                            player = players.get(playerIndex.next());
                        }
                    }
                } else {
                    Collection<Domino> combination = player.offer(table.lastDiscard());
                    if (!combination.isEmpty()) {
                        System.out.println(player.getName() + " took the " + table.lastDiscard() + " and created a combination " + combination);
                        discardTaken = true;
                        if (!isValid(combination, ruleSet)) {
                            throw new IllegalStateException("Combination " + combination + " is invalid");
                        }
                        table.addCombination(playerIndex.current(), combination);
                    }
                }
            }
            if (!discardTaken) {
                player.give(woodpile.removeFirst());
            } else {
                table.resetLastDiscard();
            }
            if (player.won()) {
                playerWon = true;
                final CombineCollection<Domino> winningHand = new CombineCollection<>(List.of(table.getCombinations(playerIndex.current()), player.getWinningHand()));
                if (!isValid(winningHand, ruleSet)) {
                    throw new IllegalStateException("Combination " + winningHand + " is invalid");
                } else {
                    if (discardTaken) {
                        wonWithDiscard = true;
                    }
                    System.out.println("Player " + player.getName() + " won with " + winningHand + (wonWithDiscard ? " from a discard ":""));
                    wonGames++;
                    break game_loop;
                }
            }
            if (ruleSet.isForceShowPairs()) {
                Collection<Domino> extractedPairs = player.extractPairs();
                if (!extractedPairs.isEmpty()) {
                    System.out.println(player.getName() + " shown pairs: " + extractedPairs);
                    table.addCombination(playerIndex.current(), extractedPairs);
                }
            }
            final Domino discard = player.discard();
            System.out.println(player.getName() + " discarded " + discard);
            table.addDiscard(discard);
            lastDiscarded = playerIndex.current();
            playerIndex.next();
        }
        final Stats stats = new Stats();
        if (playerWon) {
            final int stakesWon = players.size() - 1;
            stats.put(players.get(playerIndex.current()).getName(), stakesWon);
            if (wonWithDiscard) {
                stats.put(players.get(lastDiscarded).getName(), -stakesWon);
            } else {
                for (int i = 0; i < players.size() - 1; i++) {
                    stats.put(players.get(playerIndex.next()).getName(), -1);
                }
            }
        } else {
            System.out.println("No one won");
            for (int i = 0; i < players.size(); i++) {
                System.out.println("Combos: " + table.getCombinations(i) + ", " + players.get(i).getWinningHand());
            }
        }
        return stats;
    }

    private static boolean isValid(Collection<Domino> combination, RuleSet ruleSet) {
        return ruleSet.getPairsType().validAllPairsCombination(combination);
    }
}
