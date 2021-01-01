package io.navpil.github.zenzen.rummy.smallmahjong;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.util.HashBag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class HandCalculator {

    public static List<Hand> calculateHands(Collection<Domino> dominos) {
        final ArrayList<Hand> result = new ArrayList<>();
        executeInLoop(dominos, (t) -> {
            final TripletType evaluate = evaluate(t);
            if (evaluate != null) {
                final HashBag<Domino> copy = new HashBag<>(dominos);
                copy.strictRemoveAll(t.asBag());
                if (copy.size() > 2) {
                    final List<Hand> hands = calculateHands(new ArrayList<>(copy));
                    hands.forEach(hand -> hand.add(t));
                    result.addAll(hands);
                } else {
                    result.add(new Hand(t, copy));
                }
            }

        });
        if (result.isEmpty()) {
            result.add(Hand.deadwoodOnly(dominos));
        }
        return result;
    }

    public static void executeInLoop(Collection<Domino> dominoColl, Consumer<Triplet> consumer) {
        final ArrayList<Domino> dominos = new ArrayList<>(dominoColl);
        final int size = dominos.size();
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                for (int k = j + 1; k < size; k++) {
                    final List<Domino> triplet = List.of(dominos.get(i), dominos.get(j), dominos.get(k));
                    final Triplet t = new Triplet(triplet);
                    consumer.accept(t);
                }
            }
        }
    }

    public static TripletType evaluate(Triplet triplet) {
        return evaluate(triplet.asList());
    }
    public static TripletType evaluate(Collection<Domino> dominos) {
        final int[] ints = new int[6];
        int counter = 0;
        for (Domino d1 : dominos) {
            ints[counter * 2] = d1.getPips()[0];
            ints[counter * 2 + 1] = d1.getPips()[1];
            counter++;

        }
        Arrays.sort(ints);

        if (ints[0] == ints[2] && ints[3] == ints[5]) {
            return TripletType.SETS;
        }
        if (ints[0] == ints[2] && ints[3] + 1 == (ints[4]) && ints[4] + 1 == ints[5] && ints[2] != ints[3]) {
            return TripletType.MIXED;
        }
        if (ints[3] == ints[5] && ints[0] + 1 == (ints[1]) && ints[1] + 1 == ints[2] && ints[2] != ints[3]) {
            return TripletType.MIXED;
        }

        if (
                ints[0] + 1 == ints[1] && ints[1] + 1 == ints[2]
                        &&
                        ints[3] + 1 == ints[4] && ints[4] + 1 == ints[5]) {
            return TripletType.STRAIGHTS;
        }
        if (
                ints[0] + 1 == ints[1] && ints[1] + 1 == ints[3]
                        &&
                        ints[2] + 1 == ints[4] && ints[4] + 1 == ints[5]
                        &&
                        ints[1] == ints[2]
        ) {
            return TripletType.STRAIGHTS;
        }
        if (
                ints[0] + 1 == ints[2] && ints[2] + 1 == ints[4]
                        &&
                        ints[1] + 1 == ints[3] && ints[3] + 1 == ints[5]
                        &&
                        ints[0] == ints[1]
        ) {
            return TripletType.STRAIGHTS;
        }

        return null;
    }

}
