package io.navpil.github.zenzen.jielong.player;

import io.navpil.github.zenzen.dominos.Domino;

import java.util.Comparator;

public class MinMaxDominoSorter implements Comparator<Domino> {

    public static final Comparator<? super Domino> SMALL_FIRST = new MinMaxDominoSorter();
    public static final Comparator<? super Domino> BIG_FIRST = new MinMaxDominoSorter().reversed();

    @Override
    public int compare(Domino o1, Domino o2) {
        final int[] pips1 = o1.getPips();
        final int[] pips2 = o2.getPips();

        return pips1[0] + pips1[1] - (pips2[0] + pips2[1]);
    }
}
