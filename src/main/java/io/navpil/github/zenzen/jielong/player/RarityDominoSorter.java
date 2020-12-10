package io.navpil.github.zenzen.jielong.player;

import io.navpil.github.zenzen.dominos.Domino;

import java.util.Collection;
import java.util.Comparator;

public class RarityDominoSorter implements Comparator<Domino> {

    private final int[] occurrences;

    public RarityDominoSorter(Collection<Domino> dominos) {
        occurrences = new int[]{0, 0, 0, 0, 0, 0, 0};
        for (Domino domino : dominos) {
            final int[] pips = domino.getPips();
            occurrences[pips[0]]++;
            occurrences[pips[1]]++;
        }
    }

    @Override
    public int compare(Domino o1, Domino o2) {
        return index(o2) - index(o1);
    }

    private int index(Domino domino) {
        final int[] pips = domino.getPips();
        return occurrences[pips[0]] + occurrences[pips[1]];
    }
}
