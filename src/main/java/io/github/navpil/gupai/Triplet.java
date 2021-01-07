package io.github.navpil.gupai;

import io.github.navpil.gupai.dominos.IDomino;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Triplet {

    private final List<IDomino> dominoes;
    private Combination lastCombination = null;

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

    public static Triplet parse(String string) {
        final ArrayList<IDomino> dominos = new ArrayList<>();
        dominos.add(DominoParser.parse(string.substring(0, 5)));
        dominos.add(DominoParser.parse(string.substring(7, 12)));
        dominos.add(DominoParser.parse(string.substring(14, 19)));
        return new Triplet(dominos);
    }

    @Override
    public String toString() {
        return "Triplet{" + dominoes +'}';
    }

    public IDomino peek(int i) {
        return dominoes.get(i);
    }

    public Tuple getCombination(IDomino replacement, int i) {
        final ArrayList<IDomino> newTriplet = new ArrayList<>();
        for (int j = 0; j < 3; j++) {
            if (j != i) {
                newTriplet.add(dominoes.get(j));
            }
        }
        newTriplet.add(replacement);
        return new Tuple(ZenZenRules.calculateCombination(newTriplet), ZenZenRules.calculatePairPotential(newTriplet));
    }

    public Combination getCombination() {
        if (lastCombination == null) {
            lastCombination = ZenZenRules.calculateCombination(dominoes);
        }
        return lastCombination;

    }

    public Combination getPairPotential() {
        return ZenZenRules.calculatePairPotential(dominoes);
    }

    public void replace(int index, IDomino domino) {
        dominoes.set(index, domino);
        lastCombination = null;
    }

    public ArrayList<IDomino> asList() {
        return new ArrayList<>(dominoes);
    }

}
