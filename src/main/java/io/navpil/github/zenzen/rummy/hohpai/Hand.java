package io.navpil.github.zenzen.rummy.hohpai;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.util.HashBag;
import io.navpil.github.zenzen.util.ToHashBagCollector;

import java.util.Collection;
import java.util.Objects;

public class Hand {

    private final Collection<? extends Collection<Domino>> combinations;
    private final Collection<Domino> deadwood;

    public Hand(Collection<? extends Collection<Domino>> combinations, Collection<Domino> deadwood) {
        this.combinations = combinations.stream().map(HashBag::new).collect(new ToHashBagCollector<>());
        this.deadwood = new HashBag<>(deadwood);
    }

    public boolean isWinningHand() {
        return deadwood.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hand hand = (Hand) o;
        return Objects.equals(combinations, hand.combinations) &&
                Objects.equals(deadwood, hand.deadwood);
    }

    @Override
    public int hashCode() {
        return Objects.hash(combinations, deadwood);
    }

    public Collection<? extends Collection<Domino>> getCombinations() {
        return combinations;
    }

    public Collection<Domino> getDeadwood() {
        return deadwood;
    }

    @Override
    public String toString() {
        return "Hand{" +
                "combinations=" + combinations +
                ", deadwood=" + deadwood +
                '}';
    }
}
