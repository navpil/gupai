package io.navpil.github.zenzen.rummy.smallmahjong;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.util.Bag;
import io.navpil.github.zenzen.util.HashBag;

import java.util.Collection;
import java.util.Objects;

public class Hand {

    Bag<Triplet> triplets;
    Bag<Domino> deadWood;

    public Hand() {
    }

    public Hand(Triplet t, HashBag<Domino> copy) {
        triplets = new HashBag<>();
        if (t != null) {
            triplets.add(t);
        }
        deadWood = copy;
    }

    public static Hand deadwoodOnly(Collection<Domino> dominos) {
        return new Hand(null, new HashBag<>(dominos));
    }

    public void add(Triplet t) {
        triplets.add(t);
    }

    public boolean isWinning() {
        return deadWood.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hand hand = (Hand) o;
        return Objects.equals(triplets, hand.triplets) &&
                Objects.equals(deadWood, hand.deadWood);
    }

    @Override
    public int hashCode() {
        return Objects.hash(triplets, deadWood);
    }

    @Override
    public String toString() {
        return "Hand{" +
                "triplets=" + triplets +
                ", deadWood=" + deadWood +
                '}';
    }
}
