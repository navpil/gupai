package io.github.navpil.gupai.shiwuhu.tianjiu;

import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.fishing.CircularInteger;
import io.github.navpil.gupai.fishing.tsungshap.RunManySimulations;
import io.github.navpil.gupai.jielong.Stats;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TianJiu {

    public static void main(String[] args) {
        final List<Domino> deck = ChineseDominoSet.create();
        final ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            players.add(new RandomComputerPlayer("Comp-" + i));
        }

        final TianJiu tianJiu = new TianJiu();
        new RunManySimulations().runManySimulations(deck, players, RuleSet.classicTianJiu(), 1, tianJiu::runSimulation);
    }

    private int previousBanker = -1;
    private int bankerInARow = 0;

    private Stats runSimulation(List<Domino> deck, List<Player> players, RuleSet ruleSet, int whoGoesFirst) {
        final int tilesPerPlayer = deck.size() / players.size();
        final Table table = new Table(ruleSet);
        for (int i = 0; i < players.size(); i++) {
            final Player player = players.get(i);
            final List<Domino> deal = deck.subList(i * tilesPerPlayer, (i + 1) * tilesPerPlayer);
            player.deal(deal);
            System.out.println("Player " + player.getName() + " got " + deal);
            player.showTable(table);
        }

        int currentPlayer = whoGoesFirst;
        int totalPlayedCards = 0;
        int counter = 1;
        while (totalPlayedCards < tilesPerPlayer) {
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
                    System.out.println(trickBeater.getName() + " discarded " + discard + (mustDiscard ? " (early death)" : ""));
                    trick.discard(trickPlayer.current(), discard);
                } else {
                    System.out.println(trickBeater.getName() + " beat with " + beat);
                    trick.beat(trickPlayer.current(), beat);
                }
            }
            table.add(trick);
            currentPlayer = trick.getTrickWinner();
        }
        final Stats stats = new Stats();
        stats.put(players.get(currentPlayer).getName(), 1);
        return stats;
    }


}
