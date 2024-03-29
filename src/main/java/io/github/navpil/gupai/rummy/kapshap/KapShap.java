package io.github.navpil.gupai.rummy.kapshap;

import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.util.RunManySimulations;
import io.github.navpil.gupai.util.Stats;

import java.util.List;

public class KapShap {

    public static void main(String[] args) {
        final List<KapShapPlayer> players = List.of(
//                new RealKapShapPlayer("Jim"),
                new ComputerKapShapPlayer("Third"),
                new ComputerKapShapPlayer("Second")
        );
        //gin ~8.5
        //classic ~6.47
        //culin ~6.0
        final KapShapRuleset rules = KapShapRuleset.ginKapShap();
//        new KapShapRuleset(
//                KapShapRuleset.Offer.LAST,
//                false,//true,
//                1,
//                8,
//                false);
        final Stats stats = new RunManySimulations().runManySimulations(ChineseDominoSet.create(), players, rules, 100, KapShap::runSimulation);
        System.out.println(stats);
    }

    public static Stats runSimulation(List<Domino> dominos, List<KapShapPlayer> players, KapShapRuleset rules, int whoGoesFirst) {
        final KapShapTableVisibleInformation table = new KapShapTableVisibleInformation(dominos, rules);

        final int cardsPerPlayer = rules.getWinningHandSize() - 1;
        for (int i = 0; i < players.size(); i++) {
            final List<Domino> deal = dominos.subList(i * cardsPerPlayer, (i + 1) * cardsPerPlayer);
            players.get(i).deal(deal);
            System.out.println("Player " + players.get(i).getName() + " was dealt " + deal);
            players.get(i).showTable(table);
        }

        int currentPlayer = whoGoesFirst;

        final int firstI = players.size() * cardsPerPlayer;
        int dominoIndex = firstI;

        int rounds = 0;
        while (dominoIndex < dominos.size() || rules.isContinueAfterDominoesEnd()) {
            System.out.println("\t\t--- " + dominoIndex + "(" + (dominoIndex - firstI  + 1) + ") ---");
            final KapShapPlayer player = players.get(currentPlayer);

            final Domino wished;
            if (rules.getOfferType() == KapShapRuleset.Offer.ALL) {
                System.out.println("Open dominoes are: " + table.getOpenDominoes());
                wished = player.offer(table.getOpenDominoes());
            } else if (rules.getOfferType() == KapShapRuleset.Offer.LAST && table.getLastDiscarded() != null) {
                System.out.println("Last discarded domino is: " + table.getLastDiscarded());
                wished = player.offer(List.of(table.getLastDiscarded()));
            } else {
                wished = null;
            }
            if (wished != null) {
                System.out.println(player.getName() + " chose to take " + wished);
                table.remove(wished);
                player.give(wished);

                if (player.hasWon()) {
                    System.out.println(player.getName() + " won with winning hand of " + player.getWinningHand());
                    return statsFor(player.getName(), player.getWinningHand()).withRounds(rounds);
                }
                final Domino discard = player.getDiscard();
                table.add(discard);
                System.out.println(player.getName() + " discarded " + discard);
            }

            //In KapShap player has to take the closed domino after taking the open one, but this should be configurable
            if ((rules.isTakeAfterOffer() || wished == null) && dominoIndex < dominos.size()) {
                final Domino give = dominos.get(dominoIndex);
                dominoIndex++;
                System.out.println(player.getName() + " was given " + give);
                player.give(give);

                if (player.hasWon()) {
                    System.out.println(player.getName() + " won with winning hand of " + player.getWinningHand());
                    return statsFor(player.getName(), player.getWinningHand()).withRounds(rounds);

                }
                final Domino discard = player.getDiscard();
                table.add(discard);
                System.out.println(player.getName() + " discarded " + discard);
            }

            currentPlayer = (currentPlayer + 1) % players.size();
            rounds++;
        }

        System.out.println("No one win");
        return new Stats().deuceAdded();
    }

    private static Stats statsFor(String name, KapShapHand winningHand) {
        final Stats stats = new Stats();
        //Actual winning hand is irrelevant, the winner simply collects the pot
        stats.put(name, 1);
        return stats;
    }
}
