package io.github.navpil.gupai;

public class Tuple {
    private final Combination combination;
    private final Combination pairPotential;

    public Tuple(Combination combination, Combination pairPotential) {
        this.combination = combination;
        this.pairPotential = pairPotential;
    }

    public Combination getCombination() {
        return combination;
    }

    public Combination getPairPotential() {
        return pairPotential;
    }
}
