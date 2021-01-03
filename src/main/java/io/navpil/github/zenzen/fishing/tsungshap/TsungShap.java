package io.navpil.github.zenzen.fishing.tsungshap;

import io.navpil.github.zenzen.ChineseDominoSet;
import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.fishing.CircularInteger;
import io.navpil.github.zenzen.jielong.Stats;
import io.navpil.github.zenzen.util.HashBag;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TsungShap {

    public static void main(String[] args) {

        final List<Domino> deck = ChineseDominoSet.create();
        final List<TsungShapPlayer> players = List.of(
                new TsungShapPlayer("Comp-1"),
                new TsungShapPlayer("Comp-2")
        );

        Collections.shuffle(deck);
        final TsungShapRuleSet ruleSet = new TsungShapRuleSet(false, -1);
        final Stats stats = runSimulation(deck, players, 0, ruleSet);
        for (TsungShapPlayer player : players) {
            System.out.println(stats.getPointsFor(player.getName()));
        }

    }

    private static Stats runSimulation(List<Domino> deck, List<TsungShapPlayer> players, int whoGoesFirst, TsungShapRuleSet ruleSet) {
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
            TsungShapCatch c = null;
            switch (move.getType()) {
                case DISCARD:
                    discard(row, domino, move.getSide());
                    break;
                case PAIR:
                    final HashBag<Domino> pair = HashBag.of(
                            move.getSide() == TsungShapMove.Side.LEFT ? row.getFirst() : row.getLast(),
                            domino
                    );
                    if (ruleSet.validPair(pair)) {
                        c = new TsungShapCatch(pair);
                        diminishRow(row, move);
                    } else {
                        discard(row, domino, move.getSide());
                    }
                    break;
                case TRIPLET:
                    if (row.size() == 1) {
                        discard(row, domino, move.getSide());
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
                    final HashBag<Domino> triplet = HashBag.of(row.get(i1), row.get(i2), domino);
                    if (ruleSet.validTriplet(triplet)) {
                        c = new TsungShapCatch(triplet);
                        diminishRow(row, move);
                    } else {
                        discard(row, domino, move.getSide());
                    }
            }
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
            stats.put(player.getName(), ruleSet.calculatePoints(table.getCatch(player.getName())));
        }
        return stats;

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
