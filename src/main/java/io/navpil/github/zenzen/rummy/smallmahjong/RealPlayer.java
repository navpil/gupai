package io.navpil.github.zenzen.rummy.smallmahjong;

import io.navpil.github.zenzen.ConsoleInput;
import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.util.Bag;
import io.navpil.github.zenzen.util.HashBag;
import io.navpil.github.zenzen.util.TreeBag;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RealPlayer implements Player {

    private final String name;
    private final ConsoleInput consoleInput;
    private Bag<Domino> dominos;
    private Hand winningHand;

    public RealPlayer(String name) {
        this.name = name;
        consoleInput = new ConsoleInput();
    }

    @Override
    public void deal(List<Domino> deal) {
        this.dominos = new TreeBag<>(deal);
    }

    @Override
    public void showTable(Table table) {
    }

    @Override
    public Triplet offer(Domino lastDiscard, TripletType minRequiredType) {
        final HashBag<Domino> dominos = new HashBag<>(this.dominos);
        dominos.add(lastDiscard);
        final List<Hand> hands = HandCalculator.calculateHands(dominos);
        final List<Hand> validHands = hands.stream().filter(h -> h.isWinning() || h.triplets.stream().anyMatch(t -> t.asBag().contains(lastDiscard) && validType(t, minRequiredType))).collect(Collectors.toList());
        if (validHands.isEmpty()) {
            return null;
        }
        final ArrayList<HandWithTriplet> offerList = new ArrayList<>();
        final HashSet<HandWithTriplet> alreadyProcessed = new HashSet<>();
        for (Hand validHand : validHands) {
            for (Triplet triplet : validHand.triplets) {
                if (triplet.asBag().contains(lastDiscard) && validType(triplet, minRequiredType)) {
                    final HandWithTriplet handWithTriplet = new HandWithTriplet(triplet, validHand);
                    if (!alreadyProcessed.contains(handWithTriplet)) {
                        alreadyProcessed.add(handWithTriplet);
                        offerList.add(handWithTriplet);
                    }
                }
            }
        }

        final StringBuilder sb = new StringBuilder("You are offered ").append(lastDiscard).append(", which triplet would you like to get?\n");
        sb.append(0).append(") None \n");
        int counter = 1;
        for (HandWithTriplet withTriplet : offerList) {
            sb.append(counter).append(")").append(withTriplet).append("\n");
            counter++;
        }
        final String prompt = sb.toString();
        final int tripletChoice = consoleInput.readInt(i -> i >= 0 && i <= offerList.size(),
                prompt,
                "Invalid input");
        if (tripletChoice == 0) {
            return null;
        }
        final HandWithTriplet handWithTriplet = offerList.get(tripletChoice - 1);
        final Triplet triplet = handWithTriplet.triplet;
        this.dominos.add(lastDiscard);
        this.dominos.strictRemoveAll(triplet.asBag());
        if (handWithTriplet.hand.isWinning()) {
            handWithTriplet.hand.triplets.remove(triplet);
            winningHand = handWithTriplet.hand;
        }
        return triplet;
    }

    private static class HandWithTriplet {
        private final Triplet triplet;
        private final Hand hand;

        private HandWithTriplet(Triplet triplet, Hand hand) {
            this.triplet = triplet;
            this.hand = hand;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HandWithTriplet that = (HandWithTriplet) o;
            return Objects.equals(triplet, that.triplet) &&
                    Objects.equals(hand, that.hand);
        }

        @Override
        public int hashCode() {
            return Objects.hash(triplet, hand);
        }

        @Override
        public String toString() {
            return "Triplet " + triplet + " to form " + (hand.isWinning() ? "winning " : "") + "hand " + hand;
        }

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
    public void give(Domino give) {
        dominos.add(give);
        final List<Hand> hands = HandCalculator.calculateHands(dominos);
        winningHand = hands.stream().filter(Hand::isWinning).findAny().orElse(null);
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
        final StringBuilder sb = new StringBuilder("Which domino will you discard?\n");
        int counter = 1;
        final Domino[] dominos = new Domino[this.dominos.size()];
        for (Domino domino : this.dominos) {
            sb.append(counter).append(")").append(domino).append("\n");
            dominos[counter - 1] = domino;
            counter++;
        }
        final String prompt = sb.toString();
        final int discard = consoleInput.readInt(
                i -> i > 0 && i <= this.dominos.size(),
                prompt,
                "Invalid input"
        );
        this.dominos.remove(dominos[discard - 1]);
        return dominos[discard - 1];
    }
}
