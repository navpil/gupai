package io.github.navpil.gupai.shiwuhu.tianjiu;

import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.fishing.CircularInteger;
import io.github.navpil.gupai.fishing.tsungshap.RunManySimulations;
import io.github.navpil.gupai.jielong.Stats;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TianJiu {

    public static void main(String[] args) {
        final List<Domino> deck = ChineseDominoSet.create();

        List<Player> players = List.of(
                new RandomComputerPlayer("Jack"),
                new RealPlayer("Panas"),
                new RandomComputerPlayer("Jim"),
                new RandomComputerPlayer("John")
        );

        final TianJiu tianJiu = new TianJiu();
        if (false) {
            //To play KuPai - technically the TianJiu variation, description taken from Macao book, tiles are always exposed:
            new RunManySimulations().runManySimulations(deck, players, RuleSet.kuPai(), 10, tianJiu::runSimulation, RunManySimulations.PointsCalculationType.KEEP_AS_IS);
            //To play with 3 players:
            new RunManySimulations().runManySimulations(
                    //May choose any other small deck
                    smallTianJiuDeck(TianJiuSmallDeck.WEN_NINES),
                    players.subList(0, 3),
                    //allow 123 combination
                    new RuleSet(RuleSet.TrickType.TIAN_JIU, true, true, false, false),
                    10, tianJiu::runSimulation, RunManySimulations.PointsCalculationType.KEEP_AS_IS);
            //To play ancient TianJiu - no triples allowed, no early death rule
            new RunManySimulations().runManySimulations(deck, players,
                    new RuleSet(RuleSet.TrickType.TIAN_JIU_NO_MIXING, false, false, false, false),
                    10, tianJiu::runSimulation, RunManySimulations.PointsCalculationType.KEEP_AS_IS);

            //Many other combinations are possible
        }
        new RunManySimulations().runManySimulations(deck, players, RuleSet.classicTianJiu(), 10, tianJiu::runSimulation, RunManySimulations.PointsCalculationType.KEEP_AS_IS);
    }

    public enum TianJiuSmallDeck {
        WEN_NINES_SUPREME_NO_51,
        WEN_NINES,
        WEN_SUPREME,
        NO_SUPREME
    }

    public static List<Domino> smallTianJiuDeck(TianJiuSmallDeck deckType) {
        final List<Domino> dominos = ChineseDominoSet.create();
        switch (deckType) {
            case WEN_NINES_SUPREME_NO_51:
                //This was played with colourful dominoes
                dominos.removeAll(Domino.ofList(62, 53, 52, 43, 41, 32, /*no [5:1]*/51));
                break;
            case WEN_NINES:
                //This was played with 4 players - maybe most typical variation - nines only
                dominos.removeAll(Domino.ofList(62, 53, 52, 43, 41, 32, /*no supreme*/42, 21));
                break;
            case WEN_SUPREME:
                //Described by MinFanXin
                dominos.removeAll(Domino.ofList(62, 53, 52, 43, 41, 32, /*no nines*/63, 54));
                break;
            case NO_SUPREME:
                //Played strictly with 3 players
                dominos.removeAll(Domino.ofList(42, 21));
                break;
        }
        return dominos;

    }

    private int bankerMultiplier = 2;

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
        final Stats stats = new Stats();
        if (currentPlayer == whoGoesFirst) {
            System.out.println("Banker won");
            final Player bankerWhoWon = players.get(currentPlayer);
            stats.put(bankerWhoWon.getName(), 0);
            for (int i = 0; i < players.size(); i++) {
                if (currentPlayer == i) continue;
                final Player player = players.get(i);
                final int playerTrickCount = table.getTrickCount(i);
                int bankersWin = (playerTrickCount == 0 ? 5 : 4 - playerTrickCount) * bankerMultiplier;
                stats.add(bankerWhoWon.getName(), bankersWin);
                stats.put(player.getName(), -bankersWin);
            }
            if (ruleSet.isBankersMultiplierIncreased()) {
                bankerMultiplier++;
            }
        } else {
            //Banker lost
            final Player playerWhoWon = players.get(currentPlayer);
            for (int i = 0; i < players.size(); i++) {
                if (currentPlayer == i) continue;
                final Player player = players.get(i);
                final int playerTrickCount = table.getTrickCount(i);
                int playersWin;
                if (i == whoGoesFirst && playerTrickCount < 4) {
                    //special banker double calculation, but only if banker has less than 4 tricks, because
                    //if banker has more than 4 tricks, winner will have to pay him multiplied, which may make
                    //him lose money.
                    //The rule that bankers multiplier does not apply when other player won the last trick, but banker
                    //has more than 4 tricks is not often mentioned, but it makes a lot of sense and is described on
                    //the aloneinthefart blog post
                    playersWin = (playerTrickCount == 0 ? 5 : 4 - playerTrickCount) * bankerMultiplier;
                } else {
                    playersWin = (playerTrickCount == 0 ? 5 : 4 - playerTrickCount);
                }
                stats.add(playerWhoWon.getName(), playersWin);
                stats.put(player.getName(), -playersWin);
            }
            bankerMultiplier = 2;
        }
        return stats;
    }


}
