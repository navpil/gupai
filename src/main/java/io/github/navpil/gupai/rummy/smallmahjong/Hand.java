package io.github.navpil.gupai.rummy.smallmahjong;

import io.github.navpil.gupai.util.Bag;
import io.github.navpil.gupai.util.HashBag;
import io.github.navpil.gupai.Domino;

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

    public Bag<Triplet> getTriplets() {
        return triplets;
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
