package io.navpil.github.zenzen.rummy.smallmahjong;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.util.Bag;
import io.navpil.github.zenzen.util.HashBag;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ComputerPlayer implements Player {

    private final String name;
    private Bag<Domino> dominos;
    private Hand winningHand;
    private Domino discard;

    public ComputerPlayer(String name) {
        this.name = name;
    }

    @Override
    public void deal(List<Domino> deal) {
        dominos = new HashBag<>(deal);
    }

    @Override
    public void showTable(Table table) {

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
//            final Hand hand = hands.stream().filter(h -> h.triplets.stream().anyMatch(t -> t.asBag().contains(lastDiscard) && validType(t, type))).findAny().orElse(null);
//            if (hand != null) {
//                final Triplet triplet = hand.triplets.stream().filter(t -> t.asBag().contains(lastDiscard)).findAny().get();
//                hand.triplets.remove(triplet);
//                return triplet;
//            }
        }
        return null;
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
            discard = hands.get(0).deadWood.iterator().next();
        }
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
