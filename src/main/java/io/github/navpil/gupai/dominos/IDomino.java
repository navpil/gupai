package io.github.navpil.gupai.dominos;

import java.util.Arrays;

public interface IDomino extends Comparable<IDomino> {

    int[] getPips();

    @Override
    default int compareTo(IDomino o) {
        final int[] pips = this.getPips();
        final int[] thatPips = o.getPips();
        return Arrays.compare(pips, thatPips);
    }
}
