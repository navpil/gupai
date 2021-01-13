package io.github.navpil.gupai.fishing.tsungshap;

import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.fishing.CircularInteger;
import io.github.navpil.gupai.fishing.tsungshap.fourtiles.Player;
import io.github.navpil.gupai.jielong.Stats;
import io.github.navpil.gupai.util.HashBag;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class TsungShap {

    public static void main(String[] args) {
        final List<Domino> deck = ChineseDominoSet.create();
        final List<TsungShapPlayer> players = List.of(
                new TsungShapComputerPlayer("Comp-1"),
                new TsungShapRealPlayer("Mark")
        );

        final TsungShapRuleSet ruleSet = new TsungShapRuleSet(false, -1, false);
        Stats stats = new RunManySimulations().runManySimulations(deck, players, ruleSet, 100, TsungShap::runSimulation);
        for (NamedPlayer player : players) {
            System.out.println(player.getName() + " got points: " + stats.getPointsFor(player.getName()));
        }
    }

    private static Stats runSimulation(List<Domino> deck, List<TsungShapPlayer> players, TsungShapRuleSet ruleSet, int whoGoesFirst) {
        final List<LinkedList<Domino>> personalDecks = List.of(
                new LinkedList<>(deck.subList(0, deck.size() / 2)),
                new LinkedList<>(deck.subList(deck.size() / 2, deck.size()))
        );

        final TsungShapTable table = new TsungShapTable(ruleSet);
        final LinkedList<Domino> row = table.getRow();

        for (TsungShapPlayer player : players) {
            player.showTable(table);
        }

        final CircularInteger playerIndex = new CircularInteger(2, whoGoesFirst);
        final Domino rowStarted = personalDecks.get(playerIndex.current()).removeFirst();
        System.out.println("Player " + players.get(playerIndex.current()).getName() + " put " + rowStarted + " to start a row");
        row.addFirst(rowStarted);
        playerIndex.next();

        if (ruleSet.isSecondPutObligatory()) {
            //It's not clear from the rules description whether second player has to put a domino, or he can use it
            //for fishing, therefore it's configurable
            final Domino secondDomino = personalDecks.get(playerIndex.current()).removeFirst();
            System.out.println("Player " + players.get(playerIndex.current()).getName() + " put " + secondDomino + " to start a row (2)");
            row.addFirst(secondDomino);
            playerIndex.next();
        }

        while (personalDecks.stream().filter(Collection::isEmpty).findAny().isEmpty()) {
            System.out.println("Row is " + row);

            final TsungShapPlayer player = players.get(playerIndex.current());
            final LinkedList<Domino> personalDeck = personalDecks.get(playerIndex.current());
            final Domino domino = personalDeck.removeFirst();

            TsungShapMove move = player.chooseMove(domino, row);
            TsungShapCatch c = executeMove(ruleSet, row, move);
            if (row.isEmpty()) {
                c = c.asSweep();
                if (!personalDeck.isEmpty()) {
                    row.addFirst(personalDeck.removeFirst());
                }
            }
            System.out.println("Player " + player.getName() + " chose to play " + move + " which resulted in " + c);

            if (c != null) {
                table.addCatch(player.getName(), c);
            }
            playerIndex.next();
        }

        final Stats stats = new Stats();
        for (TsungShapPlayer player : players) {
            final Collection<TsungShapCatch> catches = table.getCatch(player.getName());
            final int points = ruleSet.calculatePoints(catches);
            System.out.println("Player " + player.getName() + " got " + points + " points from the catch: " + catches);
            stats.put(player.getName(), points);
        }
        return stats;

    }

    public static Stats runFourTilesSimulation(List<Domino> deck, List<Player> players, TsungShapRuleSet ruleSet, int whoGoesFirst) {
        final List<LinkedList<Domino>> personalDecks = List.of(
                new LinkedList<>(deck.subList(0, deck.size() / 2)),
                new LinkedList<>(deck.subList(deck.size() / 2, deck.size()))
        );

        final TsungShapTable table = new TsungShapTable(ruleSet);
        final LinkedList<Domino> row = table.getRow();

        for (Player player : players) {
            player.showTable(table);
        }

        final CircularInteger playerIndex = new CircularInteger(2, whoGoesFirst);

        for (int i = 0; i < players.size(); i++) {
            final List<Domino> initialDeal = personalDecks.get(i).subList(0, 4);
            players.get(i).deal(initialDeal);
            System.out.println(players.get(i).getName() + " was dealt " + initialDeal);
            initialDeal.clear();
        }

        final Domino rowStarted = players.get(playerIndex.current()).chooseSingleDiscard();
        System.out.println("Player " + players.get(playerIndex.current()).getName() + " put " + rowStarted + " to start a row");
        row.addFirst(rowStarted);
        players.get(playerIndex.current()).replenish(personalDecks.get(playerIndex.current()).removeFirst());
        playerIndex.next();

        if (ruleSet.isSecondPutObligatory()) {
            //It's not clear from the rules description whether second player has to put a domino, or he can use it
            //for fishing, therefore it's configurable
            final Domino secondDomino = players.get(playerIndex.current()).chooseSingleDiscard();
            System.out.println("Player " + players.get(playerIndex.current()).getName() + " put " + secondDomino + " to start a row (2)");
            row.addFirst(secondDomino);
            players.get(playerIndex.current()).replenish(personalDecks.get(playerIndex.current()).removeFirst());
            playerIndex.next();
        }

        //Ending condition is not clearly defined
        while (players.stream().filter((p) -> !p.hasTiles()).findAny().isEmpty()) {
            System.out.println("Row is " + row);

            final Player player = players.get(playerIndex.current());
            final LinkedList<Domino> personalDeck = personalDecks.get(playerIndex.current());
            TsungShapMove move = player.chooseMove(row);

            TsungShapCatch c = executeMove(ruleSet, row, move);
            if (row.isEmpty()) {
                c = c.asSweep();
            }
            System.out.println("Player " + player.getName() + " chose to play " + move + " which resulted in " + c);
            if (row.isEmpty() && player.hasTiles()) {
                System.out.println("Force discard");
                row.addFirst(player.chooseSingleDiscard());
                if (!personalDeck.isEmpty()) {
                    System.out.println(player.getName() + " replenished with " + personalDeck.getFirst());
                    player.replenish(personalDeck.removeFirst());
                }
            }
            if (!personalDeck.isEmpty()) {
                System.out.println(player.getName() + " replenished with " + personalDeck.getFirst());
                player.replenish(personalDeck.removeFirst());
            }

            if (c != null) {
                table.addCatch(player.getName(), c);
            }
            playerIndex.next();
        }

        final Stats stats = new Stats();
        for (Player player : players) {
            final Collection<TsungShapCatch> catches = table.getCatch(player.getName());
            final int points = ruleSet.calculatePoints(catches);
            System.out.println("Player " + player.getName() + " got " + points + " points from the catch: " + catches);
            stats.put(player.getName(), points);
        }
        return stats;

    }

    private static TsungShapCatch executeMove(TsungShapRuleSet ruleSet, LinkedList<Domino> row, TsungShapMove move) {
        TsungShapCatch c = null;
        switch (move.getType()) {
            case DISCARD:
                discard(row, move.getDomino(), move.getSide());
                break;
            case PAIR:
                final HashBag<Domino> pair = HashBag.of(
                        move.getSide() == TsungShapMove.Side.LEFT ? row.getFirst() : row.getLast(),
                        move.getDomino()
                );
                if (ruleSet.validPair(pair)) {
                    c = new TsungShapCatch(pair);
                    diminishRow(row, move);
                } else {
                    discard(row, move.getDomino(), move.getSide());
                }
                break;
            case TRIPLET:
                if (row.size() == 1) {
                    discard(row, move.getDomino(), move.getSide());
                }
                int i1 = -1;
                int i2 = -1;
                switch (move.getSide()) {
                    case LEFT:
                        i1 = 0;
                        i2 = 1;
                        break;
                    case RIGHT:
                        i1 = row.size() - 1;
                        i2 = row.size() - 2;
                        break;
                    case BOTH:
                        i1 = 0;
                        i2 = row.size() - 1;
                        break;
                }
                final HashBag<Domino> triplet = HashBag.of(row.get(i1), row.get(i2), move.getDomino());
                if (ruleSet.validTriplet(triplet)) {
                    c = new TsungShapCatch(triplet);
                    diminishRow(row, move);
                } else {
                    discard(row, move.getDomino(), move.getSide());
                }
        }
        return c;
    }

    private static void diminishRow(LinkedList<Domino> row, TsungShapMove move) {
        final TsungShapMove.Type t = move.getType();
        final TsungShapMove.Side s = move.getSide();
        if (t == TsungShapMove.Type.PAIR) {
            if (s == TsungShapMove.Side.LEFT) {
                row.removeFirst();
            } else {
                row.removeLast();
            }
        } else if (t == TsungShapMove.Type.TRIPLET) {
            switch (s) {
                case LEFT:
                    row.removeFirst();
                    row.removeFirst();
                    break;
                case RIGHT:
                    row.removeLast();
                    row.removeLast();
                    break;
                case BOTH:
                    row.removeLast();
                    row.removeFirst();
                    break;
            }
        }
    }

    private static void discard(LinkedList<Domino> row, Domino domino, TsungShapMove.Side side) {
        if (side == TsungShapMove.Side.LEFT) {
            row.addFirst(domino);
        } else {
            row.addLast(domino);
        }
    }

}
