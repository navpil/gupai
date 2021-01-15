package io.github.navpil.gupai;

import io.github.navpil.gupai.dominos.IDomino;
import io.github.navpil.gupai.util.Bag;
import io.github.navpil.gupai.util.HashBag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents 3 dominoes, though technically it can also represent a pair (+empty domino).
 *
 * These dominoes may or may not represent a valid combination type
 *
 * Dominoes in a Triplet are always sorted (not true!)
 *
 * Triplets are mutable
 */
public class Triplet {

    private final List<IDomino> dominoes;
    private transient Combination combinationCache = null;

    //Copy constructor
    public Triplet(Triplet triplet) {
        this(triplet.dominoes);
    }

    public Triplet(List<IDomino> dominoes) {
        this.dominoes = sorted(dominoes);
    }

    private static List<IDomino> sorted(List<IDomino> dominoes) {
        final ArrayList<IDomino> sortedDominos = new ArrayList<>(dominoes);
        Collections.sort(sortedDominos);
        return sortedDominos;
    }

    @Override
    public String toString() {
        return "Triplet{" + dominoes +'}';
    }

    /**
     * Looks at a domino at a given index
     * @param i index
     * @return domino at index
     */
    public IDomino peek(int i) {
        return dominoes.get(i);
    }

    /**
     * Evaluates what would happen if a certain domino would be replaced
     *
     * @param replacement replacement domino
     * @param i index to replace at
     * @return triplet evaluation
     */
    public TripletEvaluation evaluateReplacement(IDomino replacement, int i) {
        final ArrayList<IDomino> newTriplet = new ArrayList<>();
        for (int j = 0; j < 3; j++) {
            if (j != i) {
                newTriplet.add(dominoes.get(j));
            }
        }
        newTriplet.add(replacement);
        return new TripletEvaluation(ZenZenRules.calculateCombination(newTriplet), ZenZenRules.calculatePairPotential(newTriplet));
    }

    /**
     * Which combination this triplet represents, Combination.none if none
     *
     * @return combination type
     */
    public Combination getCombination() {
        if (combinationCache == null) {
            combinationCache = ZenZenRules.calculateCombination(dominoes);
        }
        return combinationCache;

    }

    /**
     * Does this triplet contain a pair in it
     *
     * @return pair combination which is maybe contained in a triplet
     */
    public Combination getPairPotential() {
        return ZenZenRules.calculatePairPotential(dominoes);
    }

    /**
     * Replaces a domino at a given index
     *
     * @param index index to put a new domino at
     * @param domino domino to put
     */
    public void replace(int index, IDomino domino) {
        dominoes.set(index, domino);
        combinationCache = null;
    }

    public Bag<IDomino> asBag() {
        return new HashBag<>(dominoes);
    }

}
