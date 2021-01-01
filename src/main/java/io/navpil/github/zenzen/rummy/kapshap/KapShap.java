package io.navpil.github.zenzen.rummy.kapshap;

import io.navpil.github.zenzen.ChineseDominoSet;
import io.navpil.github.zenzen.dominos.Domino;

import java.util.Collections;
import java.util.List;

public class KapShap {

    public static void main(String[] args) {
        //TODO: dmp 29.12.2020 Run several simulations and calculate the overall winner
        final List<KapShapPlayer> players = List.of(
                new RealKapShapPlayer("Jim"),
//                new KapShapPlayer("Second"),
                new ComputerKapShapPlayer("Third")
        );
        final KapShapRuleset rules = new KapShapRuleset(KapShapRuleset.Offer.LAST, true, 1, 8, false);
        runSimulation(players, 0, rules);
    }

    public static void runSimulation(List<KapShapPlayer> players, int whoGoesFirst, KapShapRuleset rules) {
        final List<Domino> dominos = ChineseDominoSet.create(rules.getSetCount());
        Collections.shuffle(dominos);
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
                    return;
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
                    return;
                }
                final Domino discard = player.getDiscard();
                table.add(discard);
                System.out.println(player.getName() + " discarded " + discard);
            }

            currentPlayer = (currentPlayer + 1) % players.size();

        }

        System.out.println("No one win");
    }
}
