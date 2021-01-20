package io.github.navpil.gupai.tricks.tianjiu;

import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.util.CircularInteger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MovingMouseSimulation {

    public static void main(String[] args) {
        final List<Player> players = List.of(new RandomComputerPlayer("Cat"), new RandomComputerPlayer("mouse"));
        final List<Domino> deck = ChineseDominoSet.create();
        Collections.shuffle(deck);

        final Table table = new Table(RuleSet.classicTianJiu());
        for (Player player : players) {
            player.showTable(table);
        }

        final ArrayList<List<Domino>> playerDominoes = new ArrayList<>();
        playerDominoes.add(new LinkedList<>());
        playerDominoes.add(new LinkedList<>());

        for (int i = 0; i < players.size(); i++) {
            playerDominoes.get(i).addAll(deck.subList(i * 16, (i + 1) * 16));
        }

        int minimalDominoSize = 8;
        int nextWhoGoesFirst = 0;

        while (minimalDominoSize > 0) {
            final int actualDealSize = Math.min(minimalDominoSize, 8);
            for (int i = 0; i < players.size(); i++) {
                final List<Domino> deal = playerDominoes.get(i).subList(0, actualDealSize);
                players.get(i).deal(deal);
                System.out.println("Player " + players.get(i).getName() + " dominoes: " + playerDominoes.get(i) + ", dealt: " + deal);
                deal.clear();
            }

            nextWhoGoesFirst = runSim(players, table, nextWhoGoesFirst, actualDealSize);

            List<Trick> tricks = table.extractTricks();
            tricks.forEach(trick -> playerDominoes.get(trick.getTrickWinner()).addAll(trick.getAllDominos()));

            minimalDominoSize = Math.min(playerDominoes.get(0).size(), playerDominoes.get(1).size());
        }

        if (playerDominoes.get(0).isEmpty()) {
            System.out.println(players.get(1).getName() + " has won");
        } else {
            System.out.println(players.get(0).getName() + " has won");
        }
    }

    private static int runSim(List<Player> players, Table table, int whoGoesFirst, int actualDealSize) {
        final RuleSet ruleSet = table.getRuleSet();
        int currentPlayer = whoGoesFirst;
        int totalPlayedCards = 0;
        int counter = 1;
        while (totalPlayedCards < actualDealSize) {
            //Round
            System.out.println("---- trick " + (counter++) + " ----");
            final CircularInteger trickPlayer = new CircularInteger(players.size(), currentPlayer);
            final Player trickLeader = players.get(trickPlayer.current());

            final Collection<Domino> lead = trickLeader.lead();
            System.out.println(trickLeader.getName() + " lead with " + lead);
            totalPlayedCards += lead.size();

            Trick trick = new Trick(trickPlayer.current(), lead, ruleSet);
            for (int i = 0; i < players.size() - 1; i++) {
                final Player trickBeater = players.get(trickPlayer.next());
                boolean mustDiscard = ruleSet.isEarlyDeath() && totalPlayedCards == 8 && lead.size() == 1 && table.getTrickCount(trickPlayer.current()) == 0;
                final Collection<Domino> beat = mustDiscard ? Collections.emptySet() : trickBeater.beat(trick.highest());
                if (beat.isEmpty()) {
                    final Collection<Domino> discard = trickBeater.discard(trick.highest());
                    System.out.println(trickBeater.getName() + " discarded " + (ruleSet.isDiscardedCardExposed() ? discard : (discard.size() > 1 ? "tiles" : "a tile")) + (mustDiscard ? " (early death)" : ""));
                    trick.discard(trickPlayer.current(), discard);
                } else {
                    System.out.println(trickBeater.getName() + " beat with " + beat);
                    trick.beat(trickPlayer.current(), beat);
                }
            }
            table.add(trick);
            currentPlayer = trick.getTrickWinner();
        }
        return currentPlayer;
    }

}
