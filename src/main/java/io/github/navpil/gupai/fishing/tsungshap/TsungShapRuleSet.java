package io.github.navpil.gupai.fishing.tsungshap;

import io.github.navpil.gupai.dominos.DominoUtil;
import io.github.navpil.gupai.dominos.Domino;

import java.util.Collection;
import java.util.HashSet;

public class TsungShapRuleSet {

    /**
     * It's not clear from the rules whether the second player has to put a domino, or he can play it.
     * If this is set to true, then a second player cannot make a sweep with his first move
     */
    private final boolean secondPutObligatory;

    /**
     * Fixed cost of a pair (not mentioned in any sources, pure invention)
     */
    private final int pairsCost;

    public static TsungShapRuleSet classic() {
        return new TsungShapRuleSet(true, -1);
    }

    /**
     * Pairs are too expensive in classic rules.
     * Western domino game 'Canton', which was inspired by TsungShap has a fixed value of 12 for pairs.
     * This sounds like a good idea, so here is a rule set, which fixes pair cost at some value.
     *
     * @return
     */
    public static TsungShapRuleSet poorPairs() {
        return new TsungShapRuleSet(true, 50);
    }

    public TsungShapRuleSet(boolean secondPutObligatory, int pairsCost) {
        this.secondPutObligatory = secondPutObligatory;
        this.pairsCost = pairsCost;
    }

    public boolean isSecondPutObligatory() {
        return secondPutObligatory;
    }

    public boolean validPair(Collection<Domino> pair) {
        return pair.size() == 2 && new HashSet<>(pair).size() == 1;
    }

    public boolean validTriplet(Collection<Domino> triplet) {
        if (triplet.size() != 3) {
            return false;
        }
        return triplet.stream().mapToInt(DominoUtil::getPipPoints).reduce(Integer::sum).orElse(42) % 10 == 0;
    }

    public int calculatePoints(Collection<TsungShapCatch> catches) {
        return catches.stream().mapToInt(this::getPoints).reduce(Integer::sum).orElse(0);
    }

    private int getPoints(TsungShapCatch c) {
        int startValue = c.isSweep() ? 40 : 0;
        if (validPair(c.getFish())) {
            if (pairsCost > 0) {
                return startValue + pairsCost;
            }
            return startValue + c.getFish().stream().mapToInt(DominoUtil::getPipPoints).reduce(Integer::sum).orElseThrow() * 10;
        } else if (validTriplet(c.getFish())) {
            return startValue + c.getFish().stream().mapToInt(DominoUtil::getPipPoints).reduce(Integer::sum).orElseThrow();
        }
        return 0;
    }

}
