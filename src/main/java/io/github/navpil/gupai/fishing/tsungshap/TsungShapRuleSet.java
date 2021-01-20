package io.github.navpil.gupai.fishing.tsungshap;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.DominoUtil;

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

    /**
     * Under classic rules a player is given a single domino and then he decides what to do with it.
     * This does not leave much space for thought and the game is based predominantly on luck.
     * German Spieldomino variation gives a player four tiles into a hand, so he can decide which one to move and which
     * one to discard.
     * This gives a player more possibilities.
     *
     * This boolean actually is not used anywhere, because it made no sense in creating a single simulation which would
     * use the boolean, because the mechanics are pretty different.
     */
    private final boolean fourTilesInHand;

    public static TsungShapRuleSet classic() {
        return new TsungShapRuleSet(true, -1, false);
    }

    /**
     * Spiel domino says that 4 tiles should get into a hand and then be replenished.
     *
     * This makes a game more interesting. It makes sense to fix pair cost at some level, because they are much easier
     * to form in this variation and are too expensive.
     *
     * Second put being not obligatory actually makes sense, because a first player has a slight advantage and making
     * second put obligatory makes this advantage even higher.
     *
     * @return optimal rules for the SpielDomino TsungShap variation
     */
    public static TsungShapRuleSet spieldomino() {
        return new TsungShapRuleSet(false, 50, true);
    }

    /**
     * Pairs are too expensive in classic rules.
     * Western domino game 'Canton', which was inspired by TsungShap has a fixed value of 12 for pairs.
     * This sounds like a good idea, so here is a rule set, which fixes pair cost at some value.
     *
     * @return
     */
    public static TsungShapRuleSet poorPairs() {
        return new TsungShapRuleSet(true, 50, false);
    }

    public TsungShapRuleSet(boolean secondPutObligatory, int pairsCost, boolean fourTilesInHand) {
        this.secondPutObligatory = secondPutObligatory;
        this.pairsCost = pairsCost;
        this.fourTilesInHand = fourTilesInHand;
    }

    public boolean isSecondPutObligatory() {
        return secondPutObligatory;
    }

    public boolean isFourTilesInHand() {
        return fourTilesInHand;
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
