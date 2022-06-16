package io.github.navpil.gupai.rummy.hohpai;

import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.CombinationType;
import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.XuanHePaiPu;
import io.github.navpil.gupai.util.CircularInteger;
import io.github.navpil.gupai.util.CombineCollection;
import io.github.navpil.gupai.util.RunManySimulations;
import io.github.navpil.gupai.util.Stats;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ThomasTok {

    public static void main(String[] args) {

        boolean humanPlayerIncluded = false;

        final List<Player> players = List.of(
                new CleverPlayer("Comp1"),
                new CleverPlayer("Comp2"),
                new CleverPlayer("Clever-Comp3"),
                humanPlayerIncluded ? new RealPlayer("Jim") : new CleverPlayer("Clever-Comp4")
        );

        final ThomasTok tok = new ThomasTok();
        final int simCount = 1000;
        new RunManySimulations().runManySimulations(
                ChineseDominoSet.create(),
                players,
                RuleSet.tok(),
                simCount,
                tok::runSimulation,
                RunManySimulations.PointsCalculationType.KEEP_AS_IS
        );

        System.out.println("Out of " + simCount + " " + tok.wonGames + " were won (" + (100.0 * tok.wonGames / simCount) + "%)");
    }

    private int wonGames;

    private Stats runSimulation(List<Domino> dominos, List<? extends Player> players, RuleSet ruleSet, int whoGoesFirst) {
        final XuanHePaiPu xuanHePaiPu = ruleSet.getXuanHePaiPu();
        final int cardsPerPlayer = 5;
        final LinkedList<Domino> woodpile = new LinkedList<>(dominos.subList(players.size() * cardsPerPlayer, dominos.size()));
        final TokTable table = new TokTable(woodpile, ruleSet);

        for (int i = 0; i < players.size(); i++) {
            final Player player = players.get(i);
            final List<Domino> deal = dominos.subList(i * cardsPerPlayer, (i + 1) * cardsPerPlayer);
            player.deal(deal);
            System.out.println("Player " + player.getName() + " was dealt " + deal);
            player.showTable(table);
        }

        final CircularInteger playerIndex = new CircularInteger(players.size(), whoGoesFirst);
        boolean playerWon = false;
        game_loop:

        while(!woodpile.isEmpty()) {
            final int current = playerIndex.current();
            final Player player = players.get(current);
            boolean discardTaken = false;
            if (table.lastDiscard() != null) {
                Collection<Domino> combination = player.offer(table.lastDiscard());
                if (!combination.isEmpty()) {
                    System.out.println(player.getName() + " took the " + table.lastDiscard() + " and created a combination " + combination);
                    discardTaken = true;
                    if (!validCombination(combination, xuanHePaiPu)) {
                        throw new IllegalStateException("Combination " + combination + " is invalid");
                    }
                    table.addCombination(playerIndex.current(), combination);
                }
            }
            if (!discardTaken) {
                player.give(woodpile.removeFirst());
            } else {
                table.resetLastDiscard();
            }
            if (player.won()) {
                playerWon = true;
                final Hand winningHand1 = player.getWinningHand();
                if (!winningHand1.isWinningHand()) {
                    throw new IllegalStateException("Player has not won with " + winningHand1);
                }
                final CombineCollection<? extends Collection<Domino>> winningHand = new CombineCollection<>(List.of(table.getCombinations(playerIndex.current()), new ArrayList<>(winningHand1.getCombinations())));
                if (!validCombinations(winningHand, xuanHePaiPu)) {
                    throw new IllegalStateException("Combination " + winningHand + " is invalid");
                } else {
                    System.out.println("Player " + player.getName() + " won with " + winningHand);
                    wonGames++;
                    break game_loop;
                }
            }
            final Domino discard = player.discard();
            System.out.println(player.getName() + " discarded " + discard);
            table.discard(discard);
            playerIndex.next();
        }
        final Stats stats = new Stats();
        if (playerWon) {
            final int stakesWon = players.size() - 1;
            stats.put(players.get(playerIndex.current()).getName(), stakesWon);
            for (int i = 0; i < players.size() - 1; i++) {
                stats.put(players.get(playerIndex.next()).getName(), -1);
            }
        } else {
            System.out.println("No one won");
            for (int i = 0; i < players.size(); i++) {
                System.out.println("Combos: " + table.getCombinations(i) + ", " + players.get(i).getWinningHand());
            }
        }
        return stats;
    }

    private static boolean validCombinations(Collection<? extends Collection<Domino>> combination, XuanHePaiPu ruleSet) {
        for (Collection<Domino> c : combination) {
            if (!validCombination(c, ruleSet)) {
                return false;
            }
        }
        return true;
    }
    private static boolean validCombination(Collection<Domino> combination, XuanHePaiPu ruleSet) {
        return ruleSet.evaluate(combination) != CombinationType.none;
    }
}
