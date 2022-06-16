package io.github.navpil.gupai.rummy.smallmahjong;

import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.jielong.player.MutableInteger;
import io.github.navpil.gupai.util.Bag;
import io.github.navpil.gupai.util.HashBag;
import io.github.navpil.gupai.Domino;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ComputerPlayer implements Player {

    private final String name;
    private Bag<Domino> dominos;
    private Hand winningHand;
    private Domino discard;
    private Table table;

    public ComputerPlayer(String name) {
        this.name = name;
    }

    @Override
    public void deal(List<Domino> deal) {
        dominos = new HashBag<>(deal);
    }

    @Override
    public void showTable(Table table) {
        this.table = table;
    }

    @Override
    public Triplet offer(Domino lastDiscard, TripletType type) {
        discard = null;
        final ArrayList<Domino> dominos = new ArrayList<>(this.dominos);
        dominos.add(lastDiscard);
        final List<Hand> hands = HandCalculator.calculateHands(dominos);
        final Hand winningHand = hands.stream().filter(Hand::isWinning).findAny().orElse(null);
        if (winningHand != null) {
            final Triplet triplet = winningHand.triplets.stream().filter(t -> t.asBag().contains(lastDiscard)).findAny().get();
            winningHand.triplets.remove(triplet);
            this.winningHand = winningHand;
            return triplet;
        } else {
            //For now only winning hands are used by the computer player
            final Hand hand = hands.stream().filter(h -> h.triplets.stream().anyMatch(t -> t.asBag().contains(lastDiscard) && validType(t, type))).findAny().orElse(null);
            if (hand != null) {
                Triplet triplet = hand.triplets.stream().findAny().get();
                dominos.removeAll(triplet.asBag());
                this.dominos = new HashBag<>(dominos);

                discard = findDiscardOfThree(dominos);
                return triplet;
            }
        }
        return null;
    }

    private Domino findDiscardOfThree(ArrayList<Domino> dominos) {
        Collection<Domino> allVisibleDominoes = table.getAllVisibleDominoes();
        List<Domino> possbilyHiddenDominoes = ChineseDominoSet.create();
        possbilyHiddenDominoes.removeAll(allVisibleDominoes);
        int worseDomino = -1;
        Domino toDiscard = null;
        ArrayList<List<TripletType>> lists = new ArrayList<>();
        for (Domino domino : dominos) {
            ArrayList<TripletType> tripletTypes = new ArrayList<>();
            int counter = 0;
            HashBag<Domino> copy = new HashBag<>(dominos);
            copy.remove(domino);
            for (Domino hidden : possbilyHiddenDominoes) {
                HashBag<Domino> copyOfCopy = new HashBag<>(copy);
                copyOfCopy.add(hidden);
                TripletType evaluate = HandCalculator.evaluate(copyOfCopy);
                if (evaluate != null) {
                    counter++;
                } else {
                    tripletTypes.add(evaluate);
                }
            }
            lists.add(tripletTypes);
            if (counter > worseDomino) {
                worseDomino = counter;
                toDiscard = domino;
            }
        }
        return toDiscard;
//        return dominos.iterator().next();
    }

    private boolean validType(Triplet t, TripletType requiredMinType) {
        final TripletType type = HandCalculator.evaluate(t);
        if (type == null) {
            return false;
        }
        switch (requiredMinType) {
            case STRAIGHTS:
                return true;
            case MIXED:
                return type != TripletType.STRAIGHTS;
            case SETS:
                return type == TripletType.SETS;
            default:
                throw new IllegalStateException("Unknown type " + requiredMinType);
        }

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void give(Domino d) {
        dominos.add(d);
        final List<Hand> hands = HandCalculator.calculateHands(dominos);
        winningHand = hands.stream().filter(Hand::isWinning).findAny().orElse(null);
        if (winningHand == null) {
            discard = deadwoodestDomino(hands);
        }
    }

    private Domino deadwoodestDomino(List<Hand> hands) {
        HashMap<Domino, MutableInteger> dominos = new HashMap<>();
        for (Hand hand : hands) {
            for (Domino domino : hand.deadWood) {
                if (!dominos.containsKey(domino)) {
                    dominos.put(domino, new MutableInteger());
                }
                dominos.get(domino).inc();
            }
        }
        Domino worstDeadwood = hands.get(0).deadWood.iterator().next();
        int deadwoodCount = 0;
        for (Map.Entry<Domino, MutableInteger> dominoMutableIntegerEntry : dominos.entrySet()) {
            if (dominoMutableIntegerEntry.getValue().getCount() > deadwoodCount) {
                deadwoodCount = dominoMutableIntegerEntry.getValue().getCount();
                worstDeadwood = dominoMutableIntegerEntry.getKey();
            }
        }
//        discard = hands.get(0).deadWood.iterator().next();
        return worstDeadwood;
    }

    @Override
    public boolean hasWon() {
        return winningHand != null;
    }

    @Override
    public Hand getWinningHand() {
        return winningHand;
    }

    @Override
    public Domino getDiscard() {
        if (discard != null) {
            dominos.remove(discard);
            return discard;
        }
        System.out.println("---Discard should always be calculated elsewhere---");
        if (true) throw new IllegalStateException("Discard should always be calculated elsewhere");
        final int rIndex = new Random().nextInt(dominos.size());
        final Iterator<Domino> iterator = dominos.iterator();
        for (int i = 0; i < rIndex - 1; i++) {
            iterator.next();
        }
        final Domino next = iterator.next();
        iterator.remove();
        return next;
    }
}
