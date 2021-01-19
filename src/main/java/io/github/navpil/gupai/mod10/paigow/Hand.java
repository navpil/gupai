package io.github.navpil.gupai.mod10.paigow;

import java.util.Objects;

public class Hand {

    private final PaiGowPair first;
    private final PaiGowPair second;

    public Hand(PaiGowPair first, PaiGowPair second) {
        this.first = first;
        this.second = second;
    }

    public PaiGowPair getFirst() {
        return first;
    }

    public PaiGowPair getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "H{" + first +
                ", " + second +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hand hand = (Hand) o;
        return Objects.equals(first, hand.first) &&
                Objects.equals(second, hand.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
