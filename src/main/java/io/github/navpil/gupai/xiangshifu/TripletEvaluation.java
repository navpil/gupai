package io.github.navpil.gupai.xiangshifu;

public class TripletEvaluation {
    private final Combination combination;
    private final Combination pairPotential;

    public TripletEvaluation(Combination combination, Combination pairPotential) {
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
