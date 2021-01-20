package io.github.navpil.gupai.xiangshifu;

import io.github.navpil.gupai.CombinationType;

public class TripletEvaluation {
    private final CombinationType combination;
    private final CombinationType pairPotential;

    public TripletEvaluation(CombinationType combination, CombinationType pairPotential) {
        this.combination = combination;
        this.pairPotential = pairPotential;
    }

    public CombinationType getCombination() {
        return combination;
    }

    public CombinationType getPairPotential() {
        return pairPotential;
    }
}
